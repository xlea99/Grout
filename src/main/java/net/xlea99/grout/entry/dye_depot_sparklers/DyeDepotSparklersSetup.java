package net.xlea99.grout.entry.dye_depot_sparklers;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link DyeDepotSparklers}. */
public final class DyeDepotSparklersSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // Runtime-gated: registered whenever both mods are there (a world may hold the sparklers); the listeners check the
        // switch.
        wiring.whenInstalled(DyeDepotSparklers.ENTRY, Sparklers::register);
    }
}
