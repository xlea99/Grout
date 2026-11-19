package net.xlea99.grout.mixin.am_grizzly_tame_cubs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear")
public abstract class EntityGrizzlyBearMixin {
    @Inject(method = "getBreedOffspring", at = @At("RETURN"))
    private void grout$tameCub(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<AgeableMob> cir) {
        TamableAnimal parent = (TamableAnimal) (Object) this;
        if (parent.isTame() && cir.getReturnValue() instanceof TamableAnimal cub) {
            cub.setOwnerUUID(parent.getOwnerUUID());
            cub.setTame(true, true);
        }
    }
}
