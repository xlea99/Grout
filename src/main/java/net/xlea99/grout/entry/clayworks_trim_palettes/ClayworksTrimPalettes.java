package net.xlea99.grout.entry.clayworks_trim_palettes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ClayworksTrimPalettes {
    /**
     * None: Vanilla Backport's resin and Etcetera's bismuth are trim materials, so they go on a decorated pot, but neither
     * mod has a pot palette, so the trim draws as a missing texture. Only textures, made from each mod's own armor palette
     * when the atlas loads. Bismuth shimmers here when etcetera_shimmering_bismuth_trims is on. (Clayworks 4.0.3.)
     */
    public static final Entry ENTRY = Entries.compat("grout:clayworks_trim_palettes", Taste.NONE).home("clayworks")
        .requiresMods("clayworks")
        .side(Side.CLIENT).restart(Restart.GAME)
        .resourcePack();

    private ClayworksTrimPalettes() {
    }
}
