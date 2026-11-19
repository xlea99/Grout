package net.xlea99.grout.mixin.am_slightly_sexier_bone_serpent;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tells clients what a jaw needs, with what vanilla already sends: the aggressive flag for hunting, which nothing else reads on this mob,
 * and an arm swing for a bite. Targeted by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent")
public abstract class EntityBoneSerpentMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$tellClients(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (self.level().isClientSide)
            return;
        self.setAggressive(self.getTarget() != null);
        if (self.getLastHurtMob() != null && self.getLastHurtMobTimestamp() == self.tickCount)
            self.swing(InteractionHand.MAIN_HAND);
    }
}
