package net.xlea99.grout.entry.citadel_iris_post_effects;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CitadelIrisPostEffects {
    /** Only when entities are drawn within the frame. */
    public static final Entry ENTRY = Entries.compat("citadel_iris_post_effects", Taste.NONE)
        .home("citadel")
        .requiresMods("citadel", "iris")
        .side(Side.CLIENT).restart(Restart.GAME);

    private CitadelIrisPostEffects() {
    }
}
