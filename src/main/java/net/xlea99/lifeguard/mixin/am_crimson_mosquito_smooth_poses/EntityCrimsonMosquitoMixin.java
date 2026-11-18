package net.xlea99.lifeguard.mixin.am_crimson_mosquito_smooth_poses;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Alex's tick moves {@code flyProgress} and {@code shootProgress} near its start and copies them into their {@code prev...}
 * fields near its end, so both always match and a frame between ticks blends nothing. This keeps what they were before the
 * tick and puts that in {@code prev...} at its end. Client only (drawing). Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito")
public abstract class EntityCrimsonMosquitoMixin {
    @Shadow
    public float flyProgress;
    @Shadow
    public float prevFlyProgress;
    @Shadow
    public float shootProgress;
    @Shadow
    public float prevShootProgress;

    @Unique
    private float lifeguard$flyBefore, lifeguard$shootBefore;

    @Inject(method = "tick", at = @At("HEAD"))
    private void lifeguard$before(CallbackInfo ci) {
        this.lifeguard$flyBefore = this.flyProgress;
        this.lifeguard$shootBefore = this.shootProgress;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void lifeguard$blend(CallbackInfo ci) {
        if (!((Entity) (Object) this).level().isClientSide)
            return;
        this.prevFlyProgress = this.lifeguard$flyBefore;
        this.prevShootProgress = this.lifeguard$shootBefore;
    }
}
