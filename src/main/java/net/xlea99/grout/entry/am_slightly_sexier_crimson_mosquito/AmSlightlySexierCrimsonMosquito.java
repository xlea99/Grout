package net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmSlightlySexierCrimsonMosquito {
    /**
     * Strong: it changes how another mod's mob moves. A Crimson Mosquito wandered in hops of 1 to 5 blocks, each at a fresh
     * height, and its flight sailed past every height it aimed for, so it bobbed up and down in place. Now it darts and cruises
     * (short sharp hops snapping onto a new line, longer straight ones), holds a plan for a few hops (cruise, climb or come
     * down) with a steady climb, hangs in the air now and then, and comes down to rest on purpose. In a trial over open ground:
     * about 16 sharp turns and 6 climb-sink reversals a minute (Alex's: 66 and 27, from turning back every hop), 60 to 90
     * blocks covered in three minutes. Its spots are ahead of it through {@code am_crimson_mosquito_fixes}, which this needs.
     * (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_crimson_mosquito", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .requiresEntries(net.xlea99.grout.entry.am_crimson_mosquito_fixes.AmCrimsonMosquitoFixes.ENTRY)
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * Alex's Mosquito flies level, its wings beating at an easy pace and its legs flapping in step. Now ({@link MosquitoMotion})
     * its wings buzz, it banks into turns and pitches into its speed, its long legs swing behind when it turns or speeds up, a
     * full belly sags and sways a beat late and pumps as it drinks, its antennae twitch, and landed it now and then rubs its
     * front legs together. Client side: it only changes how the model is posed, so it needs nothing from the server.
     */
    public static final Entry MOTION = Entries.tweak("am_slightly_sexier_crimson_mosquito_motion", Taste.STRONG).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierCrimsonMosquito() {
    }
}
