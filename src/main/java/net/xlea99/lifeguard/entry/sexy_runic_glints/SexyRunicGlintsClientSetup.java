package net.xlea99.lifeguard.entry.sexy_runic_glints;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link SexyRunicGlints}. */
public final class SexyRunicGlintsClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(SexyRunicGlints.ENTRY, GlintsClient::register);
    }
}
