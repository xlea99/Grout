package net.xlea99.grout.entry.snowy_spirit_eggnog_nerf;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SnowySpiritEggnogNerf {
    /** Food is an item's default component, set at startup: client and server must agree. */
    public static final Entry ENTRY = Entries.tweak("snowy_spirit_eggnog_nerf", Taste.DEEP_END).home("snowyspirit")
        .requiresMods("snowyspirit")
        .side(Side.SERVER).restart(Restart.GAME).registryShaping();

    private SnowySpiritEggnogNerf() {
    }
}
