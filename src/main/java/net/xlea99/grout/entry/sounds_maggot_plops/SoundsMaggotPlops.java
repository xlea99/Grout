package net.xlea99.grout.entry.sounds_maggot_plops;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SoundsMaggotPlops {
    /**
     * Sounds gives every item a sound for moving it around an inventory, chosen by definitions any resource pack can add. This entry's pack adds one, so that
     * a maggot plops. Only a definition: no code. (Sounds 2.4.22.)
     */
    public static final Entry ENTRY = Entries.compat("grout:sounds_maggot_plops", Taste.LIGHT).home("sounds")
        .requiresMods("sounds", "alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME)
        .resourcePack();

    private SoundsMaggotPlops() {
    }
}
