package net.xlea99.lifeguard.entry.am_gazelle_movement;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmGazelleMovement {
    /**
     * Strong: it changes how another mod's mob moves. A Gazelle wanders slower, a stroll rather than a speed walk. And when it flees, it
     * carves arcs and commits to a direction. Vanilla's pathing isn't made for its speed: it overshoots each waypoint, then spins up to 90
     * degrees in a tick to go back, and worst of all off ledges, where a waypoint can't count as reached. See {@link GazelleNavigation},
     * {@link GazelleMoveControl}, {@link GazelleFlee}. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_gazelle_movement", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGazelleMovement() {
    }
}
