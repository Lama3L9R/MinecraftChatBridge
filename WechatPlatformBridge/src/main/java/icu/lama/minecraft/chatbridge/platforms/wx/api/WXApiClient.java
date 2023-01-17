package icu.lama.minecraft.chatbridge.platforms.wx.api;

import com.google.gson.reflect.TypeToken;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContactQueryItem;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXMessage;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXSyncKey;
import icu.lama.minecraft.chatbridge.platforms.wx.api.in.ResponseQueryContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.in.ResponseSendMessage;
import icu.lama.minecraft.chatbridge.platforms.wx.api.in.ResponseWXSync;
import icu.lama.minecraft.chatbridge.platforms.wx.api.in.ResponseWxInit;
import icu.lama.minecraft.chatbridge.platforms.wx.api.out.*;
import icu.lama.minecraft.chatbridge.platforms.wx.api.utils.HttpRequestResult;
import icu.lama.minecraft.chatbridge.platforms.wx.api.utils.RequestHelper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WXApiClient {
    private static final Pattern SYNC_REGEX = Pattern.compile("window.synccheck=\\{retcode:\"?(\\d)\"?,selector:\"?(\\d)\"?}");

    private RequestBase base;
    private String passTicket;
    private WXSyncKey syncKey;
    private ScheduledExecutorService taskExecutor = Executors.newSingleThreadScheduledExecutor();
    private OnErrorCallback onError;
    private OnMessageCallback onMessage;
    private String wxUsername;

    public WXApiClient(RequestBase base, String passTicket, String cookie) throws Exception {
        this.base = base;
        this.passTicket = passTicket;

        RequestHelper.setSkipSSLCheck();
        RequestHelper.setCookie(cookie);
    }

    public void init(OnMessageCallback onMessage, OnErrorCallback errorCallback) throws Exception {
        this.onError = errorCallback;
        this.onMessage = onMessage;

        RequestWxInit request = new RequestWxInit(base);
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit"
                + "?pass_ticket=" + urlEncode(passTicket), request);
        ResponseWxInit response = result.deserialize(new TypeToken<>() {});
        if (result.getStatusCode() != 200) {
            throw new RuntimeException("Failed to perform wxapi init. status code=" + result.getStatusCode() + ". remote response=" + response);
        }

        syncKey = response.getSyncKey();
        wxUsername = response.getUser().getUserName();
    }

    public void startPollMessages() {
        taskExecutor.scheduleAtFixedRate(this::asyncSync, 0, 1, TimeUnit.SECONDS);
    }

    public void asyncSync() {
        if (syncKey == null) {
            throw new IllegalArgumentException("Failed to sync messages! Sync keys are missing. Are you logged in?");
        }

        try {
            HttpRequestResult result = RequestHelper.post("https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck"
                    + "?sid=" + urlEncode(base.getSid())
                    + "&skey=" + urlEncode(base.getSkey())
                    + "&uin=" + base.getUin()
                    + "&deviceid=" + urlEncode(base.getDeviceID())
                    + "&synckey=" + urlEncode("" + syncKey), null);
            Matcher syncResponse = SYNC_REGEX.matcher(result.toString());
            int ret = Integer.parseInt(syncResponse.group(1));
            int selector = Integer.parseInt(syncResponse.group(2));
            if (ret != 0) {
                throw new RuntimeException("Failed to sync with messages! Error code: " + ret);
            }

            if (selector != 2) {
                return;
            }

            result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync"
                    + "?sid=" + urlEncode(base.getSid())
                    + "&skey=" + urlEncode(base.getSkey())
                    + "&uin=" + base.getUin(), new RequestWXSync(base, syncKey));
            ResponseWXSync response = result.deserialize(new TypeToken<>() {});
            this.syncKey = response.getSyncKey();
            if (response.getAddMsgCount() == 0) {
                return;
            }

            onMessage.onMessage(response);
        } catch (Exception e) {
            onError.onError(e);
        }
    }

    public void sendMessage(String message, String userID) throws Exception {
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg"
                + "?pass_ticket=" + urlEncode(passTicket), new RequestSendMessage(base, new WXMessage(message, wxUsername, userID)));
        ResponseSendMessage response = result.deserialize(new TypeToken<ResponseSendMessage>() {});

        if (response.getBaseResponse().getRet() != 0) {
            throw new RuntimeException("Failed to send message! server return:" + response.getBaseResponse());
        }
    }

    public WXContact queryContactInformation(String contactID) throws Exception {
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact"
                + "?pass_ticket=" + urlEncode(passTicket), new RequestBatchGetContact(base, Arrays.asList(new WXContactQueryItem(contactID))));
        ResponseQueryContact response = result.deserialize(new TypeToken<>() { });
        return response.getContactList().size() == 0 ? null : response.getContactList().get(0);
    }

    public List<WXContact> queryAllContact() throws Exception {
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact"
                + "?pass_ticket=" + urlEncode(passTicket)
                + "&skey" + urlEncode(base.getSkey()), new RequestWXGetContact(base));
        ResponseQueryContact response = result.deserialize(new TypeToken<>() { });
        return response.getContactList();
    }

    private String urlEncode(String data) {
        return URLEncoder.encode(data, StandardCharsets.UTF_8);
    }

    public interface OnMessageCallback {
        void onMessage(ResponseWXSync sync);
    }

    public interface OnErrorCallback {
        void onError(Exception e);
    }
}
