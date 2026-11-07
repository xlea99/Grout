package net.xlea99.lifeguard.entry.woodworks_emi_recipes.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.xlea99.lifeguard.Lifeguard;
import net.xlea99.lifeguard.entry.woodworks_emi_recipes.WoodworksEmiRecipes;

/**
 * The sawmill's recipes in EMI, laid out like the stonecutter's. Its recipe type is found by id and its recipes read as
 * vanilla's SingleItemRecipe, so nothing of Woodworks' is compiled in. Found by EMI; does nothing unless the entry is
 * active.
 */
@EmiEntrypoint
public final class SawmillEmiPlugin implements EmiPlugin {
    private static final ResourceLocation SAWMILL = ResourceLocation.fromNamespaceAndPath("woodworks", "sawmill");
    private static final ResourceLocation SAWING = ResourceLocation.fromNamespaceAndPath("woodworks", "sawing");

    @Override
    public void register(EmiRegistry registry) {
        if (!WoodworksEmiRecipes.ENTRY.active())
            return;
        Level level = Minecraft.getInstance().level;
        RecipeType<?> type = BuiltInRegistries.RECIPE_TYPE.get(SAWING);
        if (level == null || type == null)
            return;
        EmiStack sawmill = EmiStack.of(BuiltInRegistries.ITEM.get(SAWMILL));
        EmiRecipeCategory category = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Lifeguard.MODID, "sawing"), sawmill);
        registry.addCategory(category);
        registry.addWorkstation(category, sawmill);
        for (RecipeHolder<?> holder : registry.getRecipeManager().getRecipes()) {
            if (holder.value().getType() != type || !(holder.value() instanceof SingleItemRecipe recipe))
                continue;
            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (!recipe.getIngredients().isEmpty() && !output.isEmpty())
                registry.addRecipe(new SawingRecipe(category, holder.id(), EmiIngredient.of(recipe.getIngredients().getFirst()), EmiStack.of(output)));
        }
    }

    private static final class SawingRecipe extends BasicEmiRecipe {
        SawingRecipe(EmiRecipeCategory category, ResourceLocation id, EmiIngredient input, EmiStack output) {
            super(category, id, 76, 18);
            inputs.add(input);
            outputs.add(output);
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
            widgets.addSlot(inputs.getFirst(), 0, 0);
            widgets.addSlot(outputs.getFirst(), 58, 0).recipeContext(this);
        }
    }
}
