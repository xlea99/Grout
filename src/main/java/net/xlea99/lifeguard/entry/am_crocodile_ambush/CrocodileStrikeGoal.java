package net.xlea99.lifeguard.entry.am_crocodile_ambush;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Everything a Crocodile does once it's made its mind up, in place of Alex's Mobs' melee goal: the tell, the charge, the lunge, the miss, and running
 * away. It only notices what it can charge, but anything else can hand it a target (being hit near a friend, an owner, another mod), so a target it
 * can't charge yet it walks toward for a while, on a path, until it can. It drives its own velocity and heading rather than going through a path and the speed attribute, so that its speed is exact on land or in
 * water and it runs straight at where it's heading, turning badly, which is what makes sidestepping it work. The bite itself stays Alex's Mobs':
 * this starts the lunge, and that lands in its ticks 6 to 8 if there's still something to land on.
 */
public final class CrocodileStrikeGoal extends Goal {
    private enum Phase { STALK, TELL, CHARGE, LUNGE, MISS, RUN }

    /** Walking toward a target it can't charge yet: how long before it loses interest, how often it finds a new path, and how fast against its calm speed. */
    private static final int STALK_TICKS = 400;
    private static final int STALK_REPATH = 10;
    private static final double STALK_SPEED = 1.75D;

    /** How long it takes to rise before it goes, and how fast it swings to face its target in that time, in degrees a tick. */
    private static final int TELL_TICKS = 8;
    private static final float TELL_TURN = 30F;
    /** How long it can run, how fast in blocks a tick (8 a second), and how quickly it gets there. */
    private static final int CHARGE_TICKS = 50;
    private static final float CHARGE_SPEED = 0.4F;
    private static final float CHARGE_START = 0.15F;
    private static final float CHARGE_RAMP = 0.05F;
    /** How badly it turns when charging: at this speed about a 3 block radius. */
    private static final float CHARGE_TURN = 7F;
    /** How far off dead ahead its target can be for it to throw itself forward, in degrees, and from how far away, in blocks. */
    private static final float LUNGE_AIM = 45F;
    private static final float LUNGE_FROM = 4.4F;
    /** Its lunge: how many ticks it drives for and how fast, off a charge or from standing, how far it turns, and how close it stops. */
    private static final int LUNGE_DRIVE_TICKS = 8;
    private static final float LUNGE_SPEED = 0.45F;
    private static final float SNAP_SPEED = 0.2F;
    private static final float LUNGE_TURN = 3F;
    private static final float LUNGE_STOP = 2F;
    /** Alex's Mobs' bite checks only distance. This takes it out of the bite when it isn't facing what it's biting, unless it's right on top of it. */
    private static final float BITE_ARC = 70F;
    private static final float BITE_ANYWAY = 1.5F;
    /** The tick of the lunge where the bite has had its chance. */
    private static final int LUNGE_OVER = 10;
    /** How long it stands there after a miss. {@code CrocodileGait} closes its mouth over the same time. */
    private static final int MISS_TICKS = 70;
    /** Running away: for how long, how quickly it turns, and how fast it dives once it's in water. */
    private static final int RUN_TICKS = 50;
    private static final float RUN_TURN = 14F;
    private static final double DIVE = 0.06D;
    /** How far it looks for water to run to, in blocks, and how much further it would rather go than past what it's running from. */
    private static final int WATER_SEARCH = 20;
    private static final float PAST_THE_ATTACKER = 15F;
    /** How long it can be stopped by a wall before it gives up, in ticks, and how hard it heaves itself up a bank from the water. */
    private static final int BLOCKED_TICKS = 6;
    private static final double BANK_HEAVE = 0.42D;
    /** How far ahead it looks for a hazard, and how far a drop it won't run off, in blocks. */
    private static final float LOOK_AHEAD = 2F;
    private static final int MOST_DROP = 4;
    /** How fast it can rise or sink toward a target in water. */
    private static final double SWIM_CLIMB = 0.1D;

    private final TamableAnimal mob;
    private final CrocodileAmbush ambush;
    private Phase phase;
    private int ticks;
    private int blocked;
    private LivingEntity target;
    private int hurtStamp;
    private float lungeSpeed;
    private LivingEntity runFrom;
    private BlockPos water;

    public CrocodileStrikeGoal(TamableAnimal mob, CrocodileAmbush ambush) {
        this.mob = mob;
        this.ambush = ambush;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby())
            return false;
        if (this.ambush.wantsToRun())
            return true;
        Animation animation = ((IAnimatedEntity) this.mob).getAnimation();
        return this.mob.getTarget() != null && this.mob.getPassengers().isEmpty() && (animation == null || animation == IAnimatedEntity.NO_ANIMATION);
    }

    @Override
    public boolean canContinueToUse() {
        return this.phase != null && this.mob.getPassengers().isEmpty();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.ambush.setBusy(true);
        this.mob.getNavigation().stop();
        this.phase = null;
        if (this.ambush.wantsToRun()) {
            beginRun();
            return;
        }
        this.target = this.mob.getTarget();
        if (!valid(this.target) || strikeNow())
            return;
        if (mayMove() && this.mob.distanceTo(this.target) <= this.mob.getAttributeValue(Attributes.FOLLOW_RANGE)) {
            this.phase = Phase.STALK;
            this.ticks = 0;
            this.mob.setOrderedToSit(false);
            this.mob.setAggressive(true);
        }
    }

    /** A tame one told to sit stays sitting, and only bites what comes to it. */
    private boolean mayMove() {
        return !(this.mob.isTame() && ((CrocodileAmbush.Holder) this.mob).lifeguard$sitting());
    }

    /** Starts the bite or the charge, if it can make one from here. */
    private boolean strikeNow() {
        if (!this.mob.getSensing().hasLineOfSight(this.target))
            return false;
        float distance = this.mob.distanceTo(this.target);
        if (distance <= CrocodileNoticeGoal.JAW_RANGE) {
            beginLunge(SNAP_SPEED);
            return true;
        }
        if (!this.ambush.rested() || !mayMove() || distance > (this.mob.isTame() ? this.mob.getAttributeValue(Attributes.FOLLOW_RANGE) : CrocodileNoticeGoal.RANGE))
            return false;
        this.phase = Phase.TELL;
        this.ticks = 0;
        this.mob.setOrderedToSit(false);
        this.mob.setAggressive(true);
        this.ambush.setCommitted(true);
        this.mob.getNavigation().stop();
        this.mob.playAmbientSound();
        return true;
    }

    private void stalk() {
        if (!valid(this.target) || this.mob.getTarget() == null || this.mob.distanceTo(this.target) > this.mob.getAttributeValue(Attributes.FOLLOW_RANGE) || this.ticks >= STALK_TICKS) {
            this.mob.getNavigation().stop();
            this.phase = null;
            return;
        }
        if (strikeNow())
            return;
        if (this.ticks++ % STALK_REPATH == 0)
            this.mob.getNavigation().moveTo(this.target, STALK_SPEED);
    }

    @Override
    public void stop() {
        this.ambush.setBusy(false);
        this.ambush.setCommitted(false);
        this.mob.setAggressive(false);
        this.phase = null;
        this.target = null;
        this.runFrom = null;
        this.water = null;
        if (this.mob.getPassengers().isEmpty())
            this.ambush.dropTargets();
    }

    @Override
    public void tick() {
        if (this.phase == null)
            return;
        if (this.ambush.wantsToRun())
            beginRun();
        switch (this.phase) {
            case STALK -> stalk();
            case TELL -> tell();
            case CHARGE -> charge();
            case LUNGE -> lunge();
            case MISS -> miss();
            case RUN -> run();
        }
    }

    private void tell() {
        if (!valid(this.target)) {
            this.phase = null;
            return;
        }
        turnToward(this.target.position(), TELL_TURN);
        if (++this.ticks >= TELL_TICKS) {
            this.phase = Phase.CHARGE;
            this.ticks = 0;
            this.blocked = 0;
            this.ambush.rest();
        }
    }

    private void charge() {
        if (!valid(this.target)) {
            beginMiss();
            return;
        }
        turnToward(this.target.position(), CHARGE_TURN);
        this.ticks++;
        drive(Math.min(CHARGE_SPEED, CHARGE_START + CHARGE_RAMP * this.ticks), this.target.getY() - this.mob.getY());
        if (this.mob.isInWater() && this.mob.horizontalCollision)
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().x, BANK_HEAVE, this.mob.getDeltaMovement().z);
        boolean ranOut = this.ticks >= CHARGE_TICKS || blockedTooLong() || !safeAhead();
        if (ranOut || this.mob.distanceTo(this.target) <= LUNGE_FROM && Math.abs(offAngle(this.target.position())) <= LUNGE_AIM)
            beginLunge(ranOut ? SNAP_SPEED : LUNGE_SPEED);
    }

    private void beginLunge(float speed) {
        this.phase = Phase.LUNGE;
        this.ticks = 0;
        this.lungeSpeed = speed;
        this.hurtStamp = this.target.tickCount;
        this.mob.setAggressive(true);
        this.ambush.setCommitted(true);
        this.mob.getNavigation().stop();
        if (this.ambush.rested())
            this.ambush.rest();
        this.mob.doHurtTarget(this.target);
    }

    private void lunge() {
        if (!this.mob.getPassengers().isEmpty())
            return;
        this.ticks++;
        boolean valid = valid(this.target);
        if (valid)
            turnToward(this.target.position(), LUNGE_TURN);
        float distance = valid ? this.mob.distanceTo(this.target) : Float.MAX_VALUE;
        if (this.ticks <= LUNGE_DRIVE_TICKS && distance > LUNGE_STOP)
            drive(this.lungeSpeed, 0D);
        this.mob.setTarget(valid && (distance <= BITE_ANYWAY || Math.abs(offAngle(this.target.position())) <= BITE_ARC) ? this.target : null);
        if (this.ticks < LUNGE_OVER)
            return;
        if (this.target.getLastHurtByMob() == this.mob && this.target.getLastHurtByMobTimestamp() >= this.hurtStamp)
            this.phase = null;
        else
            beginMiss();
    }

    private void beginMiss() {
        this.phase = Phase.MISS;
        this.ticks = 0;
        this.ambush.setCommitted(false);
        this.ambush.dropTargets();
        this.mob.setAggressive(false);
        this.mob.level().broadcastEntityEvent(this.mob, CrocodileAmbush.MISSED_EVENT);
    }

    private void miss() {
        if (++this.ticks >= MISS_TICKS)
            this.phase = null;
    }

    private void beginRun() {
        this.phase = Phase.RUN;
        this.ticks = 0;
        this.blocked = 0;
        this.mob.getNavigation().stop();
        this.runFrom = this.ambush.takeRunFrom();
        this.ambush.setCommitted(false);
        this.ambush.dropTargets();
        this.mob.setOrderedToSit(false);
        this.mob.setAggressive(false);
        this.ambush.rest();
        this.ambush.hide();
        this.water = this.mob.isInWater() ? null : findWater();
    }

    private void run() {
        if (this.mob.getTarget() != null)
            this.ambush.dropTargets();
        boolean swimming = this.mob.isInWater();
        Vec3 here = this.mob.position();
        if (!swimming && this.water != null)
            turnToward(Vec3.atBottomCenterOf(this.water), RUN_TURN);
        else
            turnToward(here.add(here.subtract(this.runFrom.position())), RUN_TURN);
        this.ticks++;
        float speed = Math.min(CHARGE_SPEED, CHARGE_START + CHARGE_RAMP * this.ticks);
        boolean deeper = this.mob.level().getFluidState(this.mob.blockPosition().below()).is(FluidTags.WATER);
        drive(speed, swimming && deeper ? -DIVE / SWIM_CLIMB : 0D);
        if (this.ticks >= RUN_TICKS || blockedTooLong() || !swimming && !safeAhead())
            this.phase = null;
    }

    /** The nearest water it can get to without going past what it's running from, if it can help it. */
    private BlockPos findWater() {
        Level level = this.mob.level();
        BlockPos here = this.mob.blockPosition();
        double fromAngle = Mth.atan2(this.runFrom.getZ() - this.mob.getZ(), this.runFrom.getX() - this.mob.getX());
        BlockPos best = null;
        float bestScore = Float.MAX_VALUE;
        for (int side = 0; side < 16; side++) {
            double angle = side * Mth.TWO_PI / 16D;
            float past = Math.abs(Mth.wrapDegrees((float) ((angle - fromAngle) * Mth.RAD_TO_DEG))) < 60F ? PAST_THE_ATTACKER : 0F;
            for (int distance = 2; distance <= WATER_SEARCH; distance += 2) {
                if (distance + past >= bestScore)
                    break;
                int x = here.getX() + Mth.floor(Math.cos(angle) * distance);
                int z = here.getZ() + Mth.floor(Math.sin(angle) * distance);
                BlockPos found = null;
                for (int y = here.getY() + 2; y >= here.getY() - MOST_DROP && found == null; y--) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (level.getFluidState(pos).is(FluidTags.WATER))
                        found = pos;
                }
                if (found != null) {
                    best = found;
                    bestScore = distance + past;
                    break;
                }
            }
        }
        return best;
    }

    private boolean valid(LivingEntity entity) {
        return entity != null && entity.isAlive() && this.mob.canAttack(entity)
            && !(entity instanceof Player && !this.mob.isTame() && this.mob.level().getDifficulty() == Difficulty.PEACEFUL);
    }

    /** How far its heading is off the way to a place, in degrees. */
    private float offAngle(Vec3 place) {
        return Mth.wrapDegrees(angleTo(place) - this.mob.getYRot());
    }

    private float angleTo(Vec3 place) {
        return (float) (Mth.atan2(place.z - this.mob.getZ(), place.x - this.mob.getX()) * Mth.RAD_TO_DEG) - 90F;
    }

    private void turnToward(Vec3 place, float most) {
        float yaw = Mth.approachDegrees(this.mob.getYRot(), angleTo(place), most);
        this.mob.setYRot(yaw);
        this.mob.yBodyRot = yaw;
        this.mob.yHeadRot = yaw;
    }

    /** Moves it along its heading at exactly this speed. In water it climbs or sinks toward the given height. */
    private void drive(float speed, double climb) {
        float radians = this.mob.getYRot() * Mth.DEG_TO_RAD;
        double y = this.mob.getDeltaMovement().y;
        if (this.mob.isInWater())
            y = Mth.clamp(climb * SWIM_CLIMB, -SWIM_CLIMB, SWIM_CLIMB);
        this.mob.setDeltaMovement(-Mth.sin(radians) * speed, y, Mth.cos(radians) * speed);
    }

    private boolean blockedTooLong() {
        this.blocked = this.mob.horizontalCollision ? this.blocked + 1 : 0;
        return this.blocked >= BLOCKED_TICKS;
    }

    /** No fire or lava on the way, and no long drop. It'll run into water, or anything else it can stand on. */
    private boolean safeAhead() {
        Level level = this.mob.level();
        float radians = this.mob.getYRot() * Mth.DEG_TO_RAD;
        BlockPos ahead = BlockPos.containing(this.mob.getX() - Mth.sin(radians) * LOOK_AHEAD, this.mob.getY() + 0.5D, this.mob.getZ() + Mth.cos(radians) * LOOK_AHEAD);
        for (int down = 0; down <= MOST_DROP + 1; down++) {
            BlockPos pos = ahead.below(down);
            BlockState state = level.getBlockState(pos);
            if (state.getFluidState().is(FluidTags.LAVA) || state.is(BlockTags.FIRE))
                return false;
            if (state.getFluidState().is(FluidTags.WATER) || !state.getCollisionShape(level, pos).isEmpty())
                return true;
        }
        return false;
    }
}
