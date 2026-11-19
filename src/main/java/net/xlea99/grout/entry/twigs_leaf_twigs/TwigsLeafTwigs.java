package net.xlea99.grout.entry.twigs_leaf_twigs;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class TwigsLeafTwigs {
    /** Data only: Twigs' own loot modifier ({@code twigs:twig_loot_modifier}), listed at NeoForge's path (Twigs lists it at Forge's, which NeoForge never reads). */
    public static final Entry ENTRY = Entries.fix("grout:twigs_leaf_twigs", Taste.NONE).home("twigs")
        .requiresMods("twigs")
        .side(Side.SERVER).restart(Restart.GAME);

    private TwigsLeafTwigs() {
    }
}
