package net.xlea99.grout.entry.moonlight_jade_contents;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class MoonlightJadeContents {
    /** Only what Jade shows; the contents are already on the client. */
    public static final Entry ENTRY = Entries.compat("moonlight_jade_contents", Taste.NONE).home("moonlight")
        .requiresMods("moonlight", "jade")
        .side(Side.CLIENT).restart(Restart.GAME);

    private MoonlightJadeContents() {
    }
}
