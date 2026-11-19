package net.xlea99.grout.entry.snowy_spirit_edible_treats;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link SnowySpiritEdibleTreats}. */
public final class SnowySpiritEdibleTreatsClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(SnowySpiritEdibleTreats.ENTRY, GumdropCrumbs::register);
    }
}
