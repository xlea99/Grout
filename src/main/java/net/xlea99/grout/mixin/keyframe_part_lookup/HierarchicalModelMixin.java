package net.xlea99.grout.mixin.keyframe_part_lookup;

import java.util.Optional;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.xlea99.grout.entry.keyframe_part_lookup.PartLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Finds a keyframe animation's bone with a plain walk of the part tree instead of a chain of streams ({@link PartLookup}).
 *
 * <p>Every frame, for every keyframe-animated mob (vanilla's frogs, camels, sniffers, warden, armadillos, breezes, bats, and the
 * many modded mobs built on the same system: in Deep End most of Spawn's and Species', VanillaBackport's, Peaceless',
 * Etcetera's...), vanilla's keyframe animation looks up each bone it moves by name: {@code root().getAllParts().filter(p ->
 * p.hasChild(name)).findFirst()}, building a recursive stream over the whole tree each time. 200 frogs spent 10.7% of the
 * render thread in it, 26.6% with Fresh Animations (EMF's frog has many more parts). This walks the same tree in the same
 * order and returns the same part: about 8.6 times faster on vanilla's frog (2026-11-16). No cache: EMF swaps a model's
 * children at runtime (variants, rebuilds), so it reads the live tree every time, as vanilla does.
 */
@Mixin(HierarchicalModel.class)
public abstract class HierarchicalModelMixin {
    @Shadow
    public abstract ModelPart root();

    @Inject(method = "getAnyDescendantWithName", at = @At("HEAD"), cancellable = true)
    private void grout$walk(String name, CallbackInfoReturnable<Optional<ModelPart>> cir) {
        if (!name.equals("root"))
            cir.setReturnValue(Optional.ofNullable(PartLookup.find(this.root(), name, p -> ((ModelPartAccessor) (Object) p).grout$children())));
    }
}
