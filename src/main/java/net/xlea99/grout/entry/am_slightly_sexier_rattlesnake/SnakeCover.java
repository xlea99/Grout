package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Where a Rattlesnake likes to lie: how good a spot is, and finding the best one nearby.
 *
 * <p>A spot's score: 3 in cover (a block in {@code grout:snake_cover}: dead bushes, dry grass, desert shrubs it can lie
 * inside), 2 in a nook (solid on three sides), 1 in a corner (solid on two sides that meet; two opposite walls are a
 * corridor and don't count), 0 in the open. Under a roof (no sky above: a cave, an overhang) adds one, up to 3. A den needs
 * {@code DEN} or better: a bush, a nook, a sheltered corner.
 */
public final class SnakeCover {
    public static final TagKey<Block> COVER = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("grout", "snake_cover"));
    public static final int BEST = 3, DEN = 2;
    /** How far up and down a column it looks for ground, from its own height. */
    static final int UP = 3, DOWN = 4;

    private SnakeCover() {
    }

    public static int score(Level level, BlockPos spot) {
        if (level.getBlockState(spot).is(COVER))
            return BEST;
        boolean[] solid = new boolean[4];
        int sides = 0;
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockPos n = spot.relative(d);
            if (level.getBlockState(n).isFaceSturdy(level, n, d.getOpposite())) {
                solid[d.get2DDataValue()] = true;
                sides++;
            }
        }
        int base;
        if (sides >= 3)
            base = 2;
        else if (sides == 2 && !(solid[0] && solid[2]) && !(solid[1] && solid[3]))
            base = 1;
        else
            base = 0;
        return Math.min(BEST, base + (level.canSeeSky(spot) ? 0 : 1));
    }

    /** Ground a snake can lie on: a sturdy top below (not leaves), nothing solid or wet in the spot itself. */
    public static boolean standable(Level level, BlockPos spot) {
        BlockPos below = spot.below();
        BlockState ground = level.getBlockState(below);
        return !ground.is(BlockTags.LEAVES) && ground.isFaceSturdy(level, below, Direction.UP)
            && level.getBlockState(spot).getCollisionShape(level, spot).isEmpty() && level.getFluidState(spot).isEmpty();
    }

    /** The ground in a column nearest {@code from}'s height (0, 1, -1, 2, -2 ...), so a canopy overhead isn't taken for it. */
    public static BlockPos groundNear(Level level, BlockPos from, int dx, int dz) {
        for (int k = 0; k <= 2 * Math.max(UP, DOWN); k++) {
            int dy = (k & 1) == 0 ? -k / 2 : (k + 1) / 2;
            if (dy > UP || dy < -DOWN)
                continue;
            BlockPos spot = from.offset(dx, dy, dz);
            if (standable(level, spot))
                return spot;
        }
        return null;
    }

    /**
     * The best-scoring spot between {@code least} and {@code most} blocks away (capped at {@code FURTHEST}), passing {@code ok};
     * null if none. Every column in the ring is looked at (cover is a block here and there: sampling a few dozen columns at
     * random hardly ever found a bush, 2026-11-15). A tie goes to the nearest with {@code nearest}, else to one at random.
     */
    public static BlockPos find(PathfinderMob snake, double least, double most, boolean nearest, Predicate<BlockPos> ok) {
        Level level = snake.level();
        BlockPos from = snake.blockPosition();
        double far = Math.min(most, FURTHEST);
        int r = Mth.ceil(far);
        BlockPos best = null;
        int bestScore = -1, ties = 0;
        double bestDistance = Double.MAX_VALUE;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                double d2 = dx * dx + dz * dz;
                if (d2 < least * least || d2 > far * far)
                    continue;
                BlockPos spot = groundNear(level, from, dx, dz);
                if (spot == null || !ok.test(spot))
                    continue;
                int s = score(level, spot);
                if (s < bestScore)
                    continue;
                if (s > bestScore) {
                    bestScore = s;
                    best = spot;
                    bestDistance = d2;
                    ties = 1;
                } else if (nearest ? d2 < bestDistance : snake.getRandom().nextInt(++ties) == 0) {
                    best = spot;
                    bestDistance = d2;
                }
            }
        }
        return best;
    }

    /** The furthest any search looks, in blocks. */
    static final double FURTHEST = 24.0;

    /** {@link #candidates(PathfinderMob, double, double, int, Predicate, java.util.function.ToDoubleFunction)}, nearest first within a score. */
    public static java.util.List<BlockPos> nearest(PathfinderMob snake, double least, double most, int limit, Predicate<BlockPos> ok) {
        BlockPos from = snake.blockPosition();
        return candidates(snake, least, most, limit, ok, p -> p.distSqr(from));
    }

    /**
     * The best spots between {@code least} and {@code most} blocks away passing {@code ok}, best score first, and within a
     * score by {@code order} (lowest first), at most {@code limit} of them.
     */
    public static java.util.List<BlockPos> candidates(PathfinderMob snake, double least, double most, int limit, Predicate<BlockPos> ok,
                                                      java.util.function.ToDoubleFunction<BlockPos> order) {
        Level level = snake.level();
        BlockPos from = snake.blockPosition();
        double far = Math.min(most, FURTHEST);
        int r = Mth.ceil(far);
        record Found(int score, double order, BlockPos spot) {
        }
        java.util.List<Found> found = new java.util.ArrayList<>();
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                long d2 = (long) dx * dx + (long) dz * dz;
                if (d2 < least * least || d2 > far * far)
                    continue;
                BlockPos spot = groundNear(level, from, dx, dz);
                if (spot == null || !ok.test(spot))
                    continue;
                found.add(new Found(score(level, spot), order.applyAsDouble(spot), spot));
            }
        }
        found.sort((a, b) -> a.score != b.score ? Integer.compare(b.score, a.score) : Double.compare(a.order, b.order));
        return found.stream().limit(limit).map(Found::spot).toList();
    }

    /**
     * Sets off for the first of {@code spots} it has a whole path to (a bush across a ravine or behind a wall is passed over:
     * 2026-11-16, hunting hops walked to a wall and stopped short); with none whole, for the first it can get near. Returns
     * the spot it's going to, or null with no path at all (off the ground: the caller tries again later).
     */
    public static BlockPos goToFirstReachable(PathfinderMob snake, java.util.List<BlockPos> spots, double speed) {
        for (BlockPos spot : spots) {
            var path = snake.getNavigation().createPath(spot, 0);
            if (path == null)
                return null;
            if (path.canReach() && snake.getNavigation().moveTo(path, speed))
                return spot;
        }
        return !spots.isEmpty() && goTo(snake, spots.get(0), speed) ? spots.get(0) : null;
    }

    /** How many candidates a move tries for a whole path. */
    public static final int TRY_PATHS = 6;

    /**
     * Sets off for {@code spot} itself: vanilla's {@code moveTo(x, y, z)} plans with an accuracy of one block, so a snake sent
     * into a bush stopped beside it (2026-11-15). Plans to the block exactly, or within one if that can't be reached.
     */
    public static boolean goTo(PathfinderMob snake, BlockPos spot, double speed) {
        var path = snake.getNavigation().createPath(spot, 0);
        if (path == null || !path.canReach())
            path = snake.getNavigation().createPath(spot, 1);
        // No path at all (vanilla plans none for a mob off the ground: just spawned, mid-hop): the caller tries again later.
        return path != null && snake.getNavigation().moveTo(path, speed);
    }

    /**
     * Once its path is done, the last little way onto the middle of {@code spot}, slipping into the bush. True while it's
     * still settling in: more than {@code SETTLED} off the middle, and less than {@code NEAR} (further, it got stuck, so it
     * stays where it is).
     */
    public static boolean settleInto(PathfinderMob snake, BlockPos spot, double speed) {
        double dx = spot.getX() + 0.5 - snake.getX(), dz = spot.getZ() + 0.5 - snake.getZ();
        double d2 = dx * dx + dz * dz;
        if (d2 < SETTLED * SETTLED || d2 > NEAR * NEAR || Math.abs(spot.getY() - snake.getY()) > 1.2)
            return false;
        snake.getMoveControl().setWantedPosition(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, speed);
        return true;
    }

    static final double SETTLED = 0.3, NEAR = 2.5;
}
