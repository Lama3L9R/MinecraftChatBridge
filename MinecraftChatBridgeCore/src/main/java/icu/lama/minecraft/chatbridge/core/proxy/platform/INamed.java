package icu.lama.minecraft.chatbridge.core.proxy.platform;

public interface INamed {
    String getUniqueIdentifier();

    String getName();

    ContactType getType();
}
