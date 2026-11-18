package net.xlea99.lifeguard.entry.am_slightly_sexier_gazelle;

import net.minecraft.util.Mth;

/** One Gazelle's motion over time: what can't be worked out from a single frame ({@link GazelleMotion}). */
public final class GazelleGait {
    /** How fast it gets into its stride, and out of it, per tick. */
    private static final float STRIDE_PER_TICK = 0.3F;
    /** How fast a walk turns into bounding, and back, per tick. */
    private static final float RUN_PER_TICK = 0.15F;
    /** How fast the alarm of first being frightened dies out, per tick: its first bounds are the highest. */
    private static final float ALARM_FADES_PER_TICK = 0.025F;
    /** The most time one update counts, so that a gazelle coming back into view doesn't jump. */
    private static final float MOST_TICKS = 2F;
    /** The most limb swing one update counts. Being hit jolts it, and a baby's runs at triple speed. */
    private static final float MOST_SWING = 6F;

    private float stride;
    private float run;
    private float alarm;
    private float phase;
    private float lastAge;
    private float lastSwing;
    private boolean wasRunning;

    /** @param radiansPerSwing how much of a stride one unit of limb swing is right now: by adding it up, this can change without the legs jumping */
    public void update(float strideTarget, boolean running, float radiansPerSwing, float limbSwing, float age) {
        float ticks = Math.clamp(age - this.lastAge, 0F, MOST_TICKS);
        this.lastAge = age;
        float swung = Math.clamp(limbSwing - this.lastSwing, 0F, MOST_SWING);
        this.lastSwing = limbSwing;
        this.phase = (this.phase + swung * radiansPerSwing) % Mth.TWO_PI;
        this.stride = toward(this.stride, strideTarget, STRIDE_PER_TICK * ticks);
        this.run = toward(this.run, running ? 1F : 0F, RUN_PER_TICK * ticks);
        if (running && !this.wasRunning)
            this.alarm = 1F;
        this.wasRunning = running;
        this.alarm = Math.max(0F, this.alarm - ALARM_FADES_PER_TICK * ticks);
    }

    public float stride() {
        return this.stride;
    }

    /** How much of its motion is bounding, 0 to 1. */
    public float run() {
        return this.run;
    }

    public float alarm() {
        return this.alarm;
    }

    public float phase() {
        return this.phase;
    }

    private static float toward(float value, float target, float most) {
        return value + Math.clamp(target - value, -most, most);
    }

    /** What every Gazelle is made to have. */
    public interface Holder {
        GazelleGait lifeguard$gait();

        boolean lifeguard$isRunning();
    }
}
