package icu.lama.minecraft.chatbridge.core.events;

import icu.lama.minecraft.chatbridge.core.events.platform.PlatformEvent;

public class PlatformEvents {
    public static PlatformEvent<Null> onBridgeLoad = new PlatformEvent<>();
    public static PlatformEvent<Null> onBridgeShutdown = new PlatformEvent<>();
    public static PlatformEvent<EventPlatformChatMessage> onMessage = new PlatformEvent<>();
}
