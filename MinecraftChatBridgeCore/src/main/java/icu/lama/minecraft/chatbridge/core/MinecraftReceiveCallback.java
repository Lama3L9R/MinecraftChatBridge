package icu.lama.minecraft.chatbridge.core;

import java.util.UUID;

public interface MinecraftReceiveCallback {
    public void onReceive(String name, UUID uuid, String msg);
}