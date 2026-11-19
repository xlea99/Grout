package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

/**
 * Hurt badly ({@code AT_HEALTH} of its health or less), a Rattlesnake bolts for cover instead of fighting on: the best spot
 * {@code LEAST} to {@code MOST} blocks off that's further from whoever hurt it ({@link SnakeCover}), at a fast slither. It lets
 * its target go; from there it rattles at anyone who comes on, and strikes if they come right up ({@link SnakeMind}). Once
 * for each hurt; with nowhere to go, it stands and fights.
 */
public final class SnakeRetreatGoal extends Goal {
    public static final int PRIORITY = 0;
    static final float AT_HEALTH = 0.4F;
    static final int LEAST = 4, MOST = 14, GIVE_UP = 200;
    /** Its spot must be at least this much further from whoever hurt it than it is now. */
    static final double AWAY = 2.0;
    static final double SPEED = 1.5;

    private final PathfinderMob snake;
    private final RattlesnakeAccess access;
    private final SnakeMind.Holder mind;
    private int retreatedFrom = Integer.MIN_VALUE;
    private BlockPos spot;
    private java.util.List<BlockPos> spots = java.util.List.of();
    private int ticks;
    private boolean going;

    public SnakeRetreatGoal(PathfinderMob snake, RattlesnakeAccess access, SnakeMind.Holder mind) {
        this.snake = snake;
        this.access = access;
        this.mind = mind;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity attacker = snake.getLastHurtByMob();
        int stamp = snake.getLastHurtByMobTimestamp();
        if (attacker == null || !attacker.isAlive() || stamp == retreatedFrom || snake.tickCount - stamp > 40
            || snake.getHealth() > snake.getMaxHealth() * AT_HEALTH)
            return false;
        retreatedFrom = stamp;
        double now = snake.distanceToSqr(attacker);
        spots = SnakeCover.nearest(snake, LEAST, MOST, SnakeCover.TRY_PATHS,
            p -> attacker.distanceToSqr(p.getX() + 0.5, p.getY(), p.getZ() + 0.5) > (Math.sqrt(now) + AWAY) * (Math.sqrt(now) + AWAY));
        return !spots.isEmpty();
    }

    @Override
    public void start() {
        ticks = 0;
        mind.grout$mind().setFleeing(true);
        letGo();
        unpin();
        setOff();
    }

    /** For the first of its spots it has a whole path to ({@link SnakeCover#goToFirstReachable}); not yet if it's in the air. */
    private void setOff() {
        spot = SnakeCover.goToFirstReachable(snake, spots, SPEED);
        going = spot != null;
    }

    /**
     * Setting off (the hit knocks it into the air, and vanilla plans no path off the ground, so it tries again each tick until
     * it lands: 2026-11-15, it stood and fought), on its way, then the last little way into its cover
     * ({@link SnakeCover#settleInto}).
     */
    @Override
    public boolean canContinueToUse() {
        if (++ticks >= GIVE_UP)
            return false;
        return !going || !snake.getNavigation().isDone() || SnakeCover.settleInto(snake, spot, SPEED);
    }

    @Override
    public void tick() {
        letGo();
        unpin();
        if (!going)
            setOff();
    }

    /** No rattle and no coil while it runs: Alex coils a rattling snake with no target, and a coiled snake can't move. */
    private void unpin() {
        access.grout$setRattling(false);
        if (access.grout$isCurled())
            access.grout$setCurled(false);
    }

    /**
     * Drops its target, and stops the target goals holding one: vanilla's hurt-by target goal otherwise puts whoever hurt it
     * back as its target every tick while they're in range, and the strike goal would turn it round to fight.
     */
    private void letGo() {
        snake.targetSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
        snake.setTarget(null);
    }

    @Override
    public void stop() {
        mind.grout$mind().setFleeing(false);
        snake.getNavigation().stop();
    }
}
