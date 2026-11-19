package net.xlea99.grout.entry.am_slightly_sexier_gorilla_motion;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;
import net.xlea99.grout.entry.am_slightly_sexier_gorilla.AmSlightlySexierGorilla;

public final class AmSlightlySexierGorillaMotion {
    /**
     * Alex's Gorilla walks like a trotting dog, its diagonal limbs swinging together by one sine scaled by speed, so at a march
     * its hands and feet slid and slower it tiptoed. Now it knuckle-walks ({@link GorillaMotion}): a four-beat walk where each
     * long arm plants and stays planted while the body vaults over it, shoulders rolling and twisting, short hind legs pattering
     * underneath; a charge is a bounding gallop, and upright it waddles. Client side: it only changes how the model is posed, so
     * it needs nothing from the server and doesn't require the behavior, only sits under it in the menu (until the move, where
     * the mob heading replaces that: the one place this package names the behavior's).
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_gorilla_motion", Taste.STRONG)
        .shownUnder(AmSlightlySexierGorilla.ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierGorillaMotion() {
    }
}
