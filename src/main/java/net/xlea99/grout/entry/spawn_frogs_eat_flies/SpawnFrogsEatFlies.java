package net.xlea99.grout.entry.spawn_frogs_eat_flies;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpawnFrogsEatFlies {
    /**
     * Strong: it changes how a mob behaves and takes flies off players. Spawn's flies are records on a mob, with no body for anything to hunt. A frog near anything
     * carrying some now hops over, stares for a moment and eats one with its tongue. Named flies are safe, as they are from fire and drowning. (Spawn 4.0.8.)
     */
    public static final Entry ENTRY = Entries.feature("spawn_frogs_eat_flies", Taste.STRONG).home("spawn")
        .requiresMods("spawn")
        .side(Side.SERVER).restart(Restart.GAME);

    /** Without this the tongue still goes, at vanilla's length, and the fly still vanishes: this stretches it to reach, and draws the fly on the end of it going back in. */
    public static final Entry FLY_ON_TONGUE = Entries.feature("spawn_frogs_eat_flies_on_tongue", Taste.NONE)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .side(Side.CLIENT).restart(Restart.GAME);

    private SpawnFrogsEatFlies() {
    }
}
