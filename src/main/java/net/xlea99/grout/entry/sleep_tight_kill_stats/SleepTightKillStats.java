package net.xlea99.grout.entry.sleep_tight_kill_stats;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SleepTightKillStats {
    /** Kills and stats are counted on the server. */
    public static final Entry ENTRY = Entries.fix("grout:sleep_tight_kill_stats", Taste.NONE).home("sleep_tight")
        .requiresMods("sleep_tight")
        .side(Side.SERVER).restart(Restart.GAME);

    private SleepTightKillStats() {
    }
}
