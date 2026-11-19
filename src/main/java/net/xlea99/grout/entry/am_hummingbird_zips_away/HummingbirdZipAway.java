package net.xlea99.grout.entry.am_hummingbird_zips_away;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A hit Hummingbird's escape: a run of short darts, each picked away from whatever hit it (from where it is now, if it's an
 * entity that follows), with a sideways swerve. Alex's {@code FlightMoveController} stops a bird dead when it arrives, so
 * the stops between darts come free. Runs above every goal Alex gives it; a new hit while zipping starts the clock again.
 */
public final class HummingbirdZipAway extends Goal {
    /** How long one escape lasts, in ticks. */
    static final int ZIP_MIN = 70;
    static final int ZIP_MAX = 120;
    /** Ticks before it gives up on a dart and picks the next, if it hasn't arrived. */
    static final int DART_MIN = 5;
    static final int DART_MAX = 10;
    /** How far one dart goes, in blocks. */
    static final double REACH_MIN = 3.0;
    static final double REACH_MAX = 6.5;
    /** The swerve off straight away, in degrees either side. */
    static final float SWERVE = 70F;
    /** Up and down per dart, in blocks. */
    static final double RISE_MIN = -1.5;
    static final double RISE_MAX = 1.75;
    /**
     * How far an escape may end up above or below where it began. Alex's {@code tick} slows any falling bird by 60% each tick,
     * so downward darts fall short and an unbounded escape ratchets up (a trial climbed 7-10 blocks).
     */
    static final double BAND_UP = 3.0;
    static final double BAND_DOWN = 1.5;
    /** Speed handed to Alex's move controller; its wander and feeder trips use 1. */
    static final double SPEED = 2.4;
    private static final int TRIES = 10;

    private final Mob bird;
    private DamageSource handled;
    private Entity attacker;
    private Vec3 hitFrom;
    private double startY;
    private int left;
    private int dartLeft;

    public HummingbirdZipAway(Mob bird) {
        this.bird = bird;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return freshHit();
    }

    @Override
    public boolean canContinueToUse() {
        return left > 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        left = Mth.nextInt(bird.getRandom(), ZIP_MIN, ZIP_MAX);
        dartLeft = 0;
        startY = bird.getY();
    }

    @Override
    public void tick() {
        if (freshHit()) {
            left = Math.max(left, Mth.nextInt(bird.getRandom(), ZIP_MIN, ZIP_MAX));
            dartLeft = 0;
        }
        left--;
        if (--dartLeft <= 0 || !bird.getMoveControl().hasWanted())
            dart();
    }

    @Override
    public void stop() {
        bird.getMoveControl().setWantedPosition(bird.getX(), bird.getY(), bird.getZ(), 1.0);
        attacker = null;
        hitFrom = null;
    }

    /** A hit not yet answered: vanilla keeps the last damage source for 40 ticks, a new object per hit. */
    private boolean freshHit() {
        DamageSource source = bird.getLastDamageSource();
        if (source == null || source == handled)
            return false;
        handled = source;
        attacker = source.getEntity();
        hitFrom = attacker != null ? attacker.position() : source.getSourcePosition();
        return true;
    }

    private void dart() {
        RandomSource random = bird.getRandom();
        dartLeft = Mth.nextInt(random, DART_MIN, DART_MAX);
        Vec3 from = attacker != null && attacker.isAlive() ? attacker.position() : hitFrom;
        float away = from == null ? random.nextFloat() * 360F
            : (float) (Mth.atan2(bird.getZ() - from.z, bird.getX() - from.x) * Mth.RAD_TO_DEG);
        for (int i = 0; i < TRIES; i++) {
            float swerve = i < TRIES / 2 ? SWERVE : 180F;
            float angle = (away + (random.nextFloat() * 2F - 1F) * swerve) * Mth.DEG_TO_RAD;
            double reach = REACH_MIN + random.nextDouble() * (REACH_MAX - REACH_MIN);
            Vec3 to = new Vec3(bird.getX() + Mth.cos(angle) * reach,
                Mth.clamp(bird.getY() + RISE_MIN + random.nextDouble() * (RISE_MAX - RISE_MIN), startY - BAND_DOWN, startY + BAND_UP),
                bird.getZ() + Mth.sin(angle) * reach);
            if (clear(to)) {
                bird.getMoveControl().setWantedPosition(to.x, to.y, to.z, SPEED);
                return;
            }
        }
    }

    /** Open air it can see from where it is, so a dart never aims through a wall or into the ground. */
    private boolean clear(Vec3 to) {
        BlockPos pos = BlockPos.containing(to);
        if (!bird.level().isEmptyBlock(pos) || !bird.level().isEmptyBlock(pos.above()) || pos.getY() <= bird.level().getMinBuildHeight())
            return false;
        HitResult hit = bird.level().clip(new ClipContext(bird.getEyePosition(), to, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, bird));
        return hit.getType() == HitResult.Type.MISS;
    }
}
