package net.xlea99.grout.entry.twigs_cc_flowering_azaleas;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class TwigsCcFloweringAzaleas {
    /** Trees grow on the server. See FloweringAzaleaTrees. */
    public static final Entry ENTRY = Entries.compat("grout:twigs_cc_flowering_azaleas", Taste.STRONG).home("twigs")
        .requiresMods("twigs", "caverns_and_chasms")
        .side(Side.SERVER).restart(Restart.GAME);

    private TwigsCcFloweringAzaleas() {
    }
}
