package net.xlea99.lifeguard.mixin.am_hummingbird_fixes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A bred baby comes from {@code getBreedOffspring} with the default variant, and breeding never runs the spawn setup that
 * picks one. It takes this parent's, as the Toucan, Emu and Rain Frog do in Alex's own code. Targeted by name: Lifeguard
 * isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityHummingbird")
public abstract class EntityHummingbirdMixin {
    @Shadow
    public abstract int getVariant();

    @Shadow
    public abstract void setVariant(int variant);

    @Inject(method = "getBreedOffspring", at = @At("RETURN"))
    private void lifeguard$parentColor(ServerLevel level, AgeableMob mate, CallbackInfoReturnable<AgeableMob> cir) {
        if ((Object) cir.getReturnValue() instanceof EntityHummingbirdMixin baby)
            baby.setVariant(getVariant());
    }
}
