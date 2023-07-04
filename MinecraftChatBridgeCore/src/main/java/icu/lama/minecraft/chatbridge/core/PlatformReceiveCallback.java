package icu.lama.minecraft.chatbridge.core;

import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;

import java.util.UUID;

public interface PlatformReceiveCallback {
    public void onReceive(IPlatformBridge bridge, String name, String uniqueIdentifier, String msg);
}