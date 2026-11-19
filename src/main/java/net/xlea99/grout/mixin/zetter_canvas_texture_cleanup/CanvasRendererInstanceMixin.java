package net.xlea99.grout.mixin.zetter_canvas_texture_cleanup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.xlea99.grout.entry.zetter_canvas_texture_cleanup.ZetterCanvasTextureCleanup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Closing a canvas instance also releases its TextureManager registration (TextureManager.release; closing the texture
 * twice is safe, DynamicTexture.close checks). See {@link ZetterCanvasTextureCleanup}.
 */
@Mixin(targets = "me.dantaeusb.zetter.client.renderer.CanvasRenderer$Instance")
public abstract class CanvasRendererInstanceMixin {
    @Unique
    private ResourceLocation grout$registered;

    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Ljava/lang/String;Lnet/minecraft/client/renderer/texture/DynamicTexture;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation grout$remember(ResourceLocation location) {
        this.grout$registered = location;
        return location;
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void grout$release(CallbackInfo ci) {
        if (this.grout$registered != null) {
            Minecraft.getInstance().getTextureManager().release(this.grout$registered);
            this.grout$registered = null;
        }
    }
}
