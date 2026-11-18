package net.xlea99.lifeguard.mixin.am_crocodile_ambush;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.xlea99.lifeguard.entry.am_crocodile_ambush.CrocodileAmbush;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Swaps Alex's Mobs' goals, and quiets its rhythm down. Targeted by name: Lifeguard isn't built against Alex's Mobs.
 *
 * <p>Two things here are bugs as well as restlessness (ISSUES #173). A Crocodile hit while basking kept its positive basking timer, which only goes
 * down while it's sitting, so it could never bask or head for water again. And one on land with its swim timer past -1000, where every new one starts,
 * sat and stood up in the same tick, so that it could never bask until it had been in water.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrocodile")
public abstract class EntityCrocodileMixin implements CrocodileAmbush.Holder {
    /** One tick in this many counts toward going between land and water, for about five minutes each way instead of eighty seconds. */
    @Unique
    private static final int LIFEGUARD_SWIM_TIMER_SLOWER = 4;
    /** Where the swim timer goes when it's been scared into the water: it stays until it's counted all the way back up. */
    @Unique
    private static final int LIFEGUARD_STAY_IN_WATER = -1000;
    /** How long it stays up between basks: least and most, in ticks, against Alex's Mobs' 2000 to 2750. Long enough to reach water if it wants to. */
    @Unique
    private static final int LIFEGUARD_UP_LEAST = 200;
    @Unique
    private static final int LIFEGUARD_UP_MOST = 600;

    @Shadow private int swimTimer;
    @Shadow private int baskingTimer;

    @Shadow public abstract boolean isSitting();

    @Unique
    private CrocodileAmbush lifeguard$ambush;
    @Unique
    private int lifeguard$swimTimerBefore;

    /** Made when first asked for, since the goals are registered while this object is still being constructed. */
    @Override
    public CrocodileAmbush lifeguard$ambush() {
        if (this.lifeguard$ambush == null)
            this.lifeguard$ambush = new CrocodileAmbush((TamableAnimal) (Object) this);
        return this.lifeguard$ambush;
    }

    @Override
    public boolean lifeguard$sitting() {
        return this.isSitting();
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void lifeguard$goals(CallbackInfo ci) {
        CrocodileAmbush.rework((TamableAnimal) (Object) this, this.lifeguard$ambush());
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void lifeguard$beforeTick(CallbackInfo ci) {
        this.lifeguard$swimTimerBefore = this.swimTimer;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void lifeguard$afterTick(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide)
            return;
        CrocodileAmbush ambush = this.lifeguard$ambush();
        if (self.tickCount % LIFEGUARD_SWIM_TIMER_SLOWER != 0)
            this.swimTimer = this.lifeguard$swimTimerBefore;
        if (ambush.takeStayInWater())
            this.swimTimer = LIFEGUARD_STAY_IN_WATER;
        boolean upTooLong = this.baskingTimer < -LIFEGUARD_UP_MOST;
        boolean stuck = this.baskingTimer > 0 && !this.isSitting();
        if (upTooLong || stuck)
            this.baskingTimer = -Mth.nextInt(self.getRandom(), LIFEGUARD_UP_LEAST, LIFEGUARD_UP_MOST);
        else if (ambush.busy() && this.baskingTimer == 0)
            this.baskingTimer = -LIFEGUARD_UP_LEAST;
        ambush.tick();
    }

    /** The only other thing this constant is used for is where a new one's swim timer starts, which is in its constructor. */
    @ModifyConstant(method = "tick", constant = @Constant(intValue = -1000))
    private int lifeguard$dontGetUpForWater(int getUpBelow) {
        return Integer.MIN_VALUE;
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void lifeguard$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && !((LivingEntity) (Object) this).level().isClientSide)
            this.lifeguard$ambush().onHurt(source);
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void lifeguard$quiet(CallbackInfoReturnable<SoundEvent> cir) {
        if (!((LivingEntity) (Object) this).isBaby() && this.lifeguard$ambush().quiet())
            cir.setReturnValue(null);
    }
}
