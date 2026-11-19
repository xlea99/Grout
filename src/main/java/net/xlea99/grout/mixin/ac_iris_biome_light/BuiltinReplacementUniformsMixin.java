package net.xlea99.grout.mixin.ac_iris_biome_light;

import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.uniforms.builtin.BuiltinReplacementUniforms;
import net.xlea99.grout.entry.ac_iris_biome_light.BiomeLight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Iris sets its lightmap matrix once, since it never changes; this one does, so it's asked for every frame. */
@Mixin(BuiltinReplacementUniforms.class)
public abstract class BuiltinReplacementUniformsMixin {
    @Inject(method = "addBuiltinReplacementUniforms", at = @At("HEAD"), cancellable = true)
    private static void grout$matrixEveryFrame(UniformHolder uniforms, CallbackInfo ci) {
        uniforms.uniformMatrix(UniformUpdateFrequency.PER_FRAME, "iris_LightmapTextureMatrix", BiomeLight::lightmapMatrix);
        ci.cancel();
    }
}
