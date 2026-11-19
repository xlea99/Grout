package net.xlea99.lifeguard.entry.vanilla_backport_fa_cold_pig;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link VanillaBackportFaColdPig}. */
public final class VanillaBackportFaColdPigClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(VanillaBackportFaColdPig.ENTRY, FaColdPigPack::register);
    }
}
