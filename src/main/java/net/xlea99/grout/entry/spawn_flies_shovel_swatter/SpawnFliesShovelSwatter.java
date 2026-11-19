package net.xlea99.grout.entry.spawn_flies_shovel_swatter;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpawnFliesShovelSwatter {
    /**
     * Swatting one of Spawn's flies in an inventory while holding a shovel kills it outright, for a point of durability. The tag {@code grout:fly_swatters} says what counts. (Spawn 4.0.8.)
     */
    public static final Entry ENTRY = Entries.feature("grout:spawn_flies_shovel_swatter", Taste.LIGHT).home("spawn")
        .requiresMods("spawn")
        .side(Side.SERVER).restart(Restart.GAME);

    private SpawnFliesShovelSwatter() {
    }
}
