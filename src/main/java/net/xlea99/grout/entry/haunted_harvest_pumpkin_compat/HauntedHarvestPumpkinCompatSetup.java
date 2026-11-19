package net.xlea99.grout.entry.haunted_harvest_pumpkin_compat;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link HauntedHarvestPumpkinCompat}. */
public final class HauntedHarvestPumpkinCompatSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(HauntedHarvestPumpkinCompat.ENTRY, Pumpkins::register);
    }
}
