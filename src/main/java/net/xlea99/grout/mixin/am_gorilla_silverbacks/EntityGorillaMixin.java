package net.xlea99.grout.mixin.am_gorilla_silverbacks;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.TamableAnimal;
import net.xlea99.grout.entry.am_gorilla_silverbacks.ShunGoal;
import net.xlea99.grout.entry.am_gorilla_silverbacks.Silverbacks;
import net.xlea99.grout.entry.am_gorilla_silverbacks.StandoffGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Carries each Gorilla's {@link Silverbacks} state, saves it, ticks it after Alex's own tick, and adds the standoff and shun
 * goals above Alex's. A Gorilla keeping away from a winner never joins anyone's line. Targeted by name: Grout isn't built
 * against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public abstract class EntityGorillaMixin implements Silverbacks.Holder {
    @Shadow
    @Final
    public static Animation ANIMATION_POUNDCHEST;
    @Shadow
    public boolean forcedSit;

    @Shadow
    public abstract boolean isSilverback();

    @Shadow
    public abstract void setSilverback(boolean silver);

    @Unique
    private Silverbacks grout$silverbacks;

    /** Made when first asked for, since the goals are registered while this object is still being constructed. */
    @Override
    public Silverbacks grout$silverbacks() {
        if (this.grout$silverbacks == null)
            this.grout$silverbacks = new Silverbacks((TamableAnimal) (Object) this, this);
        return this.grout$silverbacks;
    }

    @Override
    public boolean grout$isSilverback() {
        return this.isSilverback();
    }

    @Override
    public void grout$setSilverback(boolean silverback) {
        this.setSilverback(silverback);
    }

    @Override
    public boolean grout$forcedSit() {
        return this.forcedSit;
    }

    /** Through Citadel's handler, so watching players see it; only when it isn't already in an animation. */
    @Override
    public void grout$poundChest() {
        IAnimatedEntity animated = (IAnimatedEntity) this;
        if (animated.getAnimation() == IAnimatedEntity.NO_ANIMATION)
            AnimationHandler.INSTANCE.sendAnimationMessage((TamableAnimal & IAnimatedEntity) (Object) this, ANIMATION_POUNDCHEST);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void grout$goals(CallbackInfo ci) {
        TamableAnimal self = (TamableAnimal) (Object) this;
        self.goalSelector.addGoal(1, new StandoffGoal(this));
        self.goalSelector.addGoal(1, new ShunGoal(this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$tick(CallbackInfo ci) {
        if (!((TamableAnimal) (Object) this).level().isClientSide)
            this.grout$silverbacks().tick();
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void grout$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!((TamableAnimal) (Object) this).level().isClientSide)
            this.grout$silverbacks().onHurt(source, cir.getReturnValueZ());
    }

    @Inject(method = "joinCaravan", at = @At("HEAD"), cancellable = true)
    private void grout$noLineWhileShunning(CallbackInfo ci) {
        if (this.grout$silverbacks().shunning())
            ci.cancel();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void grout$save(CompoundTag tag, CallbackInfo ci) {
        this.grout$silverbacks().save(tag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void grout$load(CompoundTag tag, CallbackInfo ci) {
        this.grout$silverbacks().load(tag);
    }
}
