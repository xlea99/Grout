package net.xlea99.grout.entry.supps_moving_block_contents;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsMovingBlockContents {
    /** Only how moving blocks are drawn. */
    public static final Entry ENTRY = Entries.fix("supps_moving_block_contents", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.CLIENT).restart(Restart.GAME);

    private SuppsMovingBlockContents() {
    }
}
