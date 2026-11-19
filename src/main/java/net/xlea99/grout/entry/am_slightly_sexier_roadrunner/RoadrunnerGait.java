package net.xlea99.grout.entry.am_slightly_sexier_roadrunner;

import net.minecraft.util.Mth;

/** One Roadrunner's motion over time: what can't be worked out from a single frame ({@link RoadrunnerMotion}). */
public final class RoadrunnerGait {
    /** How fast it gets into its stride, and out of it, per tick. A Roadrunner goes from still to flat out at once. */
    private static final float STRIDE_PER_TICK = 0.5F;
    /** How fast a walk turns into a sprint, and back, per tick. */
    private static final float SPRINT_PER_TICK = 0.3F;
    /** How fast it settles into a turn, per tick. */
    private static final float TURN_PER_TICK = 0.25F;
    /** Degrees per tick of turning that count as a full lean. */
    private static final float FULL_TURN = 20F;
    /** The most time one update counts, so that a bird coming back into view doesn't jump. */
    private static final float MOST_TICKS = 2F;
    /** The most limb swing one update counts. Being hit jolts it, and a baby's runs at triple speed. */
    private static final float MOST_SWING = 6F;
    /** How hard the tail and neck are pulled to where the sprint wants them, and how fast that dies out: low, so they overshoot when it stops. */
    private static final float PULL = 0.22F;
    private static final float DAMPING = 0.28F;

    private float stride;
    private float sprint;
    private float turn;
    private float phase;
    private float lastAge;
    private float lastSwing;
    private float carried;
    private float carriedSpeed;

    /**
     * @param radiansPerSwing how much of a stride one unit of limb swing is right now: by adding it up, this can change without the legs jumping
     * @param turning degrees the body turned this tick
     */
    public void update(float strideTarget, float sprintTarget, float radiansPerSwing, float limbSwing, float turning, float age) {
        float ticks = Math.clamp(age - this.lastAge, 0F, MOST_TICKS);
        this.lastAge = age;
        float swung = Math.clamp(limbSwing - this.lastSwing, 0F, MOST_SWING);
        this.lastSwing = limbSwing;
        this.phase = (this.phase + swung * radiansPerSwing) % Mth.TWO_PI;
        this.stride = toward(this.stride, strideTarget, STRIDE_PER_TICK * ticks);
        this.sprint = toward(this.sprint, sprintTarget, SPRINT_PER_TICK * ticks);
        this.turn = toward(this.turn, Math.clamp(turning / FULL_TURN, -1F, 1F), TURN_PER_TICK * ticks);
        for (float left = ticks; left > 0F; left -= 0.25F) {
            float step = Math.min(left, 0.25F);
            this.carriedSpeed += (PULL * (this.sprint - this.carried) - DAMPING * this.carriedSpeed) * step;
            this.carried += this.carriedSpeed * step;
        }
    }

    public float stride() {
        return this.stride;
    }

    /** Never more than its stride. */
    public float sprint() {
        return Math.min(this.sprint, this.stride);
    }

    /** How far it's turning, -1 full left to 1 full right. */
    public float turn() {
        return this.turn;
    }

    public float phase() {
        return this.phase;
    }

    /** Where its tail and neck are, 0 at rest to 1 flat out, swinging past both on the way. */
    public float carried() {
        return this.carried;
    }

    private static float toward(float value, float target, float most) {
        return value + Math.clamp(target - value, -most, most);
    }

    /** What every Roadrunner is made to have. */
    public interface Holder {
        RoadrunnerGait grout$gait();

        boolean grout$isMeep();
    }
}
