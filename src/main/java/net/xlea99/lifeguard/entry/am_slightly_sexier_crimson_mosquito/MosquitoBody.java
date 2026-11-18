package net.xlea99.lifeguard.entry.am_slightly_sexier_crimson_mosquito;

/**
 * One Mosquito's flight, eased and sprung over time ({@link MosquitoMotion}): how far it's banked and pitched, and where its
 * dangling legs and its belly are, each chasing the body a beat late with a little overshoot. Client side, kept on the entity.
 */
public final class MosquitoBody {
    /** The most time one update counts, so that a Mosquito coming back into view doesn't jump; springs step a tick at most. */
    private static final float MOST_TICKS = 2F;

    float bank, pitch;
    float speed, accel;
    /** The legs' and the belly's own angles in the world, each with its speed: they chase the body's. */
    float legsX, legsXVel, legsZ, legsZVel;
    float tailX, tailXVel, tailY, tailYVel;
    private float lastAge = Float.NaN;

    /** Ticks since the last frame, clamped; 0 the first time. */
    float tick(float age) {
        float ticks = Float.isNaN(lastAge) ? 0F : Math.clamp(age - lastAge, 0F, MOST_TICKS);
        lastAge = age;
        return ticks;
    }

    /** One spring step toward {@code target}: stiffness {@code k} and damping {@code c}, a tick at most at a time. */
    static float[] spring(float x, float v, float target, float k, float c, float ticks) {
        while (ticks > 0F) {
            float dt = Math.min(ticks, 1F);
            v += (k * (target - x) - c * v) * dt;
            x += v * dt;
            ticks -= dt;
        }
        return new float[] {x, v};
    }

    /** What every Crimson Mosquito is made to have, on the client. */
    public interface Holder {
        MosquitoBody lifeguard$mosquitoBody();
    }
}
