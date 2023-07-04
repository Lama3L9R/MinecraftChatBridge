package icu.lama.minecraft.chatbridge.fabric.mixin;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "xyz.nikitacartes.easyauth.mixin.ServerPlayerEntityMixin")
@Pseudo
public class ServerPlayerEntityMixinMixin {
    @Inject(method = "getAuthMessage", at = @At("HEAD"), cancellable = true)
    public void getAuthMessageOverride(CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(Text.of("ehf0-n24-0th438-0fh280fh24"));
        cir.cancel();
    }
}
