package net.xlea99.lifeguard.entry.am_gazelle_step_height;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmGazelleStepHeight {
    /**
     * Alex's Mobs gives its Grizzly Bear and others a step height of a block; the Gazelle keeps vanilla's 0.6. So fleeing at full speed it
     * stops dead at every block in its way and jumps it, one at a time. It steps up a block now, and keeps running.
     */
    public static final Entry ENTRY = Entries.tweak("am_gazelle_step_height", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .usesIds("entity_type", "alexsmobs:gazelle")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGazelleStepHeight() {
    }
}
