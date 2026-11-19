package net.xlea99.grout.mixin.am_crimson_mosquito_smooth_poses;

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
    private float grout$flyBefore, grout$shootBefore;

    @Inject(method = "tick", at = @At("HEAD"))
    private void grout$before(CallbackInfo ci) {
        this.grout$flyBefore = this.flyProgress;
        this.grout$shootBefore = this.shootProgress;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$blend(CallbackInfo ci) {
        if (!((Entity) (Object) this).level().isClientSide)
            return;
        this.prevFlyProgress = this.grout$flyBefore;
        this.prevShootProgress = this.grout$shootBefore;
    }
}
