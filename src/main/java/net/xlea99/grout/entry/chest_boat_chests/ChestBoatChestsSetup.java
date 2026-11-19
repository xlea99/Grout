package net.xlea99.grout.entry.chest_boat_chests;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link ChestBoatChests}. */
public final class ChestBoatChestsSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // Runtime-gated: the payload is registered whenever it could be sent; the listeners ask the switch.
        wiring.whenInstalled(ChestBoatChests.ENTRY, bus -> {
            bus.addListener(BoatChestPayload::register);
            BoatChest.register();
        });
    }
}
