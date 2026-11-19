package net.xlea99.grout.entry.species_swapper_resets_fall;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpeciesSwapperResetsFall {
    /** The swap happens on the server. */
    public static final Entry ENTRY = Entries.tweak("grout:species_swapper_resets_fall", Taste.LIGHT).home("species")
        .requiresMods("species")
        .side(Side.SERVER).restart(Restart.GAME);

    private SpeciesSwapperResetsFall() {
    }
}
