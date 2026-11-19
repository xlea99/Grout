package net.xlea99.grout.entry.am_slightly_sexier_grizzly_bear;

/**
 * One bear's gait, eased over time. Being hit sets a mob's walk amount straight to 1.5 for a few ticks, which read directly would throw
 * a standing bear into a full gallop for a moment. Eased, that spike only gets as far as a wiggle.
 */
public final class GrizzlyGait {
    /** How fast a bear gets into its stride, and out of it, per tick. */
    private static final float STRIDE_PER_TICK = 0.25F;
    /** How fast a walk turns into a gallop, and back, per tick. */
    private static final float GALLOP_PER_TICK = 0.125F;
    /** The most time one update counts, so that a bear coming back into view doesn't jump. */
    private static final float MOST_TICKS = 2F;

    private float stride;
    private float gallop;
    private float lastAge;

    public void update(float strideTarget, float gallopTarget, float age) {
        float ticks = Math.clamp(age - this.lastAge, 0F, MOST_TICKS);
        this.lastAge = age;
        this.stride = toward(this.stride, strideTarget, STRIDE_PER_TICK * ticks);
        this.gallop = toward(this.gallop, gallopTarget, GALLOP_PER_TICK * ticks);
    }

    public float stride() {
        return this.stride;
    }

    /** Never more than its stride: a stopping bear's gallop fades slower, and there's no gallop without legs moving. */
    public float gallop() {
        return Math.min(this.gallop, this.stride);
    }

    private static float toward(float value, float target, float most) {
        return value + Math.clamp(target - value, -most, most);
    }

    /** What every Grizzly Bear is made to have. */
    public interface Holder {
        GrizzlyGait grout$gait();
    }
}
