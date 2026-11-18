package net.xlea99.lifeguard.mixin.am_crimson_mosquito_fixes;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A Mosquito in water buzzes up out of it. Alex's has no way out: its wander picks spots off the first block that isn't air,
 * and water isn't, so under water it aims at more water; and it can't drown, so it hung there. In a trial, one of two dropped
 * in a pool stayed under for all three minutes. Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito$MoveHelperController")
public abstract class MoveHelperControllerMixin extends MoveControl {
    @Unique
    private static final double lifeguard$UP = 0.1, lifeguard$UP_MOST = 0.3;

    private MoveHelperControllerMixin(Mob mob) {
        super(mob);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void lifeguard$outOfWater(CallbackInfo ci) {
        if (!this.mob.isInWater() || this.mob.isPassenger())
            return;
        // Alex's Mosquito only takes off from solid ground, so one that came down in water couldn't: it bobbed at the surface.
        if (this.mob instanceof EntityCrimsonMosquitoAccessor mosquito && !mosquito.lifeguard$isFlying())
            mosquito.lifeguard$setFlying(true);
        Vec3 velocity = this.mob.getDeltaMovement();
        this.mob.setDeltaMovement(velocity.x, Math.min(Math.max(velocity.y, 0) + lifeguard$UP, lifeguard$UP_MOST), velocity.z);
    }
}
