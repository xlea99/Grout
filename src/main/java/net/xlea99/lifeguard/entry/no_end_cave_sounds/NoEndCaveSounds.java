package net.xlea99.lifeguard.entry.no_end_cave_sounds;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class NoEndCaveSounds {
    /** Ambience plays on the client. */
    public static final Entry ENTRY = Entries.tweak("no_end_cave_sounds", Taste.STRONG).home(Entry.MINECRAFT)
        .side(Side.CLIENT).restart(Restart.GAME);

    private NoEndCaveSounds() {
    }
}
