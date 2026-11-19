package net.xlea99.lifeguard.entry.am_crimson_mosquito_fixes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmCrimsonMosquitoFixes {
    /**
     * A Crimson Mosquito's wandering flight picked every spot behind it: Alex's angle turns half round, which his other fliers
     * (the Tarantula Hawk) undo with a negative distance, and the Mosquito's distance is positive. So each hop it turned and
     * flew back the way it came, and never got anywhere (in a trial, 0 of 1042 spots ahead of it). Its spots are now ahead of
     * it, as the goal's name ("in view") says. And one that ended up in water hung there for good, aiming at more water and
     * unable to drown: now it buzzes up out of it. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.fix("am_crimson_mosquito_fixes", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * Its flight and spit poses moved in steps, one a tick, never smoothly between: its tick saves each pose's "last tick"
     * value after changing it, not before, so the frames between ticks had nothing to blend. Taking off, its legs jumped
     * 12 degrees at a time. They now blend. Client side: it only changes how the model is drawn.
     */
    public static final Entry SMOOTH_POSES = Entries.fix("am_crimson_mosquito_smooth_poses", Taste.NONE).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmCrimsonMosquitoFixes() {
    }
}
