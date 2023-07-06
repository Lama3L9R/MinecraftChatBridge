package icu.lama.minecraft.chatbridge.core;

import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;

public interface ErrorCallback {
    public void onError(Throwable e, IPlatformProxy source);
}
