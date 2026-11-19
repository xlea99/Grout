package net.xlea99.lifeguard.entry.am_slightly_sexier_roadrunner;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmSlightlySexierRoadrunner {
    /**
     * Strong: it changes how another mod's mob looks. A modest pass on Alex's Mobs' Roadrunner, on its own model with no new geometry:
     * it sprints low and flat, with weight in its tail and neck, leans into its turns, and at rest moves its head in a bird's quick
     * snaps ({@link RoadrunnerMotion}). A Roadrunner named Meep keeps its spinning legs, untouched. Client only. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_roadrunner", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierRoadrunner() {
    }
}
