package net.xlea99.grout.entry.no_supps_jar_fish;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class NoSuppsJarFish {
    /** Runtime-gated: jars predict the bucket on the client, so the client follows the server's setting. */
    public static final Entry ENTRY = Entries.tweak("grout:no_supps_jar_fish", Taste.DEEP_END).home("supplementaries")
        .requiresMods("supplementaries")
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private NoSuppsJarFish() {
    }
}
