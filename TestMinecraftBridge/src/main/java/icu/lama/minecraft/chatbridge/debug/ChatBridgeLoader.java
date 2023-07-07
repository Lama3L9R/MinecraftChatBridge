package icu.lama.minecraft.chatbridge.debug;

import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.events.EventMinecraftChatMessage;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.events.minecraft.MinecraftEventSource;
import icu.lama.minecraft.chatbridge.debug.impl.ExampleProxyServer;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public class ChatBridgeLoader {
    public static void main(String... args) throws Throwable {
        MinecraftChatBridge.init(new File("chat-bridge"), new ExampleProxyServer());
        System.out.println("Platform bridge tester started! you can now send messages. type '!quit' to exit");

        PlatformEvents.onMessage.subscribe((source, message) -> {
            if (message.getFromUser() != null) {
                System.out.println(message.getFromUser().getName() + " in " + message.getFromContact().getName() + " >> " + message.getMessage());
            } else {
                System.out.println("null in " + message.getFromContact().getName() + " >> " + message.getMessage());
            }
        });

        MinecraftEvents.onServerSetupComplete.trigger(null, null);
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("!quit")) {
                MinecraftEvents.onServerBeginShutdown.trigger(null, null);
                break;
            } else {
                var source = new MinecraftEventSource(UUID.nameUUIDFromBytes("Console".getBytes()), "Console", null, true);
                var event = new EventMinecraftChatMessage(line, null, true);

                MinecraftEvents.onChatMessage.trigger(source, event);

                if (!event.isCanceled()) {
                    System.out.println("[" + UUID.randomUUID() + "] [Console] Send: " + line);
                }
             }
        }
    }
}
