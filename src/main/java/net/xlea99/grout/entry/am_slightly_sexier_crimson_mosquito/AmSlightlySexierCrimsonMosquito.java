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
     * blocks covered in three minutes. Its spots are ahead of it (picking them that way is its own since 2026-11-19, no longer
     * a Grout fix's). (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_crimson_mosquito", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmSlightlySexierCrimsonMosquito() {
    }
}
