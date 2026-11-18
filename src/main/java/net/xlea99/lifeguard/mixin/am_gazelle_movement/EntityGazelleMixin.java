package net.xlea99.lifeguard.mixin.am_gazelle_movement;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.xlea99.lifeguard.entry.am_gazelle_movement.GazelleFlee;
import net.xlea99.lifeguard.entry.am_gazelle_movement.GazelleMoveControl;
import net.xlea99.lifeguard.entry.am_gazelle_movement.GazelleNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGazelle")
public abstract class EntityGazelleMixin extends Animal implements GazelleFlee.Holder {
    /** Its wander's speed, from 1. Speed goes by about its square, so this is around half as fast: a stroll. */
    @Unique
    private static final double LIFEGUARD_STROLL = 0.75D;

    protected EntityGazelleMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Shadow
    public abstract boolean isRunning();

    @Override
    public boolean lifeguard$fleeing() {
        return this.isRunning();
    }

    /** Mob's constructor asks for this before Lifeguard's fields would exist, so it keeps none. */
    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GazelleNavigation(this, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lifeguard$moveControl(CallbackInfo ci) {
        this.moveControl = new GazelleMoveControl(this);
    }

    @ModifyArg(method = "registerGoals", at = @At(value = "INVOKE",
        target = "Lcom/github/alexthe666/alexsmobs/entity/ai/AnimalAIWanderRanged;<init>(Lnet/minecraft/world/entity/PathfinderMob;IDII)V"), index = 2)
    private double lifeguard$stroll(double speed) {
        return LIFEGUARD_STROLL;
    }
}
