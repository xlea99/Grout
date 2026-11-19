package net.xlea99.grout.entry.am_crocodile_ambush;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmCrocodileAmbush {
    /**
     * Strong: it changes how another mod's mob plays, and catches far more often. A grown Crocodile sits still until something comes close, then
     * makes one fast charge. If that misses it stands there with its mouth open and won't chase again for a while, and one that's hit mostly
     * runs for the water. Tame ones rest for much less and never run. Babies stay as they were. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_crocodile_ambush", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmCrocodileAmbush() {
    }
}
