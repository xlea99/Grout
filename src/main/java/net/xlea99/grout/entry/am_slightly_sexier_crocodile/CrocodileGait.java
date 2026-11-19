package net.xlea99.grout.entry.am_slightly_sexier_crocodile;

import net.minecraft.util.Mth;

/** One Crocodile's motion over time: what can't be worked out from a single frame ({@link CrocodileMotion}). */
public final class CrocodileGait {
    /** How fast it gets into its stride, and out of it, per tick. */
    private static final float STRIDE_PER_TICK = 0.15F;
    /** Sent by {@code am_crocodile_ambush} when a strike misses. It's the same number there. */
    public static final byte MISSED_EVENT = 97;
    /** How fast it snaps up into its hunting walk, per tick, and how slowly it settles back. */
    private static final float HUNT_RISE_PER_TICK = 0.25F;
    private static final float HUNT_SETTLE_PER_TICK = 0.03F;
    /** After a miss, in ticks: its mouth falling open, staying there, and slowly closing. */
    private static final float GAPE_OPEN = 6F;
    private static final float GAPE_HOLD = 20F;
    private static final float GAPE_CLOSE = 44F;
    /** How fast it gets into a run and out of one, per tick. */
    private static final float RUN_RISE_PER_TICK = 0.2F;
    private static final float RUN_FALL_PER_TICK = 0.1F;
    /** How slowly its mouth opens to the sun, and closes again, per tick. */
    private static final float SUN_OPEN_PER_TICK = 0.02F;
    private static final float SUN_CLOSE_PER_TICK = 0.015F;
    /** How slowly it sinks to lurk, and how fast it surges back up, per tick. */
    private static final float LURK_SINK_PER_TICK = 0.015F;
    private static final float LURK_SURGE_PER_TICK = 0.2F;
    /** The most time one update counts, so that a crocodile coming back into view doesn't jump. */
    private static final float MOST_TICKS = 2F;
    /** The most limb swing one update counts. Being hit jolts it, and a baby's runs at triple speed. */
    private static final float MOST_SWING = 6F;

    private float stride;
    private float hunt;
    private float phase;
    private float lastAge;
    private float lastSwing;
    private float missedAt = -1000F;
    private float run;
    private float sunGape;
    private float lurk;

    /** @param radiansPerSwing how much of a stride one unit of limb swing is right now: by adding it up, this can change without the legs jumping */
    public void update(float strideTarget, boolean hunting, boolean running, boolean sunGaping, boolean lurking, float radiansPerSwing, float limbSwing, float age) {
        float ticks = Math.clamp(age - this.lastAge, 0F, MOST_TICKS);
        this.lastAge = age;
        float swung = Math.clamp(limbSwing - this.lastSwing, 0F, MOST_SWING);
        this.lastSwing = limbSwing;
        this.phase = (this.phase + swung * radiansPerSwing) % Mth.TWO_PI;
        this.stride = toward(this.stride, strideTarget, STRIDE_PER_TICK * ticks);
        this.hunt = hunting ? toward(this.hunt, 1F, HUNT_RISE_PER_TICK * ticks) : toward(this.hunt, 0F, HUNT_SETTLE_PER_TICK * ticks);
        this.run = running ? toward(this.run, 1F, RUN_RISE_PER_TICK * ticks) : toward(this.run, 0F, RUN_FALL_PER_TICK * ticks);
        this.sunGape = sunGaping ? toward(this.sunGape, 1F, SUN_OPEN_PER_TICK * ticks) : toward(this.sunGape, 0F, SUN_CLOSE_PER_TICK * ticks);
        this.lurk = lurking ? toward(this.lurk, 1F, LURK_SINK_PER_TICK * ticks) : toward(this.lurk, 0F, LURK_SURGE_PER_TICK * ticks);
    }

    public float stride() {
        return this.stride;
    }

    /** How far into a run it is, 0 to 1. */
    public float run() {
        return this.run;
    }

    /** How far its mouth has opened to the sun, 0 to 1. */
    public float sunGape() {
        return this.sunGape;
    }

    /** How far down it's sunk to lurk, 0 to 1. */
    public float lurk() {
        return this.lurk;
    }

    /** How far up on its hunting walk it is, 0 to 1. */
    public float hunt() {
        return this.hunt;
    }

    public void missed(float age) {
        this.missedAt = age;
    }

    /** How far its mouth is hanging open after a miss, 0 to 1. */
    public float gape(float age) {
        float since = age - this.missedAt;
        if (since < 0F || since >= GAPE_OPEN + GAPE_HOLD + GAPE_CLOSE)
            return 0F;
        if (since < GAPE_OPEN)
            return smooth(since / GAPE_OPEN);
        return since < GAPE_OPEN + GAPE_HOLD ? 1F : 1F - smooth((since - GAPE_OPEN - GAPE_HOLD) / GAPE_CLOSE);
    }

    private static float smooth(float x) {
        return x * x * (3F - 2F * x);
    }

    public float phase() {
        return this.phase;
    }

    private static float toward(float value, float target, float most) {
        return value + Math.clamp(target - value, -most, most);
    }

    /** What every Crocodile is made to have. */
    public interface Holder {
        CrocodileGait grout$gait();

        /** Which way Alex's Mobs has it curve when it basks: 0 or 1. */
        int grout$baskingType();

        /** How far into basking it is, 0 to 1. */
        float grout$basking(float partialTick);

        /** How far into swimming it is, 0 to 1. */
        float grout$swimming(float partialTick);
    }
}
