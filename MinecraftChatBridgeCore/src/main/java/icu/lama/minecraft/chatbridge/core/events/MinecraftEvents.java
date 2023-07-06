package icu.lama.minecraft.chatbridge.core.events;

import icu.lama.minecraft.chatbridge.core.events.minecraft.MinecraftEvent;

public class MinecraftEvents {
    /**
     * This event will be triggered when minecraft server complete its initialization.
     * The event arg will be the instance of MinecraftServer and the source is null
     */
    public static MinecraftEvent<Object> onServerSetupComplete = new MinecraftEvent<>();

    public static MinecraftEvent<EventMinecraftChatMessage> onChatMessage = new MinecraftEvent<>();

    /**
     * Event source will be null
     */
    public static MinecraftEvent<Null> onServerBeginShutdown = new MinecraftEvent<>();

    public static MinecraftEvent<Null> onPlayerJoin = new MinecraftEvent<>();
    public static MinecraftEvent<Null> onPlayerLeave = new MinecraftEvent<>();
    public static MinecraftEvent<Null> onPlayerDeath = new MinecraftEvent<>();
}
