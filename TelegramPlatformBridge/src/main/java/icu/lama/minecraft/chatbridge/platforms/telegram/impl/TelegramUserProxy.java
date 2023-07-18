package icu.lama.minecraft.chatbridge.platforms.telegram.impl;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import icu.lama.minecraft.chatbridge.core.proxy.platform.ContactType;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatMember;
import icu.lama.minecraft.chatbridge.platforms.telegram.TelegramChatPlatform;

public class TelegramUserProxy implements IProxyChatMember {
    private final User user;

    public TelegramUserProxy(User user) {
        this.user = user;
    }

    @Override public Object unwrap() {
        return this.user;
    }

    @Override public String getUniqueIdentifier() {
        return "telegram:" + this.user.id();
    }

    @Override public String getName() {
        return this.user.firstName() + this.user.lastName();
    }

    @Override public ContactType getType() {
        return ContactType.PRIVATE;
    }

    @Override public void dm(String msg) {
        TelegramChatPlatform.INSTANCE.getBot().execute(new SendMessage(this.user.id(), msg));
    }
}
