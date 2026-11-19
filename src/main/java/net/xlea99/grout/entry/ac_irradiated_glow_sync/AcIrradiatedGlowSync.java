package net.xlea99.grout.entry.ac_irradiated_glow_sync;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AcIrradiatedGlowSync {
    /** Only tells clients a mob is irradiated, which it already is on the server. */
    public static final Entry ENTRY = Entries.fix("grout:ac_irradiated_glow_sync", Taste.NONE)
        .home("alexscaves")
        .requiresMods("alexscaves")
        .usesIds("mob_effect", "alexscaves:irradiated")
        .side(Side.SERVER).restart(Restart.GAME);

    private AcIrradiatedGlowSync() {
    }
}
