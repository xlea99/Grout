package net.xlea99.grout.mixin.sexy_runic_glints;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.xlea99.grout.entry.sexy_runic_glints.GlintContext;
import net.xlea99.grout.entry.sexy_runic_glints.GlintVariants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The foil buffer helpers pair a glint with the render type it lies over (items, shields, tridents, elytra, ...). The
 * glint becomes a {@link GlintVariants} stand-in reading that render type's texture where needed (Quark's own swaps of
 * the glint have already happened by then). Every getBuffer call is offered up; only Quark glints change.
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    private static final String GET_BUFFER = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;";

    /** The current foil buffer call's buffer source and the render type its glint lies over. Render thread only. */
    @Unique
    private static MultiBufferSource grout$buffers;
    @Unique
    private static RenderType grout$surface;

    @Inject(method = { "getFoilBuffer", "getFoilBufferDirect" }, at = @At("HEAD"))
    private static void grout$noteSurface(MultiBufferSource buffers, RenderType type, boolean flag, boolean foil,
                                              CallbackInfoReturnable<VertexConsumer> cir) {
        grout$buffers = buffers;
        grout$surface = type;
    }

    @Inject(method = "getArmorFoilBuffer", at = @At("HEAD"))
    private static void grout$noteArmorSurface(MultiBufferSource buffers, RenderType type, boolean foil,
                                                   CallbackInfoReturnable<VertexConsumer> cir) {
        grout$buffers = buffers;
        grout$surface = type;
    }

    @ModifyArg(method = { "getFoilBuffer", "getFoilBufferDirect", "getArmorFoilBuffer" }, at = @At(value = "INVOKE", target = GET_BUFFER))
    private static RenderType grout$glintForSurface(RenderType glint) {
        MultiBufferSource buffers = grout$buffers;
        return GlintVariants.forSurface(glint, GlintContext.seesSurface(buffers) ? GlintVariants.textureOf(grout$surface) : null,
            null, GlintContext.banner(buffers), GlintContext.coordScale(buffers));
    }
}
