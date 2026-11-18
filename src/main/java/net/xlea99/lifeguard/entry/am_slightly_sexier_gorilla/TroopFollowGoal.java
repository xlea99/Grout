package net.xlea99.lifeguard.entry.am_slightly_sexier_gorilla;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.xlea99.lifeguard.entry.am_gorilla_silverbacks.Silverbacks;

/**
 * A grown, non-Silverback Gorilla keeps with its troop: it remembers its Silverback and stays within loose reach of it,
 * spread around it rather than in Alex's single-file line (which this replaces). Closer while the troop marches. It paths to
 * the Silverback itself, so from a hollow or a hole it finds a real way back if there is one. An unreachable Silverback
 * means walking as close as it can and backing off its planning (5, 10, then 20 seconds between tries), then giving up: a
 * Gorilla stranded for good is leaderless,
 * and the Silverbacks entry raises one of the stranded as theirs. Wild Gorillas follow wild Silverbacks; tame ones only a
 * Silverback with the same owner. A Gorilla never follows one it shuns. Babies ride their parents as Alex has them.
 */
public final class TroopFollowGoal extends Goal {
    public static final int PRIORITY = 2;
    /** Who it can take up with: a Silverback within this reach, across and up or down. */
    static final double JOIN_RANGE = 24.0;
    static final double JOIN_HEIGHT = 12.0;
    /** How far it keeps track of its own Silverback before losing it. */
    static final double REMEMBER_RANGE = 48.0;
    /** How far from its Silverback it lets itself be before heading back: idling, and marching. */
    static final double IDLE_SLACK = 10.0;
    static final double MARCH_SLACK = 6.0;
    /**
     * How close it comes before stopping, least and most: each Gorilla its own, so they spread out. Further than
     * {@code REACHES}: at 2.5, followers about 3 blocks off with nowhere closer to stand planned a full search every second.
     */
    static final double STOP_LEAST = 3.5;
    static final double STOP_MOST = 5.5;
    /** Re-plan when the Silverback has moved this far since the last plan, or after this many ticks. */
    static final double REPLAN_MOVED = 2.0;
    /** ...or this share of the distance between them, when that's more. */
    static final double REPLAN_SHARE = 0.3;
    static final int REPLAN_EVERY = 20;
    static final double SPEED = 1.0;
    /**
     * Fallen behind, it hurries (past a walk, so Knuckle-walking draws it bounding) until it's close again, then walks: past
     * {@code HURRY_FROM} it starts, under {@code HURRY_UNTIL} it stops, apart so it doesn't flicker between the two.
     */
    static final double HURRY = 1.25;
    static final double HURRY_FROM = 10.0;
    static final double HURRY_UNTIL = 7.0;
    private boolean hurrying;
    /** Ticks to wait after each unreachable plan in a row; after the last, it gives up on that Silverback. */
    static final int[] BACK_OFF = {100, 200, 400};
    /**
     * Plans end once a step this close (in blocks along each axis, added up) to the Silverback is found. Asking for its exact
     * block, as this goal first did, often asked for a spot a 2x2 Gorilla can't stand on beside a trunk: vanilla then searched
     * its whole budget (3-25 ms in a trial) before settling for the nearest spot, and called the path unreachable.
     */
    static final int NEAR_ENOUGH = 2;
    /** A path ending this close to the Silverback counts as reaching it. */
    static final double REACHES = 3.0;
    /** Ticks before trying again when vanilla plans nothing (the follower is mid-hop). */
    static final int RETRY_AIRBORNE = 5;
    /** Ticks after giving up on a Silverback before it may take up with that one again. */
    static final int READOPT_AFTER = 1200;

    private final TamableAnimal gorilla;
    private final GorillaMarch.Holder holder;
    private final double stopAt;
    private UUID leader;
    private UUID gaveUpOn;
    private long gaveUpAt;
    private int failures;
    private long nextTryAt;
    private long plannedAt;
    private double plannedX, plannedY, plannedZ;

    public TroopFollowGoal(TamableAnimal gorilla, GorillaMarch.Holder holder) {
        this.gorilla = gorilla;
        this.holder = holder;
        this.stopAt = STOP_LEAST + (gorilla.getUUID().getLeastSignificantBits() & 0xFF) / 255.0 * (STOP_MOST - STOP_LEAST);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!follows())
            return false;
        TamableAnimal silverback = leader();
        if (silverback == null)
            return false;
        holder.lifeguard$march().following = silverback.getUUID();
        return gorilla.level().getGameTime() >= nextTryAt && gorilla.distanceTo(silverback) > slack(silverback);
    }

    /**
     * Keeps going while backed off: it walks the best path it found and only waits to plan again. Stopping here, as this goal
     * first did, had followers in a trial standing or wandering for 5-20 seconds at a time while the march walked away.
     */
    @Override
    public boolean canContinueToUse() {
        TamableAnimal silverback = follows() ? current() : null;
        return silverback != null && gorilla.distanceTo(silverback) > stopAt;
    }

    @Override
    public void start() {
        plannedAt = Long.MIN_VALUE / 2;
        hurrying = false;
    }

    @Override
    public void stop() {
        gorilla.getNavigation().stop();
    }

    @Override
    public void tick() {
        TamableAnimal silverback = current();
        if (silverback == null)
            return;
        long now = gorilla.level().getGameTime();
        double distance = gorilla.distanceTo(silverback);
        if (hurrying ? distance < HURRY_UNTIL : distance > HURRY_FROM) {
            hurrying = !hurrying;
            gorilla.getNavigation().setSpeedModifier(hurrying ? HURRY : SPEED); // the path stays, only the pace changes
        }
        if (now < nextTryAt)
            return;
        // Further behind, the Silverback must move further before the old path is worth replacing: its end is still the way.
        double replanMoved = Math.max(REPLAN_MOVED, gorilla.distanceTo(silverback) * REPLAN_SHARE);
        boolean moved = Mth.lengthSquared(silverback.getX() - plannedX, silverback.getY() - plannedY, silverback.getZ() - plannedZ) > replanMoved * replanMoved;
        if (!moved && now - plannedAt < REPLAN_EVERY && !gorilla.getNavigation().isDone())
            return;
        double speed = hurrying ? HURRY : SPEED;
        Path path = gorilla.getNavigation().createPath(silverback, NEAR_ENOUGH);
        if (path == null) {
            // Vanilla plans nothing while a mob is off the ground (a hop on rough floor): not the Silverback's fault, try again soon.
            plannedAt = now - REPLAN_EVERY + RETRY_AIRBORNE;
            return;
        }
        plannedAt = now;
        plannedX = silverback.getX();
        plannedY = silverback.getY();
        plannedZ = silverback.getZ();
        if (!reaches(path, silverback)) {
            if (failures >= BACK_OFF.length) {
                gaveUpOn = leader;
                gaveUpAt = now;
                leader = null;
                holder.lifeguard$march().following = null;
                failures = 0;
                gorilla.getNavigation().stop();
                return;
            }
            nextTryAt = now + BACK_OFF[failures++];
            gorilla.getNavigation().moveTo(path, speed);
            return;
        }
        failures = 0;
        gorilla.getNavigation().moveTo(path, speed);
    }

    /**
     * Whether a path gets it to its Silverback: it ends within {@code REACHES} of where the Silverback stands. Not vanilla's
     * {@code canReach}, which wants the exact block: in a trial most paths it called unreachable ended 0-1.4 blocks from a
     * moving Silverback, and each one backed the follower off until it gave up on its troop.
     */
    private static boolean reaches(Path path, TamableAnimal silverback) {
        return path.canReach() || path.getEndNode() != null && path.getEndNode().asBlockPos().closerToCenterThan(silverback.position(), REACHES);
    }

    private boolean follows() {
        return gorilla.isAlive() && !gorilla.isBaby() && !holder.lifeguard$isSilverback() && !holder.lifeguard$forcedSit()
            && !holder.lifeguard$isEating() && !gorilla.isPassenger();
    }

    private double slack(TamableAnimal silverback) {
        return silverback instanceof GorillaMarch.Holder h && h.lifeguard$march().marching ? MARCH_SLACK : IDLE_SLACK;
    }

    /** Its Silverback if it's still one and in reach, else a new one in joining reach, else none. */
    private TamableAnimal leader() {
        TamableAnimal silverback = current();
        if (silverback != null)
            return silverback;
        leader = null;
        holder.lifeguard$march().following = null;
        failures = 0;
        AABB near = gorilla.getBoundingBox().inflate(JOIN_RANGE, JOIN_HEIGHT, JOIN_RANGE);
        List<TamableAnimal> candidates = gorilla.level().getEntitiesOfClass(TamableAnimal.class, near, this::canFollow);
        TamableAnimal nearest = null;
        for (TamableAnimal other : candidates)
            if (nearest == null || gorilla.distanceToSqr(other) < gorilla.distanceToSqr(nearest))
                nearest = other;
        if (nearest != null)
            leader = nearest.getUUID();
        return nearest;
    }

    private TamableAnimal current() {
        if (leader == null || !(gorilla.level() instanceof ServerLevel level))
            return null;
        Entity entity = level.getEntity(leader);
        if (entity instanceof TamableAnimal silverback && canFollow(silverback) && gorilla.distanceTo(silverback) <= REMEMBER_RANGE)
            return silverback;
        return null;
    }

    private boolean canFollow(TamableAnimal other) {
        if (other == gorilla || !other.isAlive() || other.isBaby() || !(other instanceof GorillaMarch.Holder h) || !h.lifeguard$isSilverback())
            return false;
        if (gorilla.isTame() != other.isTame() || gorilla.isTame() && (gorilla.getOwnerUUID() == null || !gorilla.getOwnerUUID().equals(other.getOwnerUUID())))
            return false;
        if (other.getUUID().equals(gaveUpOn) && gorilla.level().getGameTime() - gaveUpAt < READOPT_AFTER)
            return false;
        return !(gorilla instanceof Silverbacks.Holder s && s.lifeguard$silverbacks().shuns(other));
    }
}
