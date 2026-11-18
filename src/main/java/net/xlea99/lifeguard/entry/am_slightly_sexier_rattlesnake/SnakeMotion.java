package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import java.util.Arrays;

/**
 * How a Rattlesnake moves ({@link AmSlightlySexierRattlesnake}), added after Alex's Mobs has posed its model. Pure arithmetic
 * on a {@link Pose}.
 *
 * <p>The body is five segments head to tail: second neck, first neck, body (the model's root), first tail, second tail. Each
 * gets a heading in the world, {@code amplitude * sin(phase - i * SPACING)}: an S, its crest one {@code SPACING} further along
 * each segment. The phase moves on with the ground it covers, one wavelength per wavelength travelled, so the S stands still
 * on the ground and the snake flows through it, every segment passing where the one ahead passed (a snake's slither); stopped,
 * the phase stops, and the snake rests in the S it stopped in. Resting, the phase creeps and the S breathes, slowly. A
 * segment's own turn is its heading less its parent's; the head takes back most of the second neck's, so it keeps looking
 * where it's going. All of it fades out as Alex coils it up (his coil and rattle are kept as they are). A heading is the turn
 * of the snake's long axis at that segment, about the vertical, the same way for neck and tail pieces.
 */
public final class SnakeMotion {
    public static final int BODY = 0, NECK1 = 1, NECK2 = 2, HEAD = 3, TAIL1 = 4, TAIL2 = 5, PARTS = 6;

    private static final float PI = (float) Math.PI;
    private static final float DEG = PI / 180F;

    /** How far round the wave each segment is from the one ahead of it: a little under one whole S down the snake. */
    static final float SPACING = 1.1F;
    /**
     * The wave's phase per unit of limb swing at full size (4 a block): one wavelength (2 pi / SPACING segments of about 6
     * pixels, some 2.1 blocks) per wavelength travelled, so the S stays put on the ground. A baby (drawn at 0.35) runs through
     * its shorter S faster.
     */
    static final float PHASE_PER_SWING = 0.75F;
    /** Headings either way, slithering, and resting (breathing between the two), and how fast it eases into its slither. */
    static final float SLITHER = 32F * DEG;
    static final float REST = 14F * DEG, REST_BREATH = 4F * DEG;
    static final float MOVE_EASE = 0.12F;
    /** Limb swing a tick (4 a block) at which the slither is full. */
    static final float FULL_SLITHER = 0.25F;
    /** Resting: how fast the S creeps along the body, radians a tick, and the breath's slow pace. */
    static final float REST_CREEP = 0.004F, BREATH_PACE = 0.011F;
    /** How much of the second neck's heading the head takes back, so it looks ahead rather than swinging with the S. */
    static final float HEAD_STEADY = 0.85F;
    /** The neck held a little up, more on the move; the head tipped back down to level. */
    static final float NECK_UP_REST = 5F * DEG, NECK_UP_MOVING = 9F * DEG;
    /** Resting, the head looks about slowly. */
    static final float HEAD_LOOK = 6F * DEG;

    public static final class Pose {
        public final float[] rx = new float[PARTS], ry = new float[PARTS];

        public final float[] rz = new float[PARTS], pz = new float[PARTS];

        void clear() {
            Arrays.fill(rx, 0F);
            Arrays.fill(ry, 0F);
            Arrays.fill(rz, 0F);
            Arrays.fill(pz, 0F);
        }
    }

    // Alex's coil, all of it, from ModelRattlesnake (degrees; the body's slide in pixels), and the tail tip's two coiled poses.
    static final float COIL_BODY_SLIDE = 3F, COIL_BODY_YAW = -90F * DEG;
    static final float COIL_TAIL1_PITCH = -10F * DEG, COIL_TAIL1_YAW = -70F * DEG;
    static final float COIL_NECK_PITCH = -20F * DEG, COIL_NECK_YAW = 60F * DEG;
    static final float COIL_HEAD_PITCH = 20F * DEG, COIL_HEAD_YAW = -30F * DEG, COIL_HEAD_ROLL = 10F * DEG;
    static final float TIP_REST_PITCH = 10F * DEG, TIP_REST_YAW = -90F * DEG, TIP_RATTLE_PITCH = 70F * DEG, TIP_RATTLE_YAW = -60F * DEG;
    /** How fast the tail lifts into the rattle and settles out of it, per tick (Alex switches it in one frame). */
    static final float RATTLE_EASE = 0.25F;
    /**
     * Coiled with nothing about, it rests: neck and head laid down on the coil, chin down, still (Alex's coil holds the head up
     * the whole time: "the core of an olympian", the user, 2026-11-08). Something it notices lifts the head out of the rest to
     * watch (Alex's head turn back on); a threat close up gets the rattle, risen from there. The rest is the head lying level
     * at about the coil's top: Alex's coil lifts each neck piece 20 degrees and tips the head 20 down, and these take most of
     * that back (24 and 24 sank the chin into the ground).
     */
    static final float REST_NECK1 = 18F * DEG, REST_NECK2 = 14F * DEG, REST_HEAD = -12F * DEG;
    static final float ALERT_EASE = 0.15F;
    /**
     * Coiled, its body stays put and the head turns up to {@code COIL_HEAD_REACH} degrees off it (vanilla's 75 made the body
     * turn after the head: it spun on the spot). Alex turns the second neck and head half of the way between them; the rest of
     * the turn goes into the necks, first piece most, so watching something behind it it unwinds its neck round.
     */
    public static final int COIL_HEAD_REACH = 160;
    static final float UNWIND_NECK1 = 0.45F, UNWIND_NECK2 = 0.3F, UNWIND_BODY = 0.25F;
    /**
     * The head's turn: eased (this much of the way a tick) and capped at this many degrees a tick, so a look swung from one
     * side to the other (once what it watches passes behind it, past {@code COIL_HEAD_REACH}) sweeps across in about two thirds
     * of a second rather than jumping.
     */
    static final float LOOK_EASE = 0.4F, LOOK_PACE = 24F;
    /**
     * Resting, the neck and head keep curling the way Alex's coil bends them, round onto the tail: about 75 degrees more (the
     * user, 2026-11-08: "the head needs to turn like a good 70 or 80 degrees to be resting on its tail").
     */
    static final float REST_CURL_NECK2 = 30F * DEG, REST_CURL_HEAD = 45F * DEG;
    /** Coiled and resting, the head stirs now and then, a little. */
    static final float COIL_LOOK = 3F * DEG;
    /** Asleep: how fast it settles into it, and its breath (how far the first neck piece lifts, radians a tick: about 6 s). */
    static final float SLEEP_EASE = 0.05F, SLEEP_BREATH = 1.5F * DEG, SLEEP_BREATH_PACE = 0.05F;

    /**
     * Alex blends his coil in at an even pace (in over 10 ticks, out over 5), starting and stopping dead: this eases it, by
     * adding the difference between his blend and an eased one for every part of it. And the tail tip, which Alex switches
     * from its coiled rest to its raised rattle in a frame (and the buzz on at full), lifts and settles over a few ticks, the
     * buzz swelling with it. Returns the eased coil.
     */
    static float coil(Pose pose, SnakeBody body, float ticks, float age, float c, boolean rattling) {
        body.rattle += ((rattling ? 1F : 0F) - body.rattle) * Math.min(1F, RATTLE_EASE * ticks);
        float r = body.rattle;
        float e = smooth(c);
        float d = e - c;
        pose.pz[BODY] += d * COIL_BODY_SLIDE;
        pose.ry[BODY] += d * COIL_BODY_YAW;
        pose.rx[TAIL1] += d * COIL_TAIL1_PITCH;
        pose.ry[TAIL1] += d * COIL_TAIL1_YAW;
        pose.rx[NECK1] += d * COIL_NECK_PITCH;
        pose.ry[NECK1] += d * COIL_NECK_YAW;
        pose.rx[NECK2] += d * COIL_NECK_PITCH;
        pose.ry[NECK2] += d * COIL_NECK_YAW;
        pose.rx[HEAD] += d * COIL_HEAD_PITCH;
        pose.ry[HEAD] += d * COIL_HEAD_YAW;
        pose.rz[HEAD] += d * COIL_HEAD_ROLL;
        // The tip: what Alex put on (one pose or the other, his buzz while rattling), and what it should be.
        float buzzPitch = cos(age * 18F + 1F) * 0.1F + 0.2F, buzzYaw = cos(age * 18F) * 0.1F;
        float alexPitch = rattling ? TIP_RATTLE_PITCH * c + buzzPitch : TIP_REST_PITCH * c;
        float alexYaw = rattling ? TIP_RATTLE_YAW * c + buzzYaw : TIP_REST_YAW * c;
        float pitch = e * (TIP_REST_PITCH + (TIP_RATTLE_PITCH - TIP_REST_PITCH) * r) + r * buzzPitch;
        float yaw = e * (TIP_REST_YAW + (TIP_RATTLE_YAW - TIP_REST_YAW) * r) + r * buzzYaw;
        pose.rx[TAIL2] += pitch - alexPitch;
        pose.ry[TAIL2] += yaw - alexYaw;
        return e;
    }

    static float smooth(float x) {
        float t = Math.clamp(x, 0F, 1F);
        return t * t * (3F - 2F * t);
    }

    private static float cos(float x) {
        return (float) Math.cos(x);
    }

    private SnakeMotion() {
    }

    /**
     * @param swingPerTick its real ground speed as limb swing, 4 a block
     * @param curl how far Alex has it coiled, 0 to 1 (his even blend)
     * @param rattling whether it's rattling now
     * @param scale 1, or 0.35 for a baby
     */
    public static void apply(Pose pose, SnakeBody body, float age, int id, float swingPerTick, float curl, boolean rattling, float scale,
                             float netHeadYaw, float headPitch) {
        pose.clear();
        float ticks = body.tick(age);
        if (Float.isNaN(body.phase))
            body.phase = id * 2.399F; // each snake rests in its own S
        float moving = Math.clamp(swingPerTick / FULL_SLITHER, 0F, 1F);
        body.move += (moving - body.move) * Math.min(1F, MOVE_EASE * ticks);
        body.phase += ticks * (Math.clamp(swingPerTick, 0F, 3F) * PHASE_PER_SWING / Math.max(scale, 0.1F) + REST_CREEP * (1F - body.move));

        float coiled = coil(pose, body, ticks, age, Math.clamp(curl, 0F, 1F), rattling);
        body.alert += (body.head - body.alert) * Math.min(1F, ALERT_EASE * ticks);
        float resting = coiled * (1F - Math.max(body.alert, body.rattle));
        float watching = coiled - resting;
        pose.rx[NECK1] += resting * REST_NECK1;
        pose.rx[NECK2] += resting * REST_NECK2;
        pose.rx[HEAD] += resting * REST_HEAD;
        pose.ry[NECK2] += resting * REST_CURL_NECK2;
        pose.ry[HEAD] += resting * REST_CURL_HEAD;
        // The look it turns to, at a capped pace: when what it watches passes behind it and the server swings its look to the
        // other side in one tick, the head sweeps back across the front to it instead of jumping.
        float lookTarget = Math.clamp(netHeadYaw, -COIL_HEAD_REACH, COIL_HEAD_REACH);
        body.look += Math.clamp((lookTarget - body.look) * Math.min(1F, LOOK_EASE * ticks), -LOOK_PACE * ticks, LOOK_PACE * ticks);
        // Alex turns the second neck and the head toward what it looks at, a quarter of the way each. Coiled, that's ours
        // instead: none resting, the paced look watching.
        float alexYaw = netHeadYaw * DEG / 4F, alexPitch = headPitch * DEG / 4F, ourYaw = body.look * DEG / 4F;
        pose.ry[NECK2] += watching * ourYaw - coiled * alexYaw;
        pose.ry[HEAD] += watching * ourYaw - coiled * alexYaw;
        pose.rx[NECK2] -= resting * alexPitch;
        pose.rx[HEAD] -= resting * alexPitch;
        // Asleep, the head stops stirring and the neck lying on the coil rises and falls with a slow breath.
        body.sleep += ((body.asleep ? 1F : 0F) - body.sleep) * Math.min(1F, SLEEP_EASE * ticks);
        pose.ry[HEAD] += resting * (1F - body.sleep) * COIL_LOOK * sin(age * 0.031F + id * 2.1F);
        pose.rx[NECK1] += resting * body.sleep * SLEEP_BREATH * sin(age * SLEEP_BREATH_PACE + id);
        // Watching, coiled: the other half of the turn goes into the necks and a little into the body piece.
        float unwind = watching * body.look * DEG / 2F;
        pose.ry[NECK1] += unwind * UNWIND_NECK1;
        pose.ry[NECK2] += unwind * UNWIND_NECK2;
        pose.ry[BODY] += unwind * UNWIND_BODY;
        float w = 1F - coiled;
        if (w <= 0F)
            return;
        float rest = REST + REST_BREATH * sin(age * BREATH_PACE + id);
        float amplitude = w * (rest + (SLITHER - rest) * body.move);
        float[] heading = new float[5];
        for (int i = 0; i < 5; i++)
            heading[i] = amplitude * sin(body.phase - i * SPACING);
        // Segments head to tail: 0 second neck, 1 first neck, 2 body, 3 first tail, 4 second tail.
        pose.ry[BODY] += heading[2];
        pose.ry[NECK1] += heading[1] - heading[2];
        pose.ry[NECK2] += heading[0] - heading[1];
        pose.ry[TAIL1] += heading[3] - heading[2];
        pose.ry[TAIL2] += heading[4] - heading[3];
        float idle = 1F - body.move;
        pose.ry[HEAD] += -HEAD_STEADY * heading[0] + w * idle * HEAD_LOOK * sin(age * 0.023F + id * 1.3F);

        float neckUp = w * (NECK_UP_REST + (NECK_UP_MOVING - NECK_UP_REST) * body.move);
        pose.rx[NECK1] -= neckUp;
        pose.rx[HEAD] += neckUp * 0.8F;
    }

    private static float sin(float x) {
        return (float) Math.sin(x);
    }
}
