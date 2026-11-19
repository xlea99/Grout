package net.xlea99.grout.mixin.am_slightly_sexier_gorilla;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.xlea99.grout.entry.am_slightly_sexier_gorilla.GorillaMarch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * No foraging while the troop marches: the nearest leaves are what kept troops circling trees. Extends {@code MoveToBlockGoal}
 * only to reach its {@code mob}. Targeted by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.ai.GorillaAIForageLeaves")
public abstract class GorillaAIForageLeavesMixin extends MoveToBlockGoal {
    private GorillaAIForageLeavesMixin(PathfinderMob mob, double speed, int range) {
        super(mob, speed, range);
    }

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void grout$notWhileMarching(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob instanceof GorillaMarch.Holder holder && holder.grout$march().troopMarching())
            cir.setReturnValue(false);
    }
}
