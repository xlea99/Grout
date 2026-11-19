package net.xlea99.grout.entry.cc_golden_milk_follows_neapolitan;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CcGoldenMilkFollowsNeapolitan {
    /** Cauldron interactions are decided on the server. */
    public static final Entry ENTRY = Entries.compat("grout:cc_golden_milk_follows_neapolitan", Taste.NONE)
        .home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms", "neapolitan")
        .side(Side.SERVER).restart(Restart.GAME);

    private CcGoldenMilkFollowsNeapolitan() {
    }
}
