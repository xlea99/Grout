package net.xlea99.lifeguard.entry.am_gazelle_movement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

/**
 * Following a path at a Gazelle's fleeing speed ({@link AmGazelleMovement}). Vanilla moves on one waypoint a tick, once within half a block of
 * it and a block of its height. At over a block a tick it passes them and turns back, and falling off a ledge it can't reach them at all. Here
 * it moves past every waypoint within a radius that grows with its speed, so that what it's steering at is always ahead of it. Standing still
 * that radius is about vanilla's: with a fixed one, a path shorter than it was over before the Gazelle took a step, and on a small patch of
 * land it stood frozen, picking one after another. Everything else is vanilla's.
 */
public final class GazelleNavigation extends GroundPathNavigation {
    /** How close counts as reached at a standstill, the most it ever gets, and how much it grows per block a tick of speed. */
    private static final double REACHED_STANDING = 0.5D;
    private static final double REACHED_MOST = 2.5D;
    private static final double REACHED_PER_SPEED = 2D;
    /** The most height that counts as reached at speed: falling off a ledge, a waypoint is far above it. */
    private static final double REACHED_HEIGHT = 3D;
    /** It can't stop on a spot when it can only turn so fast, so this close to the end of the path it's done, and picks where next: at a standstill, as near as any waypoint, and at speed, up to the most. */
    private static final double END_OF_PATH_MOST = 6D;
    private static final double END_OF_PATH_PER_SPEED = 5D;

    public GazelleNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected void followThePath() {
        if (!((GazelleFlee.Holder) this.mob).lifeguard$fleeing()) {
            super.followThePath();
            return;
        }
        double speed = Math.sqrt(this.mob.getDeltaMovement().horizontalDistanceSqr());
        double radius = Math.clamp(speed * REACHED_PER_SPEED, REACHED_STANDING, REACHED_MOST);
        double endOfPath = Math.clamp(speed * END_OF_PATH_PER_SPEED, REACHED_STANDING, END_OF_PATH_MOST);
        while (!this.path.isDone() && this.path.getNextNodeIndex() < this.path.getNodeCount() - 1 && reached(this.path.getNextNodePos(), radius))
            this.path.advance();
        if (this.path.isDone() || this.path.getNextNodeIndex() >= this.path.getNodeCount() - 1 && reached(this.path.getNextNodePos(), endOfPath)) {
            this.stop();
            return;
        }
        this.doStuckDetection(this.getTempMobPos());
    }

    private boolean reached(BlockPos node, double across) {
        return Math.abs(this.mob.getX() - (node.getX() + 0.5D)) <= across
            && Math.abs(this.mob.getZ() - (node.getZ() + 0.5D)) <= across
            && Math.abs(this.mob.getY() - node.getY()) < REACHED_HEIGHT;
    }
}
