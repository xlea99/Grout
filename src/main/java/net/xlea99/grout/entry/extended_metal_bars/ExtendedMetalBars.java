package net.xlea99.grout.entry.extended_metal_bars;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.Grout;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ExtendedMetalBars {
    /**
     * Grout's own: bars for the metals without them, netherite here. Runtime-gated: each metal's bars are registered
     * whenever its mod is installed, never by the switch (a world may hold them); off, they leave the creative tab and
     * can't be crafted. See Bars.
     */
    public static final Entry ENTRY = Entries.feature("grout:extended_metal_bars", Taste.STRONG).home(Grout.MODID)
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    /** Caverns & Chasms' necromium. */
    public static final Entry CAVERNS_AND_CHASMS = Entries.compat("grout:extended_metal_bars_cc", Taste.LIGHT)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .requiresMods("caverns_and_chasms")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    /** Alex's Caves' azure and scarlet neodymium. Their bars are magnets (magnet/Magnets): off, they stop being magnets. */
    public static final Entry ALEXSCAVES = Entries.compat("grout:extended_metal_bars_ac", Taste.LIGHT)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .requiresMods("alexscaves")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated()
        .sharesMixins("ac_magnets", "alexscaves");

    private ExtendedMetalBars() {
    }
}
