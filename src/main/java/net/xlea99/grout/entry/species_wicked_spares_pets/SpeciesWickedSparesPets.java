package net.xlea99.grout.entry.species_wicked_spares_pets;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpeciesWickedSparesPets {
    /** Haunting is decided on the server. */
    public static final Entry ENTRY = Entries.fix("species_wicked_spares_pets", Taste.NONE).home("species")
        .requiresMods("species")
        .side(Side.SERVER).restart(Restart.GAME);

    private SpeciesWickedSparesPets() {
    }
}
