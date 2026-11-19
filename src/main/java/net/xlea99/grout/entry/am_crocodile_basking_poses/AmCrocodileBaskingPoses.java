package net.xlea99.grout.entry.am_crocodile_basking_poses;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmCrocodileBaskingPoses {
    /**
     * Alex's Mobs has two basking poses for its Crocodile, curving either way, and picks with {@code random.nextInt(1)}, which is always 0. It
     * picks from both now. Only the model reads it, and the client picks its own. (Alex's Mobs 1.22.9, ISSUES #172.)
     */
    public static final Entry ENTRY = Entries.fix("grout:am_crocodile_basking_poses", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmCrocodileBaskingPoses() {
    }
}
