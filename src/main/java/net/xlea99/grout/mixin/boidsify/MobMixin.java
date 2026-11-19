package net.xlea99.grout.mixin.boidsify;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.xlea99.grout.boids.SteeredMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A boids goal's velocity is applied once the mob's AI step is done (move control included), right before the mob moves
 * ({@link SteeredMob}). A mob no boids goal steers is untouched: one field read a tick.
 */
@Mixin(Mob.class)
public abstract class MobMixin implements SteeredMob {

    @Unique
    @Nullable
    private Vec3 grout$steered;

    @Override
    public void grout$steer(Vec3 velocity) {
        this.grout$steered = velocity;
    }

    @Inject(method = "serverAiStep", at = @At("TAIL"))
    private void grout$applySteering(CallbackInfo ci) {
        Vec3 velocity = this.grout$steered;
        if (velocity != null) {
            this.grout$steered = null;
            ((Mob) (Object) this).setDeltaMovement(velocity);
        }
    }

}
