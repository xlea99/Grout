package net.xlea99.lifeguard.mixin.am_crimson_mosquito_fixes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The spot ahead: Alex's angle is half a turn round from the way it faces, so the offset it builds (distance times the sine
 * and the cosine) points behind; turning both round points it ahead, the same as the negative distance his Tarantula Hawk
 * uses. Targeted by name: Lifeguard isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito$RandomFlyGoal")
public abstract class RandomFlyGoalMixin {
    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sin(F)F"))
    private float lifeguard$aheadX(float sin) {
        return -sin;
    }

    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;cos(F)F"))
    private float lifeguard$aheadZ(float cos) {
        return -cos;
    }
}
