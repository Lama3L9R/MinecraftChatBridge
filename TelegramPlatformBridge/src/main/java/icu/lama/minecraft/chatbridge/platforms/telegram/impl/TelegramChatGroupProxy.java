package icu.lama.minecraft.chatbridge.platforms.telegram.impl;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import icu.lama.minecraft.chatbridge.core.proxy.platform.ContactType;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatGroup;
import icu.lama.minecraft.chatbridge.platforms.telegram.TelegramChatPlatform;

public class TelegramChatGroupProxy implements IProxyChatGroup {
    private final Chat chat;

    public TelegramChatGroupProxy(Chat chat) {
        if (chat.title() == null) {
            throw new RuntimeException("Private chat is proxy-ed to Group!");
        }

        this.chat = chat;
    }

    @Override
    public Object unwrap() {
        return this.chat;
    }

    @Override
    public String getUniqueIdentifier() {
        return "telegram:" + chat.id();
    }

    @Override
    public String getName() {
        return chat.title();
    }

    @Override
    public ContactType getType() {
        return ContactType.GROUP;
    }

    @Override
    public void sendMessage(String msg) {

    }

    @Override
    public void replyTo(String messageIdentifier, String msg) {
        TelegramChatPlatform.INSTANCE.getBot().execute(new SendMessage(this.chat.id(), msg));
    }
}
