package net.xlea99.lifeguard.entry.am_slightly_sexier_gazelle;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AmSlightlySexierGazelle {
    /**
     * Strong: it changes how another mod's mob looks. A pass on Alex's Mobs' Gazelle, on its own model with no new geometry: its feet plant
     * when it walks, and when it's afraid it runs in real bounds, with hang time in the air ({@link GazelleMotion}). Eating, flicks and
     * looking stay Alex's. Client only. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_gazelle", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierGazelle() {
    }
}
