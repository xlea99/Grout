package net.xlea99.lifeguard.entry.spawn_flies_roam_screen;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class SpawnFliesRoamScreen {
    /**
     * Strong: more of the screen is under flies, and a click on one is a swat, so they eat more clicks. That's what they're for. Spawn's flies only ever fly to
     * somewhere inside the inventory panel. Now they wander in hops anywhere on screen, most of them keeping near the panel, and a scared one bolts anywhere. (Spawn 4.0.8.)
     */
    public static final Entry ENTRY = Entries.tweak("spawn_flies_roam_screen", Taste.STRONG).home("spawn")
        .requiresMods("spawn")
        .side(Side.CLIENT).restart(Restart.GAME);

    private SpawnFliesRoamScreen() {
    }
}
