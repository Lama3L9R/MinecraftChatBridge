package icu.lama.minecraft.chatbridge.core.proxy.platform;

import icu.lama.minecraft.chatbridge.core.proxy.IProxy;

public interface IProxyChatGroup extends INamed, IProxy {
    void sendMessage(String msg);

    void replyTo(String messageIdentifier, String msg);
}
