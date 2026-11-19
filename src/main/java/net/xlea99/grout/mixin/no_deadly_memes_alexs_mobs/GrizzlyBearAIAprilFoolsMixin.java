package net.xlea99.grout.mixin.no_deadly_memes_alexs_mobs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.ai.GrizzlyBearAIAprilFools")
public abstract class GrizzlyBearAIAprilFoolsMixin {
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void grout$neverStart(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
