package icu.lama.minecraft.chatbridge.core.events;

import icu.lama.minecraft.chatbridge.core.events.platform.PlatformEvent;

public class PlatformEvents {
    public static PlatformEvent<Null> onPlatformBridgeLoad = new PlatformEvent<>();
    public static PlatformEvent<Null> onPlatformBridgeShutdown = new PlatformEvent<>();
}
