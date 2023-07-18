package icu.lama.minecraft.chatbridge.core.proxy.command;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import org.jetbrains.annotations.Nullable;

public class MinecraftSender {
    private final boolean isConsole;
    private final @Nullable IProxyPlayer player;

    public MinecraftSender(boolean isConsole, @Nullable IProxyPlayer player) {
        this.isConsole = isConsole;
        this.player = player;
    }

    public boolean isConsole() {
        return isConsole;
    }

    public @Nullable IProxyPlayer getPlayer() {
        return player;
    }
}
