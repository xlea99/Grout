package net.xlea99.grout.entry.am_gorilla_silverbacks;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmGorillaSilverbacks {
    /**
     * Strong: it changes how another mod's mob lives, and Gorillas can now kill each other. A Silverback used to be decided once at
     * spawn, so a troop that lost its Silverback stayed leaderless and bred troops never had one. Now a grown Gorilla with no
     * settled Silverback nearby rises to be one after a while; one that's just risen can be challenged by another that rises too.
     * Two Silverbacks that meet face off, chest-pounding and charging, and usually one backs down; sometimes they fight, and a
     * badly hurt one may still give up. Only sometimes does it end in death. The loser loses its silver and keeps away from the
     * winner for a day or two. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_gorilla_silverbacks", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGorillaSilverbacks() {
    }
}
