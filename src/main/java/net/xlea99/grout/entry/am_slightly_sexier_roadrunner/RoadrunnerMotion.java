package net.xlea99.grout.entry.am_slightly_sexier_roadrunner;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;

/**
 * The Roadrunner's motion ({@link AmSlightlySexierRoadrunner}), added after Alex's Mobs has posed the model.
 *
 * <p>Its speed is so high (about 0.6 blocks a tick at a wander) that its walk amount sits at the maximum whenever it's moving: it hardly
 * walks at all, it sprints and it stands. So the sprint is the point: body sunk low and flat, neck stretched out, tail level behind it, and
 * both of those carried with some weight, so that they swing past when it stops dead. It leans into its turns, its tail swinging out.
 *
 * <p>Each leg is placed by where its foot should be, with the two bones worked out from that, the foot lying flat on the ground as in
 * Alex's Mobs' own. At a walk, the short moments it has one, its head holds still while its body catches up, then darts forward, as birds'
 * do. At rest its head moves the same way, in quick snaps with stillness between. Every number here is one dial, in degrees or model
 * pixels (16 to a block).
 */
public final class RoadrunnerMotion {
    /** The walk amount where its full stride is reached. */
    private static final float FULL_STRIDE = 0.3F;
    /** The walk amounts between which a walk turns into a sprint. */
    private static final float SPRINT_START = 0.5F;
    private static final float SPRINT_FULL = 0.85F;

    /** Radians of stride per unit of limb swing. A walk's limb swing counts distance, 4 per block; a sprint's only time, 1 per tick, so this is a turnover. */
    private static final float WALK_STRIDE = 2.4F;
    private static final float SPRINT_STRIDE = 1.5F;
    /** How much of a stride a foot is on the ground. */
    private static final float WALK_FOOT_DOWN = 0.6F;
    private static final float SPRINT_FOOT_DOWN = 0.42F;
    /** How far a foot goes along the ground, relative to the body, in pixels. */
    private static final float WALK_SWEEP = 6.2F;
    private static final float SPRINT_SWEEP = 8F;
    /** Where the middle of that is, from the hip, in pixels, forward being negative. */
    private static final float WALK_FOOT_CENTER = -0.4F;
    private static final float SPRINT_FOOT_CENTER = -0.5F;
    /** How high a foot lifts as it swings forward, in pixels. */
    private static final float WALK_LIFT = 1.2F;
    private static final float SPRINT_LIFT = 2F;
    /** How far a foot in the air tips, toes down. */
    private static final float TOES_DOWN = 40F * Mth.DEG_TO_RAD;
    /** From the model: hip to ground, and its two leg bones, in pixels. */
    private static final float HIP_HEIGHT = 5F;
    private static final float THIGH = 2F;
    private static final float SHIN = 4F;

    /** How far the body sinks at a sprint, in pixels. */
    private static final float SPRINT_CROUCH = 1F;
    private static final float WALK_BOB = 0.25F;
    private static final float SPRINT_BOB = 0.5F;
    /** A sprint's forward tilt, and how much it rocks with each step. */
    private static final float SPRINT_TILT = 2F * Mth.DEG_TO_RAD;
    private static final float SPRINT_ROCK = 1F * Mth.DEG_TO_RAD;
    private static final float WALK_ROLL = 2.5F * Mth.DEG_TO_RAD;
    private static final float SPRINT_ROLL = 3F * Mth.DEG_TO_RAD;
    private static final float WALK_SWAY = 1.5F * Mth.DEG_TO_RAD;
    private static final float SPRINT_SWAY = 3F * Mth.DEG_TO_RAD;

    /** How far the neck stretches forward at a sprint. The beak takes most of it back, so that it still looks where it's going. */
    private static final float SPRINT_NECK = 36F * Mth.DEG_TO_RAD;
    private static final float BEAK_LEVEL = 0.8F;
    /** How far the tail comes down at a sprint, near level. */
    private static final float SPRINT_TAIL = 30F * Mth.DEG_TO_RAD;
    private static final float SPRINT_TAIL_FLUTTER = 2F * Mth.DEG_TO_RAD;
    /** How much of Alex's Mobs' slow idle tail wag goes at a sprint. */
    private static final float SPRINT_NO_WAG = 0.8F;
    /** At a walk, how far the neck goes back while the head holds still, and how much of a step that takes before it darts forward. */
    private static final float WALK_HEAD_HOLD = 6F * Mth.DEG_TO_RAD;
    private static final float WALK_HEAD_HOLD_SHARE = 0.7F;
    /** How far its wings hold out at a sprint, and how much they flutter. */
    private static final float SPRINT_WINGS_OUT = 20F * Mth.DEG_TO_RAD;
    private static final float SPRINT_WINGS_UP = 25F * Mth.DEG_TO_RAD;
    private static final float SPRINT_WING_FLUTTER = 5F * Mth.DEG_TO_RAD;

    /** In a full turn: how far the body leans in, the tail swings out, and the neck leads. */
    private static final float TURN_LEAN = 12F * Mth.DEG_TO_RAD;
    private static final float TURN_TAIL = 20F * Mth.DEG_TO_RAD;
    private static final float TURN_NECK = 10F * Mth.DEG_TO_RAD;

    /** Ticks per breath: birds breathe fast. */
    private static final float BREATH = 34F;
    private static final float BREATH_RISE = 0.055F;
    private static final float BREATH_NECK = 0.5F * Mth.DEG_TO_RAD;

    /** At rest, how long its head holds a look, and how fast it snaps to the next, in ticks. */
    private static final float LOOK_HOLD = 26F;
    private static final float LOOK_SNAP = 2F;
    private static final float LOOK_YAW = 16F * Mth.DEG_TO_RAD;
    private static final float LOOK_PITCH = 5F * Mth.DEG_TO_RAD;
    /** Now and then at rest, its tail goes up quickly and sinks slowly. One slot in two. */
    private static final float TAIL_COCK_SLOT = 150F;
    private static final float TAIL_COCK_UP = 4F;
    private static final float TAIL_COCK_DOWN = 45F;
    private static final float TAIL_COCK = 22F * Mth.DEG_TO_RAD;

    private RoadrunnerMotion() {
    }

    /**
     * @param amount the mob's walk amount, 0 to 1, or up to 1.5 for a moment when it's hit
     * @param turning degrees the body turned this tick, clockwise from above
     * @param id anything unique to this bird, so that no two move together
     */
    public static void apply(AdvancedModelBox body, AdvancedModelBox neck, AdvancedModelBox beak, AdvancedModelBox tail, AdvancedModelBox leftWing, AdvancedModelBox rightWing,
                             AdvancedModelBox leftLeg, AdvancedModelBox leftKnee, AdvancedModelBox leftFoot, AdvancedModelBox rightLeg, AdvancedModelBox rightKnee, AdvancedModelBox rightFoot,
                             RoadrunnerGait gait, float limbSwing, float amount, float turning, float age, int id) {
        float sprintTarget = smooth((amount - SPRINT_START) / (SPRINT_FULL - SPRINT_START));
        gait.update(Math.clamp(amount / FULL_STRIDE, 0F, 1F), sprintTarget, Mth.lerp(sprintTarget, WALK_STRIDE, SPRINT_STRIDE), limbSwing, turning, age);
        float move = gait.stride();
        float sprint = gait.sprint();
        float walk = move - sprint;
        float blend = move > 0F ? Math.clamp(sprint / move, 0F, 1F) : 0F;
        float idle = 1F - move;
        float phase = gait.phase();
        float carried = gait.carried();
        float turn = gait.turn();

        float footDown = Mth.lerp(blend, WALK_FOOT_DOWN, SPRINT_FOOT_DOWN);
        float weight = phase + Mth.PI * (1F - footDown);
        float twice = phase * 2F;

        float roll = (WALK_ROLL * walk + SPRINT_ROLL * sprint) * Mth.sin(weight) - TURN_LEAN * turn * move;
        float sway = (WALK_SWAY * walk + SPRINT_SWAY * sprint) * Mth.cos(weight);
        float pitch = sprint * (SPRINT_TILT + SPRINT_ROCK * Mth.sin(twice));
        float rise = WALK_BOB * walk * Mth.abs(Mth.sin(weight)) + SPRINT_BOB * sprint * (0.5F + 0.5F * Mth.cos(twice));
        float crouch = SPRINT_CROUCH * sprint;
        float breath = Mth.sin(age * Mth.TWO_PI / BREATH + id) * idle;
        body.rotateAngleZ += roll;
        body.rotateAngleY += sway;
        body.rotateAngleX += pitch;
        body.rotationPointY += crouch - rise - BREATH_RISE * breath;

        float hipHeight = HIP_HEIGHT - crouch + rise;
        leg(leftLeg, leftKnee, leftFoot, body, phase, footDown, blend, move, hipHeight, roll);
        leg(rightLeg, rightKnee, rightFoot, body, phase + Mth.PI, footDown, blend, move, hipHeight, roll);
        // A breath moves the body, not its legs: they hang from it, and would take the feet into the ground.
        leftLeg.rotationPointY += BREATH_RISE * breath;
        rightLeg.rotationPointY += BREATH_RISE * breath;

        float stretched = SPRINT_NECK * carried;
        float headHold = walk * WALK_HEAD_HOLD * headHold(twice);
        neck.rotateAngleX += stretched + headHold - pitch - BREATH_NECK * breath;
        neck.rotateAngleY += TURN_NECK * turn * move - sway;
        neck.rotateAngleZ -= roll * 0.7F;
        beak.rotateAngleX -= stretched * BEAK_LEVEL + headHold;

        tail.rotateAngleY *= 1F - SPRINT_NO_WAG * sprint;
        tail.rotateAngleY += TURN_TAIL * turn * move - sway;
        tail.rotateAngleX += -SPRINT_TAIL * carried + SPRINT_TAIL_FLUTTER * sprint * Mth.sin(twice + 1F) - pitch;

        float flutter = SPRINT_WING_FLUTTER * sprint * Mth.sin(twice);
        leftWing.rotateAngleY += SPRINT_WINGS_OUT * sprint;
        rightWing.rotateAngleY -= SPRINT_WINGS_OUT * sprint;
        leftWing.rotateAngleZ -= SPRINT_WINGS_UP * sprint + flutter;
        rightWing.rotateAngleZ += SPRINT_WINGS_UP * sprint + flutter;

        look(neck, age, id, idle);
        tailCock(tail, age, id, idle);
    }

    /** @param roll the body's, which a planted leg takes back so that its foot isn't dragged sideways */
    private static void leg(AdvancedModelBox thigh, AdvancedModelBox shin, AdvancedModelBox foot, AdvancedModelBox body,
                            float phase, float footDown, float blend, float move, float hipHeight, float roll) {
        float through = Mth.frac((phase + Mth.HALF_PI) / Mth.TWO_PI);
        float air = through < footDown ? 0F : Mth.sin(Mth.PI * (through - footDown) / (1F - footDown));
        float along = through < footDown ? through / footDown - 0.5F : 0.5F - smooth((through - footDown) / (1F - footDown));

        float ahead = Mth.lerp(blend, WALK_FOOT_CENTER, SPRINT_FOOT_CENTER) + Mth.lerp(blend, WALK_SWEEP, SPRINT_SWEEP) * along;
        float down = hipHeight - Mth.lerp(blend, WALK_LIFT, SPRINT_LIFT) * air;
        float reach = Math.clamp((float) Math.sqrt(ahead * ahead + down * down), SHIN - THIGH + 0.05F, SHIN + THIGH - 0.05F);
        float toFoot = (float) Math.atan2(ahead, down);
        float atHip = (float) Math.acos(Math.clamp((THIGH * THIGH + reach * reach - SHIN * SHIN) / (2F * THIGH * reach), -1F, 1F));
        float atFoot = (float) Math.acos(Math.clamp((SHIN * SHIN + reach * reach - THIGH * THIGH) / (2F * SHIN * reach), -1F, 1F));
        float thighAngle = toFoot + atHip;
        float shinAngle = toFoot - atFoot;

        thigh.rotateAngleX = Mth.lerp(move, thigh.rotateAngleX, thighAngle - body.rotateAngleX);
        shin.rotateAngleX = Mth.lerp(move, shin.rotateAngleX, shinAngle - thighAngle);
        thigh.rotateAngleZ -= roll * (1F - air);
        foot.rotateAngleX = -(thigh.rotateAngleX + shin.rotateAngleX + body.rotateAngleX) - Mth.HALF_PI + TOES_DOWN * air * move;
    }

    /** Over one step: 1 down to -1 as the body catches up to a head holding still, then quickly back as it darts forward. */
    private static float headHold(float twice) {
        float through = Mth.frac(twice / Mth.TWO_PI);
        return through < WALK_HEAD_HOLD_SHARE
            ? 1F - 2F * through / WALK_HEAD_HOLD_SHARE
            : 2F * smooth((through - WALK_HEAD_HOLD_SHARE) / (1F - WALK_HEAD_HOLD_SHARE)) - 1F;
    }

    /** A look at rest. Worked out from the time alone, so there's nothing to keep per bird. */
    private static void look(AdvancedModelBox neck, float age, int id, float idle) {
        float time = age + id * 11;
        int slot = Mth.floor(time / LOOK_HOLD);
        float snap = smooth((time - slot * LOOK_HOLD) / LOOK_SNAP);
        neck.rotateAngleY += idle * LOOK_YAW * Mth.lerp(snap, spread(slot - 1, id, 0), spread(slot, id, 0));
        neck.rotateAngleX += idle * LOOK_PITCH * Mth.lerp(snap, spread(slot - 1, id, 1), spread(slot, id, 1));
    }

    /** -1 to 1, the same every time for the same slot. */
    private static float spread(int slot, int id, int which) {
        return (Mth.murmurHash3Mixer(slot * 31 + id * 17 + which * 7) & 255) / 127.5F - 1F;
    }

    private static void tailCock(AdvancedModelBox tail, float age, int id, float idle) {
        float time = age + id * 23;
        int slot = Mth.floor(time / TAIL_COCK_SLOT);
        if ((Mth.murmurHash3Mixer(slot * 13 + id) & 1) != 0)
            return;
        float into = time - slot * TAIL_COCK_SLOT;
        float cock = into < TAIL_COCK_UP ? smooth(into / TAIL_COCK_UP) : 1F - smooth((into - TAIL_COCK_UP) / TAIL_COCK_DOWN);
        tail.rotateAngleX += idle * TAIL_COCK * cock;
    }

    private static float smooth(float x) {
        float t = Math.clamp(x, 0F, 1F);
        return t * t * (3F - 2F * t);
    }
}
