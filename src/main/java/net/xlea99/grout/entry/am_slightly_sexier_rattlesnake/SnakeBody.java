package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

/**
 * One Rattlesnake's slither, kept over time ({@link SnakeMotion}): the wave's phase, added up from how far it has gone (so the
 * wave stays put on the ground and freezes where it stops), and how far it's in its slither, eased. Client side, on the entity.
 */
public final class SnakeBody {
    private static final float MOST_TICKS = 2F;

    float phase = Float.NaN;
    float move;
    /**
     * How far up its head should be, 0 to 1: as the server last told it ({@code told}, NaN until it has), or, without our server
     * entry, whether anything is near enough to notice. And how far up it is, eased.
     */
    public float head;
    public float told = Float.NaN;
    float alert;
    /** Whether the server says it's asleep, and how far into its sleep it is, eased. */
    public boolean asleep;
    float sleep;
    /** The head's turn it shows, in degrees off the body, paced. */
    float look;
    /** How far its tail tip is lifted into the rattle, eased. */
    float rattle;
    private float lastAge = Float.NaN;

    float tick(float age) {
        float ticks = Float.isNaN(lastAge) ? 0F : Math.clamp(age - lastAge, 0F, MOST_TICKS);
        lastAge = age;
        return ticks;
    }

    /** What every Rattlesnake is made to have, on the client. */
    public interface Holder {
        SnakeBody grout$snakeBody();
    }
}
