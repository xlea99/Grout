package net.xlea99.grout.entry.scholar_lectern_atlases;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ScholarLecternAtlases {
    /** Only how a lectern is drawn. */
    public static final Entry ENTRY = Entries.compat("grout:scholar_lectern_atlases", Taste.NONE).home("scholar")
        .requiresMods("scholar", "map_atlases")
        .side(Side.CLIENT).restart(Restart.GAME);

    private ScholarLecternAtlases() {
    }
}
