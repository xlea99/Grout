package net.xlea99.lifeguard.mixin.am_crocodile_ambush;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A grown Crocodile picks somewhere to go about once a minute, not as soon as it gets anywhere: Alex's Mobs asks one time in 7, and goals are asked
 * every other tick. This is on top of that. Targeted by name: Lifeguard isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.ai.CrocodileAIRandomSwimming")
public abstract class CrocodileAIRandomSwimmingMixin extends RandomStrollGoal {
    @Unique
    private static final int LIFEGUARD_ONE_IN = 85;

    private CrocodileAIRandomSwimmingMixin(PathfinderMob mob, double speed) {
        super(mob, speed);
    }

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void lifeguard$stayPut(CallbackInfoReturnable<Boolean> cir) {
        if (!this.mob.isBaby() && this.mob.getRandom().nextInt(LIFEGUARD_ONE_IN) != 0)
            cir.setReturnValue(false);
    }
}
