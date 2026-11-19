package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

/**
 * Going for a threat, in place of Alex's (which took any player or Roadrunner within 2 blocks): a grown snake worked up to
 * the full takes the threat it's rattling at as its target once it's within its strike reach ({@link SnakeMind}), and keeps
 * it while it stays within lunging reach.
 */
public final class SnakeThreatGoal extends TargetGoal {
    public static final int PRIORITY = 4;

    private final SnakeMind.Holder mind;
    private final RattlesnakeAccess access;

    public SnakeThreatGoal(PathfinderMob snake, SnakeMind.Holder mind, RattlesnakeAccess access) {
        super(snake, true, false);
        this.mind = mind;
        this.access = access;
    }

    @Override
    public boolean canUse() {
        return !mob.isBaby() && mind.grout$mind().strikes((PathfinderMob) mob) && mob.canAttack(mind.grout$mind().threat());
    }

    @Override
    public void start() {
        this.targetMob = mind.grout$mind().threat();
        mob.setTarget(this.targetMob);
        access.grout$setCurled(false);
        super.start();
    }

    @Override
    protected double getFollowDistance() {
        return SnakeStrikeGoal.REACH;
    }
}
