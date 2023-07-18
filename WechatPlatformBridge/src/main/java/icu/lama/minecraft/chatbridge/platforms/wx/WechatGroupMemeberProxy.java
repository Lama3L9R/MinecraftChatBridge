package icu.lama.minecraft.chatbridge.platforms.wx;

import icu.lama.minecraft.chatbridge.core.proxy.platform.ContactType;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatMember;
import icu.lama.minecraft.chatbridge.platforms.wx.api.data.WXGroupMember;

public class WechatGroupMemeberProxy implements IProxyChatMember {
    private final WXGroupMember member;

    public WechatGroupMemeberProxy(WXGroupMember member) {
        this.member = member;
    }

    @Override public Object unwrap() {
        return member;
    }

    @Override public String getUniqueIdentifier() {
        return "wechat:" + member.getUserName();
    }

    @Override public String getName() {
        return member.getNickName();
    }

    @Override public ContactType getType() {
        return ContactType.PRIVATE;
    }

    @Override public void dm(String msg) {
        WechatPlatformBridge.INSTANCE.getAPIClient().sendMessage(msg, member.getUserName());
    }
}
