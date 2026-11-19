package net.xlea99.grout.entry.am_crocodile_ambush;

import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * One grown Crocodile's ambush ({@link AmCrocodileAmbush}): how long it's resting, what it's running from, and everything it does each tick while
 * nothing's happening. {@link CrocodileNoticeGoal} decides when it strikes and {@link CrocodileStrikeGoal} is the strike.
 */
public final class CrocodileAmbush {
    /** The most a miss can leave it unwilling to chase again, in ticks: least and most. */
    private static final int REST_LEAST = 600;
    private static final int REST_MOST = 1200;
    private static final int TAME_REST_LEAST = 100;
    private static final int TAME_REST_MOST = 200;
    /** How long it stays down after diving away from something, in ticks. */
    private static final int HIDE_TICKS = 400;
    /** The chance a wild one that's hit and able to fight does, instead of running. */
    private static final float FIGHTS_BACK = 1F / 3F;
    /** Sent to clients when a strike misses: nothing vanilla uses it. Clients without Grout ignore it. */
    public static final byte MISSED_EVENT = 97;

    /** Its speed by what it's doing. Speed goes by about its square. Alex's Mobs' own for babies, swimming and hauling. */
    private static final double LAND_WANDER = 0.16D;
    private static final double BABY = 0.25D;
    private static final double SWIM = 0.25D;
    private static final double HAUL = 0.28D;
    /** So that it walks up a block rather than stopping to hop, and so that a charge doesn't stop at one. */
    private static final double STEP_HEIGHT = 1.1D;
    /** Where the surface sits on its body when it's floating, as height above its feet: eyes out, like a log. */
    private static final double FLOAT_AT = 0.55D;
    private static final double FLOAT_RATE = 0.15D;
    private static final double FLOAT_RISE = 0.04D;
    private static final double FLOAT_SINK = 0.03D;

    private final TamableAnimal mob;
    private long restUntil;
    private long hideUntil;
    private LivingEntity runFrom;
    private boolean stayInWater;
    private boolean busy;
    private boolean committed;

    public CrocodileAmbush(TamableAnimal mob) {
        this.mob = mob;
    }

    /** Takes out what kept it fidgeting and chasing, and puts these in. */
    public static void rework(TamableAnimal mob, CrocodileAmbush ambush) {
        GoalSelector goals = mob.goalSelector;
        goals.removeAllGoals(goal -> goal instanceof MeleeAttackGoal || goal instanceof RandomLookAroundGoal || goal instanceof LookAtPlayerGoal);
        goals.addGoal(0, new CrocodileStrikeGoal(mob, ambush));
        goals.addGoal(6, new RandomLookAroundGoal(mob) {
            @Override
            public boolean canUse() {
                return mob.isBaby() && super.canUse();
            }
        });
        goals.addGoal(7, new LookAtPlayerGoal(mob, Player.class, 6F) {
            @Override
            public boolean canUse() {
                return mob.isBaby() && super.canUse();
            }
        });
        mob.targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
        mob.targetSelector.addGoal(4, new CrocodileNoticeGoal(mob, ambush));
    }

    /** What goes on every tick, on the server. */
    public void tick() {
        set(Attributes.STEP_HEIGHT, STEP_HEIGHT);
        set(Attributes.MOVEMENT_SPEED, this.mob.isBaby() ? BABY : !this.mob.getPassengers().isEmpty() ? HAUL : this.mob.isInWater() ? SWIM : LAND_WANDER);
        if (this.mob.isBaby() || this.busy || !this.mob.isInWater() || this.mob.getTarget() != null || !this.mob.getNavigation().isDone()
            || !this.mob.getPassengers().isEmpty() || this.mob.level().getGameTime() < this.hideUntil)
            return;
        Vec3 motion = this.mob.getDeltaMovement();
        double sunk = this.mob.getFluidHeight(FluidTags.WATER) - FLOAT_AT;
        this.mob.setDeltaMovement(motion.x, Mth.clamp(sunk * FLOAT_RATE, -FLOAT_SINK, FLOAT_RISE), motion.z);
    }

    private void set(net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, double value) {
        AttributeInstance instance = this.mob.getAttribute(attribute);
        if (instance != null && instance.getBaseValue() != value)
            instance.setBaseValue(value);
    }

    /** A wild one mostly runs when it's hit. Tame ones and babies are left to Alex's Mobs' own answer. */
    public void onHurt(DamageSource source) {
        if (!(source.getEntity() instanceof LivingEntity attacker) || this.mob.isTame() || this.mob.isBaby() || this.committed || !this.mob.getPassengers().isEmpty())
            return;
        boolean canFight = rested() && this.mob.distanceTo(attacker) <= CrocodileNoticeGoal.RANGE && this.mob.hasLineOfSight(attacker);
        if (!canFight || this.mob.getRandom().nextFloat() >= FIGHTS_BACK)
            this.runFrom = attacker;
    }

    public boolean wantsToRun() {
        return this.runFrom != null;
    }

    public LivingEntity takeRunFrom() {
        LivingEntity from = this.runFrom;
        this.runFrom = null;
        return from;
    }

    public boolean rested() {
        return this.mob.level().getGameTime() >= this.restUntil;
    }

    public void rest() {
        int least = this.mob.isTame() ? TAME_REST_LEAST : REST_LEAST;
        int most = this.mob.isTame() ? TAME_REST_MOST : REST_MOST;
        this.restUntil = this.mob.level().getGameTime() + Mth.nextInt(this.mob.getRandom(), least, most);
    }

    /** It stays down for a while, and in the water for a long while. */
    public void hide() {
        this.hideUntil = this.mob.level().getGameTime() + HIDE_TICKS;
        this.stayInWater = true;
    }

    public boolean takeStayInWater() {
        boolean stay = this.stayInWater;
        this.stayInWater = false;
        return stay;
    }

    /** Stops every target goal that's running, since one that's still running would just put its target back. */
    public void dropTargets() {
        this.mob.targetSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
        this.mob.setTarget(null);
    }

    public boolean busy() {
        return this.busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    /** Whether it's past the point of changing its mind about a strike: being hit doesn't turn it around. */
    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    /** It only makes a sound when it's up to something. */
    public boolean quiet() {
        return !this.busy && rested();
    }

    /** What every Crocodile is made to have. */
    public interface Holder {
        CrocodileAmbush grout$ambush();

        /** Alex's Mobs keeps its own sitting flag, so {@code isOrderedToSit} is always false. */
        boolean grout$sitting();
    }
}
