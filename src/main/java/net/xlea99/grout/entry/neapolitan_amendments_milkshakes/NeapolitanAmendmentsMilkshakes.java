package net.xlea99.grout.entry.neapolitan_amendments_milkshakes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class NeapolitanAmendmentsMilkshakes {
    /** Cauldrons change on the server (the client predicts the same). */
    public static final Entry ENTRY = Entries.compat("grout:neapolitan_amendments_milkshakes", Taste.NONE).home("neapolitan")
        .requiresMods("neapolitan", "amendments")
        .side(Side.SERVER).restart(Restart.GAME);

    private NeapolitanAmendmentsMilkshakes() {
    }
}
