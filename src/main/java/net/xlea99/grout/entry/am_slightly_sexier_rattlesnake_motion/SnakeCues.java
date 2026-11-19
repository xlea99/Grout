package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

/**
 * What a Rattlesnake is doing, as its slithering ({@link SnakeMotion}) asks it: how far up its head is, and whether it's
 * asleep. Its slithering never knows the snake's mind itself (the mind is another mod's once the suite splits): something
 * that does sets a {@link Source}, and without one it guesses, head up for anything living near enough to notice and never
 * asleep. Motion-side; client only.
 */
public final class SnakeCues {
    /** How near a living thing has to be for the guess to lift the head. */
    static final double NOTICE_RANGE = 8.0;

    /**
     * Someone who knows what a snake's mind is doing: how far up its head is, 0 (resting) to 1, or NaN while it hasn't been
     * told (the slithering then guesses); and whether it's asleep.
     */
    public interface Source {
        float head(Mob snake);

        boolean asleep(Mob snake);
    }

    private static volatile Source source;

    private SnakeCues() {
    }

    /** Set once, by whoever knows (the Sleepy eyes entry); null puts the slithering back on its guess. */
    public static void set(Source given) {
        source = given;
    }

    /** The head as told, or NaN when nobody tells it. */
    public static float told(Mob snake) {
        Source known = source;
        return known == null ? Float.NaN : known.head(snake);
    }

    public static boolean asleep(Mob snake) {
        Source known = source;
        return known != null && known.asleep(snake);
    }

    /**
     * The guess: any living thing within {@code NOTICE_RANGE} but another snake, a player in creative or spectator, or
     * something invisible.
     */
    public static boolean anyoneNear(Mob snake) {
        AABB around = snake.getBoundingBox().inflate(NOTICE_RANGE, NOTICE_RANGE / 2, NOTICE_RANGE);
        return !snake.level().getEntitiesOfClass(LivingEntity.class, around, other -> other != snake && other.isAlive()
            && !other.isInvisible() && other.getType() != snake.getType()
            && (!(other instanceof Player player) || !player.isCreative() && !player.isSpectator())
            && snake.distanceToSqr(other) < NOTICE_RANGE * NOTICE_RANGE).isEmpty();
    }
}
