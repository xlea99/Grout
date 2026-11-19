package net.xlea99.grout.entry.ac_radiation_glow_batching;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AcRadiationGlowBatching {
    /** Only how many times the same glow is set up per frame. */
    public static final Entry ENTRY = Entries.fix("grout:ac_radiation_glow_batching", Taste.NONE)
        .home("alexscaves")
        .requiresMods("alexscaves")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AcRadiationGlowBatching() {
    }
}
