package net.xlea99.grout.entry.am_gazelle_movement;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * A fleeing Gazelle turns in arcs ({@link AmGazelleMovement}), unless it has to: it's hit something, or there's danger in its way, where it may
 * turn as sharply as vanilla lets it.
 */
public final class GazelleMoveControl extends MoveControl {
    /** The most it turns in a tick while fleeing, in degrees. */
    private static final float FLEEING_TURN = 10F;
    /** How far ahead it looks for danger, in blocks. */
    private static final int[] LOOKS_AHEAD = {2, 4};
    /** A drop of more than this many blocks counts as danger. */
    private static final int WORST_DROP = 4;

    public GazelleMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    protected float rotlerp(float sourceAngle, float targetAngle, float maximumChange) {
        boolean arcing = ((GazelleFlee.Holder) this.mob).grout$fleeing() && !this.mob.horizontalCollision && !dangerAhead();
        return super.rotlerp(sourceAngle, targetAngle, arcing ? Math.min(maximumChange, FLEEING_TURN) : maximumChange);
    }

    private boolean dangerAhead() {
        float heading = this.mob.getYRot() * Mth.DEG_TO_RAD;
        float x = -Mth.sin(heading);
        float z = Mth.cos(heading);
        for (int ahead : LOOKS_AHEAD) {
            BlockPos pos = BlockPos.containing(this.mob.getX() + x * ahead, this.mob.getY(), this.mob.getZ() + z * ahead);
            if (this.mob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(this.mob, pos)) < 0F || bigDrop(pos))
                return true;
        }
        return false;
    }

    private boolean bigDrop(BlockPos pos) {
        for (int down = 0; down <= WORST_DROP; down++) {
            if (!this.mob.level().getBlockState(pos.below(down)).getCollisionShape(this.mob.level(), pos.below(down)).isEmpty())
                return false;
        }
        return true;
    }
}
