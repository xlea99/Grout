package net.xlea99.grout.entry.supps_captured_mob_jitter;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsCapturedMobJitter {
    /** Visual: the renderer animates the client's copy of the captured mob. */
    public static final Entry ENTRY = Entries.fix("grout:supps_captured_mob_jitter", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.CLIENT).restart(Restart.GAME);

    private SuppsCapturedMobJitter() {
    }
}
