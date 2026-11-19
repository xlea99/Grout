package net.xlea99.grout.entry.dye_depot_sparklers;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class DyeDepotSparklers {
    /**
     * Runtime-gated: the 16 sparklers are registered whenever both mods are installed, never by the switch (a world may
     * hold them), and dyeing a sparkler runs on both sides. Off, their recipes and creative tab entries go, and a Dye
     * Depot dye no longer recolors a sparkler.
     */
    public static final Entry ENTRY = Entries.compat("grout:dye_depot_sparklers", Taste.NONE).home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms", "dye_depot")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private DyeDepotSparklers() {
    }
}
