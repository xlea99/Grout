package net.xlea99.grout.entry.am_gorilla_silverbacks;

import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.phys.AABB;

/**
 * One Gorilla's part in its troop's leadership: rising to Silverback, the fresh window after rising, rivalries and grudges.
 * Server only; made by the Gorilla mixin.
 *
 * <p>Rising: a grown, non-Silverback Gorilla with no SETTLED Silverback (one past its fresh window) within the troop's reach
 * counts down a timer, rolled when it first finds itself leaderless and dropped when a settled one turns up. The first to
 * finish rises and is fresh for a short while, and anyone else who finishes inside that window rises too: a succession
 * crisis. All timers run at once, so crises are rarer in small troops and more common in big ones: about 16% with 2 grown
 * Gorillas, 23% with 3, 35% with 5 and 45% with 7 (the chance the first two of n timers spread over {@code PROMOTE_MOST -
 * PROMOTE_LEAST} land within {@code FRESH} of each other, 1 - (1 - FRESH/spread)^n).
 *
 * <p>Rivalry: any two grown Silverbacks within {@code RIVAL_RANGE} that aren't both tamed by the same player. One of the pair
 * (the lower UUID) drives it and rolls all its dice, so each moment is decided once. They close in, then take turns pounding
 * their chests, sometimes charging first. Each round, one may back down; after {@code ROUNDS} rounds with nobody yielding, they
 * fight for real. In a fight, each hit that leaves one below {@code LOW_HEALTH} may make it give up. Giving up: it loses its
 * silver, Alex's code would heal it full for that so its health is put back, and it shuns the winner. The winner is settled.
 */
public final class Silverbacks {
    /** The troop's reach for "is there a Silverback here", across and up or down, as Alex's {@code getNearestSilverback} uses. */
    static final double TROOP_RANGE = 32.0;
    static final double TROOP_HEIGHT = 16.0;
    /** A leaderless Gorilla's timer before it rises, least and most, in ticks: 90 to 330 seconds. */
    static final int PROMOTE_LEAST = 90 * 20;
    static final int PROMOTE_MOST = 330 * 20;
    /** How long a new Silverback is fresh, in ticks: another can still rise while it is. */
    static final int FRESH = 20 * 20;
    /** How close two Silverbacks have to be to notice each other. */
    static final double RIVAL_RANGE = 24.0;
    /** How close they stand in a standoff, and the reach that counts as having closed in. */
    static final double FACE_OFF = 5.0;
    static final double CLOSED_IN = 7.0;
    /** One round of the standoff, in ticks: one chest-pound. */
    static final int ROUND = 60;
    /** Rounds before a standoff nobody backs out of turns into a fight. */
    static final int ROUNDS = 6;
    /** Each round, the chance one of them backs down: about 4 in 5 standoffs end without a fight. */
    static final float YIELD_PER_ROUND = 0.24F;
    /** How much likelier a fresh Silverback is to back down than a settled one. */
    static final float FRESH_YIELDS_MORE = 1.5F;
    /** The chance the one whose turn it is charges before it pounds. */
    static final float CHARGE_CHANCE = 0.35F;
    /** A standoff whose pair never closed in after this many ticks is dropped. */
    static final int STANDOFF_GIVE_UP = 600;
    /** A fight with no result after this many ticks is dropped. */
    static final int FIGHT_GIVE_UP = 1200;
    /**
     * Below this share of its health, each hit from its rival may make a fighter give up. Silverbacks hit for 10 against 50
     * health, so at half there are two chances (at 20 and at 10) before the killing blow: about 1 fight in 4 ends in death.
     * At 0.3 there was one chance, and half the fights in a 24-pair trial ended in death.
     */
    static final float LOW_HEALTH = 0.5F;
    static final float SURRENDER_WHEN_LOW = 0.5F;
    /** How long a loser keeps away from the one who beat it, least and most, in ticks: one to two in-game days. */
    static final int SHUN_LEAST = 24000;
    static final int SHUN_MOST = 48000;
    /** How near the winner has to be before the loser walks off. */
    static final double SHUN_RANGE = 32.0;
    /** Ticks after a dropped rivalry before a Silverback looks for another. */
    static final int PAIR_COOLDOWN = 600;
    /** Ticks after a rivalry dropped because the rival couldn't be reached before either looks for a rival again. */
    static final int UNREACHABLE_COOLDOWN = 2400;
    /** Ticks after a rivalry ends in which a hit between the two still doesn't call the troop. */
    static final int ALERT_GRACE = 100;

    /** The Gorilla mixin, so this code can reach Alex's members without being built against Alex's Mobs. */
    public interface Holder {
        Silverbacks grout$silverbacks();

        boolean grout$isSilverback();

        void grout$setSilverback(boolean silverback);

        boolean grout$forcedSit();

        void grout$poundChest();
    }

    enum Phase { NONE, STANDOFF, FIGHT }

    final TamableAnimal gorilla;
    private final Holder holder;
    private int promoteIn = -1;
    int freshFor;
    UUID shunned;
    private int shunFor;
    Phase phase = Phase.NONE;
    UUID rival;
    boolean driver;
    int phaseTicks;
    /** Ticks spent closed in, set by the driver on both: the standoff's clock. */
    int closeTicks;
    /** Whether the one whose turn it is charges this round, set by the driver on both. */
    boolean chargeThisRound;
    private int pairCooldown;
    private UUID lastRival;
    private long rivalryEnded;
    private float healthToKeep = -1F;

    public Silverbacks(TamableAnimal gorilla, Holder holder) {
        this.gorilla = gorilla;
        this.holder = holder;
    }

    public void save(CompoundTag tag) {
        CompoundTag mine = new CompoundTag();
        mine.putInt("PromoteIn", promoteIn);
        mine.putInt("FreshFor", freshFor);
        if (shunned != null) {
            mine.putUUID("Shunned", shunned);
            mine.putInt("ShunFor", shunFor);
        }
        tag.put("GroutSilverbacks", mine);
    }

    public void load(CompoundTag tag) {
        if (!tag.contains("GroutSilverbacks"))
            return;
        CompoundTag mine = tag.getCompound("GroutSilverbacks");
        promoteIn = mine.getInt("PromoteIn");
        freshFor = mine.getInt("FreshFor");
        if (mine.hasUUID("Shunned")) {
            shunned = mine.getUUID("Shunned");
            shunFor = mine.getInt("ShunFor");
        }
    }

    /** Server, at the end of the Gorilla's own tick (after Alex's code has applied any change of silver). */
    public void tick() {
        if (healthToKeep >= 0F && gorilla.getMaxHealth() < 50F) {
            gorilla.setHealth(Math.min(healthToKeep, gorilla.getMaxHealth()));
            healthToKeep = -1F;
        }
        if (freshFor > 0)
            freshFor--;
        if (shunFor > 0 && --shunFor == 0)
            shunned = null;
        if (pairCooldown > 0)
            pairCooldown--;
        if (phase != Phase.NONE) {
            phaseTicks++;
            if (driver)
                driveRivalry();
            else if (phase == Phase.FIGHT && rivalEntity() instanceof LivingEntity other)
                gorilla.setTarget(other);
            return;
        }
        if ((gorilla.tickCount + gorilla.getId()) % 20 != 0)
            return;
        if (gorilla.isBaby()) {
            promoteIn = -1;
            return;
        }
        if (holder.grout$isSilverback()) {
            promoteIn = -1;
            lookForRival();
        } else {
            countTowardRising();
        }
    }

    /** Server, after Alex's {@code hurt} answered. */
    public void onHurt(DamageSource source, boolean hurt) {
        if (!hurt || phase != Phase.FIGHT)
            return;
        Entity other = rivalEntity();
        if (gorilla.isDeadOrDying()) {
            if (other instanceof TamableAnimal winner && winner instanceof Holder winnerHolder)
                settle(winner, winnerHolder.grout$silverbacks(), this);
            else
                end();
            return;
        }
        Entity attacker = source.getEntity();
        if (attacker != null && attacker.getUUID().equals(rival) && other instanceof TamableAnimal winner && winner instanceof Holder winnerHolder
            && gorilla.getHealth() < gorilla.getMaxHealth() * LOW_HEALTH && gorilla.getRandom().nextFloat() < SURRENDER_WHEN_LOW)
            yieldTo(this, winnerHolder.grout$silverbacks());
    }

    /** Whether a hit from {@code attacker} is part of a rivalry, so it mustn't set the troop on anyone. */
    public boolean keepsFightPrivate(LivingEntity attacker) {
        if (attacker == null)
            return false;
        UUID id = attacker.getUUID();
        return id.equals(rival) || id.equals(lastRival) && gorilla.level().getGameTime() - rivalryEnded < ALERT_GRACE;
    }

    /** Whether it's keeping away from someone right now: it mustn't join anyone's line meanwhile. */
    public boolean shunning() {
        return shunned != null;
    }

    /** Whether it's keeping away from this Gorilla, the one that beat it. */
    public boolean shuns(Entity other) {
        return shunned != null && shunned.equals(other.getUUID());
    }

    /** The rival can't be reached (the terrain between them): drop the rivalry on both sides, and look for no rival for a while. */
    void cantReachRival() {
        if (phase == Phase.NONE)
            return;
        if (rivalEntity() instanceof Holder them) {
            them.grout$silverbacks().end();
            them.grout$silverbacks().pairCooldown = UNREACHABLE_COOLDOWN;
        }
        end();
        pairCooldown = UNREACHABLE_COOLDOWN;
    }

    Entity rivalEntity() {
        return rival != null && gorilla.level() instanceof ServerLevel level ? level.getEntity(rival) : null;
    }

    Entity shunnedEntity() {
        return shunned != null && gorilla.level() instanceof ServerLevel level ? level.getEntity(shunned) : null;
    }

    private void countTowardRising() {
        if (phase != Phase.NONE)
            return;
        AABB troop = gorilla.getBoundingBox().inflate(TROOP_RANGE, TROOP_HEIGHT, TROOP_RANGE);
        boolean led = !gorilla.level().getEntitiesOfClass(TamableAnimal.class, troop, other -> other != gorilla && other.isAlive()
            && !other.isBaby() && other instanceof Holder h && h.grout$isSilverback() && h.grout$silverbacks().freshFor == 0).isEmpty();
        if (led) {
            promoteIn = -1;
            return;
        }
        if (promoteIn < 0) {
            promoteIn = Mth.nextInt(gorilla.getRandom(), PROMOTE_LEAST, PROMOTE_MOST);
            return;
        }
        promoteIn -= 20;
        if (promoteIn <= 0) {
            promoteIn = -1;
            freshFor = FRESH;
            holder.grout$setSilverback(true);
            holder.grout$poundChest();
        }
    }

    private void lookForRival() {
        if (pairCooldown > 0 || holder.grout$forcedSit())
            return;
        AABB near = gorilla.getBoundingBox().inflate(RIVAL_RANGE, RIVAL_RANGE / 2, RIVAL_RANGE);
        List<TamableAnimal> rivals = gorilla.level().getEntitiesOfClass(TamableAnimal.class, near, other -> other != gorilla
            && other.isAlive() && !other.isBaby() && other instanceof Holder h && h.grout$isSilverback() && !h.grout$forcedSit()
            && h.grout$silverbacks().phase == Phase.NONE && h.grout$silverbacks().pairCooldown == 0 && !sameKeeper(other)
            && !other.getUUID().equals(shunned) && !gorilla.getUUID().equals(h.grout$silverbacks().shunned));
        TamableAnimal nearest = null;
        for (TamableAnimal other : rivals)
            if (nearest == null || gorilla.distanceToSqr(other) < gorilla.distanceToSqr(nearest))
                nearest = other;
        if (nearest != null)
            begin(((Holder) nearest).grout$silverbacks());
    }

    private boolean sameKeeper(TamableAnimal other) {
        return gorilla.isTame() && other.isTame() && gorilla.getOwnerUUID() != null && gorilla.getOwnerUUID().equals(other.getOwnerUUID());
    }

    private void begin(Silverbacks other) {
        boolean leads = gorilla.getUUID().compareTo(other.gorilla.getUUID()) < 0;
        start(this, other.gorilla.getUUID(), leads);
        start(other, gorilla.getUUID(), !leads);
    }

    private static void start(Silverbacks side, UUID rival, boolean driver) {
        side.phase = Phase.STANDOFF;
        side.rival = rival;
        side.driver = driver;
        side.phaseTicks = 0;
        side.closeTicks = 0;
        side.chargeThisRound = false;
        side.gorilla.setOrderedToSit(false);
    }

    private void driveRivalry() {
        Entity entity = rivalEntity();
        if (!(entity instanceof TamableAnimal other) || !(other instanceof Holder otherHolder) || !other.isAlive()) {
            end();
            return;
        }
        Silverbacks them = otherHolder.grout$silverbacks();
        if (them.phase != phase || !gorilla.getUUID().equals(them.rival)) {
            end();
            return;
        }
        if (phase == Phase.FIGHT) {
            gorilla.setTarget(other);
            other.setTarget(gorilla);
            if (phaseTicks > FIGHT_GIVE_UP) {
                end();
                them.end();
            }
            return;
        }
        if (gorilla.distanceTo(other) > CLOSED_IN) {
            if (phaseTicks > STANDOFF_GIVE_UP) {
                end();
                them.end();
            }
            return;
        }
        RandomSource random = gorilla.getRandom();
        if (closeTicks % ROUND == 0) {
            boolean charge = random.nextFloat() < CHARGE_CHANCE;
            chargeThisRound = charge;
            them.chargeThisRound = charge;
        }
        closeTicks++;
        them.closeTicks = closeTicks;
        if (closeTicks % ROUND != 0)
            return;
        if (random.nextFloat() < YIELD_PER_ROUND) {
            float mine = yieldWeight(this);
            float theirs = yieldWeight(them);
            if (random.nextFloat() * (mine + theirs) < mine)
                yieldTo(this, them);
            else
                yieldTo(them, this);
        } else if (closeTicks / ROUND >= ROUNDS) {
            phase = Phase.FIGHT;
            them.phase = Phase.FIGHT;
            phaseTicks = 0;
            them.phaseTicks = 0;
            gorilla.setTarget(other);
            other.setTarget(gorilla);
        }
    }

    /** The more hurt, and the fresher, the likelier to back down. */
    private static float yieldWeight(Silverbacks side) {
        float weight = 1.5F - side.gorilla.getHealth() / side.gorilla.getMaxHealth();
        return side.freshFor > 0 ? weight * FRESH_YIELDS_MORE : weight;
    }

    private static void yieldTo(Silverbacks loser, Silverbacks winner) {
        loser.healthToKeep = loser.gorilla.getHealth();
        loser.holder.grout$setSilverback(false);
        loser.freshFor = 0;
        loser.promoteIn = -1;
        loser.shunned = winner.gorilla.getUUID();
        loser.shunFor = Mth.nextInt(loser.gorilla.getRandom(), SHUN_LEAST, SHUN_MOST);
        loser.end();
        settle(winner.gorilla, winner, loser);
    }

    private static void settle(TamableAnimal winnerGorilla, Silverbacks winner, Silverbacks loser) {
        winner.freshFor = 0;
        winner.end();
        if (loser.phase != Phase.NONE)
            loser.end();
        if (winnerGorilla.isAlive())
            winner.holder.grout$poundChest();
    }

    private void end() {
        if (rival != null) {
            lastRival = rival;
            rivalryEnded = gorilla.level().getGameTime();
        }
        phase = Phase.NONE;
        rival = null;
        driver = false;
        phaseTicks = 0;
        closeTicks = 0;
        pairCooldown = PAIR_COOLDOWN;
        gorilla.setTarget(null);
        // A running target goal keeps its own copy of the target and puts it back when the mob's is cleared (vanilla
        // TargetGoal.canContinueToUse): a trial's surrendered loser re-targeted its rival a tick later and fought on to the
        // death. Stopping them clears that copy.
        for (WrappedGoal goal : gorilla.targetSelector.getAvailableGoals())
            if (goal.isRunning())
                goal.stop();
        gorilla.setLastHurtByMob(null);
        gorilla.getNavigation().stop();
    }
}
