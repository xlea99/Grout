package net.xlea99.grout.mixin.supps_soap_keeps_trims;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.mehvahdjukaar.supplementaries.common.items.crafting.SoapClearRecipe;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.xlea99.grout.entry.supps_soap_keeps_trims.SuppsSoapKeepsTrims;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Soap leaves armor trims alone, as before Supplementaries 4.1.2: a trimmed item no longer counts as something soap washes
 * ({@code hasTrim}, read by both {@code matches} and {@code assemble}), and washing the dye off trimmed leather armor no
 * longer takes the trim with it ({@code assemble}'s {@code remove(TRIM)}).
 */
@Mixin(SoapClearRecipe.class)
public abstract class SoapClearRecipeMixin {
    @Inject(method = "hasTrim", at = @At("HEAD"), cancellable = true)
    private void grout$noTrimWashing(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (SuppsSoapKeepsTrims.ENTRY.active())
            cir.setReturnValue(false);
    }

    @WrapOperation(method = "assemble",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;remove(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"))
    private Object grout$keepTrim(ItemStack stack, DataComponentType<?> type, Operation<Object> original) {
        if (type == DataComponents.TRIM && SuppsSoapKeepsTrims.ENTRY.active())
            return null;
        return original.call(stack, type);
    }
}
