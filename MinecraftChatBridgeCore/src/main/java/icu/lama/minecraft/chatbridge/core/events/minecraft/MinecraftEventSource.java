package icu.lama.minecraft.chatbridge.core.events.minecraft;

import java.util.UUID;

public class MinecraftEventSource {
    private UUID uuid;
    private String name;
    private boolean isConsole;

    public MinecraftEventSource(UUID uuid, String name, boolean isConsole) {
        this.uuid = uuid;
        this.name = name;
        this.isConsole = isConsole;
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
