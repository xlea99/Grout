package net.xlea99.lifeguard.entry.am_grizzly_tame_cubs;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AmGrizzlyTameCubs {
    /**
     * Grizzly Bears only breed when tame, yet their cubs are born wild, and grow up hostile in their own pen. A tame bear's cubs are now
     * tame and belong to its owner, as a wolf's do.
     */
    public static final Entry ENTRY = Entries.tweak("am_grizzly_tame_cubs", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGrizzlyTameCubs() {
    }
}
