package net.xlea99.grout.entry.supps_pedestal_compat;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsPedestalCompat {
    /** Visual: only the pedestal renderer changes. */
    public static final Entry ENTRY = Entries.compat("grout:supps_pedestal_compat", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries", "alexscaves")
        .side(Side.CLIENT).restart(Restart.GAME);

    private SuppsPedestalCompat() {
    }
}
