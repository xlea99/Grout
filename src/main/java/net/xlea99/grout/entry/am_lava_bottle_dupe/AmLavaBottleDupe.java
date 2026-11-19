package net.xlea99.grout.entry.am_lava_bottle_dupe;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmLavaBottleDupe {
    /** Moonlight reads its liquids from data on the server, which sends them to players. */
    public static final Entry ENTRY = Entries.tweak("grout:am_lava_bottle_dupe", Taste.DEEP_END).home("alexsmobs")
        .requiresMods("alexsmobs", "moonlight")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmLavaBottleDupe() {
    }
}
