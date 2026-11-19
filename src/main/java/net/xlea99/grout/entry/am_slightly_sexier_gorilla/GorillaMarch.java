package net.xlea99.grout.entry.am_slightly_sexier_gorilla;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * One Gorilla's part in its troop's marches. A wild, grown Silverback rolls once each morning ({@code MORNING_CHANCE}) and
 * once each midday ({@code MIDDAY_CHANCE}, if it isn't already marching) to march, at a time of its own in that window: it
 * picks a compass direction and walks {@code LEGS_LEAST}-{@code LEGS_MOST} legs that way, each leg a reachable spot about
 * {@code LEG_LEAST}-{@code LEG_MOST} blocks on (about 140 blocks in all, on average). A leg whose spot isn't in
 * {@code #grout:gorilla_home_biomes} turns to another direction; with none left, the march ends.
 *
 * <p>Every Gorilla near a marching Silverback (its troop's reach) neither forages nor sits down at random, which is what kept
 * troops circling trees; its troop follows ({@link TroopFollowGoal}). Tame Gorillas never march. Server only.
 */
public final class GorillaMarch {
    public static final TagKey<Biome> HOME_BIOMES = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("grout", "gorilla_home_biomes"));
    static final float MORNING_CHANCE = 0.75F;
    static final float MIDDAY_CHANCE = 0.25F;
    /**
     * The two windows a march can start in, in ticks of the day (0 = 6 AM, 6000 = noon). Each is rolled once as it opens, and a
     * march it grants starts at a time of its own in the window, so troops head out one by one rather than all at dawn: the
     * morning's most likely around {@code MORNING_PEAK} (7:30 AM), the midday's anywhere between 11 AM and 3 PM.
     */
    static final long MORNING_FROM = 0L;
    static final long MORNING_PEAK = 1500L;
    static final long MORNING_TO = 5000L;
    static final long MIDDAY_FROM = 5000L;
    static final long MIDDAY_TO = 9000L;
    static final int LEGS_LEAST = 5;
    static final int LEGS_MOST = 9;
    static final int LEG_LEAST = 15;
    static final int LEG_MOST = 25;
    /** Sideways wobble of a leg's aim, in blocks, so the line isn't ruler straight. */
    static final int LEG_WOBBLE = 4;
    /** How far above or below the Silverback a leg's spot may be: more is a cliff, not a walk. */
    static final int LEG_CLIMB = 12;
    /**
     * A Gorilla's normal walk, the pace its troop follows at (they hurry when far behind, and the march waits for stragglers).
     * It was 0.7, about 1.3 blocks a second, which made Alex's limb swings, scaled by speed, shrink to a tiptoe.
     */
    static final double MARCH_SPEED = 1.0;
    /** A leg it hasn't finished in this many ticks counts as done. */
    static final int LEG_GIVE_UP = 600;
    /** Ticks without moving {@code PROGRESS} blocks before a leg counts as stuck. */
    static final int STUCK = 60;
    static final double PROGRESS = 0.5;
    /** A sidestep leg's length, least and most, in blocks. */
    static final int SIDESTEP_LEAST = 4;
    static final int SIDESTEP_MOST = 7;
    static final double ARRIVED = 3.0;
    /** While marching, it waits when more than half of its followers are this far behind... */
    static final double WAIT_BEHIND = 16.0;
    /** ...for at most this many ticks a leg. */
    static final int WAIT_MOST = 200;
    /** The troop's reach, as Alex's {@code getNearestSilverback} uses: who counts as marching along. */
    static final double TROOP_RANGE = 32.0;
    static final double TROOP_HEIGHT = 16.0;

    /** The Gorilla mixin, so this code can reach Alex's members without being built against Alex's Mobs. */
    public interface Holder extends GorillaState {
        GorillaMarch grout$march();

        boolean grout$isSitting();

        boolean grout$isEating();
    }

    final TamableAnimal gorilla;
    private final Holder holder;
    private long morningRolled = -1L;
    private long middayRolled = -1L;
    /** A march a roll granted: it starts at {@code plannedAt}, in the level's day time, unless its window closes first. */
    private long plannedAt = -1L;
    private long plannedUntil = -1L;
    boolean marching;
    private Direction heading = Direction.NORTH;
    private int legsLeft;
    Vec3 legTo;
    int legTicks;
    /** Where it last made {@code PROGRESS}, and the ticks since. */
    Vec3 progressFrom;
    int stillTicks;
    /** The Silverback this Gorilla follows ({@link TroopFollowGoal}), if any. */
    java.util.UUID following;
    /** A marching Silverback holding the leg for stragglers, and the ticks it has held this leg. */
    boolean waiting;
    int waitedThisLeg;
    private boolean troopMarching;

    public GorillaMarch(TamableAnimal gorilla, Holder holder) {
        this.gorilla = gorilla;
        this.holder = holder;
    }

    public void save(CompoundTag tag) {
        CompoundTag mine = new CompoundTag();
        mine.putLong("MorningRolled", morningRolled);
        mine.putLong("MiddayRolled", middayRolled);
        mine.putLong("PlannedAt", plannedAt);
        mine.putLong("PlannedUntil", plannedUntil);
        tag.put("GroutGorillaMarch", mine);
    }

    public void load(CompoundTag tag) {
        if (!tag.contains("GroutGorillaMarch"))
            return;
        CompoundTag mine = tag.getCompound("GroutGorillaMarch");
        morningRolled = mine.getLong("MorningRolled");
        middayRolled = mine.getLong("MiddayRolled");
        plannedAt = mine.contains("PlannedAt") ? mine.getLong("PlannedAt") : -1L;
        plannedUntil = mine.contains("PlannedUntil") ? mine.getLong("PlannedUntil") : -1L;
    }

    /** Whether it, or a Silverback in its reach, is marching right now: no foraging, no sitting down at random. */
    public boolean troopMarching() {
        return troopMarching;
    }

    /** Server, at the end of the Gorilla's own tick. */
    public void tick() {
        if (marching && !canMarch())
            end();
        if ((gorilla.tickCount + gorilla.getId()) % 20 == 0) {
            roll();
            troopMarching = marching || anyMarchingNear();
            waiting = marching && waitedThisLeg < WAIT_MOST && troopLagging();
        }
        if (waiting)
            waitedThisLeg++;
        if (troopMarching && holder.grout$isSitting() && !holder.grout$forcedSit() && !holder.grout$isEating())
            gorilla.setOrderedToSit(false);
    }

    private boolean canMarch() {
        return gorilla.isAlive() && !gorilla.isBaby() && !gorilla.isTame() && holder.grout$isSilverback();
    }

    private void roll() {
        if (marching || !canMarch())
            return;
        long time = gorilla.level().getDayTime();
        long day = time / 24000L;
        long of = time % 24000L;
        long dayStart = time - of;
        RandomSource random = gorilla.getRandom();
        if (plannedAt >= 0 && (time >= plannedUntil || time < plannedAt - 24000L)) {
            plannedAt = -1L; // its window closed (or time was set back past it)
        } else if (plannedAt >= 0 && time >= plannedAt) {
            plannedAt = -1L;
            start(Direction.Plane.HORIZONTAL.getRandomDirection(random));
            return;
        }
        if (of >= MORNING_FROM && of < MORNING_TO && morningRolled != day) {
            morningRolled = day;
            if (random.nextFloat() < MORNING_CHANCE)
                plan(dayStart, Math.max(of, MORNING_FROM), MORNING_PEAK, MORNING_TO, random);
        } else if (of >= MIDDAY_FROM && of < MIDDAY_TO && middayRolled != day) {
            middayRolled = day;
            if (random.nextFloat() < MIDDAY_CHANCE)
                plan(dayStart, Math.max(of, MIDDAY_FROM), -1L, MIDDAY_TO, random);
        }
    }

    /**
     * Pick a start between {@code from} and {@code to} (ticks of the day): most likely at {@code peak} when it's still ahead
     * (a triangle from the window's opening to its close), else evenly.
     */
    private void plan(long dayStart, long from, long peak, long to, RandomSource random) {
        double u = random.nextDouble();
        double at;
        if (peak > from && peak < to) {
            double span = to - from, share = (peak - from) / span;
            at = u < share ? from + Math.sqrt(u * span * (peak - from)) : to - Math.sqrt((1 - u) * span * (to - peak));
        } else {
            at = from + u * (to - from);
        }
        plannedAt = dayStart + (long) at;
        plannedUntil = dayStart + to;
    }

    private void start(Direction direction) {
        heading = direction;
        legsLeft = Mth.nextInt(gorilla.getRandom(), LEGS_LEAST, LEGS_MOST);
        marching = true;
        troopMarching = true;
        if (!nextLeg(false))
            end();
    }

    /** A leg arrived or timed out: the next, or the end of the march. Called by the march goal. */
    void legDone() {
        if (--legsLeft <= 0 || !nextLeg(false))
            end();
    }

    /**
     * Stuck on a leg: one short leg to a side, then on in the march's direction. A Silverback is 1.35 blocks wide, but its path
     * can hug one column: in a trial one ground against a trunk beside its path, the route still saying "straight ahead".
     */
    void stuck() {
        if (--legsLeft <= 0 || !nextLeg(true) && !nextLeg(false))
            end();
    }

    /**
     * Aim the next leg. Normally this heading if its spot is home, else a side (turning the march there), else back. A
     * sidestep tries the sides first, short, and keeps the march's heading. False when there's nowhere to go.
     */
    private boolean nextLeg(boolean sidestep) {
        List<Direction> tries = sidestep ? new ArrayList<>(List.of(heading.getClockWise(), heading.getCounterClockWise()))
            : new ArrayList<>(List.of(heading, heading.getClockWise(), heading.getCounterClockWise(), heading.getOpposite()));
        RandomSource random = gorilla.getRandom();
        if (random.nextBoolean())
            java.util.Collections.swap(tries, sidestep ? 0 : 1, sidestep ? 1 : 2);
        for (Direction direction : tries) {
            int reach = sidestep ? Mth.nextInt(random, SIDESTEP_LEAST, SIDESTEP_MOST) : Mth.nextInt(random, LEG_LEAST, LEG_MOST);
            int wobble = sidestep ? 0 : Mth.nextInt(random, -LEG_WOBBLE, LEG_WOBBLE);
            Vec3 aim = gorilla.position().add(direction.getStepX() * reach + direction.getStepZ() * wobble, 0,
                direction.getStepZ() * reach + direction.getStepX() * wobble);
            Vec3 spot = groundAt(BlockPos.containing(aim), direction);
            if (spot == null)
                continue;
            if (!sidestep)
                heading = direction;
            legTo = spot;
            legTicks = 0;
            stillTicks = 0;
            waitedThisLeg = 0;
            progressFrom = gorilla.position();
            return true;
        }
        return false;
    }

    /**
     * The ground at a leg's aim, or a step or two to the side of it: not a tree trunk, not water, not a cliff away, loaded,
     * and home. Exact on purpose: vanilla's {@code LandRandomPos.getPosTowards} picks from a wide cone and keeps the candidate
     * with the nicest ground, so in a trial a "north" leg went 15 blocks east and 9 north and the march doubled back on itself.
     */
    private Vec3 groundAt(BlockPos aim, Direction direction) {
        Level level = gorilla.level();
        Direction side = direction.getClockWise();
        for (int nudge : new int[] {0, 2, -2, 4, -4}) {
            BlockPos column = aim.relative(side, nudge);
            if (!level.hasChunkAt(column))
                continue;
            BlockPos ground = standable(level, column);
            if (ground == null || !level.getBiome(ground).is(HOME_BIOMES))
                continue;
            return Vec3.atBottomCenterOf(ground);
        }
        return null;
    }

    /**
     * The highest spot in this column, within {@code LEG_CLIMB} of the Silverback, it could stand on: solid footing that isn't
     * leaves or a log, two blocks of room, no water. Not the heightmap: in a jungle its top is often a giant tree's log branch
     * or an overhang, not the floor.
     */
    private BlockPos standable(Level level, BlockPos column) {
        int from = Mth.floor(gorilla.getY()) + LEG_CLIMB;
        int to = Mth.floor(gorilla.getY()) - LEG_CLIMB;
        for (int y = from; y >= to; y--) {
            BlockPos at = column.atY(y);
            BlockPos below = at.below();
            BlockState footing = level.getBlockState(below);
            if (!footing.isFaceSturdy(level, below, Direction.UP) || footing.is(BlockTags.LEAVES) || footing.is(BlockTags.LOGS))
                continue;
            if (!level.getBlockState(at).getCollisionShape(level, at).isEmpty() || !level.getBlockState(at.above()).getCollisionShape(level, at.above()).isEmpty())
                continue;
            if (!level.getFluidState(at).isEmpty() || !level.getFluidState(below).isEmpty())
                continue;
            return at;
        }
        return null;
    }

    void end() {
        marching = false;
        legTo = null;
        legsLeft = 0;
        gorilla.getNavigation().stop();
    }

    /** More than half of the Gorillas following it are {@code WAIT_BEHIND} or further back. */
    private boolean troopLagging() {
        AABB reach = gorilla.getBoundingBox().inflate(TroopFollowGoal.REMEMBER_RANGE);
        List<TamableAnimal> followers = gorilla.level().getEntitiesOfClass(TamableAnimal.class, reach,
            other -> other != gorilla && other instanceof Holder h && gorilla.getUUID().equals(h.grout$march().following));
        if (followers.isEmpty())
            return false;
        int behind = 0;
        for (TamableAnimal follower : followers)
            if (follower.distanceTo(gorilla) >= WAIT_BEHIND)
                behind++;
        return behind * 2 > followers.size();
    }

    private boolean anyMarchingNear() {
        AABB troop = gorilla.getBoundingBox().inflate(TROOP_RANGE, TROOP_HEIGHT, TROOP_RANGE);
        return !gorilla.level().getEntitiesOfClass(TamableAnimal.class, troop,
            other -> other != gorilla && other instanceof Holder h && h.grout$march().marching).isEmpty();
    }
}
