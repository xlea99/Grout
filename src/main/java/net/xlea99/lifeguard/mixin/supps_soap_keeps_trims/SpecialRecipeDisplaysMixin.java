package net.xlea99.lifeguard.mixin.supps_soap_keeps_trims;

import java.util.List;
import net.mehvahdjukaar.supplementaries.common.items.crafting.SpecialRecipeDisplays;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.xlea99.lifeguard.entry.supps_soap_keeps_trims.SuppsSoapKeepsTrims;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** No "soap washes a trim off" example in EMI, JEI or REI, since soap no longer does ({@link SoapClearRecipeMixin}). */
@Mixin(SpecialRecipeDisplays.class)
public abstract class SpecialRecipeDisplaysMixin {
    @Inject(method = "createRemoveTrimRecipe", at = @At("HEAD"), cancellable = true)
    private static void lifeguard$noTrimExample(CallbackInfoReturnable<List<RecipeHolder<? extends CraftingRecipe>>> cir) {
        if (SuppsSoapKeepsTrims.ENTRY.active())
            cir.setReturnValue(List.of());
    }
}
