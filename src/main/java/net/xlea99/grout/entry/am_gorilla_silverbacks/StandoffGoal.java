package net.xlea99.grout.entry.am_gorilla_silverbacks;

import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * A Silverback's side of a standoff: close in on the rival, face it, and on its own turn (rounds alternate, the driver first)
 * pound its chest, sometimes charging in first. The rolls are {@link Silverbacks}'; this only moves and poses. In a fight it
 * steps aside for Alex's melee goal.
 */
public final class StandoffGoal extends Goal {
    /** How close a charge gets before it stops and pounds. */
    static final double CHARGE_TO = 2.5;
    /** Ticks into its turn that it pounds, after any charge. */
    static final int POUND_AT = 15;
    /** Closing in on a rival is a show of force: past a walk (about 4.6 blocks a second), which Knuckle-walking draws as a bound. */
    static final double APPROACH_SPEED = 1.3;
    static final double CHARGE_SPEED = 1.6;

    /** Re-plan a path to the rival only when it has moved this far, the last path is done, or this many ticks have passed. */
    static final double REPLAN_MOVED = 2.0;
    static final int REPLAN_EVERY = 20;
    /**
     * Plans in a row that can't reach the rival before the rivalry is dropped. Each one searches vanilla's whole budget (4-7 ms
     * in a trial, where two Silverbacks 30-54 blocks apart across the terrain kept trying every second for half a minute).
     */
    static final int UNREACHABLE_GIVE_UP = 2;
    private int unreachable;

    private final Silverbacks.Holder holder;
    private long plannedAt = Long.MIN_VALUE / 2;
    private double plannedSpeed;
    private Vec3 plannedTo;

    public StandoffGoal(Silverbacks.Holder holder) {
        this.holder = holder;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Silverbacks state = holder.grout$silverbacks();
        return state.phase == Silverbacks.Phase.STANDOFF && state.rivalEntity() != null;
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
    public void stop() {
        holder.grout$silverbacks().gorilla.getNavigation().stop();
        plannedTo = null;
        unreachable = 0;
    }

    @Override
    public void tick() {
        Silverbacks state = holder.grout$silverbacks();
        Entity rival = state.rivalEntity();
        if (rival == null)
            return;
        var gorilla = state.gorilla;
        gorilla.setOrderedToSit(false);
        gorilla.getLookControl().setLookAt(rival, 30F, 30F);
        double distance = gorilla.distanceTo(rival);
        if (distance > Silverbacks.CLOSED_IN) {
            goTo(gorilla, rival, APPROACH_SPEED);
            return;
        }
        int round = state.closeTicks / Silverbacks.ROUND;
        int intoRound = state.closeTicks % Silverbacks.ROUND;
        boolean myTurn = (round % 2 == 0) == state.driver;
        if (myTurn && state.chargeThisRound && intoRound < POUND_AT && distance > CHARGE_TO) {
            goTo(gorilla, rival, CHARGE_SPEED);
            gorilla.setSprinting(true);
            return;
        }
        gorilla.setSprinting(false);
        if (distance > Silverbacks.FACE_OFF + 1.0)
            goTo(gorilla, rival, APPROACH_SPEED);
        else {
            gorilla.getNavigation().stop();
            plannedTo = null;
        }
        if (myTurn && intoRound == POUND_AT)
            holder.grout$poundChest();
    }

    /**
     * Walk toward the rival, planning a new path only when it's needed. Planning one every tick (as this goal first did) was 20
     * paths a second and 0.59 ms per tick in a trial where two troops met: more than all the troops' other pathing together.
     */
    private void goTo(net.minecraft.world.entity.TamableAnimal gorilla, Entity rival, double speed) {
        long now = gorilla.level().getGameTime();
        if (plannedTo != null && speed == plannedSpeed && !gorilla.getNavigation().isDone() && now - plannedAt < REPLAN_EVERY
            && plannedTo.distanceToSqr(rival.position()) < REPLAN_MOVED * REPLAN_MOVED)
            return;
        plannedAt = now;
        plannedSpeed = speed;
        plannedTo = rival.position();
        gorilla.getNavigation().moveTo(rival, speed);
        var path = gorilla.getNavigation().getPath();
        if (path == null || !path.canReach()) {
            if (++unreachable >= UNREACHABLE_GIVE_UP)
                holder.grout$silverbacks().cantReachRival();
        } else {
            unreachable = 0;
        }
    }
}
