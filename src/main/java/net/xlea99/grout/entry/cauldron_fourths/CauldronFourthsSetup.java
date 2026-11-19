package net.xlea99.grout.entry.cauldron_fourths;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link CauldronFourths}. */
public final class CauldronFourthsSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(CauldronFourths.ENTRY, FourLevelInteractions::register);
    }
}
