package net.xlea99.grout.entry.am_slightly_sexier_crocodile;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

/**
 * The Crocodile's motion ({@link AmSlightlySexierCrocodile}), added after Alex's Mobs has posed the model.
 *
 * <p>Its own walk swings four pillar legs back and forth in a dog's trot under a nearly rigid body, with flat feet tipping along with them. A
 * crocodile walks with its spine. Here the body, neck and tail bend in a wave along its length, each part a beat after the one before, with the
 * head holding steady at the front of it.
 *
 * <p>The legs sprawl. Each foot's place on the ground is decided first, and the leg's splay is solved to reach it, so a foot stays on the ground
 * through its stride and swings out in a wide arc as it comes forward, the foot itself lying flat. How low it sprawls is one dial: wandering it's
 * nearly on its belly; hunting it rises onto straighter legs, with its tail and head up. In water its legs stay tucked, as Alex's Mobs has them,
 * and a long slow wave drives it. Every number here is one dial, in degrees or model pixels.
 *
 * <p>Running, nothing could keep a foot planted, so it stops trying: a dorky scuttle, its legs flung as far forward and back as they'll go at a much
 * slower tempo, with its whole spine thrashing. Basking its mouth is shut, where Alex's Mobs always has it open: that's only a now-and-then thing, in
 * the sun. And floating still in deep water it can lurk with only its head out, tipped back with its body hanging under the surface.
 */
public final class CrocodileMotion {
    /** The walk amount where its full stride is reached. It wanders at about 0.22 with {@code am_crocodile_ambush}. */
    private static final float FULL_STRIDE = 0.25F;
    /** Above this many blocks a tick it's running, whatever else is true. It walks at under 0.06 and a charge goes 0.4. */
    private static final double RUNNING = 0.22D;
    /** Radians of stride per unit of limb swing: wandering, hunting, swimming. Limb swing counts 4 per block on land. */
    private static final float WANDER_STRIDE = 1.82F;
    private static final float HUNT_STRIDE = 1.2F;
    private static final float SWIM_STRIDE = 0.55F;
    /**
     * Hauling prey on land. Alex's Mobs turns its body around to drag it backward, so its legs run backward too, or its feet would slide the
     * wrong way, and slowly, like the heavy work it is.
     */
    private static final float DRAG_STRIDE = -0.55F;
    /** Running. Limb swing tops out at 1 a tick, so this is about 2.4 strides a second. */
    private static final float RUN_STRIDE = 0.75F;
    /** How far each leg gets flung forward and back: stupid, dorky far. */
    private static final float RUN_REACH = 55F * Mth.DEG_TO_RAD;
    private static final float RUN_FOOT_DOWN = 0.45F;
    private static final float RUN_LIFT = 3F;
    /** How much more its spine and tail thrash than at a hunting walk, and how far it heaves up with each step, in pixels. */
    private static final float RUN_WAVE = 2.2F;
    private static final float RUN_BOB = 1.1F;
    /** A baby's limb swing goes three times as fast: this much of that stays. */
    private static final float BABY_STRIDE = 0.5F;

    /** How much of a stride a foot is on the ground. */
    private static final float WANDER_FOOT_DOWN = 0.65F;
    private static final float HUNT_FOOT_DOWN = 0.55F;
    /** How far a planted foot goes back relative to the body, and how far the shoulder or hip slides along with it, either way, in pixels. */
    private static final float FOOT_SWEEP = 10F;
    private static final float SHOULDER_SLIDE = 1.2F;
    /** How high a foot lifts as it comes forward, in pixels. */
    private static final float WANDER_LIFT = 1.5F;
    private static final float HUNT_LIFT = 2F;
    /** How far a lifted foot's toes droop. */
    private static final float TOES_DOWN = 20F * Mth.DEG_TO_RAD;
    /** How far behind its hind leg each foreleg moves, and how far off exactly opposite the two sides are. */
    private static final float LEG_LAG = 1.45F;
    private static final float SIDES_UNEVEN = 0.1F;
    /** From the model: shoulder and hip to the ground, in pixels. */
    private static final float ARM_LENGTH = 8F;
    private static final float LEG_LENGTH = 6F;
    /** How far its body sinks, wandering and hunting. Every leg's splay comes from this. */
    private static final float WANDER_SINK = 2.4F;
    private static final float HUNT_SINK = 0.15F;
    /** The most a leg can bend toward straight or out from under it, as a cosine or a sine. */
    private static final float MOST_REACH = 0.9F;
    private static final float MOST_STRAIGHT = 0.995F;

    /** How far each part of the spine swings in the wave at a full wander, in order from the front. */
    private static final float BODY_WAVE = 7F * Mth.DEG_TO_RAD;
    private static final float NECK_WAVE = 6F * Mth.DEG_TO_RAD;
    private static final float HEAD_WAVE = 3F * Mth.DEG_TO_RAD;
    private static final float TAIL1_WAVE = 9F * Mth.DEG_TO_RAD;
    private static final float TAIL2_WAVE = 14F * Mth.DEG_TO_RAD;
    private static final float TAIL3_WAVE = 18F * Mth.DEG_TO_RAD;
    /** How much of that wave stays when it's up on its hunting walk, stiffer and faster. */
    private static final float HUNT_WAVE = 0.6F;
    /** How long each part takes to answer, in radians of stride. */
    private static final float NECK_LAG = 0.5F;
    private static final float HEAD_LAG = 0.9F;
    private static final float TAIL1_LAG = 0.7F;
    private static final float TAIL2_LAG = 1.4F;
    private static final float TAIL3_LAG = 2.1F;
    /** How far it leans over the side that's planted, and how far it rises with each step, in pixels. */
    private static final float ROLL = 2.5F * Mth.DEG_TO_RAD;
    private static final float BOB = 0.3F;
    /** Hunting: how far its neck comes up and its head levels, and how far its tail lifts off the ground. */
    private static final float HUNT_NECK_UP = 8F * Mth.DEG_TO_RAD;
    private static final float HUNT_HEAD_LEVEL = 0.6F;
    private static final float HUNT_TAIL_UP = 7F * Mth.DEG_TO_RAD;
    /** Wandering: how far its tail end sags to the ground. */
    private static final float WANDER_TAIL_SAG = 3F * Mth.DEG_TO_RAD;

    /** In water, the same wave, longer. */
    private static final float SWIM_BODY_WAVE = 6F * Mth.DEG_TO_RAD;
    private static final float SWIM_NECK_WAVE = 5F * Mth.DEG_TO_RAD;
    private static final float SWIM_TAIL1_WAVE = 16F * Mth.DEG_TO_RAD;
    private static final float SWIM_TAIL2_WAVE = 24F * Mth.DEG_TO_RAD;
    private static final float SWIM_TAIL3_WAVE = 30F * Mth.DEG_TO_RAD;
    /** How much its tucked legs trail with the wave. */
    private static final float SWIM_LEG_TRAIL = 5F * Mth.DEG_TO_RAD;

    /** Ticks per breath: slow. */
    private static final float BREATH = 80F;
    private static final float BREATH_RISE = 0.1F;
    /** At rest, how far its tail drifts. */
    private static final float IDLE_TAIL = 1.2F * Mth.DEG_TO_RAD;
    /** How far its mouth hangs open after a strike misses, and how far its head tips back to do it. {@link CrocodileGait} sets the timing. */
    private static final float MISSED_JAW = 38F * Mth.DEG_TO_RAD;
    private static final float MISSED_HEAD = 10F * Mth.DEG_TO_RAD;
    /** Basking with its mouth shut: its chin down on the ground. */
    private static final float RESTING_HEAD = 5F * Mth.DEG_TO_RAD;

    /**
     * Basking, flat out. Alex's Mobs leaves its belly a pixel off the ground, this much further puts it flush. Its legs splay out to these angles from
     * its 30 degrees, arms a bit forward and hind legs a bit back, and their shoulders and hips come down inside the body by this much so that their
     * feet still just reach the ground: worked out from the model, for these angles.
     */
    private static final float BASK_SINK = 1.15F;
    private static final float BASK_ALEX_SPLAY = 30F * Mth.DEG_TO_RAD;
    private static final float BASK_ARM_SPLAY = 65F * Mth.DEG_TO_RAD;
    private static final float BASK_ARM_FORWARD = -15F * Mth.DEG_TO_RAD;
    private static final float BASK_ARM_DOWN = 3.4F;
    private static final float BASK_LEG_SPLAY = 60F * Mth.DEG_TO_RAD;
    private static final float BASK_LEG_BACK = 20F * Mth.DEG_TO_RAD;
    private static final float BASK_LEG_DOWN = 3.25F;

    /** One in this many Crocodiles lies out straight when it basks. Alex's Mobs' curve, as it has it: how far each part turns. */
    private static final int STRAIGHT_ONE_IN = 3;
    private static final float CURVE_BODY = -7F * Mth.DEG_TO_RAD;
    private static final float CURVE_NECK = -10F * Mth.DEG_TO_RAD;
    private static final float CURVE_HEAD = -10F * Mth.DEG_TO_RAD;
    private static final float CURVE_TAIL1 = 30F * Mth.DEG_TO_RAD;
    private static final float CURVE_TAIL2 = 20F * Mth.DEG_TO_RAD;
    private static final float CURVE_TAIL3 = 30F * Mth.DEG_TO_RAD;

    /** Sun gapes: one chance in a stretch this long, in ticks, this likely out of 256, for this long at the least and how much longer at most. */
    private static final int SUN_GAPE_EVERY = 3600;
    private static final int SUN_GAPE_CHANCE = 100;
    private static final int SUN_GAPE_LEAST = 200;
    private static final int SUN_GAPE_MORE = 400;
    /** When the sun's up, in day time. */
    private static final long SUNSET = 12500L;

    /**
     * Lurking. Its body's top is higher than its head's, so its neck can't do this alone: the body tips back and the whole model drops, with its
     * head brought back level and its tail back up, to leave the surface crossing the top of its head and snout. Worked out from the model's
     * bones against {@code am_crocodile_ambush}'s float height, then lowered by eye. Its real place in the world doesn't change: only the model does.
     */
    private static final float LURK_BODY = 20F * Mth.DEG_TO_RAD;
    private static final float LURK_NECK = 15F * Mth.DEG_TO_RAD;
    private static final float LURK_DROP = 14F;
    private static final float LURK_TAIL1 = 20F * Mth.DEG_TO_RAD;
    private static final float LURK_TAIL2 = 5F * Mth.DEG_TO_RAD;
    /** How much of the water it needs to be sitting in, above its feet in blocks, to count as floating at the surface, and how still it must be. */
    private static final double LURK_WATER_LEAST = 0.3D;
    private static final double LURK_WATER_MOST = 0.8D;
    private static final float LURK_STRIDE = 0.1F;

    private CrocodileMotion() {
    }

    /** @return whether it's really moving fast: a hit jolts the walk amount, so that can't say. */
    public static boolean running(Mob mob) {
        return Math.hypot(mob.getX() - mob.xo, mob.getZ() - mob.zo) > RUNNING;
    }

    /** Goes by its UUID, so that one always lies the same way. */
    public static boolean liesStraight(Mob mob) {
        return ((mob.getUUID().hashCode() >>> 1) % STRAIGHT_ONE_IN) == 0;
    }

    private static boolean daytime(Level level) {
        return level.getDayTime() % 24000L < SUNSET;
    }

    /** Each Crocodile opens up in the sun now and then, on its own schedule, worked out from the time so that it needs no signal. */
    public static boolean sunGaping(Mob mob) {
        Level level = mob.level();
        if (!daytime(level) || level.isRaining())
            return false;
        int own = mob.getUUID().hashCode();
        long time = level.getGameTime() + (own & 0xFFFF);
        int roll = Mth.murmurHash3Mixer(own ^ (int) (time / SUN_GAPE_EVERY));
        if ((roll & 0xFF) >= SUN_GAPE_CHANCE)
            return false;
        int length = SUN_GAPE_LEAST + ((roll >>> 8) & 0xFF) * SUN_GAPE_MORE / 256;
        int start = ((roll >>> 16) & 0xFFF) * (SUN_GAPE_EVERY - length) / 4096;
        long into = time % SUN_GAPE_EVERY;
        return into >= start && into < start + length && level.canSeeSky(mob.blockPosition().above());
    }

    /** At night every grown one floating still in water deep enough for it, and by day about half of them, the same half each time. */
    public static boolean lurks(Mob mob, boolean hunting, boolean dragging, CrocodileGait gait) {
        if (hunting || dragging || mob.isBaby() || gait.stride() > LURK_STRIDE)
            return false;
        Level level = mob.level();
        if (daytime(level) && (mob.getUUID().hashCode() & 1) == 0)
            return false;
        double water = mob.getFluidHeight(FluidTags.WATER);
        BlockPos below = mob.blockPosition().below();
        return water >= LURK_WATER_LEAST && water <= LURK_WATER_MOST && level.getFluidState(below).is(FluidTags.WATER);
    }

    /**
     * @param amount the mob's walk amount, 0 to 1, or up to 1.5 for a moment when it's hit
     * @param hunting whether it's set on attacking something
     * @param dragging whether it has something in its jaws
     * @param basking how far into basking it is, 0 to 1
     * @param swimming how far into swimming it is, 0 to 1
     * @param id anything unique to this crocodile, so that no two breathe together
     */
    public static void apply(AdvancedModelBox body, AdvancedModelBox neck, AdvancedModelBox head, AdvancedModelBox jaw, AdvancedModelBox tail1, AdvancedModelBox tail2, AdvancedModelBox tail3,
                             AdvancedModelBox leftArm, AdvancedModelBox leftHand, AdvancedModelBox rightArm, AdvancedModelBox rightHand,
                             AdvancedModelBox leftLeg, AdvancedModelBox leftFoot, AdvancedModelBox rightLeg, AdvancedModelBox rightFoot,
                             CrocodileGait gait, boolean hunting, boolean running, boolean dragging, boolean baby, boolean sunGaping, boolean lurking, float unbend, float basking, float swimming, float limbSwing, float amount, float age, int id) {
        boolean huntsNow = hunting || running;
        float landStride = Mth.lerp(gait.run(), huntsNow ? HUNT_STRIDE : WANDER_STRIDE, RUN_STRIDE);
        float radians = (swimming > 0.5F ? SWIM_STRIDE : dragging ? DRAG_STRIDE : landStride) * (baby ? BABY_STRIDE : 1F);
        gait.update(Math.clamp(amount / FULL_STRIDE, 0F, 1F), huntsNow, running && swimming < 0.5F, sunGaping && basking > 0F, lurking && swimming > 0.5F, radians, limbSwing, age);
        float move = gait.stride();
        float hunt = gait.hunt();
        float run = gait.run();
        float phase = gait.phase();
        float land = (1F - basking) * (1F - swimming);
        float swim = swimming * move;
        float walk = land * move;
        float idle = 1F - move;

        float footDown = Mth.lerp(run, Mth.lerp(hunt, WANDER_FOOT_DOWN, HUNT_FOOT_DOWN), RUN_FOOT_DOWN);
        float weight = phase + Mth.PI * (1F - footDown) - LEG_LAG / 2F;
        float wave = walk * Mth.lerp(hunt, 1F, HUNT_WAVE) * Mth.lerp(run, 1F, RUN_WAVE);
        float roll = ROLL * wave * Mth.sin(weight);
        float rise = Mth.lerp(run, BOB, RUN_BOB) * walk * Mth.abs(Mth.sin(weight));
        float sink = land * Mth.lerp(hunt, WANDER_SINK, HUNT_SINK);
        float breath = Mth.sin(age * Mth.TWO_PI / BREATH + id) * (1F - 0.6F * move);

        body.rotationPointY += BASK_SINK * basking;
        sprawl(leftArm, leftHand, 1F, BASK_ARM_SPLAY, BASK_ARM_FORWARD, BASK_ARM_DOWN, basking);
        sprawl(rightArm, rightHand, -1F, BASK_ARM_SPLAY, BASK_ARM_FORWARD, BASK_ARM_DOWN, basking);
        sprawl(leftLeg, leftFoot, 1F, BASK_LEG_SPLAY, BASK_LEG_BACK, BASK_LEG_DOWN, basking);
        sprawl(rightLeg, rightFoot, -1F, BASK_LEG_SPLAY, BASK_LEG_BACK, BASK_LEG_DOWN, basking);

        float straighten = unbend * basking;
        body.rotateAngleY -= CURVE_BODY * straighten;
        neck.rotateAngleY -= CURVE_NECK * straighten;
        head.rotateAngleY -= CURVE_HEAD * straighten;
        tail1.rotateAngleY -= CURVE_TAIL1 * straighten;
        tail2.rotateAngleY -= CURVE_TAIL2 * straighten;
        tail3.rotateAngleY -= CURVE_TAIL3 * straighten;

        float shut = basking * (1F - gait.sunGape());
        head.rotateAngleX = Mth.lerp(shut, head.rotateAngleX, RESTING_HEAD);
        jaw.rotateAngleX = Mth.lerp(shut, jaw.rotateAngleX, 0F);

        float bodyYaw = BODY_WAVE * wave * Mth.sin(weight) + SWIM_BODY_WAVE * swim * Mth.sin(phase);
        body.rotateAngleY += bodyYaw;
        body.rotateAngleZ += roll;
        body.rotationPointY += sink - rise - BREATH_RISE * breath;

        neck.rotateAngleY += -NECK_WAVE * wave * Mth.sin(weight - NECK_LAG) - SWIM_NECK_WAVE * swim * Mth.sin(phase - NECK_LAG);
        neck.rotateAngleX -= HUNT_NECK_UP * land * hunt;
        neck.rotateAngleZ -= roll * 0.6F;
        head.rotateAngleY -= HEAD_WAVE * wave * Mth.sin(weight - HEAD_LAG);
        head.rotateAngleX += HUNT_NECK_UP * HUNT_HEAD_LEVEL * land * hunt;
        float gape = gait.gape(age);
        head.rotateAngleX -= MISSED_HEAD * gape;
        jaw.rotateAngleX += MISSED_JAW * gape;

        tail1.rotateAngleY += TAIL1_WAVE * wave * Mth.sin(weight - TAIL1_LAG) + SWIM_TAIL1_WAVE * swim * Mth.sin(phase - TAIL1_LAG) + IDLE_TAIL * idle * Mth.sin(age / 43F + id);
        tail2.rotateAngleY += TAIL2_WAVE * wave * Mth.sin(weight - TAIL2_LAG) + SWIM_TAIL2_WAVE * swim * Mth.sin(phase - TAIL2_LAG) + IDLE_TAIL * idle * Mth.sin(age / 43F + id - 0.8F);
        tail3.rotateAngleY += TAIL3_WAVE * wave * Mth.sin(weight - TAIL3_LAG) + SWIM_TAIL3_WAVE * swim * Mth.sin(phase - TAIL3_LAG) + IDLE_TAIL * idle * Mth.sin(age / 43F + id - 1.6F);
        tail1.rotateAngleX += HUNT_TAIL_UP * land * hunt;
        tail2.rotateAngleX -= WANDER_TAIL_SAG * land * (1F - hunt);

        float lurk = gait.lurk() * swimming;
        body.rotateAngleX -= LURK_BODY * lurk;
        body.rotationPointY += LURK_DROP * lurk;
        neck.rotateAngleX -= LURK_NECK * lurk;
        head.rotateAngleX += (LURK_BODY + LURK_NECK) * lurk;
        tail1.rotateAngleX += LURK_TAIL1 * lurk;
        tail2.rotateAngleX += LURK_TAIL2 * lurk;

        limb(leftLeg, leftFoot, phase, LEG_LENGTH, 1F, sink, footDown, hunt, run, move, land, roll, rise, breath);
        limb(rightLeg, rightFoot, phase + Mth.PI + SIDES_UNEVEN, LEG_LENGTH, -1F, sink, footDown, hunt, run, move, land, roll, rise, breath);
        limb(leftArm, leftHand, phase - LEG_LAG, ARM_LENGTH, 1F, sink, footDown, hunt, run, move, land, roll, rise, breath);
        limb(rightArm, rightHand, phase + Mth.PI + SIDES_UNEVEN - LEG_LAG, ARM_LENGTH, -1F, sink, footDown, hunt, run, move, land, roll, rise, breath);

        float trail = SWIM_LEG_TRAIL * swim * Mth.sin(phase);
        leftArm.rotateAngleX += trail;
        rightArm.rotateAngleX -= trail;
        leftLeg.rotateAngleX -= trail;
        rightLeg.rotateAngleX += trail;
    }

    /**
     * Takes a limb from Alex's Mobs' basking pose out to its flat-out one. With its limb both splayed and swung, undoing each angle on the foot doesn't
     * lay it flat: its toes tip into the ground. The foot's three angles here are the exact inverse of the limb's rotation, in the order parts apply them.
     */
    private static void sprawl(AdvancedModelBox limb, AdvancedModelBox foot, float side, float splay, float swing, float down, float basking) {
        float turned = splay * side;
        limb.rotateAngleZ -= (splay - BASK_ALEX_SPLAY) * side * basking;
        limb.rotateAngleX += swing * basking;
        limb.rotationPointY += down * basking;
        float sinX = Mth.sin(-swing);
        float cosX = Mth.cos(-swing);
        float sinZ = Mth.sin(turned);
        float cosZ = Mth.cos(turned);
        foot.rotateAngleX += basking * (float) Math.atan2(sinX * cosZ, cosX);
        foot.rotateAngleY -= basking * (float) Math.asin(sinX * sinZ);
        foot.rotateAngleZ += basking * ((float) Math.atan2(cosX * sinZ, cosZ) - BASK_ALEX_SPLAY * side);
    }

    /** @param side 1 for the crocodile's left, -1 for its right */
    private static void limb(AdvancedModelBox limb, AdvancedModelBox foot, float phase, float length, float side, float sink, float footDown, float hunt, float run, float move, float land,
                             float roll, float rise, float breath) {
        float through = Mth.frac((phase + Mth.HALF_PI) / Mth.TWO_PI);
        float air = through < footDown ? 0F : Mth.sin(Mth.PI * (through - footDown) / (1F - footDown));
        float along = through < footDown ? through / footDown - 0.5F : 0.5F - smooth((through - footDown) / (1F - footDown));

        float slide = SHOULDER_SLIDE * 2F * along * move;
        float placed = (float) Math.asin(Math.clamp((FOOT_SWEEP * along * move - slide) / length, -MOST_REACH, MOST_REACH));
        float forward = Mth.lerp(run, placed, 2F * along * RUN_REACH * move);
        float reach = length * Mth.cos(forward);
        float height = length - sink - Mth.lerp(run, Mth.lerp(hunt, WANDER_LIFT, HUNT_LIFT), RUN_LIFT) * air * move;
        float splay = (float) Math.acos(Math.clamp(height / reach, 0F, MOST_STRAIGHT));
        float shortBy = (1F - run) * Math.max(0F, height - reach * Mth.cos(splay));

        limb.rotateAngleX += land * forward;
        limb.rotateAngleZ -= land * side * splay + roll * (1F - air);
        limb.rotationPointZ += land * slide;
        limb.rotationPointY += land * shortBy + rise * (1F - air) + BREATH_RISE * breath;
        foot.rotateAngleX += land * (TOES_DOWN * air * move - forward);
        foot.rotateAngleZ += land * side * splay;
    }

    private static float smooth(float x) {
        float t = Math.clamp(x, 0F, 1F);
        return t * t * (3F - 2F * t);
    }
}
