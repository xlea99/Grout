package net.xlea99.grout.mixin.map_atlases_fixes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.xlea99.grout.entry.map_atlases_fixes.AtlasFixes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.mapatlases.item.MapAtlasItem;
import pepjebs.mapatlases.utils.Slice;

/**
 * A slice is only saved for a dimension the atlas has maps in ({@link AtlasFixes}). For any other dimension Map Atlases
 * checked the height against its "top only" fallback, which a top slice always passes.
 */
@Mixin(MapAtlasItem.class)
public abstract class MapAtlasItemMixin {

    @Inject(method = "setSelectedSlice", at = @At("HEAD"), cancellable = true)
    private static void grout$onlyMappedDimensions(ItemStack atlas, Slice slice, Level level, CallbackInfo ci) {
        if (!slice.equals(Slice.defaultVanillaFor(slice.dimension()))
            && !MapAtlasItem.getMaps(atlas, level).hasDimension(slice.dimension()))
            ci.cancel();
    }
}
