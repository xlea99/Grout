package net.xlea99.grout.entry.supps_faucet_toggle;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SuppsFaucetToggle {
    /** The faucet's state is the server's. */
    public static final Entry ENTRY = Entries.fix("grout:supps_faucet_toggle", Taste.NONE).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME);

    private SuppsFaucetToggle() {
    }
}
