package net.xlea99.grout.entry.am_crocodile_grab_turn;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmCrocodileGrabTurn {
    /**
     * Alex's Mobs turns a Crocodile's body straight around the moment it grabs something, to drag it backward, and puts its prey at its head wherever
     * that now is: so the prey jumps through it to its other end. It keeps its grip on the same side now, and turns slowly. (Alex's Mobs 1.22.9, ISSUES
     * #174.)
     */
    public static final Entry ENTRY = Entries.fix("am_crocodile_grab_turn", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmCrocodileGrabTurn() {
    }
}
