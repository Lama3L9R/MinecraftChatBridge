package icu.lama.minecraft.chatbridge.platforms.telegram.impl;

import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import icu.lama.minecraft.chatbridge.core.loader.PluginType;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Plugin;
import icu.lama.minecraft.chatbridge.core.proxy.platform.*;
import icu.lama.minecraft.chatbridge.platforms.telegram.TelegramChatPlatform;

import java.util.List;

@Plugin(name = "telegram", type = PluginType.PLATFORM_PROXY)
public class TelegramClientProxy implements IPlatformProxy {
    public static final TelegramClientProxy INSTANCE = new TelegramClientProxy();

    @Override public List<PlatformFeature> getSupportedFeatures() {
        return List.of(PlatformFeature.BASIC, PlatformFeature.DELETE, PlatformFeature.BUTTONS, PlatformFeature.MODIFY);
    }

    @Override public List<INamed> getAllContacts() {
        return List.of(); // Telegram doesn't support this at all...
    }

    @Override public INamed getContact(String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);

        var chat = TelegramChatPlatform.INSTANCE.getBot()
                .execute(new GetChat(uniqueIdentifier.substring(9)))
                .chat();


        if (chat.title() == null) {
            var userId = Long.parseLong(uniqueIdentifier.substring(9));

            return new TelegramUserProxy(TelegramChatPlatform.INSTANCE.getBot()
                    .execute(new GetChatMember(chat.id(), userId))
                    .chatMember().user());

        }

        return new TelegramChatGroupProxy(chat);
    }

    @Override public IProxyChatGroup getGroup(String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);
        var chat = TelegramChatPlatform.INSTANCE.getBot()
                .execute(new GetChat(uniqueIdentifier.substring(9)))
                .chat();

        return new TelegramChatGroupProxy(chat);
    }

    @Override public IProxyChatMember getMember(String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);

        var userId = Long.parseLong(uniqueIdentifier.substring(9));
        var user = TelegramChatPlatform.INSTANCE.getBot()
                .execute(new GetChatMember(userId, userId))
                .chatMember().user();


        return new TelegramUserProxy(user);
    }

    @Override public IProxyChatMember getMember(IProxyChatGroup group, String uniqueIdentifier) {
        checkIdentifier(uniqueIdentifier);

        var userId = Long.parseLong(uniqueIdentifier.substring(9));
        var user = TelegramChatPlatform.INSTANCE.getBot()
                .execute(new GetChatMember(group.getUniqueIdentifier().substring(9), userId))
                .chatMember().user();


        return new TelegramUserProxy(user);
    }

    @Override public IProxyChatMember getMember(String groupIdentifier, String uniqueIdentifier) {
        return getMember(getGroup(groupIdentifier), uniqueIdentifier);
    }

    @Override public String getPlatformName() {
        return "telegram";
    }

    @Override public void sendMessage(String uniqueIdentifier, String msg) {
        checkIdentifier(uniqueIdentifier);

        TelegramChatPlatform.INSTANCE.getBot().execute(new SendMessage(uniqueIdentifier.substring(9), msg));
    }

    private void checkIdentifier(String uniqueIdentifier) {
        if (!uniqueIdentifier.startsWith("telegram:")) {
            throw new IllegalArgumentException("This platform only support telegram type identifier");
        }
    }

    @Override public Object unwrap() {
        return TelegramChatPlatform.INSTANCE.getBot();
    }
}
