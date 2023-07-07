package icu.lama.minecraft.chatbridge.debug.impl;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IMinecraftServerProxy;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.ServerType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ExampleProxyServer implements IMinecraftServerProxy {

    @Override
    public Object unwrap() {
        return this;
    }

    @Override
    public List<IProxyPlayer> getOnlinePlayers() {
        return List.of();
    }

    @Override
    public List<String> getMotd() {
        return List.of("Example motd");
    }

    @Override
    public @Nullable IProxyPlayer getPlayer(@NotNull UUID uuid) {
        return new ExampleProxyPlayer(uuid.toString());
    }

    @Override
    public @Nullable IProxyPlayer getPlayer(@NotNull String name) {
        return new ExampleProxyPlayer(name);
    }

    @Override
    public ServerType getServerType() {
        return ServerType.OTHER;
    }

    @Override
    public void broadcast(String msg) {
        System.out.println("Broadcast >> " + msg);
    }

    @Override
    public void broadcastPacket(Object data) {
        System.out.println("BroadcastPacket >> " + data);

    }

    @Override
    public void broadcastPacket(byte[] data) {
        System.out.println("BroadcastPacket >> " + Arrays.toString(data));
    }
}
