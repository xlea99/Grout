package net.xlea99.grout.mixin.clayworks_iris_pot_trims;

import net.irisshaders.batchedentityrendering.impl.Groupable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Groups a pot's draws in Iris's buffer, as Iris does for banners, so it keeps the body-then-trim order. A pot drawn inside
 * an entity's group (an item frame's) already has one. Asks for the level's buffer, not the one passed in: mining a pot
 * passes a wrapper around it.
 */
@Mixin(DecoratedPotRenderer.class)
public abstract class DecoratedPotRendererMixin {
    @Unique
    private static final String RENDER = "render(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V";
    @Unique
    private Groupable grout$group;

    @Inject(method = RENDER, at = @At("HEAD"))
    private void grout$startGroup(CallbackInfo ci) {
        if (Minecraft.getInstance().renderBuffers().bufferSource() instanceof Groupable group && group.maybeStartGroup())
            this.grout$group = group;
    }

    @Inject(method = RENDER, at = @At("RETURN"))
    private void grout$endGroup(CallbackInfo ci) {
        if (this.grout$group != null) {
            this.grout$group.endGroup();
            this.grout$group = null;
        }
    }
}
