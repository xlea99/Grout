package net.xlea99.grout.entry.vanilla_backport_lightning_rods;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class VanillaBackportLightningRods {
    /** Lightning picks its target on the server. */
    public static final Entry ENTRY = Entries.fix("grout:vanilla_backport_lightning_rods", Taste.NONE).home("vanillabackport")
        .requiresMods("vanillabackport")
        .side(Side.SERVER).restart(Restart.GAME);

    private VanillaBackportLightningRods() {
    }
}
