package net.xlea99.grout.entry.cc_cannon_backfire;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CcCannonBackfire {
    /** Cannons fire on the server only. */
    public static final Entry ENTRY = Entries.tweak("grout:cc_cannon_backfire", Taste.STRONG).home("supplementaries")
        .requiresMods("supplementaries", "caverns_and_chasms")
        .usesIds("block", "caverns_and_chasms:gunpowder_block")
        .side(Side.SERVER).restart(Restart.GAME);

    private CcCannonBackfire() {
    }
}
