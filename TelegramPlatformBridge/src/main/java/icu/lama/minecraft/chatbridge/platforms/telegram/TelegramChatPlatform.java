package icu.lama.minecraft.chatbridge.platforms.telegram;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.typesafe.config.Config;
import icu.lama.minecraft.chatbridge.core.events.EventPlatformChatMessage;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.loader.annotations.ConfigInject;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Finalizer;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Initializer;
import icu.lama.minecraft.chatbridge.platforms.telegram.impl.TelegramChatGroupProxy;
import icu.lama.minecraft.chatbridge.platforms.telegram.impl.TelegramClientProxy;
import icu.lama.minecraft.chatbridge.platforms.telegram.impl.TelegramUserProxy;

import java.io.IOException;

public class TelegramChatPlatform {
    public static TelegramChatPlatform INSTANCE = new TelegramChatPlatform();
    private @ConfigInject Config config;
    private TelegramBot bot;
    private long chatId;

    private int eventOnServerSetupCompleteHandle = -1;
    private int eventOnServerBeginShutdownHandle = -1;
    private int eventOnChatMessageHandle = -1;
    private int eventOnPlayerJoinHandle = -1;
    private int eventOnPlayerLeaveHandle = -1;

    private void sendRaw(String msg) {
        bot.execute(new SendMessage(chatId, msg), new Callback<SendMessage, SendResponse>() {
            @Override public void onResponse(SendMessage request, SendResponse response) { }
            @Override public void onFailure(SendMessage request, IOException e) { }
        });
    }

    @Initializer public void init() {
        bot = new TelegramBot(config.getString("telegram.botToken"));
        chatId = config.getLong("telegram.chatId");

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                Message msg = update.message();
                if (msg == null || msg.text() == null || msg.chat().id() != chatId) { return; }

                PlatformEvents.onMessage.trigger(TelegramClientProxy.INSTANCE,
                        new EventPlatformChatMessage(
                                msg.text(),
                                new TelegramChatGroupProxy(msg.chat()),
                                new TelegramUserProxy(msg.from())
                        )
                );
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        eventOnServerSetupCompleteHandle = MinecraftEvents.onServerSetupComplete.subscribe((__, ___) -> sendRaw("[+] Server"));
        eventOnServerBeginShutdownHandle = MinecraftEvents.onServerBeginShutdown.subscribe((__, ___) -> sendRaw("[-] Server"));
        eventOnChatMessageHandle = MinecraftEvents.onChatMessage.subscribe((source, msg) -> {
            if (!msg.isCanceled()) {
                sendRaw(source.getName() + ": " + msg.getMessage());
            }
        });

        eventOnPlayerJoinHandle = MinecraftEvents.onPlayerJoin.subscribe((source, __) -> sendRaw("[+] " + source.getName()));
        eventOnPlayerLeaveHandle = MinecraftEvents.onPlayerLeave.subscribe((source, __) -> sendRaw("[-] " + source.getName()));
    }

    @Finalizer public void onShutdown() {
        MinecraftEvents.onServerSetupComplete.unsubscribe(eventOnServerSetupCompleteHandle);
        MinecraftEvents.onServerBeginShutdown.unsubscribe(eventOnServerBeginShutdownHandle);
        MinecraftEvents.onChatMessage.unsubscribe(eventOnChatMessageHandle);
        MinecraftEvents.onPlayerJoin.unsubscribe(eventOnPlayerJoinHandle);
        MinecraftEvents.onPlayerLeave.unsubscribe(eventOnPlayerLeaveHandle);

        bot.shutdown();

        System.out.println("Telegram platform proxy shutdown successfully");
    }

    public TelegramBot getBot() {
        return bot;
    }
}
