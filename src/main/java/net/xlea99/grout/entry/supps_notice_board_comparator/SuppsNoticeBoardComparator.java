package net.xlea99.grout.entry.supps_notice_board_comparator;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsNoticeBoardComparator {
    /** Comparators read blocks on the server only. */
    public static final Entry ENTRY = Entries.fix("supps_notice_board_comparator", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private SuppsNoticeBoardComparator() {
    }
}
