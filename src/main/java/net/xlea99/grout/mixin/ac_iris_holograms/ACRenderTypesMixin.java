package net.xlea99.grout.mixin.ac_iris_holograms;

import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Holograms ask for vanilla's own entity translucent shader, not through the getter Iris answers with the pack's program.
 * With no shaderpack that's the same shader. Under one, Iris leaves any shader alone while a target other than the main one
 * is bound, as it is when this draws, so the mob lands in the hologram target as it does without a pack. Targeted by name:
 * Grout isn't built against Alex's Caves.
 */
@Mixin(targets = "com.github.alexmodguy.alexscaves.client.render.ACRenderTypes")
public abstract class ACRenderTypesMixin {
    @Unique
    private static final ShaderStateShard grout$VANILLA_SHADER = new ShaderStateShard(GameRendererAccessor::grout$entityTranslucentShader);

    @ModifyArg(method = "getHologram", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/RenderType$CompositeState$CompositeStateBuilder;setShaderState(Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;)Lnet/minecraft/client/renderer/RenderType$CompositeState$CompositeStateBuilder;"))
    private static ShaderStateShard grout$vanillaShader(ShaderStateShard original) {
        return grout$VANILLA_SHADER;
    }
}
