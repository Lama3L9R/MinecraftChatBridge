package icu.lama.minecraft.chatbridge.core.proxy;

import java.util.UUID;

/**
 * A proxy class of MinecraftPlayer.
 * This class will have different implementation in different MinecraftServer Implementation
 * So you should avoid using any server-specified api
 */
public interface IProxyPlayer {
    /**
     * Gets the uuid of the player
     * @return uuid of the player
     */
    UUID getUniqueID();

    /**
     * Gets the display name of the player (may contain double-s character)
     * @return this display name of the player
     */
    String getDisplayName();

    /**
     * Gets the actual name of the player
     * @return the name of the player
     */
    String getName();

    /**
     * Get the type of the world where the player is standing
     * @return the type of the world where the player is standing
     */
    String getCurrentWorldType();

    /**
     * Kick out the player
     */
    void kick();

    /**
     * Kick out the player
     * @param reason reason of the kick
     */
    void kick(String reason);

    /**
     * ALERT: THIS IS A RISKY FUNCTION AND IT MAY NOT BE IMPLEMENTED IN EVERYWHERE
     * Send a packet to the player
     * @param data packet object
     * @throws UnsupportedOperationException May throw in some implementation of MinecraftServer
     */
    @Risky void sendPacket(Object data);

    /**
     * ALERT: THIS IS A RISKY FUNCTION AND IT MAY NOT BE IMPLEMENTED IN EVERYWHERE
     * Send a packet to the player
     * @param data packet in bytes
     * @throws UnsupportedOperationException May throw in some implementation of MinecraftServer
     */
    @Risky void sendPacket(byte[] data);

    /**
     * Get the original player instance
     * @return original player instance
     */
    Object unwrap();
}
