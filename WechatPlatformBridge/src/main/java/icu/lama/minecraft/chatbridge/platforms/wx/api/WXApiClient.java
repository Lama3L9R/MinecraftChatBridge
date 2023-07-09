package icu.lama.minecraft.chatbridge.platforms.wx.api;

import com.google.gson.reflect.TypeToken;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContactQueryItem;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXMessage;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXSyncKey;
import icu.lama.minecraft.chatbridge.platforms.wx.api.in.*;
import icu.lama.minecraft.chatbridge.platforms.wx.api.out.*;
import icu.lama.minecraft.chatbridge.platforms.wx.api.utils.HttpRequestResult;
import icu.lama.minecraft.chatbridge.platforms.wx.api.utils.RequestHelper;
import org.jetbrains.annotations.Nullable;

import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WXApiClient {
    private RequestBase base;
    private String passTicket;
    private WXSyncKey syncKey;
    private ScheduledExecutorService taskExecutor;
    private OnErrorCallback onError;
    private OnMessageCallback onMessage;
    private String wxUsername;
    private final Map<String, WXContact> contacts = new HashMap<>();
    private volatile long lastSync = System.currentTimeMillis();

    public WXApiClient(RequestBase base, String passTicket, String cookie) throws Exception {
        this.base = base;
        this.passTicket = passTicket;

        RequestHelper.setSkipSSLCheck();
        RequestHelper.setCookieStore(cookie);
    }

    public void init(OnMessageCallback onMessage) throws Exception {
        this.onMessage = onMessage;

        taskExecutor = Executors.newScheduledThreadPool(2);

        RequestWxInit request = new RequestWxInit(base);
        HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit"
                + "?pass_ticket=" + urlEncode(passTicket), request);
        ResponseWxInit response = result.deserialize(new TypeToken<>() {});
        if (result.getStatusCode() != 200) {
            throw new RuntimeException("Failed to perform wxapi init. status code=" + result.getStatusCode() + ". remote response=" + response);
        }

        queryAllContact().forEach(it -> contacts.put(it.getUserName(), it));
        syncKey = response.getSyncKey();
        wxUsername = response.getUser().getUserName();
    }

    public void shutdown() {
        taskExecutor.shutdown();
    }

    public void startPollMessages() {
        taskExecutor.submit(this::asyncSync);
    }

    public void asyncSync() {
        if (syncKey == null) {
            throw new IllegalArgumentException("Failed to sync messages! Sync keys are missing. Are you logged in?");
        }

        System.out.println("Polling messages...");

        if (lastSync + 1000 > System.currentTimeMillis()) {
            System.out.println("Too soon to poll again, calm for 1s");
            try {
                Thread.sleep(1000 - (System.currentTimeMillis() - lastSync));
            } catch (Throwable ignored) { }
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
                lastSync = System.currentTimeMillis();
                startPollMessages();

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
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        lastSync = System.currentTimeMillis();
        startPollMessages();
    }// WechatPlatformBridge-1.0-SNAPSHOT-all.jar

    public void sendMessage(String message, String userID) {
        taskExecutor.submit(() -> {
            try {
                HttpRequestResult result = RequestHelper.post("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg"
                        + "?pass_ticket=" + urlEncode(passTicket), new RequestSendMessage(base, new WXMessage(message, wxUsername, userID)));
                ResponseSendMessage response = result.deserialize(new TypeToken<>() { });

                if (response.getBaseResponse().getRet() != 0) {
                    throw new RuntimeException("Failed to send message! server return:" + response.getBaseResponse());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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
                + "&skey=" + urlEncode(base.getSkey()), new RequestWXGetContact(base));
        ResponseQueryContact response = result.deserialize(new TypeToken<>() { });
        return response.getContactList();
    }

    public void refreshLogin() throws Exception {
        var result = RequestHelper.get("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxpushloginurl" +
                "?mod=desktop" +
                "&uin=" + this.base.getUin());
        ResponsePushLogin response = result.deserialize(new TypeToken<>() { });
        if (Integer.parseInt(response.getRet()) == 0) {
            waitLogin(response.getUuid());
        } else {
            throw new RuntimeException("Failed to push login. Server returned: " + response.getRet() + ", " + response.getMsg());
        }
    }

    public void waitLogin(String uuid) throws Exception {
        var redirectURL = waitLoginConfirm(uuid, 5);
        var response = RequestHelper.get(redirectURL + "&version=v2&fun=new&mod=desktop&target=t");
        var xml = response.toString();

        var skey = xml.substring(xml.indexOf("<skey>") + 1, xml.indexOf("</skey>"));
        var wxsid = xml.substring(xml.indexOf("<wxsid>") + 1, xml.indexOf("</wxsid>"));
        var wxuin = xml.substring(xml.indexOf("<wxuin>") + 1, xml.indexOf("</wxuin>"));
        var passTicket = xml.substring(xml.indexOf("<pass_ticket>") + 1, xml.indexOf("</pass_ticket>"));
        var cookie = response.getConnection().getHeaderField("set-cookie");

        this.base = new RequestBase(Long.parseLong(wxuin), wxsid, skey, "e" + String.valueOf(new Random().nextLong()).substring(1, 16));
        this.passTicket = passTicket;
        RequestHelper.setCookieStore(cookie);
    }

    private String waitLoginConfirm(String uuid, int retries) throws Exception {
        if (retries == 0) {
            throw new RuntimeException("Failed to confirm login! No more chance to retry");
        }
        var result = RequestHelper.get("https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid=" + uuid);
        var prop = new Properties();
        prop.load(new StringReader(String.join("\n", Arrays.asList(result.toString()
                .replace("\"", "")
                .split(";")))));
        if (prop.get("window.code").equals("200")) {
            return (String) prop.get("window.redirect_uri");
        } else {
            return waitLoginConfirm(uuid, retries - 1);
        }
    }

    public Collection<WXContact> getContacts() {
        return contacts.values();
    }

    public Map<String, WXContact> getContactsByUserName() { return contacts; }

    public @Nullable WXContact getContact(String userName) {
        return this.contacts.get(userName);
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
