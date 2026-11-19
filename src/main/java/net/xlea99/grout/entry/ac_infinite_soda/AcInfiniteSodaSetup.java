package net.xlea99.grout.entry.ac_infinite_soda;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link AcInfiniteSoda}. */
public final class AcInfiniteSodaSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(AcInfiniteSoda.ENTRY, InfiniteSoda::register);
    }
}
