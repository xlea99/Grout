package net.xlea99.lifeguard.entry.amendments_cauldron_dispensing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmendmentsCauldronDispensing {
    /** Dispensers fire on the server. */
    public static final Entry ENTRY = Entries.fix("amendments_cauldron_dispensing", Taste.NONE).home("amendments")
        .requiresMods("amendments")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmendmentsCauldronDispensing() {
    }
}
