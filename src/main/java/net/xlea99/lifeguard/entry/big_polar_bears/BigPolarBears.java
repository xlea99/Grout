package net.xlea99.lifeguard.entry.big_polar_bears;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class BigPolarBears {
    /**
     * Polar bears as the largest bears they are: bigger and much tougher, by their default attributes. Strong: it changes a vanilla
     * mob's fight. Size and health reach clients by themselves, so this needs nothing on the client.
     */
    public static final Entry ENTRY = Entries.tweak("big_polar_bears", Taste.STRONG).home(Entry.MINECRAFT)
        .side(Side.SERVER).restart(Restart.GAME);

    private BigPolarBears() {
    }
}
