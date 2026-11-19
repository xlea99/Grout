package net.xlea99.grout.entry.vanilla_backport_pause_pauses_music;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class VanillaBackportPausePausesMusic {
    /** Sound is the client's own. */
    public static final Entry ENTRY = Entries.tweak("vanilla_backport_pause_pauses_music", Taste.LIGHT)
        .home("vanillabackport")
        .requiresMods("vanillabackport")
        .side(Side.CLIENT).restart(Restart.GAME);

    private VanillaBackportPausePausesMusic() {
    }
}
