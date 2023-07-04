package icu.lama.minecraft.chatbridge.fabric;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.MinecraftReceiveCallback;
import icu.lama.minecraft.chatbridge.core.config.ChatBridgeConfiguration;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.minecraft.MinecraftEventSource;
import icu.lama.minecraft.chatbridge.core.minecraft.IMinecraftBridge;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricMinecraftBridge implements ModInitializer, IMinecraftBridge {
   private final Logger LOGGER = LoggerFactory.getLogger("MinecraftChatBridge");
   private final Gson GSON = new Gson();
   private final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "MinecraftChatBridge.json");
   private final HashMap<UUID, String> bindQueue = new HashMap<>();
   private static FabricMinecraftBridge INSTANCE;
   private ModConfig config;
   private MinecraftReceiveCallback callback;
   private MinecraftServer serverInstance;

   public FabricMinecraftBridge() {
      INSTANCE = this;
   }

   @Override public void onInitialize() {
      if (!this.configFile.exists()) {
         try {
            this.configFile.createNewFile();
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      try {
         // Forward events
         ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            this.serverInstance = server;

            MinecraftEvents.onServerSetupComplete.trigger(null, server);
         });

         ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
            MinecraftEvents.onServerBeginShutdown.trigger(null, null);
         });

         ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity sPlayer = handler.getPlayer();
            MinecraftEvents.onPlayerJoin.trigger(new MinecraftEventSource(sPlayer.getUuid(), sPlayer.getName().getString(), false), null);
         });

         ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity sPlayer = handler.getPlayer();
            MinecraftEvents.onPlayerLeave.trigger(new MinecraftEventSource(sPlayer.getUuid(), sPlayer.getName().getString(), false), null);
         });

         // Load core
         this.config = this.GSON.fromJson(new FileReader(this.configFile), new TypeToken<>() {});

         MinecraftChatBridge.init(config.core, config.platformConf, this, (e, source) ->
                 this.LOGGER.error("Runtime error thrown by " + source.getPlatformName(), e)
         );
         LOGGER.info("MinecraftChatBridge initialized complete");

         ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) ->
                 this.callback.onReceive(sender.getName().getString(), sender.getUuid(), message.getSignedContent())
         );
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Override public void send(String name, String uniqueIdentifier, @Nullable UUID uuid, IPlatformBridge bridge, String msg) {
      if (this.serverInstance != null) {
         if (msg.startsWith("/bind")) {
            var args = msg.split(" ", 2);
            if (args.length == 2) {
               var playerUUID = bindQueue.entrySet()
                       .stream()
                       .filter(it -> it.getValue().equalsIgnoreCase(args[1])).findFirst();
               if (playerUUID.isPresent()) {
                  if (bridge.getBindingDatabase() != null) {
                     bridge.getBindingDatabase().update(uniqueIdentifier, playerUUID.get().getKey());
                     this.config.formats.bindSuccess.forEach(it -> {
                        bridge.send(String.format(it, bridge.getPlatformName()));
                     });
                  }
               }
            }
         }

         config.formats.messageFormat.forEach(it ->
                 this.serverInstance.getPlayerManager().broadcast(Text.of(String.format(it, bridge.getPlatformName(), name, msg)), false)
         );
      }
   }

   @Override public void setReceiveCallback(MinecraftReceiveCallback callback) {
      this.callback = callback;
   }

   public ModConfig getConfig() {
      return config;
   }

   /**
    * Add a uuid to bind queue
    * @param uuid player uuid
    * @return bind key
    */
   public String addBindQueue(UUID uuid) {
      var key = UUID.randomUUID().toString();
      bindQueue.put(uuid, key);

      return key;
   }

   public static FabricMinecraftBridge getInstance() {
      return INSTANCE;
   }

}
