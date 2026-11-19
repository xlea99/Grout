package net.xlea99.grout.entry.amendments_hanging_pot_names;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmendmentsHangingPotNames {
    /** Only what Jade shows. */
    public static final Entry ENTRY = Entries.compat("grout:amendments_hanging_pot_names", Taste.NONE).home("amendments")
        .requiresMods("amendments", "jade")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmendmentsHangingPotNames() {
    }
}
