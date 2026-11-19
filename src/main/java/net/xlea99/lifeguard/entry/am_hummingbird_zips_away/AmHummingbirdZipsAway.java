package net.xlea99.lifeguard.entry.am_hummingbird_zips_away;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmHummingbirdZipsAway {
    /**
     * Light: a hit Hummingbird used to go on hovering as if nothing happened. Now it zips away in short, fast darts, each
     * one away from whatever hit it, stopping dead between them, for a few seconds. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_hummingbird_zips_away", Taste.LIGHT).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmHummingbirdZipsAway() {
    }
}
