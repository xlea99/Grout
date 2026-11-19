package net.xlea99.grout.mixin.am_grizzly_tempting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Its one {@code Ingredient.of} is the tempt goal's. Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear")
public abstract class EntityGrizzlyBearMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/item/crafting/Ingredient;of(Lnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/crafting/Ingredient;"))
    private static TagKey<Item> grout$ownFood(TagKey<Item> gorillaFood) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("alexsmobs", "grizzly_foodstuffs"));
    }
}
