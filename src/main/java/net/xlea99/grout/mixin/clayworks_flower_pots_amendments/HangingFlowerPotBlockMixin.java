package net.xlea99.grout.mixin.clayworks_flower_pots_amendments;

import java.util.List;
import javax.annotation.Nullable;
import net.mehvahdjukaar.amendments.common.block.HangingFlowerPotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.xlea99.grout.entry.clayworks_flower_pots.FlowerPots;
import net.xlea99.grout.entry.clayworks_flower_pots.HangingPots;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A hung pot gives its looks back: breaking it drops the pot dyed and etched (Amendments drops a brand new plain one),
 * and pick-block on an empty one picks the same. Hanging one takes the item's looks (see the block entity mixin).
 */
@Mixin(HangingFlowerPotBlock.class)
public abstract class HangingFlowerPotBlockMixin {

    // The item's components normally reach the block entity as it's placed; this covers a placement that skips that.
    @Inject(method = "setPlacedBy", at = @At("TAIL"))
    private void grout$takeLooks(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack,
                                     CallbackInfo ci) {
        if (level.getBlockEntity(pos) instanceof HangingPots.Looks pot && pot.grout$color() == null && pot.grout$design() == null
            && (FlowerPots.colorOf(stack) != null || FlowerPots.designOf(stack) != null))
            pot.grout$setLooks(FlowerPots.colorOf(stack), FlowerPots.designOf(stack));
    }

    @Inject(method = "getDrops", at = @At("RETURN"))
    private void grout$dropWithLooks(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof HangingPots.Looks pot)
            for (ItemStack drop : cir.getReturnValue())
                if (drop.is(Items.FLOWER_POT))
                    grout$stamp(drop, pot);
    }

    @Inject(method = "getCloneItemStack", at = @At("RETURN"), cancellable = true)
    private void grout$cloneWithLooks(LevelReader level, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();
        if (stack.is(Items.FLOWER_POT) && level.getBlockEntity(pos) instanceof HangingPots.Looks pot) {
            ItemStack withLooks = stack.copy();
            grout$stamp(withLooks, pot);
            cir.setReturnValue(withLooks);
        }
    }

    private static void grout$stamp(ItemStack pot, HangingPots.Looks looks) {
        if (looks.grout$color() != null)
            pot.set(DataComponents.BASE_COLOR, looks.grout$color());
        if (looks.grout$design() != null)
            pot.set(com.teamabnormals.clayworks.core.registry.ClayworksDataComponents.POT_TRIM.get(), looks.grout$design());
    }
}
