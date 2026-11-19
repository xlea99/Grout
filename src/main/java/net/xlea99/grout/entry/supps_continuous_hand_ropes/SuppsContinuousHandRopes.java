package net.xlea99.grout.entry.supps_continuous_hand_ropes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsContinuousHandRopes {
    /** The server moves the blocks; the client only predicts whether the click took. */
    public static final Entry ENTRY = Entries.tweak("supps_continuous_hand_ropes", Taste.LIGHT).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private SuppsContinuousHandRopes() {
    }
}
