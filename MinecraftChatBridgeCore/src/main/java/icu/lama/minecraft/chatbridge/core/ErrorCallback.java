package icu.lama.minecraft.chatbridge.core;

import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;

public interface ErrorCallback {
    public void onError(Throwable e, IPlatformBridge source);
}
