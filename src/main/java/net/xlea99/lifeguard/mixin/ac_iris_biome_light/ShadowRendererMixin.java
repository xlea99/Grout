package net.xlea99.lifeguard.mixin.ac_iris_biome_light;

import net.irisshaders.iris.shadows.ShadowRenderer;
import net.xlea99.lifeguard.entry.ac_iris_biome_light.BiomeLight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShadowRenderer.class)
public abstract class ShadowRendererMixin {
    @Inject(method = "getSkyAngle", at = @At("RETURN"), cancellable = true)
    private static void lifeguard$pinnedSkyAngle(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(BiomeLight.skyAngle(cir.getReturnValueF()));
    }
}
