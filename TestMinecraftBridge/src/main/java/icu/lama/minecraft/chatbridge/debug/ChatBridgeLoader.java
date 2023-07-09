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
            if (line.startsWith("!")) {
                var command = line.substring(1).split(" ");
                sw: switch (command[0]) {
                    case "quit":
                        MinecraftEvents.onServerBeginShutdown.trigger(null, null);
                        break;
                    case "list-plugins":
                        System.out.println("[SYSTEM] >> Listing loaded plugins: ");
                        MinecraftChatBridge.getLoadedPlugins().forEach(it -> System.out.println("[SYSTEM] >> " + it.getIdentifier() + " " + it.getPluginStatus()));
                        break sw;
                    case "load-plugin":
                        if (command.length < 2) {
                            System.out.println("Not enough arguments");
                        } else {
                            System.out.println("Loading plugin " + command[1]);
                            var pl = MinecraftChatBridge.loadPlugin(command[1]);
                            System.out.println("Plugin " + pl.getIdentifier() + " is loaded now");
                        }
                        break sw;
                    case "unload-plugin":
                        if (command.length < 2) {
                            System.out.println("Not enough arguments");
                        } else {
                            System.out.println("Unloading plugin " + command[1]);
                            MinecraftChatBridge.unloadPlugin(command[1]);
                            System.out.println("Plugin " + command[1] + " is unloaded now");
                        }
                        break sw;
                    case "trigger-init":
                        if (command.length < 2) {
                            System.out.println("Not enough arguments");
                        } else {
                            var pl = MinecraftChatBridge.getPlugin(command[1]);
                            if (pl == null) {
                                System.out.println("No such plugin");
                            } else {
                                pl.init();
                            }
                        } // WechatPlatformBridge-1.0-SNAPSHOT-all.jar
                        break sw;
                    case "reload-config":
                        break sw;
                }
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
