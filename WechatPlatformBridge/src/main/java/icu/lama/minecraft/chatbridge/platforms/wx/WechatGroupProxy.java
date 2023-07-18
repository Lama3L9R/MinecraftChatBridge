package icu.lama.minecraft.chatbridge.platforms.wx;

import icu.lama.minecraft.chatbridge.core.proxy.platform.ContactType;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatGroup;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;

public class WechatGroupProxy implements IProxyChatGroup {
    private final WXContact contact;

    public WechatGroupProxy(WXContact contact) {
        if (!contact.getUserName().startsWith("@@")) {
            throw new IllegalArgumentException("Can't proxy none-group contact!");
        }

        this.contact = contact;
    }

    @Override public Object unwrap() {
        return contact;
    }

    @Override public String getUniqueIdentifier() {
        return "wechat:" + contact.getUserName();
    }

    @Override public String getName() {
        return contact.getNickName();
    }

    @Override public ContactType getType() {
        return ContactType.GROUP;
    }

    @Override public void sendMessage(String msg) {
        WechatPlatformBridge.INSTANCE.getAPIClient().sendMessage(msg, contact.getUserName());
    }

    @Override public void replyTo(String messageIdentifier, String msg) { // Not supported at all... Wechat sucks
        WechatPlatformBridge.INSTANCE.getAPIClient().sendMessage(msg, contact.getUserName());
    }
}
