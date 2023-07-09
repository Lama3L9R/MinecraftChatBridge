package icu.lama.minecraft.chatbridge.core;

import icu.lama.minecraft.chatbridge.core.loader.BridgePlugin;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;

public interface ErrorCallback {
    void onError(Throwable e, BridgePlugin source);
    void onError(Throwable e, IPlatformProxy platform);
}
