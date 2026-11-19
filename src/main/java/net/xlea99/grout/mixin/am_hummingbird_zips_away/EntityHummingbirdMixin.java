package net.xlea99.grout.mixin.am_hummingbird_zips_away;

import net.minecraft.world.entity.Mob;
import net.xlea99.grout.entry.am_hummingbird_zips_away.HummingbirdZipAway;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adds the escape above every goal Alex gives it. Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityHummingbird")
public abstract class EntityHummingbirdMixin {
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void grout$zipAway(CallbackInfo ci) {
        Mob bird = (Mob) (Object) this;
        bird.goalSelector.addGoal(0, new HummingbirdZipAway(bird));
    }
}
