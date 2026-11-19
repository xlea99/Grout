package net.xlea99.grout.entry.fire_resistance_no_fire_overlay;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.Grout;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class FireResistanceNoFireOverlay {
    /** Light: with Fire Resistance on, the flames over your screen go away. They're telling you about a danger you're immune to, and blocking the view of everything else in the lava. */
    public static final Entry ENTRY = Entries.tweak("fire_resistance_no_fire_overlay", Taste.LIGHT).home(Grout.MODID)
        .side(Side.CLIENT).restart(Restart.GAME);

    private FireResistanceNoFireOverlay() {
    }
}
