package icu.lama.minecraft.chatbridge.platforms.wx;

import icu.lama.minecraft.chatbridge.core.proxy.platform.ContactType;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatMember;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXContact;

public class WechatUserProxy implements IProxyChatMember {
    private final WXContact contact;

    public WechatUserProxy(WXContact contact) {
        if (contact.getUserName().startsWith("@@")) {
            throw new IllegalArgumentException("Can't proxy group contact!");
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
        return ContactType.PRIVATE;
    }

    @Override public void dm(String msg) {
        WechatPlatformBridge.INSTANCE.getAPIClient().sendMessage(msg, contact.getUserName());
    }
}
