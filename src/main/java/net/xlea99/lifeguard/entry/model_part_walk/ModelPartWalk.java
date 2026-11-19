package net.xlea99.lifeguard.entry.model_part_walk;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;
import net.xlea99.lifeguard.entry.keyframe_part_lookup.KeyframePartLookup;

public final class ModelPartWalk {
    /**
     * Dangerous for now (the user, 2026-11-16): a vanilla performance fix touching how every model's parts are walked, on its
     * own switch so it can be told apart from {@link KeyframePartLookup}, beside which the menu shows it (it doesn't need it).
     * Client side: drawing only.
     */
    public static final Entry ENTRY = Entries.fix("model_part_walk", Taste.DANGEROUS)
        .shownUnder(KeyframePartLookup.ENTRY)
        .side(Side.CLIENT).restart(Restart.GAME);

    private ModelPartWalk() {
    }
}
