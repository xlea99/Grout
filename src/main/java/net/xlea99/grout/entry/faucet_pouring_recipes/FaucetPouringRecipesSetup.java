package net.xlea99.grout.entry.faucet_pouring_recipes;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link FaucetPouringRecipes}. */
public final class FaucetPouringRecipesSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(FaucetPouringRecipes.ENTRY, PouringRecipes::register);
        wiring.whenInstalled(FaucetPouringRecipes.ENTRY, bus -> bus.addListener(PouringRecipesPayload::register));
    }
}
