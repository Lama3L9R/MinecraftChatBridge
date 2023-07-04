package icu.lama.minecraft.chatbridge.debug;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.MinecraftReceiveCallback;
import icu.lama.minecraft.chatbridge.core.config.ChatBridgeConfiguration;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.minecraft.IMinecraftBridge;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class BridgeLoader implements IMinecraftBridge {
    private static MinecraftReceiveCallback callback = MinecraftChatBridge.minecraftReceiveCallback;
    private static final Gson gson = new Gson();

    private static final BridgeLoader bridge = new BridgeLoader();

    public static void main(String[] args) throws Exception {
        File conf = new File("config.json");
        if (!conf.exists()) {
            conf.createNewFile();
            return;
        }
        BridgeMixedConfig config = gson.fromJson(new FileReader(conf), new TypeToken<>() { });
        PlatformEvents.onPlatformBridgeLoad.subscribe((source, __) -> {
            System.out.println("[+] " + source.getPlatformName());
        });

        MinecraftChatBridge.init(config.core, config.platformConf, bridge);
        System.out.println("Platform bridge tester started! you can now send messages. type '!quit' to exit");

        MinecraftEvents.onServerSetupComplete.trigger(null, null);
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("!quit")) {
                MinecraftEvents.onServerBeginShutdown.trigger(null, null);
                return;
            } else {
                callback.onReceive("Console", UUID.randomUUID(), line);
                System.out.println("[" + UUID.randomUUID() + "] [Console] Send: " + line);
            }
        }
    }

    @Override public void send(String name, String uniqueIdentifier, UUID uuid, IPlatformBridge bridge, String msg) {
        System.out.println("[" + uuid + "] [" + uniqueIdentifier + "] [" + name + "] [" + bridge.getPlatformName() + "]: " + msg);
    }

    @Override public void setReceiveCallback(MinecraftReceiveCallback callback) {
        BridgeLoader.callback = callback;
    }

    public class BridgeMixedConfig {
        public ChatBridgeConfiguration core;
        public Map<String, PlatformConfiguration> platformConf;
    }
}
