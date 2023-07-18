package icu.lama.minecraft.chatbridge.core.proxy.command;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IProxyChatMember;
import org.jetbrains.annotations.Nullable;

public interface CommandManager {
    void registerMinecraft(String command, GeneralCommandHandler<MinecraftSender> handler);

    void registerPlatform(String command, GeneralCommandHandler<IProxyChatMember> handler);

    void removeMinecraft(String command);

    void removePlatform(String command);

    CommandExecutionResult dispatchMinecraft(String raw, @Nullable IProxyPlayer sender);

    CommandExecutionResult dispatchPlatform(String raw, IProxyChatMember sender);
}
