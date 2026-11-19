package net.xlea99.grout.entry.cc_necromium_armor_slowness;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CcNecromiumArmorSlowness {
    /** Damage is dealt on the server. */
    public static final Entry ENTRY = Entries.fix("grout:cc_necromium_armor_slowness", Taste.NONE).home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms")
        .side(Side.SERVER).restart(Restart.GAME);

    private CcNecromiumArmorSlowness() {
    }
}
