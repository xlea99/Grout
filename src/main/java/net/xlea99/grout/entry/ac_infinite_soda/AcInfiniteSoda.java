package net.xlea99.grout.entry.ac_infinite_soda;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AcInfiniteSoda {
    /** Fluids spread on the server. */
    public static final Entry ENTRY = Entries.tweak("ac_infinite_soda", Taste.DEEP_END).home("alexscaves")
        .requiresMods("alexscaves")
        .usesIds("fluid", "alexscaves:purple_soda")
        .side(Side.SERVER).restart(Restart.GAME);

    /** A faucet on a soda source pours it like water: a bottle at a time if need be, never draining the source. */
    public static final Entry FAUCETS = Entries.tweak("ac_infinite_soda_faucets", Taste.NONE)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private AcInfiniteSoda() {
    }
}
