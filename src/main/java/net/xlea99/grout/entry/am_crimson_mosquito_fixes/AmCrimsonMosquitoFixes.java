package net.xlea99.grout.entry.am_crimson_mosquito_fixes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmCrimsonMosquitoFixes {
    /**
     * A Crimson Mosquito that ended up in water hung there for good, aiming at more water and unable to drown: now it buzzes
     * up out of it. (Alex's Mobs 1.22.9.) Picking its wandering spots ahead instead of behind was here too until 2026-11-19;
     * that's the Lively Crimson Mosquito behavior's now (the user: "this is NOT a bugfix at all").
     */
    public static final Entry ENTRY = Entries.fix("grout:am_crimson_mosquito_fixes", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * Its flight and spit poses moved in steps, one a tick, never smoothly between: its tick saves each pose's "last tick"
     * value after changing it, not before, so the frames between ticks had nothing to blend. Taking off, its legs jumped
     * 12 degrees at a time. They now blend. Client side: it only changes how the model is drawn.
     */
    public static final Entry SMOOTH_POSES = Entries.fix("grout:am_crimson_mosquito_smooth_poses", Taste.NONE).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmCrimsonMosquitoFixes() {
    }
}
