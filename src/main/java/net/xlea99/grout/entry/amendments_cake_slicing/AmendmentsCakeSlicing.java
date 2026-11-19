package net.xlea99.grout.entry.amendments_cake_slicing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmendmentsCakeSlicing {
    /** The server slices; the client only holds back its guess. */
    public static final Entry ENTRY = Entries.compat("grout:amendments_cake_slicing", Taste.NONE).home("amendments")
        .requiresMods("amendments")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmendmentsCakeSlicing() {
    }
}
