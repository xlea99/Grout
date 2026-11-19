package net.xlea99.grout.mixin.ac_iris_biome_light;

import net.irisshaders.iris.uniforms.CommonUniforms;
import net.xlea99.grout.entry.ac_iris_biome_light.BiomeLight;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommonUniforms.class)
public abstract class CommonUniformsMixin {
    @Inject(method = "getEyeBrightness", at = @At("RETURN"), cancellable = true)
    private static void grout$biomeEyeBrightness(CallbackInfoReturnable<Vector2i> cir) {
        cir.setReturnValue(BiomeLight.eyeBrightness(cir.getReturnValue()));
    }

    /** Also where {@code wetness} comes from. */
    @Inject(method = "getRainStrength", at = @At("RETURN"), cancellable = true)
    private static void grout$noBiomeRain(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(BiomeLight.rainStrength(cir.getReturnValueF()));
    }

    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private static void grout$biomeSkyColor(CallbackInfoReturnable<Vector3d> cir) {
        cir.setReturnValue(BiomeLight.skyColor(cir.getReturnValue()));
    }
}
