package net.xlea99.lifeguard.entry.quark_hollow_log_paths;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class QuarkHollowLogPaths {
    /**
     * Mobs planned their paths straight through Quark's hollow logs and stood against them: vanilla counts any block whose
     * collision isn't a full cube as walkable-through unless the block says otherwise, and the hollow logs don't. Now land
     * mobs treat them as solid, so they step over or go around. Fish in waterlogged ones are untouched. (Quark 4.1-488.)
     */
    public static final Entry ENTRY = Entries.fix("quark_hollow_log_paths", Taste.NONE).home("quark")
        .requiresMods("quark")
        .side(Side.SERVER).restart(Restart.GAME);

    private QuarkHollowLogPaths() {
    }
}
