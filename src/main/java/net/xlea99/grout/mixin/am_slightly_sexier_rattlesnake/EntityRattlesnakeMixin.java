package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeRetreatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.RattlesnakeAccess;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeAmbushGoal;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeMind;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakePreyGoal;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeStrikeGoal;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeThreatGoal;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeWarnGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Swaps Alex's chase for the strike, his long-range prey targets for prey in reach, his warning and his short-range target
 * on players for ones that follow how worked up it is ({@link SnakeMind}), and adds the wait. His hurt-by target, breeding,
 * following and wandering stay. Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public abstract class EntityRattlesnakeMixin extends Animal implements RattlesnakeAccess, SnakeMind.Holder {
    @Shadow
    public int maxCurlTime;

    private EntityRattlesnakeMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    /** Alex's Rattlesnake saves nothing of its own; this saves its den ({@link SnakeMind#save}). */
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.grout$mind().save(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.grout$mind().load(tag);
    }

    /** Made on first use: Alex's goals are registered from the constructor, before a field's initializer would run. */
    @Unique
    private SnakeMind grout$mind;

    @Shadow
    public abstract boolean isCurled();

    @Shadow
    public abstract void setCurled(boolean curled);

    @Shadow
    public abstract boolean isRattling();

    @Shadow
    public abstract void setRattling(boolean rattling);

    @Override
    public boolean grout$isCurled() {
        return this.isCurled();
    }

    @Override
    public void grout$setCurled(boolean curled) {
        this.setCurled(curled);
    }

    @Override
    public boolean grout$isRattling() {
        return this.isRattling();
    }

    @Override
    public void grout$setRattling(boolean rattling) {
        this.setRattling(rattling);
    }

    @Override
    public void grout$setMaxCurlTime(int ticks) {
        this.maxCurlTime = ticks;
    }

    @Override
    public SnakeMind grout$mind() {
        if (this.grout$mind == null)
            this.grout$mind = new SnakeMind();
        return this.grout$mind;
    }

    /**
     * How far its head may turn from its body (vanilla's {@code Mob#getMaxHeadYRot} is 75 for every mob; Alex's Rattlesnake
     * keeps it). Coiled, much further, so that it watches with its head and neck while its body stays put; the client draws
     * the turn into the neck.
     */
    public int getMaxHeadYRot() {
        return this.isCurled() ? SnakeMind.COIL_HEAD_REACH : 75;
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void grout$ambushHunter(CallbackInfo ci) {
        PathfinderMob self = (PathfinderMob) (Object) this;
        self.goalSelector.removeAllGoals(goal -> goal instanceof MeleeAttackGoal || goal.getClass().getName().endsWith("$WarnPredatorsGoal"));
        // Alex's two prey goals (rabbits, Jerboas) are exactly this class; his player one is a subclass.
        self.targetSelector.removeAllGoals(goal -> goal.getClass() == NearestAttackableTargetGoal.class
            || goal.getClass().getName().endsWith("$ShortDistanceTarget"));
        self.goalSelector.addGoal(SnakeRetreatGoal.PRIORITY, new SnakeRetreatGoal(self, this, this));
        self.goalSelector.addGoal(SnakeStrikeGoal.PRIORITY, new SnakeStrikeGoal(self));
        self.goalSelector.addGoal(SnakeWarnGoal.PRIORITY, new SnakeWarnGoal(self, this, this));
        self.goalSelector.addGoal(SnakeAmbushGoal.PRIORITY, new SnakeAmbushGoal(self, this, this));
        self.targetSelector.addGoal(SnakePreyGoal.PRIORITY, new SnakePreyGoal(self));
        self.targetSelector.addGoal(SnakeThreatGoal.PRIORITY, new SnakeThreatGoal(self, this, this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$think(CallbackInfo ci) {
        PathfinderMob self = (PathfinderMob) (Object) this;
        if (!self.level().isClientSide)
            this.grout$mind().tick(self);
    }
}
