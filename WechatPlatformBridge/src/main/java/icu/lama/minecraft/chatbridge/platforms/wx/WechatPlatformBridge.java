package icu.lama.minecraft.chatbridge.platforms.wx;

import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.PlatformReceiveCallback;
import icu.lama.minecraft.chatbridge.core.binding.IBindingDatabase;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import icu.lama.minecraft.chatbridge.platforms.wx.api.WXApiClient;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;
import icu.lama.minecraft.chatbridge.platforms.wx.api.out.RequestBase;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WechatPlatformBridge implements IPlatformBridge {
    @SuppressWarnings("unused")
    public static WechatPlatformBridge INSTANCE = new WechatPlatformBridge();
    private static final Pattern SPLIT_NAME_CONTENT = Pattern.compile("[@a-zA-z0-9]*:(<br\\/>)?(.+)");

    private final HashMap<String, String> uidToName = new HashMap<>();
    private PlatformReceiveCallback callback;
    private PlatformConfiguration config;
    private WXApiClient apiClient;
    private WXContact groupContact;

    @Override
    public void send(String name, UUID playerUUID, String msg) {
        try {
            apiClient.sendMessage("[" + name + "]: " + msg, groupContact.getUserName());
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
        String cookies = new String(Base64.getDecoder().decode(this.config.getCredentials("cookie")), StandardCharsets.UTF_8);
        String passTicket = this.config.getCredentials("passTicket");

        RequestBase base = new RequestBase(
            Integer.parseInt(this.config.getCredentials("wxuin")),
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
                            Matcher content = SPLIT_NAME_CONTENT.matcher(it.getContent());

                            this.callback.onReceive(this, uidToName.get(content.group(1)), content.group(3));
                        });
            }, (e) -> MinecraftChatBridge.throwException(e, this));

            String targetGroupName = "" + this.config.get("group");
            apiClient.queryAllContact().stream()
                    .filter(it -> it.getMemberCount() > 0 && it.getUserName().startsWith("@@"))
                    .filter(it -> it.getNickName().equals(targetGroupName)).findFirst().ifPresentOrElse((target) -> groupContact = target,
                            () -> { throw new RuntimeException("Group not found!"); });

            groupContact.getMemberList().forEach((it) -> uidToName.put(it.getUserName(), it.getDisplayName()));

            apiClient.startPollMessages();
        } catch (Exception e) {
            MinecraftChatBridge.throwException(e, this);
        }
    }

    @Override
    public IBindingDatabase getBindingDatabase() {
        return null;
    }

}
