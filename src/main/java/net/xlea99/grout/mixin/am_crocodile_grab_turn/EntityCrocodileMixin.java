package net.xlea99.grout.mixin.am_crocodile_grab_turn;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * {@code positionRider} faces its body opposite to where it's heading, every tick, so that it hauls its prey backward. At the grab that's a 180 degree
 * snap with the prey going through it. Here its heading turns around instead, with its body staying put, so that nothing has to move until it does; then
 * its body follows a turn slowly rather than at once. Its body has to be kept here: vanilla sets a moving mob's body to its heading each tick, before
 * this runs. Targeted by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrocodile")
public abstract class EntityCrocodileMixin {
    /** How fast its body can swing around with something in its jaws, in degrees a tick. */
    @Unique
    private static final float GROUT_HAUL_TURN = 10F;

    @Unique
    private boolean grout$holding;
    @Unique
    private float grout$bodyYaw;
    @Unique
    private int grout$turnedAt;

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$grip(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getPassengers().isEmpty()) {
            this.grout$holding = false;
        } else if (!this.grout$holding) {
            this.grout$holding = true;
            this.grout$bodyYaw = self.yBodyRot;
            if (!self.level().isClientSide)
                self.setYRot(self.getYRot() + 180F);
        }
    }

    @ModifyArg(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;wrapDegrees(F)F"))
    private float grout$turnSlowly(float facing) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!this.grout$holding)
            return self.yBodyRot;
        if (this.grout$turnedAt != self.tickCount) {
            this.grout$turnedAt = self.tickCount;
            this.grout$bodyYaw = Mth.approachDegrees(this.grout$bodyYaw, facing, GROUT_HAUL_TURN);
        }
        return this.grout$bodyYaw;
    }
}
