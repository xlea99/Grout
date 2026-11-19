package net.xlea99.grout.mixin.am_gorillas_forage_neapolitan_bananas;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * {@code breakLeaves} makes its banana from {@code AMItemRegistry.BANANA.get()}, the only registry holder it reads. Targeted
 * by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.ai.GorillaAIForageLeaves")
public abstract class GorillaAIForageLeavesMixin {
    @Unique
    private static final ResourceLocation GROUT_BANANA = ResourceLocation.fromNamespaceAndPath("neapolitan", "banana");

    @ModifyExpressionValue(method = "breakLeaves", at = @At(value = "INVOKE",
        target = "Lnet/neoforged/neoforge/registries/DeferredHolder;get()Ljava/lang/Object;"))
    private Object grout$neapolitanBanana(Object alexsBanana) {
        return BuiltInRegistries.ITEM.get(GROUT_BANANA);
    }
}
