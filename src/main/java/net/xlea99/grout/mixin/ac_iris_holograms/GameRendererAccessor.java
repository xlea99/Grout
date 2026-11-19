package net.xlea99.grout.mixin.ac_iris_holograms;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Accessor("rendertypeEntityTranslucentShader")
    static ShaderInstance grout$entityTranslucentShader() {
        throw new AssertionError();
    }
}
