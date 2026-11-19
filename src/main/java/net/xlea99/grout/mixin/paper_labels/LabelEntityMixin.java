package net.xlea99.grout.mixin.paper_labels;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mehvahdjukaar.labels.LabelEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.xlea99.grout.entry.paper_labels.LabelPlacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Broken and picked labels give paper ({@link LabelPlacing}). */
@Mixin(LabelEntity.class)
public abstract class LabelEntityMixin {

    @ModifyArg(method = "dropItem", at = @At(value = "INVOKE",
        target = "Lnet/mehvahdjukaar/labels/LabelEntity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemLike grout$dropPaper(ItemLike label) {
        return Items.PAPER;
    }

    @ModifyReturnValue(method = "getPickResult", at = @At("RETURN"))
    private ItemStack grout$pickPaper(ItemStack picked) {
        return picked == null ? null : new ItemStack(Items.PAPER);
    }
}
