package net.xlea99.lifeguard.mixin.model_part_walk;

import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** A part's children, read each time (EMF replaces this map at runtime). This entry's own, as the lookup entry may be off. */
@Mixin(ModelPart.class)
public interface ModelPartChildren {
    @Accessor("children")
    Map<String, ModelPart> lifeguard$walkChildren();
}
