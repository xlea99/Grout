package net.xlea99.grout.entry.citadel_animation_smoothing;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CitadelAnimationSmoothing {
    /** Rendering only. */
    public static final Entry ENTRY = Entries.fix("citadel_animation_smoothing", Taste.NONE).home("citadel")
        .requiresMods("citadel")
        .side(Side.CLIENT).restart(Restart.GAME);

    private CitadelAnimationSmoothing() {
    }
}
