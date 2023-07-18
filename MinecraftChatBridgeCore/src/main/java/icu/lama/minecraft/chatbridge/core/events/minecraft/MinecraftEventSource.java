package icu.lama.minecraft.chatbridge.core.events.minecraft;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MinecraftEventSource {
    private UUID uuid;
    private String name;
    private @Nullable IProxyPlayer player;
    private boolean isConsole;

    public MinecraftEventSource(UUID uuid, String name, @Nullable IProxyPlayer player, boolean isConsole) {
        this.uuid = uuid;
        this.name = name;
        this.player = player;
        this.isConsole = isConsole;
    }

    public @Nullable IProxyPlayer getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConsole() {
        return isConsole;
    }

    public void setConsole(boolean console) {
        isConsole = console;
    }
}
