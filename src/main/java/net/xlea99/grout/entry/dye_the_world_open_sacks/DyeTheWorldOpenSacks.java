package net.xlea99.grout.entry.dye_the_world_open_sacks;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class DyeTheWorldOpenSacks {
    /** Only models. Dye the World only adds these sacks with Supplementaries Squared installed; without it, nothing. */
    public static final Entry ENTRY = Entries.fix("dye_the_world_open_sacks", Taste.NONE).home("dye_the_world")
        .requiresMods("dye_the_world")
        .side(Side.CLIENT).restart(Restart.GAME);

    private DyeTheWorldOpenSacks() {
    }
}
