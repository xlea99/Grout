package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * How worked up a Rattlesnake is, in place of fixed rings (Alex's warning at 5 blocks, his bite at 2, and our head lift at 8).
 * Server side, on the entity.
 *
 * <p>Every {@code EVERY} ticks it weighs everything it can sense within its hearing ({@link SnakeTemper}): each creature
 * presses on it by how close it is, how loud (still, walking, running; sneaking players hardly at all), whether it's
 * coming at it or passing by, and how big it is, plus a sharp jump for anything right on top of it however quiet. Prey
 * presses harder: it's interesting. The pressure sets a level it's heading for; it works itself up toward it quickly and
 * calms down slowly. Its nerve drifts a little over minutes (its mood). Getting hurt puts it at the top at once.
 *
 * <p>What it does follows from how worked up it is: past {@code GLANCE} its head starts to lift, by {@code WATCH} it's up
 * watching what presses on it most; past {@code RATTLE}, with a threat about (a player or a Roadrunner, Alex's two), it
 * rattles at it until it has calmed to {@code CALM}; at {@code STRIKE} it goes for a threat that's within its strike reach.
 * So a player walking past gets a look, one marching at it gets rattled at from several blocks, and one sneaking can get
 * close before it notices.
 *
 * <p>Asleep ({@link SnakeAmbushGoal} decides when), it has {@code SLEEP_NERVE} of its nerve and {@code SLEEP_HEARING} of its
 * hearing, so a walk wakes it at about 4 blocks and a sneak at about 2; whatever works it up to {@code WATCH} wakes it with a
 * start ({@code STARTLE} more), straight into a rattle at a threat. Its warmth is {@link SnakeWarmth}'s.
 *
 * <p>The client is told how far up its head is ({@code HEAD_EVENT} plus a step, or {@code ASLEEP}), when that changes and
 * now and then while it's up or asleep, as a vanilla entity event, which a client without Grout ignores.
 */
public final class SnakeMind {
    public static final byte HEAD_EVENT = 110;
    public static final int HEAD_STEPS = 8;
    static final int EVERY = 5;
    /** Loudness: a creature standing still, the speed (blocks a tick) that counts as a walk, the most, and sneaking's share. */
    static final double PRESENCE = 0.25, LOUD_SPEED = 0.2, LOUD_MOST = 1.6, SNEAKING = 0.3;
    /** How it fades with distance: (1 - distance / hearing) to this power. */
    static final double NEAR_FALLOFF = 1.5;
    /** Coming straight at it presses this much more (going straight away this much less); slower than this isn't moving. */
    static final double APPROACH = 0.6, MOVING = 0.02;
    /** Anything within this many blocks presses on it however quiet, up to this much right on top of it. */
    static final double TOO_CLOSE = 2.5, TOO_CLOSE_WEIGHT = 1.5;
    /** Size: a player's (the square root of width times height), and the least and most it counts for. */
    static final double SIZE_PLAYER = 1.04, SIZE_LEAST = 0.3, SIZE_MOST = 1.6;
    static final double PREY_INTEREST = 2.0;
    static final double GAIN = 2.2;
    /** How much of the way to its level it goes each look, working up and calming down. */
    static final float RISE = 0.55F, FALL = 0.08F;
    static final float GLANCE = 0.12F, WATCH = 0.3F, RATTLE = 0.55F, CALM = 0.43F, STRIKE = 0.8F;
    /** Its mood: nerve drifts this far either way, over about seven minutes. */
    static final float MOOD = 0.2F;
    static final double MOOD_PACE = 0.0007;
    /** While its head is up, the client is told again this often, for anyone who came in range since. */
    static final int RETELL = 40;
    static final ResourceLocation ROADRUNNER = ResourceLocation.fromNamespaceAndPath("alexsmobs", "roadrunner");

    /** Cold, how much of its hearing it has at the least. */
    static final double SLUGGISH_HEARING = 0.6;
    /**
     * Asleep ({@link SnakeAmbushGoal} puts it to sleep): this much of its nerve and of its hearing, and woken, how much more
     * worked up it is for the start. The client is told {@code HEAD_EVENT + ASLEEP}.
     */
    static final float SLEEP_NERVE = 0.5F, STARTLE = 0.35F;
    static final double SLEEP_HEARING = 0.75;
    public static final int ASLEEP = HEAD_STEPS + 1;

    private SnakeTemper temper;
    private final SnakeWarmth warmth = new SnakeWarmth();
    float alertness;
    private LivingEntity focus, threat;
    private boolean rattle, asleep, fleeing, justWoke;
    private int calmFor, groggy;
    static final int GROGGY_LEAST = 5, GROGGY_MOST = 24;
    private BlockPos den;
    static final String DEN_KEY = "GroutDen";
    private Int2ObjectMap<Vec3> seen = new Int2ObjectOpenHashMap<>();
    private int told, toldAt, hurtStamp;

    public SnakeTemper temper(PathfinderMob snake) {
        if (this.temper == null)
            this.temper = SnakeTemper.of(snake.getUUID());
        return this.temper;
    }

    /** Called every tick on the server. */
    public void tick(PathfinderMob snake) {
        if (this.justWoke) {
            this.justWoke = false;
            this.groggy = snake.getRandom().nextInt(GROGGY_LEAST, GROGGY_MOST + 1);
        } else if (this.groggy > 0) {
            this.groggy--;
        }
        if ((snake.tickCount + snake.getId()) % EVERY != 0 || !snake.isAlive())
            return;
        think(snake);
        int head = this.asleep ? ASLEEP : headLevel();
        if (head != this.told || head > 0 && snake.tickCount - this.toldAt >= RETELL) {
            snake.level().broadcastEntityEvent(snake, (byte) (HEAD_EVENT + head));
            this.told = head;
            this.toldAt = snake.tickCount;
        }
    }

    private void think(PathfinderMob snake) {
        SnakeTemper t = temper(snake);
        this.warmth.update(snake);
        float warm = this.warmth.nerve(), nerve = warm;
        // Cold, it hears less: down to SLUGGISH_HEARING of its hearing at its coldest. Asleep, less again.
        double hearing = t.hearing() * (SLUGGISH_HEARING + (1 - SLUGGISH_HEARING) * Math.min(1F, warm));
        if (this.asleep) {
            nerve *= SLEEP_NERVE;
            hearing *= SLEEP_HEARING;
        }
        AABB around = snake.getBoundingBox().inflate(hearing, hearing / 2, hearing);
        Int2ObjectMap<Vec3> now = new Int2ObjectOpenHashMap<>();
        double total = 0, most = 0, mostThreat = 0;
        LivingEntity focus = null, threat = null;
        for (LivingEntity other : snake.level().getEntitiesOfClass(LivingEntity.class, around, e -> SnakeNotice.noticeable(snake, e))) {
            double d = snake.distanceTo(other);
            if (d > hearing)
                continue;
            Vec3 at = other.position();
            Vec3 was = this.seen.get(other.getId());
            now.put(other.getId(), at);
            double p = pressure(snake, other, d, hearing, was == null ? Vec3.ZERO : at.subtract(was).scale(1.0 / EVERY));
            total += p;
            if (p > most) {
                most = p;
                focus = other;
            }
            if (isThreat(other) && p > mostThreat) {
                mostThreat = p;
                threat = other;
            }
        }
        this.seen = now;
        this.focus = focus;
        this.threat = threat;
        double mood = 1 + MOOD * Mth.sin((float) (snake.level().getGameTime() * MOOD_PACE + snake.getId()));
        float level = (float) (1 - Math.exp(-total * GAIN * t.nerve() * nerve * mood));
        this.alertness += (level - this.alertness) * (level > this.alertness ? RISE * Math.min(1F, warm) : FALL);
        if (snake.getLastHurtByMobTimestamp() != this.hurtStamp) {
            this.hurtStamp = snake.getLastHurtByMobTimestamp();
            if (snake.getLastHurtByMob() != null)
                this.alertness = 1F;
        }
        this.calmFor = this.alertness < GLANCE ? this.calmFor + EVERY : 0;
        // Whatever works it up as far as watching wakes it, with a start.
        if (this.asleep && this.alertness >= WATCH) {
            this.asleep = false;
            this.justWoke = true;
            this.alertness = Math.min(1F, this.alertness + STARTLE);
        }
        this.rattle = threat != null && this.alertness >= (this.rattle ? CALM : RATTLE);
    }

    /**
     * How much one creature presses on it.
     *
     * @param velocity how far it moved a tick since the last look, zero the first time it's seen
     */
    static double pressure(PathfinderMob snake, LivingEntity other, double distance, double hearing, Vec3 velocity) {
        double near = 1 - distance / hearing;
        double speed = velocity.length();
        double loud = PRESENCE + Math.min(speed / LOUD_SPEED, LOUD_MOST) * (1 - PRESENCE);
        if (other.isCrouching() || other instanceof Player player && player.isShiftKeyDown())
            loud *= SNEAKING;
        double approach = 1;
        double across = velocity.horizontalDistance();
        if (speed > MOVING && across > 1e-4) {
            double tx = snake.getX() - other.getX(), tz = snake.getZ() - other.getZ();
            double toward = Math.sqrt(tx * tx + tz * tz);
            if (toward > 1e-4)
                approach = 1 + APPROACH * (velocity.x * tx + velocity.z * tz) / (across * toward) * (across / speed);
        }
        double size = Mth.clamp(Math.sqrt(other.getBbWidth() * other.getBbHeight()) / SIZE_PLAYER, SIZE_LEAST, SIZE_MOST);
        double p = Math.pow(near, NEAR_FALLOFF) * loud * approach * size + Math.max(0, 1 - distance / TOO_CLOSE) * size * TOO_CLOSE_WEIGHT;
        return SnakePreyGoal.isPrey(other) ? p * PREY_INTEREST : p;
    }

    /** What it warns off and strikes at when worked up: Alex's own two, a player (not in creative) and a Roadrunner. */
    static boolean isThreat(LivingEntity other) {
        return other instanceof Player || ROADRUNNER.equals(BuiltInRegistries.ENTITY_TYPE.getKey(other.getType()));
    }

    /** How far up its head is, 0 (resting) to {@code HEAD_STEPS}. */
    public int headLevel() {
        return Math.round(HEAD_STEPS * Mth.clamp((this.alertness - GLANCE) / (WATCH - GLANCE), 0F, 1F));
    }

    /** What it's watching: the threat it rattles at, or else whatever presses on it most, once its head is up at all. */
    public LivingEntity watching() {
        LivingEntity what = this.rattle ? this.threat : headLevel() > 0 ? this.focus : null;
        return what != null && what.isAlive() ? what : null;
    }

    public boolean rattles() {
        return this.rattle && !this.fleeing && this.threat != null && this.threat.isAlive();
    }

    /**
     * Running for cover ({@link SnakeRetreatGoal}): no rattling meanwhile, since Alex coils a rattling snake with no target and
     * a coiled snake can't move (2026-11-15: the retreat planned its path, was coiled the same tick, and gave up).
     */
    public void setFleeing(boolean fleeing) {
        this.fleeing = fleeing;
    }

    public boolean fleeing() {
        return this.fleeing;
    }

    public LivingEntity threat() {
        return this.threat;
    }

    /** Whether it goes for the threat now: worked up to the full and the threat within its strike reach. */
    public boolean strikes(PathfinderMob snake) {
        return this.threat != null && this.threat.isAlive() && this.alertness >= STRIKE
            && snake.distanceTo(this.threat) <= temper(snake).strikeReach();
    }

    public float alertness() {
        return this.alertness;
    }

    public SnakeWarmth warmth() {
        return this.warmth;
    }

    public boolean asleep() {
        return this.asleep;
    }

    /** Its den: the spot it goes home to sleep (a bush, a nook, a cave corner; {@link SnakeCover}), or null. Saved with it. */
    public BlockPos den() {
        return this.den;
    }

    public void setDen(BlockPos den) {
        this.den = den == null ? null : den.immutable();
    }

    public void save(CompoundTag tag) {
        if (this.den != null)
            tag.putIntArray(DEN_KEY, new int[] {this.den.getX(), this.den.getY(), this.den.getZ()});
    }

    public void load(CompoundTag tag) {
        int[] at = tag.getIntArray(DEN_KEY);
        this.den = at.length == 3 ? new BlockPos(at[0], at[1], at[2]) : null;
    }

    /** Falls asleep (it should be coiled, waiting and calm); {@link #wake} wakes it quietly, without a start. */
    public void sleep() {
        this.asleep = true;
    }

    public void wake() {
        if (this.asleep)
            this.justWoke = true;
        this.asleep = false;
    }

    /**
     * Just woken, for {@code GROGGY_LEAST} to {@code GROGGY_MOST} ticks: its head is up and its tongue going, but it doesn't go
     * for prey yet (the user, 2026-11-16: it lunged at a rabbit the instant it woke; now it's wake, look, strike, and a quick
     * rabbit gets away). A threat still gets the startled rattle at once.
     */
    public boolean groggy() {
        return this.groggy > 0 || this.justWoke;
    }

    /** How many ticks it has been calm (not even glancing at anything). */
    public int calmTicks() {
        return this.calmFor;
    }

    public LivingEntity focus() {
        return this.focus;
    }

    /** What every Rattlesnake is made to have, on the server. */
    public interface Holder {
        SnakeMind grout$mind();
    }
}
