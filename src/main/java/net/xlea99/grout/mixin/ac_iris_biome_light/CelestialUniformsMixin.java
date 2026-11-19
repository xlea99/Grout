package net.xlea99.grout.mixin.ac_iris_biome_light;

import net.irisshaders.iris.uniforms.CelestialUniforms;
import net.xlea99.grout.entry.ac_iris_biome_light.BiomeLight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CelestialUniforms.class)
public abstract class CelestialUniformsMixin {
    @Inject(method = "getSkyAngle", at = @At("RETURN"), cancellable = true)
    private static void grout$pinnedSkyAngle(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(BiomeLight.skyAngle(cir.getReturnValueF()));
    }
}
