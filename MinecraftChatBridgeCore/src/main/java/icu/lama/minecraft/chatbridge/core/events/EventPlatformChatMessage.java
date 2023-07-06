package icu.lama.minecraft.chatbridge.core.events;

import icu.lama.minecraft.chatbridge.core.proxy.platform.INamed;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatMember;
import org.jetbrains.annotations.Nullable;

public class EventPlatformChatMessage {
    private final String message;
    private final INamed fromContact;
    private final @Nullable IProxyChatMember fromUser;

    public EventPlatformChatMessage(String message, @Nullable INamed fromContact, @Nullable IProxyChatMember fromUser) {
        this.message = message;
        this.fromContact = fromContact;
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public INamed getFromContact() {
        return fromContact;
    }

    public @Nullable IProxyChatMember getFromUser() {
        return fromUser;
    }
}
