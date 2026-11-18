package net.xlea99.lifeguard.entry.am_gorillas_forage_neapolitan_bananas;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AmGorillasForageNeapolitanBananas {
    /**
     * Deep End: one banana for the pack, Neapolitan's. A Gorilla tearing at banana-dropping leaves shakes loose Neapolitan's
     * banana, never Alex's Mobs' own, which that drop names in code where no tag or loot table can reach it. Taming and
     * breeding already take Neapolitan's through {@code #alexsmobs:bananas}. (Alex's Mobs 1.22.9, Neapolitan 6.0.1.)
     */
    public static final Entry ENTRY = Entries.compat("am_gorillas_forage_neapolitan_bananas", Taste.DEEP_END).home("alexsmobs")
        .requiresMods("alexsmobs", "neapolitan")
        .usesIds("item", "neapolitan:banana")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGorillasForageNeapolitanBananas() {
    }
}
