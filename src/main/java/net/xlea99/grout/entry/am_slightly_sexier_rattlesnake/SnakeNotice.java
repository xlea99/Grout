package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

/**
 * What a coiled Rattlesnake notices: any living thing within {@code RANGE} but another snake, a player in creative or
 * spectator, or something invisible. The server turns its head to the nearest; the client lifts its head out of its rest for
 * it. Both work it out the same way from what each can see, so it takes no packets.
 */
public final class SnakeNotice {
    public static final double RANGE = 8.0;

    private SnakeNotice() {
    }

    public static LivingEntity nearest(Mob snake) {
        AABB around = snake.getBoundingBox().inflate(RANGE, RANGE / 2, RANGE);
        LivingEntity nearest = null;
        double best = RANGE * RANGE;
        for (LivingEntity other : snake.level().getEntitiesOfClass(LivingEntity.class, around, e -> noticeable(snake, e))) {
            double d = snake.distanceToSqr(other);
            if (d < best) {
                best = d;
                nearest = other;
            }
        }
        return nearest;
    }

    static boolean noticeable(Mob snake, LivingEntity other) {
        if (other == snake || !other.isAlive() || other.isInvisible() || other.getType() == snake.getType())
            return false;
        return !(other instanceof Player player) || !player.isCreative() && !player.isSpectator();
    }
}
