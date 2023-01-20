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
    private RequestBase base;
    private String passTicket;
    private WXSyncKey syncKey;
    private ScheduledExecutorService taskExecutor = Executors.newSingleThreadScheduledExecutor();
    private OnErrorCallback onError;
    private OnMessageCallback onMessage;
    private String wxUsername;
    private List<WXContact> mainContacts;

    public WXApiClient(RequestBase base, String passTicket, String cookie) throws Exception {
        this.base = base;
        this.passTicket = passTicket;

        RequestHelper.setSkipSSLCheck();
        RequestHelper.setCookieStore(cookie);
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

        mainContacts = response.getContactList();
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
            String response = result.toString().split("=", 2)[1]
                    .replace(" ", "")
                    .replace("{", "")
                    .replace("}", "");
            int ret = Integer.MIN_VALUE;
            int selector = Integer.MIN_VALUE;
            for (String field : response.split(",")) {
                String[] kv = field.split(":", 2);
                switch (kv[0]) {
                    case "retcode":
                        ret = Integer.parseInt(kv[1].replace("\"", ""));
                        break;
                    case "selector":
                        selector = Integer.parseInt(kv[1].replace("\"", ""));
                        break;
                }
            }

            if (ret != 0) {
                throw new RuntimeException("Failed to sync with messages! Error code: " + ret);
            }

            if (selector == 0) {
                return;
            }

            result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync"
                    + "?sid=" + urlEncode(base.getSid())
                    + "&skey=" + urlEncode(base.getSkey())
                    + "&uin=" + base.getUin(), new RequestWXSync(base, syncKey));
            ResponseWXSync syncResponse = result.deserialize(new TypeToken<>() {});
            this.syncKey = syncResponse.getSyncKey();
            if (syncResponse.getAddMsgCount() == 0) {
                return;
            }

            onMessage.onMessage(syncResponse);
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
        List<WXContact> result = queryContactInformation(new WXContactQueryItem(contactID));
        return result.size() == 0 ? null : result.get(0);
    }

    public List<WXContact> queryContactInformation(WXContactQueryItem... items) throws Exception {
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact"
                + "?pass_ticket=" + urlEncode(passTicket), new RequestBatchGetContact(base, Arrays.asList(items)));
        ResponseQueryContact response = result.deserialize(new TypeToken<>() { });
        return response.getContactList();
    }


    public List<WXContact> queryAllContact() throws Exception {
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact"
                + "?pass_ticket=" + urlEncode(passTicket)
                + "&skey" + urlEncode(base.getSkey()), new RequestWXGetContact(base));
        ResponseQueryContact response = result.deserialize(new TypeToken<>() { });
        return response.getContactList();
    }

    public List<WXContact> getMainContacts() {
        return mainContacts;
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
