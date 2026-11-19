package net.xlea99.grout.entry.chest_boats_take_any_chest;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;
import net.xlea99.grout.data.RecipeEdits;

/** Setup for {@link ChestBoatsTakeAnyChest}. */
public final class ChestBoatsTakeAnyChestSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(ChestBoatsTakeAnyChest.ENTRY,
            () -> RecipeEdits.addRule(ChestBoatsTakeAnyChest.ENTRY, StandardChestBoatRecipes::widen));
    }
}
