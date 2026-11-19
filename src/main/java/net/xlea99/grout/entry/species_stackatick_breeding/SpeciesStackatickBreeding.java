package net.xlea99.grout.entry.species_stackatick_breeding;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpeciesStackatickBreeding {
    /**
     * Runtime-gated: a right-click is handled on both sides (the client mustn't start a ride or a sit the server won't
     * do), so the listener is registered whenever Species is there and asks the switch. See StackatickBreeding.
     */
    public static final Entry ENTRY = Entries.fix("grout:species_stackatick_breeding", Taste.NONE).home("species")
        .requiresMods("species")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private SpeciesStackatickBreeding() {
    }
}
