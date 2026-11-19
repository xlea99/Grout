package net.xlea99.grout.entry.species_dyeable_chaindeliers;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link SpeciesDyeableChaindeliers}. */
public final class SpeciesDyeableChaindeliersSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // Runtime-gated: registered whenever Species is there; the listeners ask the switch.
        wiring.whenInstalled(SpeciesDyeableChaindeliers.ENTRY, Chaindeliers::register);
    }
}
