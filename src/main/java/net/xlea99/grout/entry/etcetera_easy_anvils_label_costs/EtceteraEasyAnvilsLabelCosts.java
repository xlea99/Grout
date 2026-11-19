package net.xlea99.grout.entry.etcetera_easy_anvils_label_costs;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class EtceteraEasyAnvilsLabelCosts {
    /** Runtime-gated: the anvil works out its result on both sides. */
    public static final Entry ENTRY = Entries.compat("grout:etcetera_easy_anvils_label_costs", Taste.NONE).home("etcetera")
        .requiresMods("etcetera", "easyanvils")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private EtceteraEasyAnvilsLabelCosts() {
    }
}
