package net.xlea99.grout.entry.clayworks_emi_recipes.emi;

import com.teamabnormals.clayworks.common.item.crafting.BakingRecipe;
import com.teamabnormals.clayworks.core.registry.ClayworksBlocks;
import com.teamabnormals.clayworks.core.registry.ClayworksMenuTypes;
import com.teamabnormals.clayworks.core.registry.ClayworksRecipes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.handler.CookingRecipeHandler;
import dev.emi.emi.recipe.EmiCookingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.xlea99.grout.Grout;
import net.xlea99.grout.entry.clayworks_emi_recipes.ClayworksEmiRecipes;

/** The kiln in EMI, drawn with EMI's own furnace recipe. Found by EMI; does nothing unless the entry is active. */
@EmiEntrypoint
public final class KilnEmiPlugin implements EmiPlugin {
    /** The kiln burns fuel twice as fast as a furnace, like the smoker and blast furnace, which EMI also shows as 2. */
    private static final int FUEL_MULTIPLIER = 2;

    @Override
    public void register(EmiRegistry registry) {
        if (!ClayworksEmiRecipes.ENTRY.active())
            return;
        EmiStack kiln = EmiStack.of(ClayworksBlocks.KILN.get());
        EmiRecipeCategory baking = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Grout.MODID, "baking"), kiln);
        registry.addCategory(baking);
        registry.addWorkstation(baking, kiln);
        registry.addWorkstation(VanillaEmiRecipeCategories.FUEL, kiln);
        registry.addRecipeHandler(ClayworksMenuTypes.KILN.get(), new CookingRecipeHandler<>(baking));
        for (RecipeHolder<BakingRecipe> recipe : registry.getRecipeManager().getAllRecipesFor(ClayworksRecipes.ClayworksRecipeTypes.BAKING.get()))
            registry.addRecipe(new EmiCookingRecipe(recipe.value(), baking, FUEL_MULTIPLIER, false));
    }
}
