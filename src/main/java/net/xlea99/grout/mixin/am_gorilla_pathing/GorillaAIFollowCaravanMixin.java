package net.xlea99.grout.mixin.am_gorilla_pathing;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Alex's follow-the-line goal plans a new path whenever its last one is done, and a follower standing behind the one ahead
 * is always done: about five paths a second each, 72% of all Gorilla pathing in a trial. Now it plans only when its spot
 * behind the one ahead has moved {@code MOVED} blocks, or once {@code EVERY} of its ticks (the goal ticks every other game
 * tick: about a second). Targeted by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.ai.GorillaAIFollowCaravan")
public abstract class GorillaAIFollowCaravanMixin {
    @Unique
    private static final double GROUT_MOVED = 1.5;
    @Unique
    private static final int GROUT_EVERY = 10;
    @Unique
    private Vec3 grout$planned;
    @Unique
    private int grout$ticks;
    @Unique
    private int grout$plannedAt = -GROUT_EVERY;

    @Inject(method = "tick", at = @At("HEAD"))
    private void grout$count(CallbackInfo ci) {
        this.grout$ticks++;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;moveTo(DDDD)Z"))
    private boolean grout$onlyWhenMoved(PathNavigation navigation, double x, double y, double z, double speed, Operation<Boolean> original) {
        Vec3 spot = new Vec3(x, y, z);
        if (this.grout$planned != null && this.grout$planned.distanceToSqr(spot) < GROUT_MOVED * GROUT_MOVED
            && this.grout$ticks - this.grout$plannedAt < GROUT_EVERY)
            return false;
        this.grout$planned = spot;
        this.grout$plannedAt = this.grout$ticks;
        return original.call(navigation, x, y, z, speed);
    }
}
