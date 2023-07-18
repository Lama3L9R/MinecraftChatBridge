package icu.lama.minecraft.chatbridge.core.proxy.platform;

import icu.lama.minecraft.chatbridge.core.proxy.IProxy;
import icu.lama.minecraft.chatbridge.core.proxy.Risky;

public interface IProxyChatMember extends INamed, IProxy {
    /**
     * Send direct message
     * @param msg message content
     */
    @Risky void dm(String msg);

}
