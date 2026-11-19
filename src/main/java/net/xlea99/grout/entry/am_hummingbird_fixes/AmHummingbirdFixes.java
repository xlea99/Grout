package net.xlea99.grout.entry.am_hummingbird_fixes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmHummingbirdFixes {
    /**
     * Two Hummingbird bugs. Its feeder goal resets its own cooldown on every countdown step, so a bird that looked for a full
     * feeder and found none, or that has sipped once, never looks again until it's reloaded: the countdown now runs out.
     * Bred babies were always the first color, since breeding never picks one: they take a parent's, as Alex's other bred
     * mobs do. (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.fix("grout:am_hummingbird_fixes", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmHummingbirdFixes() {
    }
}
