package net.xlea99.grout.entry.big_polar_bears;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class BigPolarBears {
    /**
     * Polar bears as the largest bears they are: bigger, by vanilla's scale attribute (hitbox and model together, which every
     * client draws by itself, so this needs nothing on the client). Strong: a bigger hitbox changes where it fits and how it
     * fights. Their toughness is {@link #TOUGHER}, switchable on its own (the user, 2026-11-19).
     */
    public static final Entry ENTRY = Entries.tweak("grout:big_polar_bears", Taste.STRONG).home(Entry.MINECRAFT)
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * More health and attack damage, by their default attributes: a fight to match the size. Shown under {@link #ENTRY} but
     * not requiring it (a tougher bear works at any size). Strong: it changes a vanilla mob's fight.
     */
    public static final Entry TOUGHER = Entries.tweak("grout:tougher_polar_bears", Taste.STRONG).shownUnder(ENTRY)
        .side(Side.SERVER).restart(Restart.GAME);

    private BigPolarBears() {
    }
}
