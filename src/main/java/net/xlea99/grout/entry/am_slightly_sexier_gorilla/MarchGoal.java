package net.xlea99.grout.entry.am_slightly_sexier_gorilla;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * The marching Silverback walks its legs. Below Alex's melee (2) so a fight interrupts it, and the Silverbacks entry's
 * standoff (1); above Alex's tempting (4), foraging (5) and wandering (6). The march itself is {@link GorillaMarch}'s.
 */
public final class MarchGoal extends Goal {
    public static final int PRIORITY = 3;

    private final GorillaMarch.Holder holder;
    private boolean wasWaiting;

    public MarchGoal(GorillaMarch.Holder holder) {
        this.holder = holder;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        GorillaMarch march = holder.grout$march();
        return march.marching && march.legTo != null;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        walk();
    }

    @Override
    public void stop() {
        holder.grout$march().gorilla.getNavigation().stop();
    }

    @Override
    public void tick() {
        GorillaMarch march = holder.grout$march();
        if (march.legTo == null)
            return;
        if (march.waiting) {
            march.gorilla.getNavigation().stop();
            march.progressFrom = march.gorilla.position();
            march.stillTicks = 0;
            wasWaiting = true;
            return;
        }
        if (wasWaiting) {
            wasWaiting = false;
            walk();
            return;
        }
        boolean arrived = march.gorilla.position().distanceTo(march.legTo) < GorillaMarch.ARRIVED;
        if (march.progressFrom == null || march.gorilla.position().distanceTo(march.progressFrom) > GorillaMarch.PROGRESS) {
            march.progressFrom = march.gorilla.position();
            march.stillTicks = 0;
        }
        if (!arrived && ++march.stillTicks > GorillaMarch.STUCK) {
            march.stuck();
            if (march.marching)
                walk();
        } else if (arrived || ++march.legTicks > GorillaMarch.LEG_GIVE_UP || march.gorilla.getNavigation().isDone()) {
            march.legDone();
            if (march.marching)
                walk();
        }
    }

    private void walk() {
        GorillaMarch march = holder.grout$march();
        if (march.legTo == null)
            return;
        // Near enough, not the exact spot: a leg's spot is picked for any mob, and when a 2x2 Gorilla can't stand on it vanilla
        // searched its whole budget (4-13 ms in a trial) before settling for the nearest.
        var navigation = march.gorilla.getNavigation();
        var path = navigation.createPath(net.minecraft.core.BlockPos.containing(march.legTo), NEAR_ENOUGH);
        if (path != null)
            navigation.moveTo(path, GorillaMarch.MARCH_SPEED);
        else
            navigation.moveTo(march.legTo.x, march.legTo.y, march.legTo.z, GorillaMarch.MARCH_SPEED);
    }

    /** Within this many blocks of a leg's spot (along each axis, added up) ends its plan. */
    static final int NEAR_ENOUGH = 3;
}
