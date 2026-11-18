package net.xlea99.lifeguard.mixin.am_slightly_sexier_crimson_mosquito;

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
 * against Alex's 66. When a plan runs out it may instead land: one hop down to the ground. Which way is the fix's business
 * ({@code am_crimson_mosquito_fixes}, which this entry requires). Sick Mosquitoes keep Alex's low hops. Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito$RandomFlyGoal")
public abstract class RandomFlyGoalMixin {
    /** Cruises: blocks on, least and most, and how much of Alex's spread of headings (about 57 degrees either way) is kept. */
    @Unique
    private static final int lifeguard$CRUISE_NEAREST = 6, lifeguard$CRUISE_FURTHEST = 14;
    @Unique
    private static final float lifeguard$CRUISE_SPREAD = 0.5F;
    /** Darts: how often a hop is one, blocks on, the spread kept, the speed (Alex's is 1), and the chance of a jink and its size. */
    @Unique
    private static final float lifeguard$DART_CHANCE = 0.6F;
    @Unique
    private static final int lifeguard$DART_NEAREST = 2, lifeguard$DART_FURTHEST = 6;
    @Unique
    private static final float lifeguard$DART_SPREAD = 2.0F;
    @Unique
    private static final double lifeguard$DART_SPEED = 1.35;
    @Unique
    private static final float lifeguard$JINK_CHANCE = 0.5F;
    @Unique
    private static final int lifeguard$JINK = 1;
    /** Heights off the ground, in blocks: the lowest and highest it plans for, and the change a hop climbing or descending. */
    @Unique
    private static final int lifeguard$LOWEST = 1, lifeguard$HIGHEST = 9, lifeguard$STEP_LEAST = 1, lifeguard$STEP_MOST = 2;
    @Unique
    private static final int lifeguard$PLAN_LEAST = 3, lifeguard$PLAN_MOST = 7;
    /** Mid-range, the chance a new plan is a cruise (the rest split between climbing and coming down). */
    @Unique
    private static final float lifeguard$CRUISE_CHANCE = 0.7F;
    /** After a hop, the chance it stops and hangs, after a dart or a cruise, and for how long (its goal's checks, about one a tick). */
    @Unique
    private static final float lifeguard$HOVER_AFTER_DART = 0.2F, lifeguard$HOVER_AFTER_CRUISE = 0.08F;
    @Unique
    private static final int lifeguard$HOVER_LEAST = 6, lifeguard$HOVER_MOST = 18;
    /**
     * When a plan runs out, the chance the next is to land: one hop down to the ground, where Alex's Mosquito settles after
     * enough flying (it rests, then takes off). Alex means it to come down after 10 seconds aloft, but the push down he writes
     * is never applied (its result is thrown away, ISSUES #185); it only landed by sailing past a low spot into the ground,
     * which steadied flight no longer does.
     */
    @Unique
    private static final float lifeguard$LAND_CHANCE = 0.3F;
    @Unique
    private static final int lifeguard$LAND_SIT_LEAST = 40, lifeguard$LAND_SIT_MOST = 100;
    @Unique
    private static final int lifeguard$CRUISE = 0, lifeguard$CLIMB = 1, lifeguard$DESCEND = 2, lifeguard$LAND = 3;

    /** This Mosquito's plan (one goal per Mosquito), the hops left on it, and this hop. */
    @Unique
    private int lifeguard$plan = lifeguard$CRUISE;
    @Unique
    private int lifeguard$hopsLeft;
    @Unique
    private boolean lifeguard$dart, lifeguard$landing;
    @Unique
    private int lifeguard$hover;

    /** Hanging still: no new spot until it's done. Alex's flight holds still with nowhere to go, and the flight mixin holds its height. */
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void lifeguard$hovering(CallbackInfoReturnable<Boolean> cir) {
        if (this.lifeguard$hover > 0) {
            this.lifeguard$hover--;
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "stop", at = @At("TAIL"))
    private void lifeguard$maybeHover(CallbackInfo ci) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (this.lifeguard$landing) {
            // Down at the ground: sit a moment, long enough for Alex's Mosquito to settle if it has flown long enough.
            this.lifeguard$landing = false;
            this.lifeguard$hover = random.nextInt(lifeguard$LAND_SIT_LEAST, lifeguard$LAND_SIT_MOST + 1);
        } else if (random.nextFloat() < (this.lifeguard$dart ? lifeguard$HOVER_AFTER_DART : lifeguard$HOVER_AFTER_CRUISE)) {
            this.lifeguard$hover = random.nextInt(lifeguard$HOVER_LEAST, lifeguard$HOVER_MOST + 1);
        }
    }

    /** Alex's distance, 1 plus this roll: a dart's or a cruise's. Decides which this hop is: it's the first roll of a pick. */
    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0))
    private int lifeguard$distance(int roll) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.lifeguard$dart = random.nextFloat() < lifeguard$DART_CHANCE;
        return this.lifeguard$dart ? random.nextInt(lifeguard$DART_NEAREST, lifeguard$DART_FURTHEST + 1) - 1
            : random.nextInt(lifeguard$CRUISE_NEAREST, lifeguard$CRUISE_FURTHEST + 1) - 1;
    }

    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 0))
    private float lifeguard$heading(float spread) {
        return spread * (this.lifeguard$dart ? lifeguard$DART_SPREAD : lifeguard$CRUISE_SPREAD);
    }

    /** The pace it flies a hop at: Alex asks for 1 (both when it starts a hop and as it goes); a dart's is quicker. */
    @ModifyArg(method = {"canUse", "tick"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/control/MoveControl;setWantedPosition(DDDD)V"), index = 3)
    private double lifeguard$pace(double speed) {
        return this.lifeguard$dart && speed >= 1.0 ? speed * lifeguard$DART_SPEED : speed;
    }

    /**
     * The spot's height off its ground, in blocks above the surface: Alex's spot is {@code ground.above(1 + roll)}, the ground
     * being the first solid block at or below the Mosquito's height plus 2 over the spot, so the roll is that height (his 0 to
     * 5). The Mosquito's own height off that ground is {@code radialPos.y - 3 - ground.y}.
     */
    @ModifyExpressionValue(method = "getBlockInViewMosquito", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 1))
    private int lifeguard$plannedHeight(int roll, @Local(ordinal = 0) BlockPos radialPos, @Local(ordinal = 1) BlockPos ground, @Local(ordinal = 0) int up) {
        if (up < 6)
            return roll; // sick: Alex's low hops
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int height = radialPos.getY() - 3 - ground.getY();
        if (this.lifeguard$hopsLeft <= 0) {
            float dice = random.nextFloat();
            if (this.lifeguard$plan != lifeguard$LAND && random.nextFloat() < lifeguard$LAND_CHANCE) {
                this.lifeguard$plan = lifeguard$LAND;
                this.lifeguard$hopsLeft = 0;
                this.lifeguard$landing = true;
                return 0;
            }
            if (height >= lifeguard$HIGHEST - 1)
                this.lifeguard$plan = dice < 0.6F ? lifeguard$DESCEND : lifeguard$CRUISE;
            else if (height <= lifeguard$LOWEST + 1)
                this.lifeguard$plan = dice < 0.6F ? lifeguard$CLIMB : lifeguard$CRUISE;
            else
                this.lifeguard$plan = dice < lifeguard$CRUISE_CHANCE ? lifeguard$CRUISE
                    : dice < (1F + lifeguard$CRUISE_CHANCE) / 2F ? lifeguard$CLIMB : lifeguard$DESCEND;
            this.lifeguard$hopsLeft = random.nextInt(lifeguard$PLAN_LEAST, lifeguard$PLAN_MOST + 1);
        }
        this.lifeguard$hopsLeft--;
        int step = random.nextInt(lifeguard$STEP_LEAST, lifeguard$STEP_MOST + 1);
        int wanted = this.lifeguard$plan == lifeguard$CLIMB ? height + step : this.lifeguard$plan == lifeguard$DESCEND ? height - step : height;
        if (this.lifeguard$dart && random.nextFloat() < lifeguard$JINK_CHANCE)
            wanted += random.nextBoolean() ? lifeguard$JINK : -lifeguard$JINK;
        return Mth.clamp(wanted, lifeguard$LOWEST, lifeguard$HIGHEST);
    }
}
