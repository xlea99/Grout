package net.xlea99.lifeguard.entry.keyframe_part_lookup;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class KeyframePartLookup {
    /**
     * Dangerous for now (the user, 2026-11-16): a vanilla performance fix touching every keyframe-animated mob's drawing, on
     * trial in Lifeguard until a vanilla performance mod exists (see the "maybe later" list). Client side: drawing only.
     */
    public static final Entry ENTRY = Entries.fix("keyframe_part_lookup", Taste.DANGEROUS).home(Entry.MINECRAFT)
        .side(Side.CLIENT).restart(Restart.GAME);

    private KeyframePartLookup() {
    }
}
