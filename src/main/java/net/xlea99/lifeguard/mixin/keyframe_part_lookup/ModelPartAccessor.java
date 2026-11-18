package net.xlea99.lifeguard.mixin.keyframe_part_lookup;

import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** A part's children, as {@code getAllParts} walks them (read each time: EMF replaces this map at runtime). */
@Mixin(ModelPart.class)
public interface ModelPartAccessor {
    @Accessor("children")
    Map<String, ModelPart> lifeguard$children();
}
