package net.xlea99.grout.entry.am_slightly_sexier_gorilla;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmSlightlySexierGorilla {
    /**
     * Strong: it changes how another mod's mob moves and lives. A troop used to circle the nearest trees eating leaves, so it
     * never seemed to go anywhere. Now most mornings, and some middays, a wild Silverback marches its troop off in one
     * direction for a while, nobody stopping to forage on the way, staying within {@code #grout:gorilla_home_biomes}, and
     * waiting when most of the troop falls behind. Alex's single-file line gives way to a loose troop: each Gorilla keeps
     * within reach of its own Silverback, spread out, and finds its way back from hollows; one stranded for good gives up after
     * a few tries and its group raises its own Silverback. In a trial in a slice of a real jungle, a marching troop of 8 kept
     * within 16 blocks of its Silverback 98% of the time, for 0.085 ms of pathing per tick. An angry Gorilla closes in faster
     * (Alex's chased you slower than you walk). (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_gorilla", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * Alex's Gorilla walks like a trotting dog, its diagonal limbs swinging together by one sine scaled by speed, so at a march
     * its hands and feet slid and slower it tiptoed. Now it knuckle-walks ({@link GorillaMotion}): a four-beat walk where each
     * long arm plants and stays planted while the body vaults over it, shoulders rolling and twisting, short hind legs pattering
     * underneath; a charge is a bounding gallop, and upright it waddles. Client side: it only changes how the model is posed, so
     * it needs nothing from the server and doesn't require {@link #ENTRY}, only sits under it in the menu.
     */
    public static final Entry MOTION = Entries.tweak("am_slightly_sexier_gorilla_motion", Taste.STRONG).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierGorilla() {
    }
}
