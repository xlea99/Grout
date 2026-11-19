package net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito;

import java.util.Arrays;

/**
 * How a Crimson Mosquito moves ({@link AmSlightlySexierCrimsonMosquito#MOTION}), added after Alex's Mobs has posed its model.
 * Pure arithmetic on a {@link Pose}.
 *
 * <p>Alex's Mosquito flies level whatever it does, its wings flapping about five times a second and its six legs flapping in
 * step. Here its wings buzz; it banks into its turns and pitches nose-down into its speed, nose-up as it climbs; its long legs
 * hang from it and swing behind as it speeds up and out as it turns, catching up a beat late, still while it cruises; a full
 * belly droops heavy, swings a beat behind every pitch and turn, and pumps as it drinks; the antennae twitch; and landed, now
 * and then, it snaps its front legs up and rubs them together. All in Alex's model space: +x its left, +y down, -z ahead.
 */
public final class MosquitoMotion {
    public static final int BODY = 0, HEAD = 1, TAIL = 2, WING_L = 3, WING_R = 4, LEGS_L = 5, LEGS_R = 6, LEG_L1 = 7, LEG_R1 = 8,
        ANTENNA_L = 9, ANTENNA_R = 10, PARTS = 11;

    private static final float PI = (float) Math.PI;
    private static final float DEG = PI / 180F;

    /** Wings: radians of beat per tick (Alex's is 1.65, about 5 beats a second; this is about 8), how far, and Alex's lift. */
    static final float WING_BEAT = 2.6F;
    static final float WING_SWING = 0.45F;
    static final float WING_LIFT = 0.2F;
    /** A wing's tip traces a figure eight: it sweeps fore and aft a little twice a beat. */
    static final float WING_SWEEP = 6F * DEG;

    /** Bank: degrees of roll per degree a tick of turn, at most; and how fast it leans in and out, per tick. */
    static final float BANK_PER_TURN = 1.6F;
    static final float BANK_MOST = 35F * DEG;
    static final float BANK_EASE = 0.25F;
    /**
     * Pitch: nose-down a little per block a tick of speed ahead, more per block a tick a tick of speeding up (it tips into a
     * dash and levels out at speed), nose-up per block a tick of climb, at most, and its ease. (70 degrees per block a tick of
     * speed, 20 at a cruise, had it always aimed like it was diving, even flying level or up: the user, 2026-11-08.)
     */
    static final float PITCH_PER_SPEED = 20F * DEG;
    static final float PITCH_PER_ACCEL = 300F * DEG;
    static final float PITCH_PER_CLIMB = 150F * DEG;
    static final float PITCH_MOST = 28F * DEG;
    static final float PITCH_EASE = 0.2F;
    /** A full belly is heavy at the back: it flies this much more nose-up, full. */
    static final float FULL_NOSE_UP = 8F * DEG;
    static final float SPEED_EASE = 0.3F;

    /**
     * Legs: trail back per block a tick of speed, the whole time it flies; swing further back per block a tick a tick of
     * speeding up; out a little per degree a tick of turn; at most; their springiness, the sideways one steadier. (The user,
     * 2026-11-08: lean into the legs drifting behind, way less in-out: turning out at 1.2 degrees per degree, on top of
     * Alex's own in-out leg flap, pulsed.)
     */
    static final float LEGS_PER_SPEED = 110F * DEG;
    static final float LEGS_PER_ACCEL = 700F * DEG;
    static final float LEGS_PER_TURN = 0.4F * DEG;
    static final float LEGS_MOST = 45F * DEG, LEGS_SIDE_MOST = 15F * DEG;
    static final float LEGS_STIFF = 0.09F, LEGS_DAMP = 0.32F, LEGS_SIDE_DAMP = 0.6F;
    /** How much of Alex's in-flight leg flap (in and out, about twice a second) is left: a fifth. */
    static final float ALEX_LEG_FLAP_KEPT = 0.2F;

    /** Belly: how far it droops full; how far it swings out on a turn per degree a tick; springiness, empty and full. */
    static final float TAIL_SAG = 18F * DEG;
    static final float TAIL_PER_TURN = 0.8F * DEG;
    static final float TAIL_MOST = 25F * DEG;
    static final float TAIL_STIFF = 0.12F, TAIL_STIFF_FULL = 0.05F, TAIL_DAMP = 0.3F, TAIL_DAMP_FULL = 0.16F;
    /** Drinking: a gulp a second (Alex's drink runs on 20 ticks), the belly swelling this much with each. */
    static final float GULP_TICKS = 20F;
    static final float GULP_SWELL = 0.08F;
    /** Resting: the belly breathes. */
    static final float BREATH_TICKS = 46F, BREATH_SWELL = 0.025F;

    /**
     * Holding station: hovering, it sways gently, a little yaw and roll at about one and a half and two seconds a sway, gone
     * once it moves (by {@code HOLD_SPEED} blocks a tick). A quick 2 degree jitter all through its flight, 4 to 9 wobbles a
     * second, read as glitching, not nerves (the user, 2026-11-08).
     */
    static final float HOLD_SWAY = 2.5F * DEG;
    static final float HOLD_SPEED = 0.06F;

    /** Antennae: each gets a slot this long and flicks at its start about one slot in three. */
    static final float ANTENNA_SLOT = 40F, ANTENNA_FLICK_TICKS = 5F, ANTENNA_FLICK = 22F * DEG;

    /**
     * Grooming, landed and still: each slot this long, about one in four, it snaps its front legs up and forward and rubs them
     * together, quick, for {@code GROOM_TICKS}, easing in and out over {@code GROOM_SNAP}.
     */
    static final float GROOM_SLOT = 90F, GROOM_TICKS = 26F, GROOM_SNAP = 3F;
    static final float GROOM_RAISE = 38F * DEG, GROOM_FORWARD = 42F * DEG, GROOM_RUB = 14F * DEG, GROOM_RUB_PACE = 1.9F;
    static final float GROOM_HEAD = 12F * DEG;

    /** What to add to each part, in its own frame; the wings' roll set outright while they beat; the belly's scale multiplied. */
    public static final class Pose {
        public final float[] rx = new float[PARTS], ry = new float[PARTS], rz = new float[PARTS];
        public boolean wingsOwn;
        public float wingLZ, wingRZ;
        public float tailScale = 1F;

        void clear() {
            Arrays.fill(rx, 0F);
            Arrays.fill(ry, 0F);
            Arrays.fill(rz, 0F);
            wingsOwn = false;
            tailScale = 1F;
        }
    }

    private MosquitoMotion() {
    }

    /**
     * @param fly how far it's in flight pose, 0 landed to 1 (Alex's own progress)
     * @param flapping whether Alex has its wings beating (in flight, or a random flap on the ground)
     * @param turn its body's turn, degrees a tick, positive to its right
     * @param ahead its speed along the way it faces, blocks a tick; {@code climb} upward
     * @param fullness how full of blood, 0 to 1
     */
    public static void apply(Pose pose, MosquitoBody body, float age, int id, float fly, boolean flapping, float turn, float ahead, float climb,
                             float fullness, boolean drinking) {
        pose.clear();
        float ticks = body.tick(age);
        fly = Math.clamp(fly, 0F, 1F);

        float lastSpeed = body.speed;
        body.speed += (ahead - body.speed) * Math.min(1F, SPEED_EASE * ticks);
        body.accel = ticks > 0F ? (body.speed - lastSpeed) / ticks : 0F;

        float bankTarget = fly * Math.clamp(-turn * BANK_PER_TURN * DEG, -BANK_MOST, BANK_MOST);
        float pitchTarget = fly * (Math.clamp(body.speed * PITCH_PER_SPEED + body.accel * PITCH_PER_ACCEL - climb * PITCH_PER_CLIMB, -PITCH_MOST, PITCH_MOST)
            - FULL_NOSE_UP * fullness);
        body.bank += (bankTarget - body.bank) * Math.min(1F, BANK_EASE * ticks);
        body.pitch += (pitchTarget - body.pitch) * Math.min(1F, PITCH_EASE * ticks);
        pose.rz[BODY] += body.bank;
        pose.rx[BODY] += body.pitch;
        float holding = fly * (1F - smooth(Math.abs(body.speed) / HOLD_SPEED));
        pose.ry[BODY] += holding * HOLD_SWAY * (sin(age * 0.2F + id) * 0.7F + sin(age * 0.33F + id * 2F) * 0.3F);
        pose.rz[BODY] += holding * HOLD_SWAY * sin(age * 0.16F + id * 3F);
        // The head keeps a little more level than the body.
        pose.rx[HEAD] -= body.pitch * 0.3F;

        // Legs hang from the body and chase its angle, swinging behind as it speeds up and out as it turns.
        float trail = Math.max(0F, body.speed) * LEGS_PER_SPEED + body.accel * LEGS_PER_ACCEL;
        float[] lx = MosquitoBody.spring(body.legsX, body.legsXVel,
            body.pitch + fly * Math.clamp(trail, -LEGS_MOST * 0.5F, LEGS_MOST), LEGS_STIFF, LEGS_DAMP, ticks);
        body.legsX = lx[0];
        body.legsXVel = lx[1];
        float[] lz = MosquitoBody.spring(body.legsZ, body.legsZVel,
            body.bank + Math.clamp(-turn * LEGS_PER_TURN * fly, -LEGS_SIDE_MOST, LEGS_SIDE_MOST), LEGS_STIFF, LEGS_SIDE_DAMP, ticks);
        body.legsZ = lz[0];
        body.legsZVel = lz[1];
        float legsX = fly * (body.legsX - body.pitch), legsZ = fly * (body.legsZ - body.bank);
        pose.rx[LEGS_L] += legsX;
        pose.rx[LEGS_R] += legsX;
        pose.rz[LEGS_L] += legsZ;
        pose.rz[LEGS_R] += legsZ;

        // The belly: chases the body's pitch, and swings out on turns, heavier and slower the fuller it is; droops when full.
        float k = TAIL_STIFF + (TAIL_STIFF_FULL - TAIL_STIFF) * fullness, c = TAIL_DAMP + (TAIL_DAMP_FULL - TAIL_DAMP) * fullness;
        float[] tx = MosquitoBody.spring(body.tailX, body.tailXVel, body.pitch, k, c, ticks);
        body.tailX = tx[0];
        body.tailXVel = tx[1];
        float[] ty = MosquitoBody.spring(body.tailY, body.tailYVel, Math.clamp(-turn * TAIL_PER_TURN * fly, -TAIL_MOST, TAIL_MOST), k, c, ticks);
        body.tailY = ty[0];
        body.tailYVel = ty[1];
        pose.rx[TAIL] += (body.tailX - body.pitch) - TAIL_SAG * fullness;
        pose.ry[TAIL] += body.tailY;
        if (drinking) {
            float gulp = Math.max(0F, sin(age * 2F * PI / GULP_TICKS + id));
            pose.tailScale *= 1F + GULP_SWELL * gulp * gulp;
        } else {
            pose.tailScale *= 1F + BREATH_SWELL * (1F - fly) * sin(age * 2F * PI / BREATH_TICKS + id);
        }

        if (flapping) {
            float beat = age * WING_BEAT + id;
            float swing = WING_SWING * (1F + 0.15F * sin(age * 0.9F + id));
            pose.wingsOwn = true;
            pose.wingLZ = -(WING_LIFT + swing * cos(beat));
            pose.wingRZ = WING_LIFT + swing * cos(beat);
            pose.ry[WING_L] += WING_SWEEP * sin(beat * 2F);
            pose.ry[WING_R] -= WING_SWEEP * sin(beat * 2F);
        }

        antenna(pose, ANTENNA_L, age, id, 1);
        antenna(pose, ANTENNA_R, age, id, 2);

        float groom = (1F - fly) * (drinking ? 0F : 1F) * (Math.abs(ahead) < 0.02F ? 1F : 0F) * groomEnvelope(age, id);
        if (groom > 0F) {
            float rub = GROOM_RUB * snappy(sin(age * GROOM_RUB_PACE));
            pose.rz[LEG_L1] -= GROOM_RAISE * groom;
            pose.rz[LEG_R1] += GROOM_RAISE * groom;
            pose.ry[LEG_L1] += (GROOM_FORWARD + rub) * groom;
            pose.ry[LEG_R1] += (-GROOM_FORWARD + rub) * groom;
            pose.rx[HEAD] += GROOM_HEAD * groom;
        }
    }

    // Taking off and landing. Alex's flight pose blends in over 5 ticks, but three of its parts switch on at full strength
    // the moment it's above 0 and off the moment it's back: a bob of the body (up to 5 pixels), a flap of each leg, a sway
    // of the belly; and the wings go from folded to spread in one frame. A jump inside a blend: "a slight beat where it
    // doesn't look quite right" (the user, 2026-11-08). Each of these takes Alex's part as he adds it (Citadel's bob, flap
    // and walk) and returns what to add so that it grows in with the flight pose instead.

    /** Folded wings, as Alex holds them while they aren't beating. */
    public static final float ALEX_WING_FOLD_X = 30F * DEG, ALEX_WING_FOLD_Y = 70F * DEG;
    /** Alex's body lift in flight, in pixels, which this eases in and out rather than starting and stopping dead. */
    static final float ALEX_FLIGHT_LIFT = 10F;

    /**
     * How much of Alex's in-flight bob is kept (5 pixels on a steady 1.25 second beat, which read as predictable up and down:
     * the user, 2026-11-08), drawn from two paces so that it never quite repeats.
     */
    static final float BOB_KEPT = 0.35F;

    /** The body's height: Alex's bob made smaller and uneven, grown in by {@code f}; and his straight lift eased. */
    public static float bodyHeightCorrection(float age, float f, int id) {
        float alex = sin(age * 0.25F) * 2.5F - 2.5F;
        float uneven = 0.6F * sin(age * 0.21F + id) + 0.4F * sin(age * 0.47F + id * 2F);
        float ours = f * BOB_KEPT * (uneven * 2.5F - 2.5F);
        return ours - alex - ALEX_FLIGHT_LIFT * (smooth(f) - f);
    }

    /** The belly's pitch: Alex's sway (speed 0.5, swing 0.075, set back 0.1) grown in by {@code f}. */
    public static float tailCorrection(float age, float f) {
        return -(1F - f) * (cos(age * 0.5F) * 0.075F - 0.1F);
    }

    /**
     * A leg's roll: Alex's flap (speed 0.5, swing 0.25, set out 0.1, this leg's offset, the left ones turned round) brought
     * down to {@link #ALEX_LEG_FLAP_KEPT} of its in-and-out, and the whole of it grown in by {@code f}.
     */
    public static float legFlapCorrection(float age, float offset, boolean left, float f) {
        float swing = cos(age * 0.5F + offset) * 0.25F;
        float side = left ? -1F : 1F;
        float alex = side * (swing + 0.1F);
        float wanted = side * f * (ALEX_LEG_FLAP_KEPT * swing + 0.1F);
        return wanted - alex;
    }

    /** How far the wings have unfolded and taken up their beat, taking off or landing. */
    public static float wingsOpen(float f) {
        return smooth(f);
    }

    /** 0 to 1: whether this Mosquito is grooming now, easing in and out quickly. Worked out from the time, nothing kept. */
    static float groomEnvelope(float age, int id) {
        float time = age + id * 17;
        int slot = (int) Math.floor(time / GROOM_SLOT);
        if ((mix(slot * 31 + id * 7) & 3) != 0)
            return 0F;
        float into = time - slot * GROOM_SLOT;
        if (into > GROOM_TICKS)
            return 0F;
        return smooth(into / GROOM_SNAP) * smooth((GROOM_TICKS - into) / GROOM_SNAP);
    }

    static void antenna(Pose pose, int part, float age, int id, int which) {
        float time = age + id * 5 + which * 23;
        int slot = (int) Math.floor(time / ANTENNA_SLOT);
        float into = time - slot * ANTENNA_SLOT;
        if (into < ANTENNA_FLICK_TICKS && mix(slot * 13 + id * 3 + which) % 3 == 0)
            pose.rx[part] -= ANTENNA_FLICK * sin(PI * into / ANTENNA_FLICK_TICKS);
    }

    /** A sine pushed toward its ends: quick through the middle, a beat at each end. */
    static float snappy(float s) {
        return Math.signum(s) * (float) Math.sqrt(Math.abs(s));
    }

    static int mix(int x) {
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        x *= 0x846ca68b;
        x ^= x >>> 16;
        return x & 0x7fffffff;
    }

    static float smooth(float x) {
        float t = Math.clamp(x, 0F, 1F);
        return t * t * (3F - 2F * t);
    }

    private static float sin(float x) {
        return (float) Math.sin(x);
    }

    private static float cos(float x) {
        return (float) Math.cos(x);
    }
}
