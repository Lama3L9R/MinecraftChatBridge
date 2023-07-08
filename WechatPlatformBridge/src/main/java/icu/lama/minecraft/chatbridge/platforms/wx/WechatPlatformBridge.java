package icu.lama.minecraft.chatbridge.platforms.wx;

import com.typesafe.config.Config;
import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.events.EventPlatformChatMessage;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.loader.PluginType;
import icu.lama.minecraft.chatbridge.core.loader.annotations.ConfigInject;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Initializer;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Plugin;
import icu.lama.minecraft.chatbridge.core.proxy.platform.*;
import icu.lama.minecraft.chatbridge.platforms.wx.api.WXApiClient;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContactQueryItem;
import icu.lama.minecraft.chatbridge.platforms.wx.api.out.RequestBase;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Plugin(name = "wechat", type = PluginType.PLATFORM_PROXY)
public class WechatPlatformBridge implements IPlatformProxy {
    public static final WechatPlatformBridge INSTANCE = new WechatPlatformBridge();
    private final HashMap<String, String> uidToName = new HashMap<>();
    private @ConfigInject Config config;
    private WXApiClient apiClient;
    private WXContact groupContact;
    private String wechatFormat = "";
    private String playerJoinFormat = "";
    private String playerLeaveFormat = "";
    private String serverOnlineFormat = "";
    private String serverOfflineFormat = "";

    @Initializer
    public void init() {
        wechatFormat = this.config.getString("wechat.lang.wechatFormat");
        playerJoinFormat = this.config.getString("wechat.lang.playerJoinFormat");
        playerLeaveFormat = this.config.getString("wechat.lang.playerLeaveFormat");
        serverOnlineFormat = this.config.getString("wechat.lang.serverOnlineFormat");
        serverOfflineFormat = this.config.getString("wechat.lang.serverOfflineFormat");

        String cookies = new String(Base64.getDecoder().decode(this.config.getString("wechat.credentials.cookie").trim()), StandardCharsets.UTF_8);
        String passTicket = this.config.getString("wechat.credentials.passTicket");

        RequestBase base = new RequestBase(
            Long.parseLong(this.config.getString("wechat.credentials.wxuin")),
            this.config.getString("wechat.credentials.wxsid"),
            this.config.getString("wechat.credentials.skey"),
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
                            var group = new WechatGroupProxy(groupContact);

                            PlatformEvents.onMessage.trigger(this, new EventPlatformChatMessage(content[1], group, getMember(group, "wechat:" + content[0])));
                        });
            }, (e) -> MinecraftChatBridge.throwException(e, this));

            String targetGroupName = this.config.getString("wechat.listenOn");
            apiClient.getContacts().stream()
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
        MinecraftEvents.onChatMessage.subscribe((source, msg) ->
                apiClient.sendMessage(String.format(wechatFormat, source.getName(), msg.getMessage()), groupContact.getUserName()));

        PlatformEvents.onBridgeLoad.trigger(this, null);
    }

    @Override public List<PlatformFeature> getSupportedFeatures() {
        return List.of(PlatformFeature.BASIC);
    }

    @Override public List<INamed> getAllContacts() {
        return null;
    }

    @Override public INamed getContact(String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);
        var userName = uniqueIdentifier.substring(7);
        var contact = apiClient.getContact(userName);
        if (contact == null) {
            return null;
        }

        if (userName.startsWith("@@")) {
            return new WechatGroupProxy(contact);
        } else {
            return new WechatUserProxy(contact);
        }
    }

    @Override public IProxyChatGroup getGroup(String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);
        var userName = uniqueIdentifier.substring(7);
        var contact = apiClient.getContact(userName);
        if (contact == null) {
            return null;
        }

        if (userName.startsWith("@@")) {
            return new WechatGroupProxy(contact);
        } else {
            throw new IllegalArgumentException("The identifier is for none-group!");
        }
    }

    @Override public IProxyChatMember getMember(String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);
        var userName = uniqueIdentifier.substring(7);
        var contact = apiClient.getContact(userName);
        if (contact == null) {
            return null;
        }

        if (!userName.startsWith("@@")) {
            return new WechatUserProxy(contact);
        } else {
            throw new IllegalArgumentException("The identifier is for group!");
        }
    }

    @Override public IProxyChatMember getMember(IProxyChatGroup group, String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);
        var userName = uniqueIdentifier.substring(7);

        var contact = ((WXContact) group.unwrap()).getMemberList().stream()
                .filter(it -> userName.equalsIgnoreCase(it.getUserName()))
                .findAny();
        return contact.map(WechatGroupMemeberProxy::new).orElse(null);
    }

    @Override public IProxyChatMember getMember(String groupIdentifier, String uniqueIdentifier) {
        return getMember(getGroup(groupIdentifier), uniqueIdentifier);
    }

    @Override public String getPlatformName() {
        return "wechat";
    }

    @Override public void sendMessage(String uniqueIdentifier, String msg) {
        checkIdentifier(uniqueIdentifier);
        var userName = uniqueIdentifier.substring(7);

        apiClient.sendMessage(msg, userName);
    }

    @Override public Object unwrap() {
        return apiClient;
    }

    public WXApiClient getAPIClient() {
        return apiClient;
    }

    private void checkIdentifier(String uniqueIdentifier) {
        if (!uniqueIdentifier.startsWith("wechat:")) {
            throw new IllegalArgumentException("This platform only support wechat type identifier");
        }
    }
}
