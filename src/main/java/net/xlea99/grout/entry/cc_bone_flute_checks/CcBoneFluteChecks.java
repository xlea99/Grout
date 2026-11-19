package net.xlea99.grout.entry.cc_bone_flute_checks;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CcBoneFluteChecks {
    /** The server takes the commands. See BoneFluteCommands. */
    public static final Entry ENTRY = Entries.fix("cc_bone_flute_checks", Taste.NONE).home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms")
        .side(Side.SERVER).restart(Restart.GAME);

    private CcBoneFluteChecks() {
    }
}
