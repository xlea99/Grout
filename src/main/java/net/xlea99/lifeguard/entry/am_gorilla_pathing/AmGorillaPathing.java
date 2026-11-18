package net.xlea99.lifeguard.entry.am_gorilla_pathing;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AmGorillaPathing {
    /**
     * Three Gorilla pathing bugs. Citadel's pathfinder, which Alex's Mobs gives it, plans any mob one to two blocks wide as one
     * column, so Gorillas (1.15, Silverbacks 1.35) were routed into gaps they can't fit and wedged there: vanilla's pathfinder
     * now, which plans them two columns wide. Alex marks leaves as free to path into, which Citadel ignored but vanilla's
     * honors, sending them into solid bushes: leaves are impassable again (walking on top of them is untouched). And a
     * Gorilla following the line re-planned its path every tick its last one finished, about five a second each: now only
     * when the one ahead has moved, or once a second. Trials in a slice of a real jungle: 5 of 5 legs crossed against 2, and
     * Gorilla pathing went from 0.83 to 0.25 ms per tick for 24 Gorillas (3 troops, marching). (Alex's Mobs 1.22.9, Citadel 2.7.1.)
     */
    public static final Entry ENTRY = Entries.fix("am_gorilla_pathing", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGorillaPathing() {
    }
}
