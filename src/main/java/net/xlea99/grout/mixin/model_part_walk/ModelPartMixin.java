package net.xlea99.grout.mixin.model_part_walk;

import java.util.stream.Stream;
import net.minecraft.client.model.geom.ModelPart;
import net.xlea99.grout.entry.model_part_walk.PartWalk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@code getAllParts} as one lazy walker ({@link PartWalk}) instead of a stream pipeline built at every part: the same parts,
 * in the same order, still lazy. Keyframe models reset every part's pose each frame with {@code
 * root().getAllParts().forEach(ModelPart::resetPose)}: 400 frogs spent 10% of the render thread in it, 23% with Fresh
 * Animations (2026-11-16).
 */
@Mixin(ModelPart.class)
public abstract class ModelPartMixin {
    @Inject(method = "getAllParts", at = @At("HEAD"), cancellable = true)
    private void grout$walk(CallbackInfoReturnable<Stream<ModelPart>> cir) {
        cir.setReturnValue(PartWalk.of((ModelPart) (Object) this, p -> ((ModelPartChildren) (Object) p).grout$walkChildren()));
    }
}
