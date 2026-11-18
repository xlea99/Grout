package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * The warning, in place of Alex's (which rattled at any player or Roadrunner within 5 blocks): it rattles, coiled, at the
 * threat it's watching while it's worked up enough ({@link SnakeMind}). Like Alex's, it claims nothing, so the wait carries
 * on around it.
 */
public final class SnakeWarnGoal extends Goal {
    public static final int PRIORITY = 2;

    private final PathfinderMob snake;
    private final SnakeMind.Holder mind;
    private final RattlesnakeAccess access;

    public SnakeWarnGoal(PathfinderMob snake, SnakeMind.Holder mind, RattlesnakeAccess access) {
        this.snake = snake;
        this.mind = mind;
        this.access = access;
    }

    @Override
    public boolean canUse() {
        return snake.getTarget() == null && mind.lifeguard$mind().rattles();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        access.lifeguard$setRattling(true);
        access.lifeguard$setCurled(true);
        LivingEntity threat = mind.lifeguard$mind().threat();
        if (threat != null)
            snake.getLookControl().setLookAt(threat, 30F, 30F);
    }

    @Override
    public void stop() {
        access.lifeguard$setRattling(false);
    }
}
