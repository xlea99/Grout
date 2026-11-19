package net.xlea99.grout.entry.amendments_mixed_potion_brewing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmendmentsMixedPotionBrewing {
    /** Brewing stands brew on the server. */
    public static final Entry ENTRY = Entries.fix("grout:amendments_mixed_potion_brewing", Taste.NONE).home("amendments")
        .requiresMods("amendments")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmendmentsMixedPotionBrewing() {
    }
}
