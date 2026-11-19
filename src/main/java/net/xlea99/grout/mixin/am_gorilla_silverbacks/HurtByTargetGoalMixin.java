package net.xlea99.grout.mixin.am_gorilla_silverbacks;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.xlea99.grout.entry.am_gorilla_silverbacks.Silverbacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Two Silverbacks fighting are a private matter: a hit between them doesn't call every Gorilla within 32 blocks onto the
 * hitter, as vanilla's {@code alertOthers} would. Extends {@code TargetGoal} only to reach its {@code mob}.
 */
@Mixin(HurtByTargetGoal.class)
public abstract class HurtByTargetGoalMixin extends TargetGoal {
    private HurtByTargetGoalMixin(Mob mob, boolean mustSee) {
        super(mob, mustSee);
    }

    @Inject(method = "alertOthers", at = @At("HEAD"), cancellable = true)
    private void grout$rivalsFightAlone(CallbackInfo ci) {
        if (this.mob instanceof Silverbacks.Holder holder && holder.grout$silverbacks().keepsFightPrivate(this.mob.getLastHurtByMob()))
            ci.cancel();
    }
}
