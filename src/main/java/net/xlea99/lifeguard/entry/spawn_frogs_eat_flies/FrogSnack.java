package net.xlea99.lifeguard.entry.spawn_frogs_eat_flies;

import net.xlea99.lifeguard.compat.SpawnFlies;
import com.ninni.spawn.registry.SpawnSoundEvents;
import com.ninni.spawn.server.gui.fly.FlyData;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.frog.Frog;

/**
 * One frog going for a fly ({@link SpawnFrogsEatFlies}). It runs beside the frog's brain, not in it, with nothing there changed: vanilla's own tongue is a fixed
 * animation set off by a pose, with no aiming, so all this has to do is walk the frog into reach with the walk memory vanilla's tongue uses, face it the right way,
 * set the target clients read for how far to draw the tongue, and set the pose. It stands aside for anything the brain is doing that matters, and a frog that's disturbed just loses interest for a bit.
 */
public final class FrogSnack {
    /** Sent to clients when the tongue goes: nothing vanilla uses it. Clients without Lifeguard ignore it. */
    public static final byte FLY_ON_TONGUE_EVENT = 98;

    /** How far it notices a carrier, and how often it looks, in blocks and ticks. */
    private static final double RANGE = 8D;
    private static final int LOOK_EVERY = 40;
    /** Between snacks, in ticks: 15 to 30 seconds. */
    private static final int REST_LEAST = 300;
    private static final int REST_MOST = 600;
    /** After giving up on one, in ticks. */
    private static final int GAVE_UP_REST = 200;
    /** How long it'll hop after a carrier, in ticks, how fast against its walk, and how often it finds the way again. */
    private static final int HOP_TICKS = 100;
    private static final float HOP_SPEED = 2F;
    private static final int HOP_REPATH = 10;
    /** How far off it strikes from, in blocks. The tongue is drawn to whatever length gets it there: {@code FlyOnTongueLayer}. */
    private static final float STRIKE_FROM = 4.5F;
    /** How long it sits there staring before it goes, in ticks, and how far the carrier can get before it's missed its chance. */
    private static final int STARE_LEAST = 10;
    private static final int STARE_MOST = 60;
    private static final float STARE_MOST_AWAY = 6F;
    private static final float STARE_TURN = 20F;
    /** As vanilla's tongue does, it won't go mid-croak, since the end of the croak would cut it off. It waits one out, up to this long in all, in ticks: a croak is 60. */
    private static final int STARE_LONGEST = 140;
    /** Vanilla's timing for its tongue: the tick the prey goes, and the tick it's over. */
    private static final int TONGUE_EAT = 6;
    private static final int TONGUE_OVER = 10;

    private enum Phase { WAITING, HOPPING, STARING, TONGUE }

    private final Frog frog;
    private Phase phase = Phase.WAITING;
    private long nextLook;
    private int ticks;
    private int stareFor;
    private LivingEntity carrier;

    public FrogSnack(Frog frog) {
        this.frog = frog;
    }

    public void tick() {
        long now = this.frog.level().getGameTime();
        if (this.phase == Phase.WAITING) {
            if (now >= this.nextLook) {
                this.nextLook = now + LOOK_EVERY;
                if (!busy() && this.frog.getPose() == Pose.STANDING)
                    look();
            }
            return;
        }
        if (busy() || this.carrier == null || !this.carrier.isAlive() || this.carrier.isRemoved() || this.carrier.level() != this.frog.level()) {
            finish(GAVE_UP_REST);
            return;
        }
        // Don't go jumping off in the middle of this.
        this.frog.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, 40);
        switch (this.phase) {
            case HOPPING -> hop();
            case STARING -> stare();
            case TONGUE -> tongue();
            default -> {
            }
        }
    }

    private void look() {
        double nearest = Double.MAX_VALUE;
        for (LivingEntity entity : this.frog.level().getEntitiesOfClass(LivingEntity.class, this.frog.getBoundingBox().inflate(RANGE), EntitySelector.NO_SPECTATORS)) {
            if (entity == this.frog || !entity.isAlive() || edibleFlies(entity).isEmpty())
                continue;
            double distance = this.frog.distanceToSqr(entity);
            if (distance < nearest && distance <= RANGE * RANGE && this.frog.hasLineOfSight(entity)) {
                nearest = distance;
                this.carrier = entity;
            }
        }
        if (this.carrier != null) {
            this.phase = Phase.HOPPING;
            this.ticks = 0;
        }
    }

    private void hop() {
        if (this.frog.distanceTo(this.carrier) <= STRIKE_FROM && this.frog.hasLineOfSight(this.carrier)) {
            this.phase = Phase.STARING;
            this.ticks = 0;
            this.stareFor = Mth.nextInt(this.frog.getRandom(), STARE_LEAST, STARE_MOST);
            return;
        }
        if (this.ticks >= HOP_TICKS) {
            finish(GAVE_UP_REST);
            return;
        }
        if (this.ticks++ % HOP_REPATH == 0) {
            this.frog.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(this.carrier, HOP_SPEED, 0));
            BehaviorUtils.lookAtEntity(this.frog, this.carrier);
        }
    }

    private void stare() {
        holdStill();
        face();
        if (this.frog.distanceTo(this.carrier) > STARE_MOST_AWAY || !this.frog.hasLineOfSight(this.carrier) || edibleFlies(this.carrier).isEmpty()) {
            finish(GAVE_UP_REST);
            return;
        }
        if (++this.ticks > STARE_LONGEST) {
            finish(GAVE_UP_REST);
            return;
        }
        if (this.ticks >= this.stareFor && this.frog.getPose() == Pose.STANDING) {
            this.phase = Phase.TONGUE;
            this.ticks = 0;
            this.frog.setTongueTarget(this.carrier);
            this.frog.setPose(Pose.USING_TONGUE);
            this.frog.level().broadcastEntityEvent(this.frog, FLY_ON_TONGUE_EVENT);
            this.frog.level().playSound(null, this.frog, SoundEvents.FROG_TONGUE, SoundSource.NEUTRAL, 2F, 1F);
        }
    }

    private void tongue() {
        holdStill();
        this.ticks++;
        if (this.ticks == TONGUE_EAT) {
            eatOne();
            this.frog.level().playSound(null, this.frog, SoundEvents.FROG_EAT, SoundSource.NEUTRAL, 2F, 1F);
        }
        if (this.ticks >= TONGUE_OVER)
            finish(Mth.nextInt(this.frog.getRandom(), REST_LEAST, REST_MOST));
    }

    private void eatOne() {
        if (SpawnFlies.eatOne(this.carrier, this.frog.getRandom()) && this.carrier instanceof ServerPlayer player)
            player.playNotifySound(SpawnSoundEvents.FLY_DEATH.get(), SoundSource.PLAYERS, 1F, 1F);
    }

    private static List<FlyData> edibleFlies(LivingEntity entity) {
        return SpawnFlies.edible(entity);
    }

    private void holdStill() {
        this.frog.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.frog.getNavigation().stop();
    }

    /** Its tongue goes where its body points, which won't otherwise turn while it's sitting still. */
    private void face() {
        float wanted = (float) (Mth.atan2(this.carrier.getZ() - this.frog.getZ(), this.carrier.getX() - this.frog.getX()) * Mth.RAD_TO_DEG) - 90F;
        float yaw = Mth.approachDegrees(this.frog.getYRot(), wanted, STARE_TURN);
        this.frog.setYRot(yaw);
        this.frog.yBodyRot = yaw;
        this.frog.yHeadRot = yaw;
    }

    private void finish(int rest) {
        if (this.phase == Phase.TONGUE) {
            this.frog.eraseTongueTarget();
            if (this.frog.getPose() == Pose.USING_TONGUE)
                this.frog.setPose(Pose.STANDING);
        }
        if (this.phase == Phase.HOPPING || this.phase == Phase.STARING)
            holdStill();
        this.phase = Phase.WAITING;
        this.carrier = null;
        this.nextLook = this.frog.level().getGameTime() + rest;
    }

    /** What its brain is doing that this should stay out of: hunting its real food, panicking, breeding, laying spawn, or in the air. */
    private boolean busy() {
        Brain<Frog> brain = this.frog.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) || brain.hasMemoryValue(MemoryModuleType.IS_PANICKING) || brain.hasMemoryValue(MemoryModuleType.BREED_TARGET)
            || brain.hasMemoryValue(MemoryModuleType.IS_PREGNANT) || brain.hasMemoryValue(MemoryModuleType.LONG_JUMP_MID_JUMP) || this.frog.isInLove();
    }

    public boolean active() {
        return this.phase != Phase.WAITING;
    }

    /** What every frog is made to have, on the server. */
    public interface Snacker {
        boolean lifeguard$snacking();
    }

    /** What every frog is made to have, on the client. */
    public interface Holder {
        float lifeguard$flyOnTongueAt();
    }
}
