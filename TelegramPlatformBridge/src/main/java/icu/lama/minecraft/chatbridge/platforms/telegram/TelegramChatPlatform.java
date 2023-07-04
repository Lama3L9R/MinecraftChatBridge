package icu.lama.minecraft.chatbridge.platforms.telegram;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import icu.lama.minecraft.chatbridge.core.PlatformReceiveCallback;
import icu.lama.minecraft.chatbridge.core.binding.GeneralBindingDatabase;
import icu.lama.minecraft.chatbridge.core.binding.IBindingDatabase;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

public class TelegramChatPlatform implements IPlatformBridge {
    public static TelegramChatPlatform INSTANCE = new TelegramChatPlatform();
    private final IBindingDatabase database = new GeneralBindingDatabase();

    private PlatformReceiveCallback callback;
    private PlatformConfiguration config;

    private TelegramBot bot;
    private long chatId;

    @Override
    public void send(String name, UUID playerUUID, String msg) {
        sendRaw(name + ": " + msg);
    }

    @Override
    public void send(String msg) {
        sendRaw(msg);
    }

    private void sendRaw(String msg) {
        bot.execute(new SendMessage(chatId, msg), new Callback<SendMessage, SendResponse>() {
            @Override public void onResponse(SendMessage request, SendResponse response) { }
            @Override public void onFailure(SendMessage request, IOException e) { }
        });
    }

    @Override
    public String getPlatformName() {
        return "telegram";
    }

    @Override
    public boolean getAllowNSFWContent() {
        return true;
    }

    @Override
    public void setReceiveCallback(PlatformReceiveCallback callback) {
        this.callback = callback;
    }

    @Override
    public void setConfiguration(PlatformConfiguration config) {
        this.config = config;
    }

    @Override
    public void init() {
        bot = new TelegramBot(config.getCredentials("botToken"));
        chatId = (long) ((double) config.get("chatId"));

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                Message msg = update.message();
                if (msg == null || msg.text() == null || msg.chat().id() != chatId) { return; }

                callback.onReceive(this, msg.from().username() != null ? msg.from().username() : msg.from().firstName(), msg.from().id() + "", msg.text());
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        MinecraftEvents.onServerSetupComplete.subscribe((__, ___) -> sendRaw("[+] Server"));
        MinecraftEvents.onServerBeginShutdown.subscribe((__, ___) -> sendRaw("[-] Server"));
        MinecraftEvents.onPlayerJoin.subscribe((source, __) -> sendRaw("[+] " + source.getName()));
        MinecraftEvents.onPlayerLeave.subscribe((source, __) -> sendRaw("[-] " + source.getName()));

        PlatformEvents.onPlatformBridgeLoad.trigger(this, null);
    }

    @Override
    public @Nullable IBindingDatabase getBindingDatabase() {
        return database;
    }
}
