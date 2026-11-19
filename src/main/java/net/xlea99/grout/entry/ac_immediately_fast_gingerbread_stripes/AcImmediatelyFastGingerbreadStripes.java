package net.xlea99.grout.entry.ac_immediately_fast_gingerbread_stripes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AcImmediatelyFastGingerbreadStripes {
    /** Only how the stripe is drawn. */
    public static final Entry ENTRY = Entries.compat("grout:ac_immediately_fast_gingerbread_stripes", Taste.NONE)
        .home("alexscaves")
        .requiresMods("alexscaves", "immediatelyfast")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AcImmediatelyFastGingerbreadStripes() {
    }
}
