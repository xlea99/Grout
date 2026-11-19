package net.xlea99.grout.entry.am_slightly_sexier_gorilla;

import java.util.Arrays;

/**
 * How a Gorilla moves ({@link AmSlightlySexierGorilla#MOTION}), added after Alex's Mobs has posed its model. Pure arithmetic on
 * a {@link Pose}, so that it can be checked without the game: where every hand and foot lands, frame by frame.
 *
 * <p><b>Alex's walk</b> swings all four limbs by one sine, diagonal pairs together like a trotting dog, scaled by the walk
 * amount: at a march the hands and feet slide, and slower it's a tiptoe.
 *
 * <p><b>Knuckle-walking</b>, here, is a big ape vaulting over its arms. Each long arm plants far forward and stays planted while the
 * body swings over it like a pole vault: the shoulders rise as the arm comes upright and sink as it trails, so the chest seems
 * to haul itself forward. The shoulder over the planted arm rides high, the one whose arm swings drops and twists forward to
 * reach, and the head stays level through all of it. The short hind legs patter underneath, each about a quarter stride ahead
 * of the arm on its side (a four-beat walk), with the hips rocking a little on their own. Every hand and foot on the ground
 * stays put on the ground: its angle comes from where it must be, not the other way round.
 *
 * <p><b>Anything faster than a march</b> (hurrying, angry, a charge) turns into a bound: both arms reach and land as one, then
 * both legs swing up under as one, the body tipping onto the arms and driving off the legs, a lunge each stride.
 *
 * <p><b>Upright</b> (Alex stands them up to pound their chests and to swing), moving turns into a waddle: the whole body rocks
 * over each short leg, the hips twisting, the arms held out for balance.
 *
 * <p>Every number is one dial, in degrees or model pixels (16 to a block, before the Gorilla's own scale).
 */
public final class GorillaMotion {
    public static final int BODY = 0, CHEST = 1, HEAD = 2, LEFT_ARM = 3, RIGHT_ARM = 4, LEFT_LEG = 5, RIGHT_LEG = 6, PARTS = 7;

    private static final float PI = (float) Math.PI;
    private static final float TWO_PI = 2F * PI;
    private static final float DEG = PI / 180F;

    // Alex's model, in pixels: limbs from pivot to the ground, and where the joints sit relative to the body's pivot.
    static final float ARM_LENGTH = 16.5F;
    static final float LEG_LENGTH = 9.5F;
    /** Half a limb's depth, front to back: tilted, its sole stands on its lower edge. */
    static final float ARM_HALF = 2.5F, LEG_HALF = 2.5F;
    /** And half its width, side to side: tilted sideways, likewise. */
    static final float ARM_SIDE = 3F, LEG_SIDE = 2.5F;
    /** Upright, how far each hip sits from the middle (Alex spreads the legs a pixel as it stands). */
    static final float UPRIGHT_HIP_X = 4F;
    static final float SHOULDER_X = 6F;
    /** A shoulder from the chest's pivot, and the chest's pivot from the body's. */
    static final float SHOULDER_UP_OF_CHEST = 6.5F, SHOULDER_FORE_OF_CHEST = 9.5F;
    static final float SHOULDER_UP = 2.5F, SHOULDER_FORE = 12.5F;
    static final float HIP_X = 3F, HIP_DOWN = 4.5F, HIP_BACK = 2.5F;

    /** Radians of stride per unit of limb swing, at scale 1 (a Gorilla twice the size takes half as many). */
    static final float STRIDE = 0.93F;
    /**
     * Limb swing counts 4 per block and a block is 16 pixels (16 / scale of this model's), so the ground passes 4 pixels per unit
     * of limb swing at scale 1, and a stride is this many pixels at any scale.
     */
    static final float STRIDE_PIXELS = 4F * TWO_PI / STRIDE;
    /** How much of a stride a hand is planted: most of it. Arms are what a Gorilla walks on. */
    static final float ARM_DOWN = 0.65F;
    /** How much of a stride a foot is down: short, quick steps under the body. */
    static final float LEG_DOWN = 0.5F;
    static final float ARM_SWEEP = ARM_DOWN * STRIDE_PIXELS;
    static final float LEG_SWEEP = LEG_DOWN * STRIDE_PIXELS;
    /** How far a hip slides along with its foot, either way, in pixels: the legs are too short to sweep that far by angle alone. */
    static final float HIP_REACH = 3F;
    /** How far behind its hind leg each arm moves, in radians of stride: about a quarter, for four beats. */
    static final float PACE_LAG = 1.5F;
    /** How far off exactly opposite the two sides are, so that the beats don't fall perfectly even. */
    static final float SIDES_UNEVEN = 0.12F;
    /** The walk amount where the stride is full. A wandering Gorilla reads about 0.35, a marching one about 0.54. */
    static final float FULL_WALK = 0.3F;

    /** How much of the rise and fall over a planted arm the body takes (the rest the arm takes, sliding in its shoulder). */
    static final float VAULT = 0.6F;
    /** The shoulder over a swinging arm drops this far, at its lowest. (7 was "too diva-like", the user, 2026-11-08.) */
    static final float SHOULDER_ROLL = 4.5F * DEG;
    /** And turns forward with the arm reaching, this far at the most. */
    static final float SHOULDER_TWIST = 4.5F * DEG;
    static final float HIP_ROLL = 1.8F * DEG;
    static final float HIP_TWIST = 2F * DEG;
    /** A Silverback (scale above 1) carries itself heavier: this much more roll and twist. */
    static final float SILVERBACK_HEFT = 1.15F;
    static final float ARM_LIFT = 1.5F;
    static final float LEG_LIFT = 1.8F;
    static final float ARM_ARC = 4F * DEG;
    static final float LEG_ARC = 3F * DEG;
    /** How much of the body's pitch, roll and twist the head takes back: a Gorilla's eyes stay on where it's going. */
    static final float HEAD_STEADY = 0.8F;
    static final float HEAD_LOW = 4F * DEG;
    static final float HEAD_NOD = 1.5F * DEG;

    /**
     * The walk amounts between which the walk turns into the bound: anything past a march (about 0.54) is a Gorilla in a
     * hurry, angry or showing off, and lunges. A hurrying follower reads about 0.84, a charge 1.
     */
    static final float GALLOP_START = 0.58F;
    static final float GALLOP_FULL = 0.7F;
    /**
     * Radians of bound per unit of limb swing, at scale 1: about 3 blocks a bound (4 for a Silverback), at a slower tempo than the
     * walk's 1.7, so each one covers ground. The same tempo as the walk looked like a walk with its limbs paired.
     */
    static final float BOUND_STRIDE = 0.52F;
    /**
     * How much of a bound each hand, and each foot, is down: a bound's 48 pixels of ground pass quickly, and short legs can only
     * sweep so far, so the feet are down briefly and push. Longer, and a planted foot slid along (13 pixels in the offline check).
     */
    static final float GALLOP_DOWN = 0.35F, GALLOP_LEG_DOWN = 0.3F;
    /** A bound, in pixels of ground: the ground passes 4 pixels per unit of limb swing at scale 1. */
    static final float BOUND_PIXELS = 4F * TWO_PI / BOUND_STRIDE;
    /**
     * Through the air each arm, and leg, reaches further than where it will land, this much more late in its swing, and settles
     * back as it lands: a hand landing at full reach would land in the air. Where they land and leave comes from the ground.
     */
    static final float GALLOP_FORE_OVER = 8F * DEG, GALLOP_HIND_OVER = 22F * DEG;
    /**
     * Each pair's second limb trails its first by this much, in radians of bound: the hands land close together but not as one,
     * a beat apart; the feet nearly as one.
     */
    static final float GALLOP_ARM_SPLIT = 0.8F, GALLOP_LEG_SPLIT = 0.25F;
    /** How much of its swing a hand, or foot, takes to peel off the ground: a hand eases off, a foot kicks off. */
    static final float GALLOP_ARM_RELEASE = 0.45F, GALLOP_LEG_RELEASE = 0.25F;
    /** And how much of the end of its swing it takes to settle onto the ground again. */
    static final float GALLOP_CATCH = 0.3F;
    /**
     * Where the arms work in the bound, from the legs: the hands grab the ground a fifth of a bound after the feet land, while
     * the legs are mid-push, so the push drives the body forward over them; they let go as the legs swing up under. (The user,
     * 2026-11-08: "they grab the ground in front of them and use it to lunge themselves forward", the strength in the legs'
     * push, the arms "just a casual pull". Arms leading with a big wind-up read backwards.)
     */
    static final float GALLOP_ARMS_AFTER = -0.4F * PI;
    static final float GALLOP_LIFT = 3F;
    /** The legs' push lifts the hips and tips the body forward onto the hands: the lunge. It settles back as the legs come under. */
    static final float GALLOP_ROCK = 12F * DEG;
    static final float GALLOP_BOUND = 2.5F;
    /** The chest leans out a little as the hands go for the ground. */
    static final float GALLOP_CHEST = 3F * DEG;

    /** Upright: radians of stride per unit of limb swing, at scale 1. Short legs, short quick steps. */
    static final float WADDLE_STRIDE = 1.9F;
    static final float WADDLE_DOWN = 0.6F;
    static final float WADDLE_SWEEP = WADDLE_DOWN * 4F * TWO_PI / WADDLE_STRIDE;
    static final float WADDLE_ROLL = 10F * DEG;
    static final float WADDLE_TWIST = 8F * DEG;
    static final float WADDLE_LIFT = 2F;
    /** Arms out to the sides and a little forward for balance, swinging against the legs. */
    static final float WADDLE_ARMS_OUT = 28F * DEG;
    static final float WADDLE_ARMS_FORWARD = 18F * DEG;
    static final float WADDLE_ARM_SWING = 12F * DEG;
    /** Bow legs: each leg turned out this far. */
    static final float WADDLE_BOW = 6F * DEG;

    /**
     * Standing up and dropping back down. Alex's Mobs blends its standing pose in at a constant rate over 10 ticks, every part
     * together: a garage door (the user, 2026-11-08). This reshapes the blend instead (its numbers are Alex's own, from the
     * model) and adds what a body does on the way. The rise takes {@code STAND_SHARE} of Alex's ticks, gathering first, heaving
     * fast, overshooting a little and settling; the drop waits that much at the top, then falls the same way, landing with a
     * dip. The arms shove off the ground going up and reach to catch it coming down; the head leads up; the spine curls.
     */
    static final float STAND_SHARE = 1F;
    /**
     * How far the rise sinks back before it heaves, and carries past before it settles, as a share of the whole move. The rise
     * itself eases in and out, its middle about 1.7 times Alex's even pace: a quick "ease in-out back" over 7 ticks peaked at
     * 45 degrees a tick and read "zippy" (the user, 2026-11-08).
     */
    static final float STAND_WAVE = 0.03F;
    static final float STAND_SPINE = 7F * DEG;
    static final float STAND_HEAD_LEAD = 12F * DEG;
    static final float STAND_SHOVE = 25F * DEG;
    static final float STAND_CATCH = 30F * DEG;
    // Alex's standing pose, all of it, from ModelGorilla (rotations in degrees, positions in pixels).
    static final float ALEX_STAND_BODY = -80F * DEG, ALEX_STAND_LIMB = 80F * DEG;

    /** Ticks per breath. */
    static final float BREATH = 70F;
    static final float BREATH_SWELL = 0.012F;
    static final float BREATH_RISE = 0.15F;
    static final float BREATH_HEAD = 0.7F * DEG;
    static final float BREATH_MOVING = 0.7F;
    static final float IDLE_HEAD_YAW = 4F * DEG;
    static final float IDLE_HEAD_PITCH = 2F * DEG;

    /** What to add to each part, in its own frame (radians, pixels), plus the chest's scale. */
    public static final class Pose {
        public final float[] rx = new float[PARTS], ry = new float[PARTS], rz = new float[PARTS];
        public final float[] px = new float[PARTS], py = new float[PARTS], pz = new float[PARTS];
        public float chestSwell = 1F;

        void clear() {
            Arrays.fill(rx, 0F);
            Arrays.fill(ry, 0F);
            Arrays.fill(rz, 0F);
            Arrays.fill(px, 0F);
            Arrays.fill(py, 0F);
            Arrays.fill(pz, 0F);
            chestSwell = 1F;
        }
    }

    private GorillaMotion() {
    }

    /**
     * @param amount the mob's walk amount, 0 to 1, or up to 1.5 for a moment when it's hit
     * @param swingPerTick its real ground speed as limb swing, 4 per block a tick: vanilla's limb swing stops at 1, a sprint
     * @param sit how far it's sat down, 0 to 1; {@code stand} likewise, up on its hind legs, as Alex's Mobs has it
     * @param rising whether it's on its way up (else down, or still)
     * @param bodyPitch the body's pitch as Alex's Mobs posed it (standing is about -80 degrees)
     * @param armsBusy Alex's Mobs is playing one of its own animations, which moves the arms
     * @param scale the Gorilla's own size: 1, 1.3 for a Silverback, 0.5 for a baby
     * @param id anything unique to this Gorilla, so that no two breathe together
     */
    public static void apply(Pose pose, GorillaGait gait, float limbSwing, float amount, float swingPerTick, float age, int id, float scale,
                             float sit, float stand, boolean rising, float bodyPitch, boolean armsBusy) {
        pose.clear();
        float shown = standTransition(pose, Math.clamp(stand, 0F, 1F), rising);
        bodyPitch += (shown - stand) * ALEX_STAND_BODY;
        stand = shown;
        float fours = 1F - Math.clamp(Math.max(sit, stand), 0F, 1F);
        float upright = Math.clamp(stand, 0F, 1F) * (1F - Math.clamp(sit, 0F, 1F));
        gait.update(Math.clamp(amount / FULL_WALK, 0F, 1F), smooth((amount - GALLOP_START) / (GALLOP_FULL - GALLOP_START)), armsBusy, age);
        float move = gait.stride();
        float gallop = gait.gallop() * fours;
        float walk = (move - gait.gallop()) * fours;
        float arms = gait.armsFree();
        float heft = scale > 1.1F ? SILVERBACK_HEFT : 1F;
        float size = Math.max(scale, 0.1F);
        float bounding = move > 0F ? Math.clamp(gait.gallop() / move, 0F, 1F) : 0F;
        float phase = gait.advance(swingPerTick, (STRIDE + (BOUND_STRIDE - STRIDE) * bounding) / size);
        if (walk > 0F)
            knuckleWalk(pose, walk, phase, arms, heft);
        if (gallop > 0F)
            gallop(pose, gallop, phase, arms);
        if (upright * move > 0F)
            waddle(pose, upright * move, limbSwing * WADDLE_STRIDE / size, arms, bodyPitch);
        breathe(pose, age, id, move, fours);
        float idle = fours * (1F - move);
        pose.ry[HEAD] += IDLE_HEAD_YAW * idle * sin(age / 41F + id);
        pose.rx[HEAD] += IDLE_HEAD_PITCH * idle * sin(age / 57F + id * 1.7F);
    }

    static void knuckleWalk(Pose pose, float w, float phase, float arms, float heft) {
        float tHL = through(phase), tHR = through(phase + PI + SIDES_UNEVEN);
        float tFL = through(phase - PACE_LAG), tFR = through(phase + PI + SIDES_UNEVEN - PACE_LAG);
        float nHL = footAt(tHL, LEG_DOWN), nHR = footAt(tHR, LEG_DOWN), nFL = footAt(tFL, ARM_DOWN), nFR = footAt(tFR, ARM_DOWN);
        float aHL = airAt(tHL, LEG_DOWN), aHR = airAt(tHR, LEG_DOWN), aFL = airAt(tFL, ARM_DOWN), aFR = airAt(tFR, ARM_DOWN);

        float roll = heft * SHOULDER_ROLL * (aFL - aFR);
        float twist = heft * SHOULDER_TWIST * (nFR - nFL);
        float hipRoll = heft * HIP_ROLL * (aHL - aHR);
        float hipTwist = heft * HIP_TWIST * (nHR - nHL);

        // The vault: the body pitches forward over the planted arms as far as they trail or reach.
        float gFL = 1F - aFL, gFR = 1F - aFR;
        float need = (gFL * rootDrop(ARM_LENGTH, ARM_HALF, asin(ARM_SWEEP * nFL / ARM_LENGTH))
            + gFR * rootDrop(ARM_LENGTH, ARM_HALF, asin(ARM_SWEEP * nFR / ARM_LENGTH))) / Math.max(gFL + gFR, 1e-3F);
        float pitch = (float) Math.asin(Math.clamp(VAULT * need / SHOULDER_FORE, -1F, 1F));

        // Hips: the body sinks as far as the lower planted foot needs, and a planted leg that needs less slides up into it.
        float hipDzPitch = HIP_DOWN * sin(pitch) - HIP_BACK * (1F - cos(pitch));
        float hipDyPitch = -(HIP_DOWN * (1F - cos(pitch)) + HIP_BACK * sin(pitch));
        float[] legAngle = new float[2], legNeed = new float[2], legSlide = new float[2], legDy = new float[2];
        float bob = 0F;
        for (int i = 0; i < 2; i++) {
            float side = i == 0 ? 1F : -1F;
            float n = i == 0 ? nHL : nHR, g = 1F - (i == 0 ? aHL : aHR);
            legSlide[i] = HIP_REACH * 2F * n;
            float dz = hipDzPitch - side * HIP_X * sin(hipTwist);
            legAngle[i] = asin((LEG_SWEEP * n - legSlide[i] - dz) / LEG_LENGTH);
            legDy[i] = hipDyPitch + side * HIP_X * sin(hipRoll) - HIP_DOWN * (1F - cos(hipRoll));
            legNeed[i] = rootDrop(LEG_LENGTH, LEG_HALF, legAngle[i]) - legDy[i];
            bob = Math.max(bob, g * legNeed[i]);
        }

        pose.rx[BODY] += w * pitch;
        pose.rz[BODY] += w * hipRoll;
        pose.ry[BODY] += w * hipTwist;
        pose.py[BODY] += w * bob;
        pose.rz[CHEST] += w * (roll - hipRoll);
        pose.ry[CHEST] += w * (twist - hipTwist);

        for (int i = 0; i < 2; i++) {
            float side = i == 0 ? 1F : -1F;
            float air = i == 0 ? aHL : aHR;
            int leg = i == 0 ? LEFT_LEG : RIGHT_LEG;
            // Sideways: the hip moves with the body's roll and twist, and a leg swung on a twisted body points a little aside.
            float dx = -HIP_DOWN * sin(hipRoll) + HIP_BACK * sin(hipTwist) + LEG_LENGTH * sin(legAngle[i]) * sin(hipTwist);
            float tilt = asin(dx / LEG_LENGTH);
            pose.rx[leg] += w * (legAngle[i] - pitch);
            pose.pz[leg] += w * legSlide[i];
            pose.py[leg] += w * (legNeed[i] - bob - LEG_LIFT * air - LEG_SIDE * Math.abs(sin(tilt)));
            pose.rz[leg] += w * (tilt - hipRoll - side * LEG_ARC * air);
        }

        float shDyCommon = SHOULDER_FORE * sin(pitch) + SHOULDER_UP * (1F - cos(pitch)) + bob;
        float shDzCommon = -SHOULDER_UP * sin(pitch) + SHOULDER_FORE * (1F - cos(pitch));
        for (int i = 0; i < 2; i++) {
            float side = i == 0 ? 1F : -1F;
            float n = i == 0 ? nFL : nFR, air = i == 0 ? aFL : aFR;
            int arm = i == 0 ? LEFT_ARM : RIGHT_ARM;
            float dy = shDyCommon + side * SHOULDER_X * sin(roll);
            float dz = shDzCommon - side * SHOULDER_X * sin(twist);
            float angle = asin((ARM_SWEEP * n - dz) / ARM_LENGTH);
            float dx = SHOULDER_UP_OF_CHEST * sin(roll - hipRoll) + SHOULDER_UP * sin(hipRoll)
                - SHOULDER_FORE_OF_CHEST * sin(twist - hipTwist) - SHOULDER_FORE * sin(hipTwist) + ARM_LENGTH * sin(angle) * sin(twist);
            float tilt = asin(dx / ARM_LENGTH);
            float a = w * arms;
            pose.rx[arm] += a * (angle - pitch);
            pose.py[arm] += a * (rootDrop(ARM_LENGTH, ARM_HALF, angle) - dy - ARM_LIFT * air - ARM_SIDE * Math.abs(sin(tilt)));
            pose.rz[arm] += a * (tilt - roll - side * ARM_ARC * air);
        }

        float nod = HEAD_NOD * sin(phase * 2F + 0.6F);
        pose.rx[HEAD] += w * (-HEAD_STEADY * pitch + HEAD_LOW + nod);
        pose.rz[HEAD] += w * (-HEAD_STEADY * roll);
        pose.ry[HEAD] += w * (-HEAD_STEADY * twist);
    }

    static void gallop(Pose pose, float w, float phase, float arms) {
        // In fractions of a bound from the feet landing (phase = 2 pi t - pi/2): the feet push until 0.3, the hands grab at 0.2 and
        // let go at 0.55, the feet land again at 1. Hips highest and body tipped furthest onto the hands as the push ends (0.3);
        // the body highest just after it leaves the ground (0.45); the chest leaning out as the hands reach (0.1).
        float rock = GALLOP_ROCK * sin(phase + 0.4F * PI);
        float reach = GALLOP_CHEST * sin(phase + 0.8F * PI);
        float rise = GALLOP_BOUND * (0.5F + 0.5F * sin(phase + 0.1F * PI));
        pose.rx[BODY] += w * rock;
        pose.py[BODY] -= w * rise;
        pose.rx[CHEST] += w * reach;
        pose.rx[HEAD] -= w * (rock + reach) * HEAD_STEADY;

        // How far the body's rock, the chest's reach and the rise moved each shoulder and hip down: a planted hand or foot takes
        // it back, so that it stays on the ground while the body flies over it.
        float shoulderDy = SHOULDER_FORE * sin(rock) + SHOULDER_UP * (1F - cos(rock))
            + SHOULDER_FORE_OF_CHEST * sin(reach) + SHOULDER_UP_OF_CHEST * (1F - cos(reach)) - rise;
        float hipDy = -(HIP_DOWN * (1F - cos(rock)) + HIP_BACK * sin(rock)) - rise;
        float shoulderDz = -SHOULDER_UP * sin(rock) + SHOULDER_FORE * (1F - cos(rock))
            - SHOULDER_UP_OF_CHEST * sin(reach) + SHOULDER_FORE_OF_CHEST * (1F - cos(reach));
        float hipDz = HIP_DOWN * sin(rock) - HIP_BACK * (1F - cos(rock));
        float[] phases = {phase, phase - GALLOP_LEG_SPLIT, phase + GALLOP_ARMS_AFTER, phase + GALLOP_ARMS_AFTER - GALLOP_ARM_SPLIT};
        int[] parts = {LEFT_LEG, RIGHT_LEG, LEFT_ARM, RIGHT_ARM};
        for (int i = 0; i < 4; i++) {
            boolean arm = i >= 2;
            float down = arm ? GALLOP_DOWN : GALLOP_LEG_DOWN;
            float t = through(phases[i]);
            float back = 2F * footAt(t, down);
            float air = airAt(t, down);
            float a = arm ? w * arms : w;
            // On the ground: where the hand or foot must be, under a shoulder or hip the body's rock has moved. In the air: on
            // from where it left to where it'll land, by the same sum, plus the overreach.
            float length = arm ? ARM_LENGTH : LEG_LENGTH;
            float along = down * BOUND_PIXELS * 0.5F * back - (arm ? shoulderDz : hipDz);
            float s = t < down ? 0F : (t - down) / (1F - down);
            float over = s * sin(PI * s) / 0.58F; // peaks about two-thirds through the swing
            float world = asin(along / length) - (arm ? GALLOP_FORE_OVER : GALLOP_HIND_OVER) * over;
            float tilt = rock + (arm ? reach : 0F);
            // Leaving the ground, the hold that kept it down eases off over the first part of the swing, and the lift eases in
            // behind it: let go at once, a hand jumped up by both (the user: "they touch the ground, then sorta snap back up").
            float release = arm ? GALLOP_ARM_RELEASE : GALLOP_LEG_RELEASE;
            // ...and comes back over the end of the swing, so that the hand arrives lined up with the ground, not jumped onto it.
            float planted = t < down ? 1F : Math.max(1F - smooth(s / release), smooth((s - 1F + GALLOP_CATCH) / GALLOP_CATCH));
            float lift = air * smooth(s / release);
            float hold = arm ? rootDrop(ARM_LENGTH, ARM_HALF, world) - shoulderDy : rootDrop(LEG_LENGTH, LEG_HALF, world) - hipDy;
            pose.rx[parts[i]] += a * (world - tilt);
            pose.py[parts[i]] += a * (planted * hold - GALLOP_LIFT * lift);
        }
    }

    static void waddle(Pose pose, float w, float phase, float arms, float bodyPitch) {
        float tL = through(phase), tR = through(phase + PI);
        float nL = footAt(tL, WADDLE_DOWN), nR = footAt(tR, WADDLE_DOWN);
        float aL = airAt(tL, WADDLE_DOWN), aR = airAt(tR, WADDLE_DOWN);
        float roll = WADDLE_ROLL * (aR - aL);
        float twist = WADDLE_TWIST * (nR - nL);
        pose.rz[BODY] += w * roll;
        pose.ry[BODY] += w * twist;
        // Upright, the body's own "up" lies along its z: lift a foot along the world's up, seen from the body.
        float upY = -cos(bodyPitch), upZ = sin(bodyPitch);
        for (int i = 0; i < 2; i++) {
            float side = i == 0 ? 1F : -1F;
            float n = i == 0 ? nL : nR, air = i == 0 ? aL : aR;
            int leg = i == 0 ? LEFT_LEG : RIGHT_LEG;
            // The hips twist, bringing one forward: the foot's angle takes that back. Leaning over a leg lowers its hip: its root rises.
            float angle = asin((WADDLE_SWEEP * n + side * UPRIGHT_HIP_X * sin(twist)) / LEG_LENGTH);
            float rise = WADDLE_LIFT * air - rootDrop(LEG_LENGTH, LEG_HALF, angle) + side * UPRIGHT_HIP_X * sin(roll)
                + LEG_SIDE * sin(WADDLE_BOW);
            pose.rx[leg] += w * angle;
            pose.ry[leg] += w * (roll + side * WADDLE_BOW);
            pose.py[leg] += w * upY * rise;
            pose.pz[leg] += w * upZ * rise;
            int arm = i == 0 ? LEFT_ARM : RIGHT_ARM;
            float a = w * arms;
            pose.rx[arm] += a * (-WADDLE_ARMS_FORWARD + WADDLE_ARM_SWING * 2F * (i == 0 ? nR : nL));
            pose.ry[arm] += a * (side * WADDLE_ARMS_OUT - roll * 0.5F);
        }
        pose.rz[HEAD] -= w * roll * HEAD_STEADY;
        pose.ry[HEAD] -= w * twist * HEAD_STEADY;
    }

    /**
     * Turn Alex's straight blend into ours: add the difference between where its standing pose is and where ours should be,
     * plus the overlapping action. Returns how far up ours is, past 1 or under 0 for a moment at the ends.
     */
    static float standTransition(Pose pose, float p, boolean rising) {
        if (p <= 0F || p >= 1F)
            return p;
        float q = rising ? Math.clamp(p / STAND_SHARE, 0F, 1F) : Math.clamp((p - (1F - STAND_SHARE)) / STAND_SHARE, 0F, 1F);
        float e = easeStand(q);
        float d = e - p;
        pose.rx[BODY] += d * ALEX_STAND_BODY;
        pose.py[BODY] += d * 1F;
        for (int leg : new int[] {LEFT_LEG, RIGHT_LEG}) {
            pose.rx[leg] += d * ALEX_STAND_LIMB;
            pose.px[leg] += d * (leg == LEFT_LEG ? 1F : -1F);
            pose.py[leg] += d * -3F;
            pose.pz[leg] += d * 1.2F;
        }
        for (int arm : new int[] {LEFT_ARM, RIGHT_ARM}) {
            pose.rx[arm] += d * ALEX_STAND_LIMB;
            pose.px[arm] += d * (arm == LEFT_ARM ? 2F : -2F);
            pose.py[arm] += d * 1F;
        }
        pose.rx[HEAD] += d * ALEX_STAND_LIMB;
        pose.py[HEAD] += d * 4F;
        pose.pz[HEAD] += d * -2F;

        float through = 4F * q * (1F - q); // 0 at either end, 1 midway
        pose.rx[CHEST] += STAND_SPINE * through;
        if (rising) {
            pose.rx[HEAD] -= STAND_HEAD_LEAD * bump(q, 0F, 0.7F);
            float shove = STAND_SHOVE * bump(q, 0.05F, 0.5F);
            pose.rx[LEFT_ARM] += shove;
            pose.rx[RIGHT_ARM] += shove;
        } else {
            pose.rx[HEAD] -= STAND_HEAD_LEAD * 0.5F * through;
            float reach = STAND_CATCH * bump(q, 0.05F, 0.6F);
            pose.rx[LEFT_ARM] -= reach;
            pose.rx[RIGHT_ARM] -= reach;
        }
        return e;
    }

    /** 0 outside {@code from} to {@code to}, rising and falling smoothly to 1 between. */
    static float bump(float x, float from, float to) {
        if (x <= from || x >= to)
            return 0F;
        return sin(PI * (x - from) / (to - from));
    }

    /** Slow in, slow out, sinking back a touch first and carrying past a touch at the end. */
    static float easeStand(float x) {
        return smooth(x) - STAND_WAVE * sin(TWO_PI * x);
    }

    static void breathe(Pose pose, float age, int id, float move, float fours) {
        float breath = sin(age * TWO_PI / BREATH + id) * (1F - BREATH_MOVING * move);
        pose.chestSwell = 1F + BREATH_SWELL * breath;
        float rise = BREATH_RISE * breath * fours;
        // A breath lifts the body, not its limbs: they hang from it and would take the hands and feet off the ground.
        pose.py[BODY] -= rise;
        pose.py[LEFT_LEG] += rise;
        pose.py[RIGHT_LEG] += rise;
        pose.py[LEFT_ARM] += rise;
        pose.py[RIGHT_ARM] += rise;
        pose.rx[HEAD] -= BREATH_HEAD * breath;
    }

    /**
     * How far a rigid limb's root must sink, tilted this far, to keep its lowest edge on the ground: its tip rises as it tilts and
     * one edge of its sole dips, so a hand rolls onto its knuckles as it plants and off them as it leaves.
     */
    static float rootDrop(float length, float half, float angle) {
        return length * (1F - cos(angle)) - half * Math.abs(sin(angle));
    }

    /** 0 to 1 through a stride, starting as the foot touches down. */
    static float through(float phase) {
        float t = (phase + PI / 2F) / TWO_PI;
        return t - (float) Math.floor(t);
    }

    /** Where a foot is along its sweep, -0.5 at the front to 0.5 at the back: steadily back while down, then forward through the air. */
    static float footAt(float through, float down) {
        return through < down ? through / down - 0.5F : 0.5F - smooth((through - down) / (1F - down));
    }

    /** How far through its arc a foot in the air is, 0 on the ground to 1 at the top. */
    static float airAt(float through, float down) {
        return through < down ? 0F : sin(PI * (through - down) / (1F - down));
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

    private static float asin(float x) {
        return (float) Math.asin(Math.clamp(x, -1F, 1F));
    }
}
