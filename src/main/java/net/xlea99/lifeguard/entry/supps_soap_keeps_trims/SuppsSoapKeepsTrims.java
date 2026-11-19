package net.xlea99.lifeguard.entry.supps_soap_keeps_trims;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class SuppsSoapKeepsTrims {
    /**
     * Light: Supplementaries 4.1.2 chose to have soap wash armor trims off, with no config for it. This puts 4.0.2's soap
     * back: trims stay. Runtime-gated: crafting previews its result on the client, and EMI's example recipes are built
     * there.
     */
    public static final Entry ENTRY = Entries.tweak("supps_soap_keeps_trims", Taste.LIGHT).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private SuppsSoapKeepsTrims() {
    }
}
