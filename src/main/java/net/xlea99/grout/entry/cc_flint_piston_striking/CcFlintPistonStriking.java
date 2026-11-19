package net.xlea99.grout.entry.cc_flint_piston_striking;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CcFlintPistonStriking {
    /** Pistons move blocks on the server. See FlintStriking. */
    public static final Entry ENTRY = Entries.compat("grout:cc_flint_piston_striking", Taste.STRONG)
        .home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms", "supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private CcFlintPistonStriking() {
    }
}
