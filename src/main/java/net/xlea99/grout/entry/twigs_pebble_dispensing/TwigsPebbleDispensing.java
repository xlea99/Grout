package net.xlea99.grout.entry.twigs_pebble_dispensing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class TwigsPebbleDispensing {
    /** No mixins: a dispenser behavior registered at startup ({@link PebbleDispensers}) when this is active. */
    public static final Entry ENTRY = Entries.fix("grout:twigs_pebble_dispensing", Taste.NONE).home("twigs")
        .requiresMods("twigs")
        .side(Side.SERVER).restart(Restart.GAME);

    private TwigsPebbleDispensing() {
    }
}
