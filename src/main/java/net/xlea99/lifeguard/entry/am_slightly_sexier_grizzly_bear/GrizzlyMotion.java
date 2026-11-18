package net.xlea99.lifeguard.entry.am_slightly_sexier_grizzly_bear;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;

/**
 * The Grizzly Bear's motion on all fours ({@link AmSlightlySexierGrizzlyBear}), added after Alex's Mobs has posed the model.
 *
 * <p>Its own walk swings all four limbs by the mob's walk amount, about 0.41 at a walk and 1 at a run, so a walking bear takes quick,
 * small steps whose feet slide, with diagonal limbs together like a trot. A bear walks in four beats, each foot about a quarter stride
 * after the last, hind before fore on each side, with its weight rolling from side to side, and runs with front and back legs each
 * working as a pair, the body rocking between reaching and gathering. Every number here is one dial, in degrees or model pixels (16 to a block).
 *
 * <p>The walk starts from where each foot should be, not from an angle. A foot on the ground goes straight back at the speed the ground
 * passes, which vanilla's limb swing makes the same at any walking speed: it counts 4 per block. Then the leg's angle comes from where
 * the foot is, and its hip drops to keep the foot on the ground, so that feet stay planted while the body goes over them.
 */
public final class GrizzlyMotion {
    /** Radians of stride per unit of limb swing. One for both gaits, so that blending between them never jumps. */
    private static final float STRIDE = 0.75F;
    /** How much of a walking stride a foot is on the ground. */
    private static final float FOOT_DOWN = 0.65F;
    /** How far a planted foot goes back relative to the body, in pixels: exactly the distance the ground passes while it's down. */
    private static final float FOOT_SWEEP = FOOT_DOWN * Mth.TWO_PI / (STRIDE * 4F) * 16F;
    /** How far a hip or shoulder slides along with its foot, either way, in pixels. The rest of the sweep is the leg's angle. */
    private static final float WALK_REACH = 3F;
    /** From hip or shoulder to the ground, in pixels. */
    private static final float HIND_LENGTH = 11F;
    private static final float FORE_LENGTH = 15F;
    /** The walk amount where a walk's full stride is reached, under a normal walk. */
    private static final float FULL_WALK = 0.35F;
    /** The walk amounts between which a walk turns into a gallop. A wandering bear reads about 0.41, a charging or ridden one 1. */
    private static final float GALLOP_START = 0.55F;
    private static final float GALLOP_FULL = 0.85F;

    /** How much of a galloping stride a foot spends going back. */
    private static final float GALLOP_DOWN = 0.55F;

    private static final float GALLOP_HIND_SWING = 44F * Mth.DEG_TO_RAD;
    private static final float GALLOP_FORE_SWING = 42F * Mth.DEG_TO_RAD;
    /** How far a limb's hip or shoulder shifts along with its swing at a gallop, in pixels. */
    private static final float GALLOP_REACH = 1.2F;
    /** How high a paw lifts as it swings forward, in pixels. */
    private static final float WALK_LIFT = 2.2F;
    private static final float GALLOP_LIFT = 2.5F;
    /** How far a swinging paw arcs outward, so it doesn't go straight through its neighbor's space. */
    private static final float PAW_ARC = 3F * Mth.DEG_TO_RAD;

    /** At a walk, how far behind its hind leg each foreleg moves, in radians of stride. About a quarter stride: this is what gives the walk its four beats. */
    private static final float PACE_LAG = 1.45F;
    /** At a walk, how far off exactly opposite the two sides are, so that the beats don't fall perfectly even. */
    private static final float SIDES_UNEVEN = 0.15F;
    /** At a gallop, how far each pair's second limb trails its first. */
    private static final float GALLOP_SPLIT = 0.4F;

    private static final float WALK_ROLL = 5.5F * Mth.DEG_TO_RAD;
    /** Lines the body's roll, sway and bob up with the stride: all the way over one side when that side's two feet are mid-plant. */
    private static final float WEIGHT_TIMING = Mth.PI * (1F - FOOT_DOWN) - PACE_LAG / 2F;
    /** The shoulder hump is a heavy thing riding on the body: it swings the same way, this much further, and this long after, in radians of stride. */
    private static final float HUMP_ROLL = 3.5F * Mth.DEG_TO_RAD;
    private static final float HUMP_LAG = 0.8F;
    /** And it slides toward the low side and bounces, in pixels. */
    private static final float HUMP_SHIFT = 0.5F;
    private static final float HUMP_BOUNCE = 0.25F;
    private static final float WALK_SWAY = 2.2F * Mth.DEG_TO_RAD;
    private static final float WALK_BOB = 0.4F;
    private static final float WALK_HEAD_NOD = 1.2F * Mth.DEG_TO_RAD;
    /** A walking bear carries its head low. */
    private static final float WALK_HEAD_LOW = 4F * Mth.DEG_TO_RAD;

    private static final float GALLOP_ROCK = 6F * Mth.DEG_TO_RAD;
    private static final float GALLOP_BOUND = 1.6F;
    private static final float GALLOP_ROLL = 1F * Mth.DEG_TO_RAD;
    private static final float GALLOP_HUMP = 3F * Mth.DEG_TO_RAD;
    /** The head stays steadier than the body under it, taking back this much of the rock. */
    private static final float GALLOP_HEAD_STEADY = 0.75F;
    private static final float GALLOP_HEAD_LOW = 3F * Mth.DEG_TO_RAD;
    /** A charging bear's ears lie back. */
    private static final float GALLOP_EARS_BACK = 25F * Mth.DEG_TO_RAD;

    /** Ticks per breath. */
    private static final float BREATH = 64F;
    /** How much the shoulder hump swells with a breath, as a share of its size. */
    private static final float BREATH_SWELL = 0.01F;
    private static final float BREATH_RISE = 0.12F;
    private static final float BREATH_HEAD = 0.6F * Mth.DEG_TO_RAD;
    /** Breathing shows less on the move: this much of it goes at full stride. */
    private static final float BREATH_MOVING = 0.7F;

    private static final float IDLE_HEAD_YAW = 2.8F * Mth.DEG_TO_RAD;
    private static final float IDLE_HEAD_PITCH = 1.5F * Mth.DEG_TO_RAD;
    private static final float IDLE_LEAN = 0.6F * Mth.DEG_TO_RAD;

    /** Each ear gets a slot this many ticks long, and flicks at the start of about one slot in four. */
    private static final float EAR_SLOT = 70F;
    private static final float EAR_FLICK_TICKS = 7F;
    private static final float EAR_FLICK_ROLL = 20F * Mth.DEG_TO_RAD;
    private static final float EAR_FLICK_PITCH = 8F * Mth.DEG_TO_RAD;

    private GrizzlyMotion() {
    }

    /**
     * @param gait this bear's own, eased
     * @param amount the mob's walk amount, 0 to 1, or up to 1.5 for a moment when it's hit
     * @param fours how much the bear is on all fours, 0 sitting or standing to 1
     * @param id anything unique to this bear, so that no two move together
     */
    public static void apply(AdvancedModelBox body, AdvancedModelBox midbody, AdvancedModelBox head, AdvancedModelBox leftEar, AdvancedModelBox rightEar,
                             AdvancedModelBox leftLeg, AdvancedModelBox rightLeg, AdvancedModelBox leftArm, AdvancedModelBox rightArm,
                             GrizzlyGait gait, float limbSwing, float amount, float age, int id, float fours) {
        gait.update(Math.clamp(amount / FULL_WALK, 0F, 1F), smooth((amount - GALLOP_START) / (GALLOP_FULL - GALLOP_START)), age);
        float move = gait.stride() * fours;
        float gallop = gait.gallop() * fours;
        float walk = move - gallop;
        float stride = limbSwing * STRIDE;

        float weight = stride + WEIGHT_TIMING;
        float walkRoll = WALK_ROLL * walk * Mth.sin(weight);
        float walkRise = WALK_BOB * walk * Mth.abs(Mth.sin(weight));
        float blend = Mth.clamp(move > 0F ? gallop / move : 0F, 0F, 1F);
        limb(leftLeg, stride, HIND_LENGTH, GALLOP_HIND_SWING, 1F, walk, gallop, walkRoll, walkRise);
        limb(rightLeg, stride + Mth.lerp(blend, Mth.PI + SIDES_UNEVEN, GALLOP_SPLIT), HIND_LENGTH, GALLOP_HIND_SWING, -1F, walk, gallop, walkRoll, walkRise);
        limb(leftArm, stride + Mth.lerp(blend, -PACE_LAG, Mth.PI), FORE_LENGTH, GALLOP_FORE_SWING, 1F, walk, gallop, walkRoll, walkRise);
        limb(rightArm, stride + Mth.lerp(blend, Mth.PI + SIDES_UNEVEN - PACE_LAG, Mth.PI + GALLOP_SPLIT), FORE_LENGTH, GALLOP_FORE_SWING, -1F, walk, gallop, walkRoll, walkRise);

        float roll = walkRoll + GALLOP_ROLL * gallop * Mth.sin(stride);
        float sway = WALK_SWAY * walk * Mth.cos(weight);
        float rock = -GALLOP_ROCK * gallop * Mth.sin(stride - 0.4F);
        body.rotateAngleZ += roll;
        body.rotateAngleY += sway;
        body.rotateAngleX += rock;
        body.rotationPointY -= walkRise + GALLOP_BOUND * gallop * (0.5F + 0.5F * Mth.sin(stride + 0.3F));
        float hump = weight - HUMP_LAG;
        midbody.rotateAngleZ += HUMP_ROLL * walk * Mth.sin(hump);
        midbody.rotationPointX += HUMP_SHIFT * walk * Mth.sin(hump);
        midbody.rotationPointY += HUMP_BOUNCE * walk * Mth.abs(Mth.sin(hump));
        midbody.rotateAngleX += GALLOP_HUMP * gallop * Mth.sin(stride + 1.2F);
        head.rotateAngleY -= sway;
        head.rotateAngleZ -= roll * 0.6F;
        head.rotateAngleX += walk * (WALK_HEAD_LOW + WALK_HEAD_NOD * Mth.sin(stride * 2F + 0.5F)) + gallop * GALLOP_HEAD_LOW - rock * GALLOP_HEAD_STEADY;

        float breath = Mth.sin(age * Mth.TWO_PI / BREATH + id) * (1F - BREATH_MOVING * move);
        midbody.setScale(1F + BREATH_SWELL * breath, 1F + BREATH_SWELL * breath, 1F);
        body.rotationPointY -= BREATH_RISE * breath;
        // A breath moves the body, not its legs: they hang from it, and would take the feet into the ground.
        leftLeg.rotationPointY += BREATH_RISE * breath;
        rightLeg.rotationPointY += BREATH_RISE * breath;
        leftArm.rotationPointY += BREATH_RISE * breath;
        rightArm.rotationPointY += BREATH_RISE * breath;
        head.rotateAngleX -= BREATH_HEAD * breath;

        float idle = fours - move;
        head.rotateAngleY += IDLE_HEAD_YAW * idle * Mth.sin(age / 37F + id);
        head.rotateAngleX += IDLE_HEAD_PITCH * idle * Mth.sin(age / 53F + id * 1.7F);
        body.rotateAngleZ += IDLE_LEAN * idle * Mth.sin(age / 61F + id * 0.6F);

        ear(leftEar, age, id, 1, 1F, gallop);
        ear(rightEar, age, id, 2, -1F, gallop);
    }

    /**
     * @param side 1 for the bear's left, -1 for its right
     * @param walk how much of the limb's motion is the walk, and {@code gallop} how much is the gallop: together its stride, 0 to 1
     * @param walkRoll the body's roll at a walk, which a planted leg takes back so that its foot isn't dragged sideways
     * @param walkRise the body's bob at a walk, likewise
     */
    private static void limb(AdvancedModelBox box, float phase, float length, float gallopSwing, float side, float walk, float gallop, float walkRoll, float walkRise) {
        float through = Mth.frac((phase + Mth.HALF_PI) / Mth.TWO_PI);
        float foot = FOOT_SWEEP * footAt(through, FOOT_DOWN);
        float footAir = airAt(through, FOOT_DOWN);
        float slide = WALK_REACH * foot / (FOOT_SWEEP / 2F);
        float angle = (float) Math.atan2(foot - slide, length);
        float hipDrop = length * (1F - Mth.cos(angle));

        float back = 2F * footAt(through, GALLOP_DOWN);
        float gallopAir = airAt(through, GALLOP_DOWN);

        box.rotateAngleX += walk * angle + gallop * gallopSwing * back;
        box.rotationPointZ += walk * slide + gallop * GALLOP_REACH * back;
        box.rotationPointY += walk * (hipDrop - WALK_LIFT * footAir) + walkRise * (1F - footAir) - gallop * GALLOP_LIFT * gallopAir;
        box.rotateAngleZ -= walkRoll * (1F - footAir) + side * PAW_ARC * (walk * footAir + gallop * gallopAir);
    }

    /** Where a foot is along its sweep, -0.5 at the front to 0.5 at the back: steadily back while it's down, then forward through the air. */
    private static float footAt(float through, float down) {
        return through < down ? through / down - 0.5F : 0.5F - smooth((through - down) / (1F - down));
    }

    /** How far through its arc a foot in the air is, 0 on the ground to 1 at the top. */
    private static float airAt(float through, float down) {
        return through < down ? 0F : Mth.sin(Mth.PI * (through - down) / (1F - down));
    }

    /** Worked out from the time alone, so there's nothing to keep per bear. */
    private static void ear(AdvancedModelBox ear, float age, int id, int which, float side, float gallop) {
        float time = age + id * 7 + which * 31;
        int slot = Mth.floor(time / EAR_SLOT);
        float into = time - slot * EAR_SLOT;
        if (into < EAR_FLICK_TICKS && (Mth.murmurHash3Mixer(slot * 31 + id * 17 + which) & 3) == 0) {
            float flick = Mth.sin(Mth.PI * into / EAR_FLICK_TICKS);
            ear.rotateAngleZ += side * EAR_FLICK_ROLL * flick;
            ear.rotateAngleX -= EAR_FLICK_PITCH * flick;
        }
        ear.rotateAngleX -= GALLOP_EARS_BACK * gallop;
    }

    private static float smooth(float x) {
        float t = Math.clamp(x, 0F, 1F);
        return t * t * (3F - 2F * t);
    }
}
