package icu.lama.minecraft.chatbridge.fabric.command;

import icu.lama.minecraft.chatbridge.fabric.FabricMinecraftBridge;
import icu.lama.minecraft.chatbridge.fabric.utils.UUIDHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.*;

import javax.swing.*;

import static net.minecraft.server.command.CommandManager.*;

public class BindCommand {
    private static final BindCommand INSTANCE = new BindCommand();
    public void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(
            literal("bind").executes(ctx -> {
                var player = ctx.getSource().getPlayerOrThrow();
                var key = FabricMinecraftBridge.getInstance().addBindQueue(UUIDHelper.getFakeUUID(player));

                var button = Text
                        .literal(FabricMinecraftBridge.getInstance().getConfig().formats.bindHintButtonStyle)
                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "/bind " + key)));
                FabricMinecraftBridge.getInstance().getConfig().formats.bindHint.forEach(it -> {
                    var chunks = String.format(it, key).split("<ButtonPlaceholder>", 2);
                    if (chunks.length == 1) {
                        player.sendMessage(Text.of(chunks[0]));
                    } else {
                        player.sendMessage(Text.literal(chunks[0]).append(button).append(Text.of(chunks[1])));
                    }
                });

                return 1;
            }
        ))));
    }

    public static BindCommand getInstance() {
        return INSTANCE;
    }
}
