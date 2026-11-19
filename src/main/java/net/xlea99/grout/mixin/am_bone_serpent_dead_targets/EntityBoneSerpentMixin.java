package net.xlea99.grout.mixin.am_bone_serpent_dead_targets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent")
public abstract class EntityBoneSerpentMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void grout$letGoOfTheDead(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        LivingEntity target = self.getTarget();
        if (target != null && !target.isAlive())
            self.setTarget(null);
    }
}
