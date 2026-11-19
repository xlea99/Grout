package net.xlea99.lifeguard.entry.quark_amethyst_beacon_tint;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class QuarkAmethystBeaconTint {
    /** The beam's color only matters where it's drawn, so this is a client fix. */
    public static final Entry ENTRY = Entries.fix("quark_amethyst_beacon_tint", Taste.NONE).home("quark")
        .requiresMods("quark")
        .side(Side.CLIENT).restart(Restart.GAME);

    private QuarkAmethystBeaconTint() {
    }
}
