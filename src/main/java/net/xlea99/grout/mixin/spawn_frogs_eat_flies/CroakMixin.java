package net.xlea99.grout.mixin.spawn_frogs_eat_flies;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Croak;
import net.minecraft.world.entity.animal.frog.Frog;
import net.xlea99.grout.entry.spawn_frogs_eat_flies.FrogSnack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A frog with nowhere to walk picks something idle to do, and that's a croak 3 times in 7. So one sitting still to stare would croak again and again, and it can't use its
 * tongue mid-croak, since the end of the croak resets its pose and cuts the tongue off. Once it's going for a fly it starts no new croaks: one already going just finishes.
 */
@Mixin(Croak.class)
public abstract class CroakMixin {
    @Inject(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/frog/Frog;)Z", at = @At("HEAD"), cancellable = true)
    private void grout$notNow(ServerLevel level, Frog frog, CallbackInfoReturnable<Boolean> cir) {
        if (((FrogSnack.Snacker) frog).grout$snacking())
            cir.setReturnValue(false);
    }
}
