package net.xlea99.lifeguard.entry.cc_immediately_fast_rat_collars;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class CcImmediatelyFastRatCollars {
    /** Only how the collar is drawn. */
    public static final Entry ENTRY = Entries.compat("cc_immediately_fast_rat_collars", Taste.NONE)
        .home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms", "immediatelyfast")
        .side(Side.CLIENT).restart(Restart.GAME);

    private CcImmediatelyFastRatCollars() {
    }
}
