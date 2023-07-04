package icu.lama.minecraft.chatbridge.fabric.mixin;

import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.fabric.FabricMinecraftBridge;
import icu.lama.minecraft.chatbridge.fabric.utils.UUIDHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;


@Mixin(targets = "xyz.nikitacartes.easyauth.commands.RegisterCommand")
public class RegisterCommandMixin implements OptionalMixin {
    @Inject(method = "register", at = @At(value = "HEAD"), cancellable = true)
    private static void injectCommandHandler(ServerCommandSource source, String pass1, String pass2, CallbackInfoReturnable<Integer> cir) throws Throwable {
        ServerPlayerEntity p = source.getPlayerOrThrow();
        UUID playerFakeUUID = UUIDHelper.getFakeUUID(p);
        var notBinded = MinecraftChatBridge
                .getPlatforms()
                .keySet()
                .stream()
                .filter(it -> FabricMinecraftBridge.getInstance().getConfig().forceBinding.contains(it))
                .map(MinecraftChatBridge::getPlatform)
                .filter(Objects::nonNull)
                .filter(it -> it.getBindingDatabase() != null)
                .filter(it -> it.getBindingDatabase().getName(playerFakeUUID) == null)
                .findAny();

        if (notBinded.isPresent()) {
            FabricMinecraftBridge.getInstance().getConfig().formats.noBinding.forEach(it ->
                p.sendMessage(Text.of(String.format(it, notBinded.get().getPlatformName())))
            );

            cir.setReturnValue(1);
            cir.cancel();
        }
    }
}
