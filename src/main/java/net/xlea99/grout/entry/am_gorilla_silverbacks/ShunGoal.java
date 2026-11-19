package net.xlea99.grout.entry.am_gorilla_silverbacks;

import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

/** A beaten Gorilla walks away from the one who beat it whenever it's within {@link Silverbacks#SHUN_RANGE}. */
public final class ShunGoal extends Goal {
    static final double SPEED = 1.15;
    /** How far it heads off in one go, across and up or down. */
    static final int AWAY = 20;
    static final int AWAY_HEIGHT = 7;

    private final Silverbacks.Holder holder;
    private Vec3 to;

    public ShunGoal(Silverbacks.Holder holder) {
        this.holder = holder;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        Silverbacks state = holder.grout$silverbacks();
        if (state.shunned == null || state.phase != Silverbacks.Phase.NONE || holder.grout$forcedSit())
            return false;
        Entity winner = state.shunnedEntity();
        if (winner == null || state.gorilla.distanceTo(winner) > Silverbacks.SHUN_RANGE)
            return false;
        to = DefaultRandomPos.getPosAway(state.gorilla, AWAY, AWAY_HEIGHT, winner.position());
        return to != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !holder.grout$silverbacks().gorilla.getNavigation().isDone();
    }

    @Override
    public void start() {
        holder.grout$silverbacks().gorilla.getNavigation().moveTo(to.x, to.y, to.z, SPEED);
    }
}
