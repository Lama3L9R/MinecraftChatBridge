package icu.lama.minecraft.chatbridge.core.proxy.minecraft;

import icu.lama.minecraft.chatbridge.core.proxy.IProxy;
import icu.lama.minecraft.chatbridge.core.proxy.Risky;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * A proxy class of MinecraftServer.
 * This class will have different implementation in different MinecraftServer Implementation
 * So you should avoid using any server-specified api
 */
public interface IMinecraftServerProxy extends IProxy {
    /**
     * Get all online players
     * @return list of minecraft player instance
     */
    List<IProxyPlayer> getOnlinePlayers();

    /**
     * Get motd of the server
     * @return motd of the server
     */
    List<String> getMotd();

    /**
     * Get IProxyPlayer by uuid
     * @param uuid uuid of the player
     * @return IProxyPlayer instance
     */
    @Nullable IProxyPlayer getPlayer(@NotNull UUID uuid);

    /**
     * Get IProxyPlayer by name
     * @param name name of the player
     * @return IProxyPlayer instance
     */
    @Nullable IProxyPlayer getPlayer(@NotNull String name);

    /**
     * Get the type of the server
     * @return type of the server
     */
    ServerType getServerType();

    /**
     * Broadcast message to server
     * @param msg message that you want to broadcast
     */
    void broadcast(String msg);

    /**
     * ALERT: THIS IS A RISKY FUNCTION AND IT MAY NOT BE IMPLEMENTED IN EVERYWHERE
     * Send a packet to all players
     * @param data packet object
     * @throws UnsupportedOperationException May throw in some implementation of MinecraftServer
     */
    @Risky void broadcastPacket(Object data);

    /**
     * ALERT: THIS IS A RISKY FUNCTION AND IT MAY NOT BE IMPLEMENTED IN EVERYWHERE
     * Send a packet to all players
     * @param data packet in bytes
     * @throws UnsupportedOperationException May throw in some implementation of MinecraftServer
     */
    @Risky void broadcastPacket(byte[] data);
}
