package net.xlea99.grout.entry.woodworks_emi_recipes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class WoodworksEmiRecipes {
    /**
     * None: Woodworks' sawmill has its own recipe type and only a JEI plugin, so EMI shows none of its recipes
     * (user-confirmed 2026-11-05): nobody could look up what it makes. Now EMI has a Sawing category with the sawmill as
     * its workstation, Every Compat's sawing recipes included. Display only. (Woodworks 4.0.2.)
     */
    public static final Entry ENTRY = Entries.compat("woodworks_emi_recipes", Taste.NONE).home("woodworks")
        .requiresMods("woodworks", "emi")
        .usesIds("item", "woodworks:sawmill")
        .usesIds("recipe_type", "woodworks:sawing")
        .side(Side.CLIENT).restart(Restart.GAME);

    private WoodworksEmiRecipes() {
    }
}
