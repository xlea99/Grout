package net.xlea99.grout.entry.am_slightly_sexier_gazelle;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;

/**
 * The Gazelle's motion ({@link AmSlightlySexierGazelle}), added after Alex's Mobs has posed the model.
 *
 * <p>Its own walk swings its legs by the walk amount in a trot, too little for the ground passing under them, so its feet slide: it skates.
 * Here the walk starts from where each foot should be, as on the Grizzly Bear: four beats, a foot on the ground going straight back at the
 * speed of the ground. But where a bear is mass, this is light: quick short steps, hooves barely down and picked up high. The body answers
 * each step in small motions, each a beat after what moved it: the forequarters dip as a foreleg lands, then the neck follows, the head, the
 * ears, and last the tail. That lag is what keeps it from looking like legs under a body on rails.
 *
 * <p>Its own run bobs the body on a sine wave, always rising or falling, so it rocks. A leap isn't that: it's a short, hard contact with the
 * ground and a long arc through the air, with hang time at the top. So a bound here is a sequence, through one cycle: forelegs land with the
 * nose down; hind legs land, ahead of where the forelegs were, and drive it up with the nose high; then it flies on a true parabola, forelegs
 * reaching, hind legs trailing until late. Its first bounds when frightened are the highest, and its tail flags as it goes.
 *
 * <p>Every number here is one dial, in degrees or model pixels. The renderer draws it at 0.8, so there are 20 pixels to a block.
 */
public final class GazelleMotion {
    /** The walk amount where its full stride is reached. It wanders at about 0.4 with {@code am_gazelle_movement}, and about 0.7 without. */
    private static final float FULL_STRIDE = 0.3F;
    /** Radians of stride per unit of limb swing. A walk's limb swing counts distance, 4 per block; a run's only time, 1 per tick, so this sets how long a bound takes. */
    private static final float WALK_STRIDE = 1.35F;
    private static final float RUN_STRIDE = 0.55F;
    /** A baby's limb swing goes three times as fast: this much of that stays. */
    private static final float BABY_STRIDE = 0.5F;

    /** How much of a walking stride a foot is on the ground. */
    private static final float FOOT_DOWN = 0.52F;
    /** How far a planted foot goes back relative to the body, in pixels: the distance the ground passes while it's down. */
    private static final float FOOT_SWEEP = FOOT_DOWN * Mth.TWO_PI / (WALK_STRIDE * 4F) * 20F;
    private static final float WALK_REACH = 1F;
    private static final float WALK_LIFT = 3F;
    /** At a walk, how far behind its hind leg each foreleg moves, and how far off exactly opposite the two sides are. */
    private static final float WALK_LAG = 1.45F;
    private static final float SIDES_UNEVEN = 0.15F;
    /** From hip or shoulder to the ground, in pixels. */
    private static final float HIND_LENGTH = 11F;
    private static final float FORE_LENGTH = 10F;

    private static final float WALK_ROLL = 1.6F * Mth.DEG_TO_RAD;
    private static final float WALK_SWAY = 1F * Mth.DEG_TO_RAD;
    private static final float WALK_BOB = 0.35F;
    /** How far the forequarters dip as each foreleg lands. */
    private static final float WALK_PITCH = 1.5F * Mth.DEG_TO_RAD;
    /** How far the body's weight slides over the side that's planted, in pixels. */
    private static final float WALK_SHIFT = 0.3F;
    private static final float WALK_NECK_NOD = 2.5F * Mth.DEG_TO_RAD;
    private static final float WALK_NECK_SWAY = 1.5F * Mth.DEG_TO_RAD;
    /** The head takes back this much of the neck's nod, a beat later, so that it rides steadier. */
    private static final float WALK_HEAD_NOD = 0.6F;
    /** How far its ears and tail bob with each step. */
    private static final float WALK_EAR_BOB = 4F * Mth.DEG_TO_RAD;
    private static final float WALK_TAIL_BOB = 4F * Mth.DEG_TO_RAD;
    /** How long each part takes to answer, in radians of stride. */
    private static final float NECK_LAG = 0.6F;
    private static final float HEAD_LAG = 1.1F;
    private static final float EAR_LAG = 1.5F;
    private static final float TAIL_LAG = 0.9F;
    /** It carries its neck up as it walks, the head taking back most of that to stay level. */
    private static final float WALK_NECK_UP = 5F * Mth.DEG_TO_RAD;
    private static final float WALK_TAIL_SWAY = 9F * Mth.DEG_TO_RAD;
    private static final float WEIGHT_TIMING = Mth.PI * (1F - FOOT_DOWN) - WALK_LAG / 2F;
    /** Where in a stride a foreleg lands. */
    private static final float FORE_LANDING = WALK_LAG - Mth.HALF_PI;

    /** Through one bound, 0 to 1: when forelegs land and for how long, and the same for hind legs. After them it's in the air, until 1. */
    private static final float FORE_LANDS = 0F;
    private static final float FORE_DOWN = 0.22F;
    private static final float HIND_LANDS = 0.14F;
    private static final float HIND_DOWN = 0.24F;
    private static final float TAKES_OFF = HIND_LANDS + HIND_DOWN;
    /** How far through a bound the right legs trail the left, so that the pairs don't move as one. */
    private static final float SPLIT = 0.03F;
    /** How far its legs swing either way from straight down. */
    private static final float FORE_SWING = 44F * Mth.DEG_TO_RAD;
    private static final float HIND_SWING = 49F * Mth.DEG_TO_RAD;
    /** In the air, how much of the flight its forelegs take to reach forward, right after takeoff, and its hind legs to come forward, right before landing. */
    private static final float FORE_REACHES_IN = 0.45F;
    private static final float HIND_GATHERS_IN = 0.45F;
    private static final float RUN_REACH = 1.5F;
    /** How high it flies, in pixels, and how much higher the first bounds go. */
    private static final float BOUND_HEIGHT = 8F;
    private static final float ALARM_HEIGHT = 0.6F;
    /** How far its body sinks into each landing, in pixels. */
    private static final float LANDING_DIP = 0.8F;
    /** How far its legs draw up through the middle of the flight, in pixels. */
    private static final float FLIGHT_TUCK = 2F;
    /** The body's pitch through a bound, nose down being positive: at each of {@link #PITCH_AT}. */
    private static final float[] PITCH_AT = {0F, 0.14F, 0.28F, 0.5F, 0.7F, 0.9F, 1F};
    private static final float[] PITCH = {9F, 3F, -11F, -5F, 0F, 6F, 9F};
    /** The neck reaches forward at a run, and takes back this much of the body's pitch, so that the head rides steadier than the body. */
    private static final float RUN_NECK = 15F * Mth.DEG_TO_RAD;
    private static final float NECK_STEADY = 0.8F;
    /** And the head takes back this much of the neck's reach, to still look where it's going. */
    private static final float HEAD_LEVEL = 0.6F;
    private static final float RUN_ROLL = 2F * Mth.DEG_TO_RAD;
    /** How far up its tail flags in the air, and how far its ears lie back at a run. */
    private static final float TAIL_FLAG = 55F * Mth.DEG_TO_RAD;
    private static final float EARS_BACK = 25F * Mth.DEG_TO_RAD;

    /** Ticks per breath. */
    private static final float BREATH = 50F;
    private static final float BREATH_RISE = 0.07F;
    private static final float BREATH_NECK = 0.5F * Mth.DEG_TO_RAD;

    private GazelleMotion() {
    }

    /**
     * @param amount the mob's walk amount, 0 to 1, or up to 1.5 for a moment when it's hit
     * @param id anything unique to this gazelle, so that no two breathe together
     */
    public static void apply(AdvancedModelBox body, AdvancedModelBox neck, AdvancedModelBox head, AdvancedModelBox earL, AdvancedModelBox earR, AdvancedModelBox tail,
                             AdvancedModelBox foreL, AdvancedModelBox foreR, AdvancedModelBox hindL, AdvancedModelBox hindR,
                             GazelleGait gait, boolean running, boolean baby, float limbSwing, float amount, float age, int id) {
        float radians = (running ? RUN_STRIDE : WALK_STRIDE) * (baby ? BABY_STRIDE : 1F);
        gait.update(Math.clamp(amount / FULL_STRIDE, 0F, 1F), running, radians, limbSwing, age);
        float move = gait.stride();
        float bound = move * gait.run();
        float walk = move - bound;
        float phase = gait.phase();

        float weight = phase + WEIGHT_TIMING;
        float walkRoll = WALK_ROLL * walk * Mth.sin(weight);
        float walkRise = WALK_BOB * walk * Mth.abs(Mth.sin(weight));

        float through = Mth.frac(phase / Mth.TWO_PI);
        boolean flying = through >= TAKES_OFF;
        float flight = flying ? (through - TAKES_OFF) / (1F - TAKES_OFF) : 0F;
        float height = BOUND_HEIGHT * (1F + ALARM_HEIGHT * gait.alarm()) * 4F * flight * (1F - flight);
        float dip = flying ? 0F : LANDING_DIP * Mth.sin(Mth.PI * through / TAKES_OFF);
        float runPitch = bound * pitchAt(through) * Mth.DEG_TO_RAD;
        float step = (phase - FORE_LANDING) * 2F;
        float pitch = runPitch + WALK_PITCH * walk * Mth.cos(step);
        float shift = WALK_SHIFT * walk * Mth.sin(weight);
        float tuck = FLIGHT_TUCK * Mth.sin(Mth.PI * flight);
        float runRoll = RUN_ROLL * bound * Mth.sin(phase);

        float breath = Mth.sin(age * Mth.TWO_PI / BREATH + id) * (1F - move);

        body.rotateAngleZ += walkRoll + runRoll;
        body.rotateAngleY += WALK_SWAY * walk * Mth.cos(weight);
        body.rotateAngleX += pitch;
        body.rotationPointY += bound * (dip - height) - walkRise - BREATH_RISE * breath;
        body.rotationPointX += shift;
        foreL.rotationPointX -= shift;
        foreR.rotationPointX -= shift;
        hindL.rotationPointX -= shift;
        hindR.rotationPointX -= shift;

        limb(hindL, phase, HIND_LENGTH, walk, walkRoll, walkRise, through, HIND_LANDS, HIND_DOWN, HIND_SWING, false, bound, pitch, tuck, dip, breath);
        limb(hindR, phase + Mth.PI + SIDES_UNEVEN, HIND_LENGTH, walk, walkRoll, walkRise, through, HIND_LANDS + SPLIT, HIND_DOWN, HIND_SWING, false, bound, pitch, tuck, dip, breath);
        limb(foreL, phase - WALK_LAG, FORE_LENGTH, walk, walkRoll, walkRise, through, FORE_LANDS, FORE_DOWN, FORE_SWING, true, bound, pitch, tuck, dip, breath);
        limb(foreR, phase + Mth.PI + SIDES_UNEVEN - WALK_LAG, FORE_LENGTH, walk, walkRoll, walkRise, through, FORE_LANDS + SPLIT, FORE_DOWN, FORE_SWING, true, bound, pitch, tuck, dip, breath);

        float reach = RUN_NECK * bound - WALK_NECK_UP * walk;
        neck.rotateAngleX += reach - runPitch * NECK_STEADY + WALK_NECK_NOD * walk * Mth.cos(step - NECK_LAG) - BREATH_NECK * breath;
        head.rotateAngleX -= reach * HEAD_LEVEL + WALK_NECK_NOD * WALK_HEAD_NOD * walk * Mth.cos(step - HEAD_LAG);
        neck.rotateAngleZ -= walkRoll * 0.6F;
        neck.rotateAngleY += WALK_NECK_SWAY * walk * Mth.sin(weight - NECK_LAG);
        head.rotateAngleY -= WALK_NECK_SWAY * 0.5F * walk * Mth.sin(weight - HEAD_LAG);

        tail.rotateAngleX += TAIL_FLAG * bound * Mth.sin(Mth.PI * flight);
        tail.rotateAngleZ += WALK_TAIL_SWAY * walk * Mth.sin(weight - TAIL_LAG);
        tail.rotateAngleX += WALK_TAIL_BOB * walk * Mth.cos(step - TAIL_LAG);
        float earBob = WALK_EAR_BOB * walk * Mth.cos(step - EAR_LAG);
        earL.rotateAngleX += earBob - EARS_BACK * bound;
        earR.rotateAngleX += earBob - EARS_BACK * bound;
    }

    /**
     * @param phase this leg's place in a walking stride
     * @param through how far through a bound the whole animal is, and {@code lands} and {@code down} when this leg's on the ground in that
     * @param fore a foreleg, which reaches forward early in the flight; a hind leg trails until late
     */
    private static void limb(AdvancedModelBox box, float phase, float length, float walk, float walkRoll, float walkRise,
                             float through, float lands, float down, float swing, boolean fore, float bound, float pitch, float tuck, float dip, float breath) {
        float inStride = Mth.frac((phase + Mth.HALF_PI) / Mth.TWO_PI);
        float foot = FOOT_SWEEP * (inStride < FOOT_DOWN ? inStride / FOOT_DOWN - 0.5F : 0.5F - smooth((inStride - FOOT_DOWN) / (1F - FOOT_DOWN)));
        float footAir = inStride < FOOT_DOWN ? 0F : Mth.sin(Mth.PI * (inStride - FOOT_DOWN) / (1F - FOOT_DOWN));
        float slide = WALK_REACH * foot / (FOOT_SWEEP / 2F);
        float walkAngle = (float) Math.atan2(foot - slide, length);
        float walkDrop = length * (1F - Mth.cos(walkAngle));

        float inBound = Mth.frac(through - lands);
        float back;
        boolean planted = inBound < down;
        if (planted) {
            back = 2F * inBound / down - 1F;
        } else {
            float air = (inBound - down) / (1F - down);
            float forward = fore ? smooth(air / FORE_REACHES_IN) : smooth((air - (1F - HIND_GATHERS_IN)) / HIND_GATHERS_IN);
            back = 1F - 2F * forward;
        }
        float runAngle = swing * back;
        float runDrop = planted ? length * (1F - Mth.cos(runAngle)) - dip : -tuck;

        box.rotateAngleX += walk * walkAngle + bound * runAngle - pitch;
        box.rotationPointZ += walk * slide + bound * RUN_REACH * back;
        box.rotationPointY += walk * (walkDrop - WALK_LIFT * footAir) + walkRise * (1F - footAir) + bound * runDrop + BREATH_RISE * breath;
        box.rotateAngleZ -= walkRoll * (1F - footAir);
    }

    private static float pitchAt(float through) {
        for (int i = 1; i < PITCH_AT.length; i++) {
            if (through <= PITCH_AT[i])
                return Mth.lerp(smooth((through - PITCH_AT[i - 1]) / (PITCH_AT[i] - PITCH_AT[i - 1])), PITCH[i - 1], PITCH[i]);
        }
        return PITCH[PITCH.length - 1];
    }

    private static float smooth(float x) {
        float t = Math.clamp(x, 0F, 1F);
        return t * t * (3F - 2F * t);
    }
}
