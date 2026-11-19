package net.xlea99.grout.entry.themed_candle_holder_flames;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ThemedCandleHolderFlames {
    /** Flames are particles: client only. */
    public static final Entry ENTRY = Entries.compat("themed_candle_holder_flames", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries", "quark")
        .side(Side.CLIENT).restart(Restart.GAME);

    /** Candles, candle cakes and candle holders on copper burn with Caverns & Chasms' cupric flames. */
    public static final Entry CUPRIC = Entries.compat("themed_candle_holder_flames_cupric", Taste.LIGHT)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .requiresMods("caverns_and_chasms")
        .side(Side.CLIENT).restart(Restart.GAME);

    private ThemedCandleHolderFlames() {
    }
}
