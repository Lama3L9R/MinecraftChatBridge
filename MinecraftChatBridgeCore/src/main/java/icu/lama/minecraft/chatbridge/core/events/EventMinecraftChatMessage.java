package icu.lama.minecraft.chatbridge.core.events;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import org.jetbrains.annotations.Nullable;

public class EventMinecraftChatMessage implements Cancellable {
    private boolean isCanceled = false;
    private String message;
    private final @Nullable IProxyPlayer from;
    private final boolean isConsole;

    public EventMinecraftChatMessage(String message, @Nullable IProxyPlayer from, boolean isConsole) {
        this.message = message;
        this.from = from;
        this.isConsole = isConsole;
    }

    @Override public void cancel() {
        this.isCanceled = true;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public @Nullable IProxyPlayer getFrom() {
        return from;
    }

    public boolean isConsole() {
        return isConsole;
    }
}
