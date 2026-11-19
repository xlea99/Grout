package net.xlea99.grout.mixin.am_slightly_sexier_crimson_mosquito;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Where a wandering Mosquito flies: it goes somewhere, in darts and cruises. Alex's hops were 1 to 5 blocks, each at a fresh
 * height 0 to 5 blocks off the ground (up, down, up, predictable: the user, 2026-11-08). Here it holds a plan for 3 to 7 hops
 * (mostly cruising at its height, else climbing or coming down a block or two a hop) like Alex's Tarantula Hawk, and each hop
 * is either a cruise (6 to 14 blocks, close to the way it faces) or, more often, a dart (2 to 6 blocks, swung up to about 115 degrees off,
 * faster, sometimes jinking a block up or down), and after hops, more often after darts, it stops and hangs a moment. Long
 * cruises alone lost the zip Alex clearly meant (the user, 2026-11-08): in a trial, 4 turns of over 45 degrees a minute
 * against Alex's 66. When a plan runs out it may instead land: one hop down to the ground. Sick Mosquitoes keep Alex's low
 * hops. Targeted by name.
 *
 * <p>And every spot is ahead of it. Alex's angle is half a turn round from the way it faces, so the offset it builds
 * (distance times the sine and the cosine) points behind, and each hop turned back the way it came (in a trial, 0 of 1042
 * spots ahead); turning both round points it ahead, the same as the negative distance his Tarantula Hawk uses. This was
 * {@code am_crimson_mosquito_fixes}' until 2026-11-19 (the user: "this is NOT a bugfix at all"), so the behavior stands on
 * its own; the fix keeps the water escape.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito$RandomFlyGoal")
public abstract class RandomFlyGoalMixin {
    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;sin(F)F"))
    private float grout$aheadX(float sin) {
        return -sin;
    }

    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;cos(F)F"))
    private float grout$aheadZ(float cos) {
        return -cos;
    }

    /** Cruises: blocks on, least and most, and how much of Alex's spread of headings (about 57 degrees either way) is kept. */
    @Unique
    private static final int grout$CRUISE_NEAREST = 6, grout$CRUISE_FURTHEST = 14;
    @Unique
    private static final float grout$CRUISE_SPREAD = 0.5F;
    /** Darts: how often a hop is one, blocks on, the spread kept, the speed (Alex's is 1), and the chance of a jink and its size. */
    @Unique
    private static final float grout$DART_CHANCE = 0.6F;
    @Unique
    private static final int grout$DART_NEAREST = 2, grout$DART_FURTHEST = 6;
    @Unique
    private static final float grout$DART_SPREAD = 2.0F;
    @Unique
    private static final double grout$DART_SPEED = 1.35;
    @Unique
    private static final float grout$JINK_CHANCE = 0.5F;
    @Unique
    private static final int grout$JINK = 1;
    /** Heights off the ground, in blocks: the lowest and highest it plans for, and the change a hop climbing or descending. */
    @Unique
    private static final int grout$LOWEST = 1, grout$HIGHEST = 9, grout$STEP_LEAST = 1, grout$STEP_MOST = 2;
    @Unique
    private static final int grout$PLAN_LEAST = 3, grout$PLAN_MOST = 7;
    /** Mid-range, the chance a new plan is a cruise (the rest split between climbing and coming down). */
    @Unique
    private static final float grout$CRUISE_CHANCE = 0.7F;
    /** After a hop, the chance it stops and hangs, after a dart or a cruise, and for how long (its goal's checks, about one a tick). */
    @Unique
    private static final float grout$HOVER_AFTER_DART = 0.2F, grout$HOVER_AFTER_CRUISE = 0.08F;
    @Unique
    private static final int grout$HOVER_LEAST = 6, grout$HOVER_MOST = 18;
    /**
     * When a plan runs out, the chance the next is to land: one hop down to the ground, where Alex's Mosquito settles after
     * enough flying (it rests, then takes off). Alex means it to come down after 10 seconds aloft, but the push down he writes
     * is never applied (its result is thrown away, ISSUES #185); it only landed by sailing past a low spot into the ground,
     * which steadied flight no longer does.
     */
    @Unique
    private static final float grout$LAND_CHANCE = 0.3F;
    @Unique
    private static final int grout$LAND_SIT_LEAST = 40, grout$LAND_SIT_MOST = 100;
    @Unique
    private static final int grout$CRUISE = 0, grout$CLIMB = 1, grout$DESCEND = 2, grout$LAND = 3;

    /** This Mosquito's plan (one goal per Mosquito), the hops left on it, and this hop. */
    @Unique
    private int grout$plan = grout$CRUISE;
    @Unique
    private int grout$hopsLeft;
    @Unique
    private boolean grout$dart, grout$landing;
    @Unique
    private int grout$hover;

    /** Hanging still: no new spot until it's done. Alex's flight holds still with nowhere to go, and the flight mixin holds its height. */
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void grout$hovering(CallbackInfoReturnable<Boolean> cir) {
        if (this.grout$hover > 0) {
            this.grout$hover--;
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "stop", at = @At("TAIL"))
    private void grout$maybeHover(CallbackInfo ci) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (this.grout$landing) {
            // Down at the ground: sit a moment, long enough for Alex's Mosquito to settle if it has flown long enough.
            this.grout$landing = false;
            this.grout$hover = random.nextInt(grout$LAND_SIT_LEAST, grout$LAND_SIT_MOST + 1);
        } else if (random.nextFloat() < (this.grout$dart ? grout$HOVER_AFTER_DART : grout$HOVER_AFTER_CRUISE)) {
            this.grout$hover = random.nextInt(grout$HOVER_LEAST, grout$HOVER_MOST + 1);
        }
    }

    /** Alex's distance, 1 plus this roll: a dart's or a cruise's. Decides which this hop is: it's the first roll of a pick. */
    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0))
    private int grout$distance(int roll) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.grout$dart = random.nextFloat() < grout$DART_CHANCE;
        return this.grout$dart ? random.nextInt(grout$DART_NEAREST, grout$DART_FURTHEST + 1) - 1
            : random.nextInt(grout$CRUISE_NEAREST, grout$CRUISE_FURTHEST + 1) - 1;
    }

    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 0))
    private float grout$heading(float spread) {
        return spread * (this.grout$dart ? grout$DART_SPREAD : grout$CRUISE_SPREAD);
    }

    /** The pace it flies a hop at: Alex asks for 1 (both when it starts a hop and as it goes); a dart's is quicker. */
    @ModifyArg(method = {"canUse", "tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/control/MoveControl;setWantedPosition(DDDD)V"), index = 3)
    private double grout$pace(double speed) {
        return this.grout$dart && speed >= 1.0 ? speed * grout$DART_SPEED : speed;
    }

    /**
     * The spot's height off its ground, in blocks above the surface: Alex's spot is {@code ground.above(1 + roll)}, the ground
     * being the first solid block at or below the Mosquito's height plus 2 over the spot, so the roll is that height (his 0 to
     * 5). The Mosquito's own height off that ground is {@code radialPos.y - 3 - ground.y}.
     */
    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 1))
    private int grout$plannedHeight(int roll, @Local(ordinal = 0) BlockPos radialPos, @Local(ordinal = 1) BlockPos ground, @Local(ordinal = 0) int up) {
        if (up < 6)
            return roll; // sick: Alex's low hops
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int height = radialPos.getY() - 3 - ground.getY();
        if (this.grout$hopsLeft <= 0) {
            float dice = random.nextFloat();
            if (this.grout$plan != grout$LAND && random.nextFloat() < grout$LAND_CHANCE) {
                this.grout$plan = grout$LAND;
                this.grout$hopsLeft = 0;
                this.grout$landing = true;
                return 0;
            }
            if (height >= grout$HIGHEST - 1)
                this.grout$plan = dice < 0.6F ? grout$DESCEND : grout$CRUISE;
            else if (height <= grout$LOWEST + 1)
                this.grout$plan = dice < 0.6F ? grout$CLIMB : grout$CRUISE;
            else
                this.grout$plan = dice < grout$CRUISE_CHANCE ? grout$CRUISE
                    : dice < (1F + grout$CRUISE_CHANCE) / 2F ? grout$CLIMB : grout$DESCEND;
            this.grout$hopsLeft = random.nextInt(grout$PLAN_LEAST, grout$PLAN_MOST + 1);
        }
        this.grout$hopsLeft--;
        int step = random.nextInt(grout$STEP_LEAST, grout$STEP_MOST + 1);
        int wanted = this.grout$plan == grout$CLIMB ? height + step : this.grout$plan == grout$DESCEND ? height - step : height;
        if (this.grout$dart && random.nextFloat() < grout$JINK_CHANCE)
            wanted += random.nextBoolean() ? grout$JINK : -grout$JINK;
        return Mth.clamp(wanted, grout$LOWEST, grout$HIGHEST);
    }
}
