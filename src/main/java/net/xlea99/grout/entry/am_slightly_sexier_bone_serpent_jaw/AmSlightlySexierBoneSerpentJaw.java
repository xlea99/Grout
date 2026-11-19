package net.xlea99.grout.entry.am_slightly_sexier_bone_serpent_jaw;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;
import net.xlea99.grout.entry.am_slightly_sexier_bone_serpent.AmSlightlySexierBoneSerpent;

public final class AmSlightlySexierBoneSerpentJaw {
    /**
     * It has no attack animation, only a small wobble as it moves. Now its jaws unhinge when it leaps at its prey, and snap when
     * it bites ({@link SerpentMaw}). It needs the behavior by id: when the serpent hunts and bites is only told to clients by
     * the behavior's server hook (the aggressive flag and an arm swing), so without it the jaw has nothing to go on. Shown under
     * the behavior until the move, where the mob heading replaces that.
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_bone_serpent_jaw", Taste.NONE)
        .requiresEntries("grout:am_slightly_sexier_bone_serpent").shownUnder(AmSlightlySexierBoneSerpent.ENTRY)
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierBoneSerpentJaw() {
    }
}
