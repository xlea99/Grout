package net.xlea99.grout.entry.am_slightly_sexier_gorilla;

/**
 * One Gorilla's gait, eased over time ({@link GorillaMotion}). Being hit sets a mob's walk amount straight to 1.5 for a few
 * ticks, which read directly would throw a standing Gorilla into a full gallop for a moment; eased, it's a twitch. Its arms
 * likewise ease out of the walk while Alex's Mobs plays one of its own animations on them (a chest pound, a swing, breaking a
 * block), and back in after.
 */
public final class GorillaGait {
    private static final float STRIDE_PER_TICK = 0.2F;
    private static final float GALLOP_PER_TICK = 0.1F;
    private static final float ARMS_PER_TICK = 0.25F;
    /** The most time one update counts, so that a Gorilla coming back into view doesn't jump. */
    private static final float MOST_TICKS = 2F;
    /** The most limb swing a tick counts: about 12 blocks a second. */
    private static final float MOST_SWING = 3F;

    private float stride;
    private float gallop;
    private float armsFree = 1F;
    private float lastAge;
    /** The stride's phase, added up from the limb swing at whatever tempo the gait has, so that a change of tempo never jumps. */
    private float phase;
    private float lastTicks;

    public void update(float strideTarget, float gallopTarget, boolean armsBusy, float age) {
        float ticks = Math.clamp(age - this.lastAge, 0F, MOST_TICKS);
        this.lastAge = age;
        this.lastTicks = ticks;
        this.stride = toward(this.stride, strideTarget, STRIDE_PER_TICK * ticks);
        this.gallop = toward(this.gallop, gallopTarget, GALLOP_PER_TICK * ticks);
        this.armsFree = toward(this.armsFree, armsBusy ? 0F : 1F, ARMS_PER_TICK * ticks);
    }

    /**
     * Move the phase on by the time since the last {@link #update}, at this much limb swing a tick (4 per block: the Gorilla's
     * real ground speed, which vanilla's own limb swing caps at 1 a tick, a sprint's worth) and this many radians per unit.
     */
    public float advance(float swingPerTick, float radiansPerSwing) {
        this.phase = (this.phase + this.lastTicks * Math.clamp(swingPerTick, 0F, MOST_SWING) * radiansPerSwing) % (float) (2 * Math.PI * 64);
        return this.phase;
    }

    public float phase() {
        return this.phase;
    }

    public float stride() {
        return this.stride;
    }

    /** Never more than its stride: there's no gallop without legs moving. */
    public float gallop() {
        return Math.min(this.gallop, this.stride);
    }

    public float armsFree() {
        return this.armsFree;
    }

    private static float toward(float value, float target, float most) {
        return value + Math.clamp(target - value, -most, most);
    }

    /** What every Gorilla is made to have, on the client. */
    public interface Holder {
        GorillaGait grout$gait();
    }
}
