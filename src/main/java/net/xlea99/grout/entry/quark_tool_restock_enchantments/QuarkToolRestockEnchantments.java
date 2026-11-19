package net.xlea99.grout.entry.quark_tool_restock_enchantments;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class QuarkToolRestockEnchantments {
    /** Restocking happens on the server only. */
    public static final Entry ENTRY = Entries.fix("grout:quark_tool_restock_enchantments", Taste.NONE).home("quark")
        .requiresMods("quark")
        .side(Side.SERVER).restart(Restart.GAME);

    private QuarkToolRestockEnchantments() {
    }
}
