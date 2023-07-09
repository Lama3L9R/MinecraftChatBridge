package icu.lama.minecraft.chatbridge.fabric;

import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.events.EventMinecraftChatMessage;
import icu.lama.minecraft.chatbridge.core.events.MinecraftEvents;
import icu.lama.minecraft.chatbridge.core.events.PlatformEvents;
import icu.lama.minecraft.chatbridge.core.events.minecraft.MinecraftEventSource;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import icu.lama.minecraft.chatbridge.core.proxy.command.CommandManager;
import icu.lama.minecraft.chatbridge.core.proxy.command.SimpleCommandManagerImpl;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IMinecraftServerProxy;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.ServerType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricMinecraftBridge implements ModInitializer, IMinecraftServerProxy {
    private final Logger LOGGER = LoggerFactory.getLogger("MinecraftChatBridge");
    private final CommandManager commandManager = new SimpleCommandManagerImpl();
    public static FabricMinecraftBridge INSTANCE;
    private MinecraftServer serverInstance;

    public FabricMinecraftBridge() {
        INSTANCE = this;
    }

    @Override public void onInitialize() {
        try {
            // Load core
            MinecraftChatBridge.init(new File("chat-bridge"), this);
            LOGGER.info("MinecraftChatBridge initialized complete");

            // Forward events
            ServerLifecycleEvents.SERVER_STARTING.register(server -> this.serverInstance = server);

            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                this.serverInstance = server;

                MinecraftEvents.onServerSetupComplete.trigger(null, server);
            });

            ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
                MinecraftEvents.onServerBeginShutdown.trigger(null, null);
            });

            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                ServerPlayerEntity sPlayer = handler.getPlayer();
                MinecraftEvents.onPlayerJoin.trigger(
                        new MinecraftEventSource(
                                sPlayer.getUuid(),
                                sPlayer.getName().getString(),
                                new MinecraftServerPlayerEntityProxy(sPlayer),
                                false
                        ),
                        null
                );
            });

            ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
                ServerPlayerEntity sPlayer = handler.getPlayer();
                MinecraftEvents.onPlayerLeave.trigger(
                        new MinecraftEventSource(
                                sPlayer.getUuid(),
                                sPlayer.getName().getString(),
                                new MinecraftServerPlayerEntityProxy(sPlayer),
                                false
                        ),
                        null
                );
            });

            ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
                var from = new MinecraftServerPlayerEntityProxy(sender);
                var msg = new EventMinecraftChatMessage(message.getContent().getString(), from, false);

                MinecraftEvents.onChatMessage.trigger(
                        new MinecraftEventSource(
                                sender.getUuid(),
                                sender.getName().getString(),
                                from,
                                false
                        ),
                        msg
                );

                if (msg.isCanceled()) {
                    // It looks like it is impossible to cancel this event...
                    // I guess it is possible to do it with mixins
                    // lazy...
                }
            });

            PlatformEvents.onMessage.subscribe((source, msg) -> {
                var config = MinecraftChatBridge.getConfig();

                this.serverInstance.getPlayerManager().broadcast(Text.of(String.format(MinecraftChatBridge.getConfig().getString("loader.format"), source.getPlatformName(), msg.getFromUser().getName(), msg.getMessage())), false);
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FabricMinecraftBridge getInstance() {
        return INSTANCE;
    }

    @Override public Object unwrap() {
        return this.serverInstance;
    }

    @Override public List<IProxyPlayer> getOnlinePlayers() {
        return serverInstance.getPlayerManager().getPlayerList()
                .stream()
                .map(MinecraftServerPlayerEntityProxy::new)
                .collect(Collectors.toList());
    }
    @Override public List<String> getMotd() {
        return null;
    }

    @Override public @Nullable IProxyPlayer getPlayer(@NotNull UUID uuid) {
        return new MinecraftServerPlayerEntityProxy(serverInstance.getPlayerManager().getPlayer(uuid));
    }

    @Override public @Nullable IProxyPlayer getPlayer(@NotNull String name) {
        return new MinecraftServerPlayerEntityProxy(serverInstance.getPlayerManager().getPlayer(name));
    }

    @Override public ServerType getServerType() {
        return ServerType.MINECRAFT_MODDED_SERVER;
    }

    @Override public void broadcast(String msg) {

    }

    @Override public void broadcastPacket(Object data) {
        throw new NotImplementedException("Not supported operation");
    }

    @Override public void broadcastPacket(byte[] data) {
        throw new NotImplementedException("Not supported operation");
    }

    @Override public CommandManager getCommandManager() {
        return this.commandManager;
    }
}
