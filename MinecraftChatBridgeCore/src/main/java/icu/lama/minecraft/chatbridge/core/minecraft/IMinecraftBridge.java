package icu.lama.minecraft.chatbridge.core.minecraft;


import icu.lama.minecraft.chatbridge.core.MinecraftReceiveCallback;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The implementation of this class should call <code>MinecraftChatBridge.init()</code> somewhere
 */
public interface IMinecraftBridge {
    /**
     * Send a msg to target platform
     * @param name from chat user's name (will never be player name)
     * @param uniqueIdentifier unique identifier for chat platform user
     * @param uuid from player uuid (Will be null if player never entered server or never binds his name or binding feature is disabled)
     * @param msg message content
     */
    void send(String name, String uniqueIdentifier, @Nullable UUID uuid, IPlatformBridge bridge, String msg);

    /**
     * Set the callback that should be called by minecraft bridge when received a message.
     * Should be called when MinecraftChatBridge loads and never should be called by anything else.
     * @param callback the callback method when platform bridge received a message.
     */
    public void setReceiveCallback(MinecraftReceiveCallback callback);
}
