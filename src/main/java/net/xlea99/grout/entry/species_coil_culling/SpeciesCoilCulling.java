package net.xlea99.grout.entry.species_coil_culling;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpeciesCoilCulling {
    /** Culling is the client's. */
    public static final Entry ENTRY = Entries.fix("species_coil_culling", Taste.NONE).home("species")
        .requiresMods("species")
        .side(Side.CLIENT).restart(Restart.GAME);

    private SpeciesCoilCulling() {
    }
}
