package net.xlea99.grout.entry.clayworks_emi_recipes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ClayworksEmiRecipes {
    /**
     * None: Clayworks' kiln has its own recipe type and only a JEI plugin, so EMI shows none of its baking recipes. This
     * does what that plugin does: a Baking category, the kiln as its workstation and a fuel workstation, and filling the
     * kiln from a recipe. Display only. (Clayworks 4.0.3.)
     */
    public static final Entry ENTRY = Entries.compat("clayworks_emi_recipes", Taste.NONE).home("clayworks")
        .requiresMods("clayworks", "emi")
        .side(Side.CLIENT).restart(Restart.GAME);

    private ClayworksEmiRecipes() {
    }
}
