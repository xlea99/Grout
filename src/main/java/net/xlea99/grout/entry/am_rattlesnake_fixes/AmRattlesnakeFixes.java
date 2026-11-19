package net.xlea99.grout.entry.am_rattlesnake_fixes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmRattlesnakeFixes {
    /**
     * A Rattlesnake's rattle often began silent, its sound starting up to 2.5 seconds later: the sound loops on a count that
     * only runs while it rattles and is never put back when a rattle ends, so the next one began wherever the last stopped.
     * It starts with the rattle now. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.fix("am_rattlesnake_fixes", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * A coiled Rattlesnake uncoiled on your screen after 4 to 6 seconds while the server still had it coiled, so it lay there
     * straight and frozen until something changed: the coil's timeout runs on the client too, with the client's own short
     * timer, and the server's coil never changes, so it's never sent again. Alex's own random coils last 15 to 27 seconds on
     * the server. Only the server ends a coil now. Client side. (1.20.1 did the same.)
     */
    public static final Entry COIL_SYNC = Entries.fix("am_rattlesnake_coil_sync", Taste.NONE).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmRattlesnakeFixes() {
    }
}
