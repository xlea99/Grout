package net.xlea99.grout.entry.keyframe_part_lookup;

import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;

/**
 * {@code HierarchicalModel.getAnyDescendantWithName} for any name but "root", without its stream: the first part, in
 * {@code getAllParts}' order (a part before its children, depth first), that has a child named {@code name}: that child; null
 * when none. Reads the live tree each time.
 */
public final class PartLookup {
    private PartLookup() {
    }

    public static ModelPart find(ModelPart part, String name, Function<ModelPart, Map<String, ModelPart>> childrenOf) {
        if (part.hasChild(name))
            return part.getChild(name);
        for (ModelPart child : childrenOf.apply(part).values()) {
            ModelPart found = find(child, name, childrenOf);
            if (found != null)
                return found;
        }
        return null;
    }
}
