package net.xlea99.grout.entry.scholar_amendments_lectern_sync;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ScholarAmendmentsLecternSync {
    /** The server builds what a lectern sends to clients. */
    public static final Entry ENTRY = Entries.compat("grout:scholar_amendments_lectern_sync", Taste.NONE).home("scholar")
        .requiresMods("scholar", "amendments")
        .side(Side.SERVER).restart(Restart.GAME);

    private ScholarAmendmentsLecternSync() {
    }
}
