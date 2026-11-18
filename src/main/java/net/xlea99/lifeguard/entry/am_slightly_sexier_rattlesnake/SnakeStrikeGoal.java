package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * The strike, in place of Alex's melee chase: a Rattlesnake doesn't crawl after anything. Within {@code REACH} of its target
 * it lunges, a quick hop straight at it, and bites with Alex's own bite (his animation lands the damage and the poison 8 ticks
 * in, if it's within 2 blocks by then); then it gathers itself for {@code COOLDOWN} ticks before it can strike again. Further
 * than {@code LET_GO} for {@code LET_GO_AFTER} ticks, it lets the target go. So it only keeps fighting someone who stays
 * beside it.
 */
public final class SnakeStrikeGoal extends Goal {
    public static final int PRIORITY = 1;
    static final double REACH = 3.5;
    /** The lunge: blocks a tick at full reach (less when closer), and the hop. */
    static final double LUNGE = 0.45, LUNGE_LEAST = 0.15, LUNGE_UP = 0.2;
    static final int COOLDOWN = 30;
    static final double LET_GO = 5.0;
    static final int LET_GO_AFTER = 30;

    private final PathfinderMob snake;
    private int cooldown;
    private int away;

    public SnakeStrikeGoal(PathfinderMob snake) {
        this.snake = snake;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = snake.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        away = 0;
        struck = false;
        unreachable = 0;
        plannedAt = Long.MIN_VALUE / 2;
        snake.getNavigation().stop();
    }

    @Override
    public void stop() {
        snake.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = snake.getTarget();
        if (target == null)
            return;
        snake.getLookControl().setLookAt(target, 60F, 60F);
        double distance = snake.distanceTo(target);
        if (SnakePreyGoal.isPrey(target)) {
            chase(target, distance);
        } else {
            snake.getNavigation().stop();
            if (distance > LET_GO) {
                if (++away > LET_GO_AFTER)
                    snake.setTarget(null);
                return;
            }
            away = 0;
        }
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (distance > REACH || !snake.onGround())
            return;
        double dx = target.getX() - snake.getX(), dz = target.getZ() - snake.getZ();
        double across = Math.sqrt(dx * dx + dz * dz);
        if (across < 1e-3)
            return;
        double speed = Mth.clamp(LUNGE * distance / REACH, LUNGE_LEAST, LUNGE);
        float yaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90F;
        snake.setYRot(yaw);
        snake.yBodyRot = yaw;
        snake.yHeadRot = yaw;
        snake.setDeltaMovement(dx / across * speed, LUNGE_UP, dz / across * speed);
        snake.hasImpulse = true;
        snake.doHurtTarget(target);
        struck = true;
        cooldown = COOLDOWN;
        plannedAt = Long.MIN_VALUE / 2; // after a lunge, plan afresh from where it landed
    }

    /**
     * Prey it has gone for is dinner: it follows, at a quick slither, striking again whenever it's in reach. Within reach it
     * holds still to strike; further, it plans a path only when the prey has moved {@code REPLAN_MOVED} or every
     * {@code REPLAN_EVERY} ticks, and gives up past {@code CHASE_RANGE} or after {@code UNREACHABLE_GIVE_UP} plans in a row
     * that can't reach it (a plan that can't reach searches its whole budget).
     */
    private void chase(LivingEntity prey, double distance) {
        if (distance > (struck ? CHASE_RANGE : STALK_GIVE_UP)) {
            snake.setTarget(null);
            return;
        }
        if (distance <= REACH * 0.8) {
            snake.getNavigation().stop();
            return;
        }
        long now = snake.level().getGameTime();
        if (prey.position().distanceToSqr(plannedTo) < REPLAN_MOVED * REPLAN_MOVED && now - plannedAt < REPLAN_EVERY && !snake.getNavigation().isDone())
            return;
        plannedAt = now;
        plannedTo = prey.position();
        var path = snake.getNavigation().createPath(prey, 1);
        if (path == null || !path.canReach() && (path.getEndNode() == null || !path.getEndNode().asBlockPos().closerToCenterThan(prey.position(), 2.5))) {
            if (++unreachable >= UNREACHABLE_GIVE_UP)
                snake.setTarget(null);
            if (path == null)
                return;
        } else {
            unreachable = 0;
        }
        snake.getNavigation().moveTo(path, struck ? CHASE_SPEED : STALK_SPEED);
    }

    /**
     * Before its first strike it stalks: a slow creep at {@code STALK_SPEED} until it's in reach, giving up if the prey gets
     * {@code STALK_GIVE_UP} away; once it has struck, the prey is dinner and it chases at {@code CHASE_SPEED}.
     */
    static final double STALK_SPEED = 0.55, STALK_GIVE_UP = 16.0;
    private boolean struck;

    static final double CHASE_RANGE = 32.0, CHASE_SPEED = 1.3, REPLAN_MOVED = 1.5;
    static final int REPLAN_EVERY = 15, UNREACHABLE_GIVE_UP = 3;
    private long plannedAt = Long.MIN_VALUE / 2;
    private net.minecraft.world.phys.Vec3 plannedTo = net.minecraft.world.phys.Vec3.ZERO;
    private int unreachable;
}
