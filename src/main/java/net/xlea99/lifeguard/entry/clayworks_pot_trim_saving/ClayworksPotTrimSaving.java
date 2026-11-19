package net.xlea99.lifeguard.entry.clayworks_pot_trim_saving;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class ClayworksPotTrimSaving {
    /**
     * None: trimming a placed decorated pot, or taking its trim off, never marks the pot as changed or sends the change to
     * clients. In a chunk with nothing else unsaved the trim is lost on leaving, with the item it took, and other players
     * don't see it until the chunk reloads. (Clayworks 4.0.3.)
     */
    public static final Entry ENTRY = Entries.fix("clayworks_pot_trim_saving", Taste.NONE).home("clayworks")
        .requiresMods("clayworks")
        .side(Side.SERVER).restart(Restart.GAME);

    private ClayworksPotTrimSaving() {
    }
}
