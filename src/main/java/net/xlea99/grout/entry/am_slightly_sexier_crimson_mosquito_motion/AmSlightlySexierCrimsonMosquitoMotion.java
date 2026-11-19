package net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito_motion;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;
import net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito.AmSlightlySexierCrimsonMosquito;

public final class AmSlightlySexierCrimsonMosquitoMotion {
    /**
     * Alex's Mosquito flies level, its wings beating at an easy pace and its legs flapping in step. Now ({@link MosquitoMotion})
     * its wings buzz, it banks into turns and pitches into its speed, its long legs swing behind when it turns or speeds up, a
     * full belly sags and sways a beat late and pumps as it drinks, its antennae twitch, and landed it now and then rubs its
     * front legs together. Client side: it only changes how the model is posed, so it needs nothing from the server. Shown under
     * the behavior until the move, where the mob heading replaces that (the one place this package names the behavior's).
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_crimson_mosquito_motion", Taste.STRONG)
        .shownUnder(AmSlightlySexierCrimsonMosquito.ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierCrimsonMosquitoMotion() {
    }
}
