package icu.lama.minecraft.chatbridge.core.proxy.command;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatMember;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleCommandManagerImpl implements CommandManager {
    private final HashMap<String, GeneralCommandHandler<MinecraftSender>> minecraftHandlers = new HashMap<>();
    private final HashMap<String, GeneralCommandHandler<IProxyChatMember>> platformHandlers = new HashMap<>();

    @Override public void registerMinecraft(String command, GeneralCommandHandler<MinecraftSender> handler) {
        this.minecraftHandlers.put(command, handler);
    }

    @Override public void registerPlatform(String command, GeneralCommandHandler<IProxyChatMember> handler) {
        this.platformHandlers.put(command, handler);
    }

    @Override public void removeMinecraft(String command) {
        this.minecraftHandlers.remove(command);
    }

    @Override public void removePlatform(String command) {
        this.platformHandlers.remove(command);
    }

    @Override public CommandExecutionResult dispatchMinecraft(String raw, @Nullable IProxyPlayer sender) {
        ArrayList<String> args = new ArrayList<>(List.of(raw.split(" ")));
        var command = args.remove(0);

        if (minecraftHandlers.containsKey(command)) {
            return minecraftHandlers.get(command).handle(new MinecraftSender(sender == null, sender), args);
        }

        return null;
    }

    @Override public CommandExecutionResult dispatchPlatform(String raw, IProxyChatMember sender) {
        ArrayList<String> args = new ArrayList<>(List.of(raw.split(" ")));
        var command = args.remove(0);

        if (minecraftHandlers.containsKey(command)) {
            return platformHandlers.get(command).handle(sender, args);
        }

        return null;
    }
}
