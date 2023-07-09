package icu.lama.minecraft.chatbridge.fabric;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.Location;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Date;
import java.util.UUID;

public class MinecraftServerPlayerEntityProxy implements IProxyPlayer {
    private final ServerPlayerEntity player;

    public MinecraftServerPlayerEntityProxy(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override public UUID getUniqueID() {
        return player.getUuid();
    }

    @Override public String getDisplayName() {
        return player.getDisplayName().getString();
    }

    @Override public String getName() {
        return player.getEntityName();
    }

    @Override public Location getStandingLocation() {
        return new Location(
            player.getBlockX(),
            player.getBlockY(),
            player.getBlockZ(),
            player.getYaw(),
            player.getPitch(),
            new MinecraftServerWorldProxy(player.getServerWorld())
        );
    }

    @Override public void kick() {
        player.networkHandler.disconnect(Text.empty());
    }

    @Override public void kick(String reason) {
        player.networkHandler.disconnect(Text.of(reason));
    }

    @Override public void ban(Date till, String reason1, String reason2) {
        player.getServer()
                .getPlayerManager()
                .getUserBanList()
                .add(new BannedPlayerEntry(player.getGameProfile(), new Date(), reason1, till, reason2));
    }

    @Override public void sendMessage(String msg) {
        player.sendMessage(Text.of(msg));
    }

    @Override public void sendPacket(Object data) { // I guess this will works?
        if (data instanceof Packet<?>) {
            player.networkHandler.sendPacket((Packet<?>) data);
        } else {
            throw new IllegalArgumentException("Instance isn't a implementation of net.minecraft.network.packet.Packet");
        }
    }

    @Override public void sendPacket(byte[] data) { // Not sure about this...
        player.networkHandler.connection.channel.writeAndFlush(data);
    }

    @Override public Object unwrap() {
        return this.player;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }
}
