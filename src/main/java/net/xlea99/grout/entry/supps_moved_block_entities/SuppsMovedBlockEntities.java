package net.xlea99.grout.entry.supps_moved_block_entities;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsMovedBlockEntities {
    /** Ropes move blocks on the server. */
    public static final Entry ENTRY = Entries.fix("supps_moved_block_entities", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private SuppsMovedBlockEntities() {
    }
}
