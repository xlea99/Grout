package net.xlea99.grout.entry.supps_blue_bomb_dispensing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsBlueBombDispensing {
    /** Dispensers work on the server only. */
    public static final Entry ENTRY = Entries.fix("grout:supps_blue_bomb_dispensing", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private SuppsBlueBombDispensing() {
    }
}
