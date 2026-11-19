package net.xlea99.grout.mixin.am_slightly_sexier_crimson_mosquito;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A wandering Mosquito's flight, steadied. Alex's control adds a little speed toward the spot every tick, and only the air
 * slows it, so it sailed past every height it aimed for and bounced back: in a trial a Mosquito aiming 2.5 blocks up rose to
 * 5.2, then aiming at 5.5 sank to 0.6, about 25 turns between climbing and sinking a minute (the user: "too up-down-y").
 * Here its climb or sink eases toward a steady rate set by how far it has to go, so it rises or drops to a height and holds
 * it, and still holds its height when it stops (hovering). It also weaves a little side to side on its way (Alex's Tarantula
 * Hawk weaves likewise). Only while flying with nothing to hunt: a chase or a spit run keeps Alex's darting. Flying is read
 * from its gravity, which Alex's Mosquito switches off exactly while it flies. Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito$MoveHelperController")
public abstract class MoveHelperControllerMixin extends MoveControl {
    /** Side to side only: an up-and-down weave as well made its flight bob predictably (the user, 2026-11-08). */
    @Unique
    private static final double grout$ACROSS = 0.018;
    /** Climb or sink: blocks a tick per block still to go, at most, and how much of the way to that it eases each tick. */
    @Unique
    private static final double grout$CLIMB_PER_BLOCK = 0.08, grout$CLIMB_MOST = 0.16, grout$CLIMB_EASE = 0.45;
    /** Settling: within this far of solid ground, stopped, it sinks onto it at this many blocks a tick. */
    @Unique
    private static final double grout$SETTLE_WITHIN = 1.2, grout$SETTLE = 0.06;
    /** A new spot more than about 40 degrees off its line keeps this much of its speed; and its fastest, blocks a tick (7 a second). */
    @Unique
    private static final double grout$SNAP_BELOW_COS = 0.77, grout$SNAP_KEEP = 0.3, grout$FASTEST = 0.35;
    @Unique
    private double grout$lastWantX, grout$lastWantZ;

    private MoveHelperControllerMixin(Mob mob) {
        super(mob);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$steady(CallbackInfo ci) {
        if (!this.mob.isNoGravity() || this.mob.getTarget() != null || this.mob.isPassenger())
            return;
        Vec3 velocity = this.mob.getDeltaMovement();
        boolean going = this.operation == MoveControl.Operation.MOVE_TO;
        double climb = going ? Mth.clamp((this.wantedY - this.mob.getY()) * grout$CLIMB_PER_BLOCK, -grout$CLIMB_MOST, grout$CLIMB_MOST) : 0;
        // Stopped within about a block of solid ground (a landing hop aims just above it): settle onto it, where Alex's
        // Mosquito lands once it has flown long enough. Water isn't solid here, so it never settles into it.
        if (!going && !this.mob.level().noCollision(this.mob, this.mob.getBoundingBox().move(0, -grout$SETTLE_WITHIN, 0)))
            climb = -grout$SETTLE;
        double vy = velocity.y + (climb - velocity.y) * grout$CLIMB_EASE;
        double wx = velocity.x, wz = velocity.z;
        double dx = this.wantedX - this.mob.getX(), dz = this.wantedZ - this.mob.getZ();
        double across = Math.sqrt(dx * dx + dz * dz);
        // A new spot well off its line: shed most of its speed and snap onto the new line, as an insect does, instead of
        // curving round on its momentum (which rounded every dart off: about 3 turns of over 45 degrees a minute).
        boolean newSpot = going && (this.wantedX != this.grout$lastWantX || this.wantedZ != this.grout$lastWantZ);
        this.grout$lastWantX = this.wantedX;
        this.grout$lastWantZ = this.wantedZ;
        double speed = Math.sqrt(wx * wx + wz * wz);
        if (newSpot && across > 0.5 && speed > 0.05 && (wx * dx + wz * dz) / (speed * across) < grout$SNAP_BELOW_COS) {
            wx *= grout$SNAP_KEEP;
            wz *= grout$SNAP_KEEP;
            speed *= grout$SNAP_KEEP;
        }
        if (speed > grout$FASTEST) {
            wx *= grout$FASTEST / speed;
            wz *= grout$FASTEST / speed;
        }
        if (going && across >= 1.0) {
            float t = this.mob.tickCount + this.mob.getId() * 13;
            double side = grout$ACROSS * Math.sin(t * 0.21);
            wx += -dz / across * side;
            wz += dx / across * side;
        }
        this.mob.setDeltaMovement(wx, vy, wz);
    }
}
