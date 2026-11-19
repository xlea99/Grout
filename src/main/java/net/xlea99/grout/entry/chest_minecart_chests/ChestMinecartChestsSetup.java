package net.xlea99.grout.entry.chest_minecart_chests;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link ChestMinecartChests}. */
public final class ChestMinecartChestsSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // Runtime-gated: the payload is registered whenever it could be sent; the listeners ask the switch.
        wiring.whenInstalled(ChestMinecartChests.ENTRY, bus -> {
            bus.addListener(CartChestPayload::register);
            CartChest.register();
        });
    }
}
