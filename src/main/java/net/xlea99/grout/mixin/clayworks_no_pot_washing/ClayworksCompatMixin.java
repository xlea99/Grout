package net.xlea99.grout.mixin.clayworks_no_pot_washing;

import com.teamabnormals.clayworks.core.other.ClayworksCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClayworksCompat.class)
public abstract class ClayworksCompatMixin {
    @Inject(method = "registerCauldronInteractions", at = @At("HEAD"), cancellable = true)
    private static void grout$noWashing(CallbackInfo ci) {
        ci.cancel();
    }
}
