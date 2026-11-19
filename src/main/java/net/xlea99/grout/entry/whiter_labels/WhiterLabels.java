package net.xlea99.grout.entry.whiter_labels;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class WhiterLabels {
    /** Only how placed labels look. */
    public static final Entry ENTRY = Entries.tweak("grout:whiter_labels", Taste.DEEP_END).home("labels")
        .requiresMods("labels")
        .side(Side.CLIENT).restart(Restart.GAME);

    private WhiterLabels() {
    }
}
