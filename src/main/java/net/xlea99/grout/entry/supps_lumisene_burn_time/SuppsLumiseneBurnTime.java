package net.xlea99.grout.entry.supps_lumisene_burn_time;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsLumiseneBurnTime {
    /** Runtime-gated: entities inside blocks are ticked on both sides, so the client follows the server's setting. */
    public static final Entry ENTRY = Entries.fix("supps_lumisene_burn_time", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private SuppsLumiseneBurnTime() {
    }
}
