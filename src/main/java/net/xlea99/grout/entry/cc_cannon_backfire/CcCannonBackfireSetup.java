package net.xlea99.grout.entry.cc_cannon_backfire;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link CcCannonBackfire}. */
public final class CcCannonBackfireSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(CcCannonBackfire.ENTRY, CannonBackfire::register);
    }
}
