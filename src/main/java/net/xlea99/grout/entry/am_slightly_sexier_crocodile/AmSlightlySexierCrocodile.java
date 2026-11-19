package net.xlea99.grout.entry.am_slightly_sexier_crocodile;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmSlightlySexierCrocodile {
    /**
     * Strong: it changes how another mod's mob looks. Alex's Mobs' Crocodile walks like a stiff dog. On its own model with no new geometry, it
     * now sprawls low with its spine and tail in an S-curve, rises onto straighter legs when it hunts, and swims on a long tail wave
     * ({@link CrocodileMotion}). Basking, the lunge and the death roll stay Alex's. Client only. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_crocodile", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierCrocodile() {
    }
}
