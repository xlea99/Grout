package net.xlea99.lifeguard.entry.am_slightly_sexier_grizzly_bear;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AmSlightlySexierGrizzlyBear {
    /**
     * Strong: it changes how another mod's mob looks. A modest pass on Alex's Mobs' Grizzly Bear, on its own model with no new
     * geometry: on all fours its walk becomes a bear's rolling pace and its run a gallop ({@link GrizzlyMotion}), and it breathes,
     * looks about and flicks its ears. Sitting, standing, mauling, eating and sniffing stay Alex's. Client only. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_grizzly_bear", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierGrizzlyBear() {
    }
}
