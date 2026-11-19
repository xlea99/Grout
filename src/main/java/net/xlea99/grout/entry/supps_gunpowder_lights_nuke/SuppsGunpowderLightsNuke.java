package net.xlea99.grout.entry.supps_gunpowder_lights_nuke;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsGunpowderLightsNuke {
    /** Gunpowder burns on the server. */
    public static final Entry ENTRY = Entries.fix("supps_gunpowder_lights_nuke", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private SuppsGunpowderLightsNuke() {
    }
}
