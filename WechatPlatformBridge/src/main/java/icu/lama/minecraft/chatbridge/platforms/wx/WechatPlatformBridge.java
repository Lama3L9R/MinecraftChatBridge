package icu.lama.minecraft.chatbridge.platforms.wx;

import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.PlatformReceiveCallback;
import icu.lama.minecraft.chatbridge.core.binding.GeneralBindingDatabase;
import icu.lama.minecraft.chatbridge.core.binding.IBindingDatabase;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import icu.lama.minecraft.chatbridge.platforms.wx.api.WXApiClient;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContactQueryItem;
import icu.lama.minecraft.chatbridge.platforms.wx.api.out.RequestBase;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class WechatPlatformBridge implements IPlatformBridge {
    @SuppressWarnings("unused")
    public static WechatPlatformBridge INSTANCE = new WechatPlatformBridge();
    private final HashMap<String, String> uidToName = new HashMap<>();
    private final IBindingDatabase bindings = new GeneralBindingDatabase();
    private PlatformReceiveCallback callback;
    private PlatformConfiguration config;
    private WXApiClient apiClient;
    private WXContact groupContact;
    private String wechatFormat = "[%s] %s";
    private String playerJoinFormat = "[+] %s";
    private String playerLeaveFormat = "[-] %s";
    private String serverOnlineFormat = "[+] Server";
    private String serverOfflineFormat = "[-] Server";

    @Override
    public void send(String name, UUID playerUUID, String msg) {
        try {
            apiClient.sendMessage(String.format(wechatFormat, name, msg), groupContact.getUserName());
        } catch (Exception e) { MinecraftChatBridge.throwException(e, this); }
    }

    @Override
    public String getPlatformName() {
        return "wechat";
    }

    @Override
    public boolean getAllowNSFWContent() {
        return false;
    }

    @Override
    public void setReceiveCallback(PlatformReceiveCallback callback) {
        this.callback = callback;
    }

    @Override
    public void setConfiguration(PlatformConfiguration config) {
        this.config = config;
    }

    @Override
    public void init() {
        this.config.get("lang.wechatFormat", f -> wechatFormat = (String) f);
        this.config.get("lang.playerJoinFormat", f -> playerJoinFormat = (String) f);
        this.config.get("lang.playerLeaveFormat", f -> playerLeaveFormat = (String) f);
        this.config.get("lang.serverOnlineFormat", f -> serverOnlineFormat = (String) f);
        this.config.get("lang.serverOfflineFormat", f -> serverOfflineFormat = (String) f);

        String cookies = new String(Base64.getDecoder().decode(this.config.getCredentials("cookie")), StandardCharsets.UTF_8);
        String passTicket = this.config.getCredentials("passTicket");

        RequestBase base = new RequestBase(
            Long.parseLong(this.config.getCredentials("wxuin")),
            this.config.getCredentials("wxsid"),
            this.config.getCredentials("skey"),
            "e" + String.valueOf(new Random().nextLong()).substring(1, 16)
        );

        try {
            apiClient = new WXApiClient(base, passTicket, cookies);

            apiClient.init((response) -> {
                response.getAddMsgList().stream()
                        .filter(it -> it.getFromUserName().equals(groupContact.getUserName()))
                        .forEach(it -> {
                            if (it.getMsgType() != 1) {
                                return;
                            }

                            String[] content = it.getContent().replace("<br/>", "").split(":", 2);

                            this.callback.onReceive(this, uidToName.get(content[0]), content[1]);
                        });
            }, (e) -> MinecraftChatBridge.throwException(e, this));

            String targetGroupName = "" + this.config.get("group");
            apiClient.getMainContacts().stream()
                    .filter(it -> it.getMemberCount() > 0 && it.getUserName().startsWith("@@"))
                    .filter(it -> it.getNickName().equals(targetGroupName))
                    .findFirst()
                    .ifPresentOrElse(
                        (target) -> groupContact = target,
                        () -> { throw new RuntimeException("Group not found!");
                    });

            String encry = apiClient.queryContactInformation(groupContact.getUserName()).getEncryChatRoomId();
            apiClient.queryContactInformation(groupContact.getMemberList()
                    .stream()
                    .map(it -> new WXContactQueryItem(it.getUserName(), encry))
                    .toArray(WXContactQueryItem[]::new)
            ).forEach(it -> uidToName.put(it.getUserName(), it.getDisplayName()));

            apiClient.startPollMessages();
        } catch (Exception e) {
            MinecraftChatBridge.throwException(e, this);
        }

        MinecraftEvents.onPlayerJoin.subscribe((source, d) ->
                apiClient.sendMessage(String.format(playerJoinFormat, source.getName()), groupContact.getUserName()));
        MinecraftEvents.onPlayerLeave.subscribe((source, d) ->
                apiClient.sendMessage(String.format(playerLeaveFormat, source.getName()), groupContact.getUserName()));
        MinecraftEvents.onServerSetupComplete.subscribe((source, d) ->
                apiClient.sendMessage(String.format(serverOnlineFormat), groupContact.getUserName()));
        MinecraftEvents.onServerBeginShutdown.subscribe((source, d) ->
                apiClient.sendMessage(String.format(serverOfflineFormat), groupContact.getUserName()));

        PlatformEvents.onPlatformBridgeLoad.trigger(this, null);
    }

    @Override
    public IBindingDatabase getBindingDatabase() {
        return this.bindings;
    }

}
