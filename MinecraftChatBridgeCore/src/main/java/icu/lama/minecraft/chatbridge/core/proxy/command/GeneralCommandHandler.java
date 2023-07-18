package icu.lama.minecraft.chatbridge.core.proxy.command;

import java.util.List;

public interface GeneralCommandHandler <Sender> {
    CommandExecutionResult handle(Sender sender, List<String> args);
}
