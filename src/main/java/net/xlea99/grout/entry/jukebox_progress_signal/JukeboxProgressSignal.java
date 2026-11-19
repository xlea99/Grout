package net.xlea99.grout.entry.jukebox_progress_signal;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;
import net.xlea99.grout.entry.jukebox_synced_playback.JukeboxSyncedPlayback;

public final class JukeboxProgressSignal {
    /** Comparators are the server's. Needs synced playback, so the reading and the music never drift apart. */
    public static final Entry ENTRY = Entries.tweak("grout:jukebox_progress_signal", Taste.STRONG).home(Entry.MINECRAFT)
        .requiresEntries(JukeboxSyncedPlayback.ENTRY)
        .side(Side.SERVER).restart(Restart.GAME);

    private JukeboxProgressSignal() {
    }
}
