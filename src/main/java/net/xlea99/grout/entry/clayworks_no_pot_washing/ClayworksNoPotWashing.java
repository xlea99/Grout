package net.xlea99.grout.entry.clayworks_no_pot_washing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ClayworksNoPotWashing {
    /** Deep End's call: a water cauldron no longer washes the color off Clayworks' decorated pots. (Clayworks 4.0.3.) */
    public static final Entry ENTRY = Entries.tweak("grout:clayworks_no_pot_washing", Taste.DEEP_END).home("clayworks")
        .requiresMods("clayworks")
        .side(Side.SERVER).restart(Restart.GAME);

    private ClayworksNoPotWashing() {
    }
}
