package net.xlea99.lifeguard.mixin.am_slightly_sexier_gorilla;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.xlea99.lifeguard.entry.am_slightly_sexier_gorilla.GorillaMarch;
import net.xlea99.lifeguard.entry.am_slightly_sexier_gorilla.MarchGoal;
import net.xlea99.lifeguard.entry.am_slightly_sexier_gorilla.TroopFollowGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Carries each Gorilla's {@link GorillaMarch}, saves it, ticks it after Alex's own tick, and adds the march goal. Targeted by
 * name: Lifeguard isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public abstract class EntityGorillaMixin implements GorillaMarch.Holder {
    @Shadow
    public boolean forcedSit;

    @Shadow
    public abstract boolean isSilverback();

    @Shadow
    public abstract boolean isSitting();

    @Shadow
    public abstract boolean isEating();

    @Unique
    private GorillaMarch lifeguard$march;

    /** Made when first asked for, since the goals are registered while this object is still being constructed. */
    @Override
    public GorillaMarch lifeguard$march() {
        if (this.lifeguard$march == null)
            this.lifeguard$march = new GorillaMarch((TamableAnimal) (Object) this, this);
        return this.lifeguard$march;
    }

    @Override
    public boolean lifeguard$isSilverback() {
        return this.isSilverback();
    }

    @Override
    public boolean lifeguard$isSitting() {
        return this.isSitting();
    }

    @Override
    public boolean lifeguard$isEating() {
        return this.isEating();
    }

    @Override
    public boolean lifeguard$forcedSit() {
        return this.forcedSit;
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void lifeguard$goals(CallbackInfo ci) {
        TamableAnimal self = (TamableAnimal) (Object) this;
        self.goalSelector.addGoal(MarchGoal.PRIORITY, new MarchGoal(this));
        // Alex's single-file line gives way to the loose troop.
        self.goalSelector.removeAllGoals(goal -> goal.getClass().getSimpleName().equals("GorillaAIFollowCaravan"));
        self.goalSelector.addGoal(TroopFollowGoal.PRIORITY, new TroopFollowGoal(self, this));
        // An angry Gorilla closes in fast: Alex's 1.2 is about 3.9 blocks a second, slower than a player walks (4.3). 1.6 is about
        // 6.9, Alex's own bluff charge, faster than a sprint (5.6): you don't outrun one (the user: "gorillas are fast as hell").
        self.goalSelector.removeAllGoals(goal -> goal instanceof MeleeAttackGoal);
        self.goalSelector.addGoal(2, new MeleeAttackGoal(self, lifeguard$ANGRY_SPEED, true));
    }

    @Unique
    private static final double lifeguard$ANGRY_SPEED = 1.6;

    @Inject(method = "tick", at = @At("TAIL"))
    private void lifeguard$tick(CallbackInfo ci) {
        if (!((TamableAnimal) (Object) this).level().isClientSide)
            this.lifeguard$march().tick();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void lifeguard$save(CompoundTag tag, CallbackInfo ci) {
        this.lifeguard$march().save(tag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void lifeguard$load(CompoundTag tag, CallbackInfo ci) {
        this.lifeguard$march().load(tag);
    }
}
