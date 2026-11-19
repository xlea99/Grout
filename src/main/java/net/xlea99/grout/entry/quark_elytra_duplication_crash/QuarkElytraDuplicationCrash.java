package net.xlea99.grout.entry.quark_elytra_duplication_crash;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class QuarkElytraDuplicationCrash {
    /**
     * Runtime-gated: crafting runs on both sides (the client predicts the result slot), so the client needs the mixin
     * too, and it has to follow the server's setting rather than the player's local file.
     */
    public static final Entry ENTRY = Entries.fix("grout:quark_elytra_duplication_crash", Taste.NONE).home("quark")
        .requiresMods("quark")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private QuarkElytraDuplicationCrash() {
    }
}
