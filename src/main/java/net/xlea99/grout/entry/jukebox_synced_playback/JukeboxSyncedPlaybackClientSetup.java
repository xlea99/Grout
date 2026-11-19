package net.xlea99.grout.entry.jukebox_synced_playback;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link JukeboxSyncedPlayback}. */
public final class JukeboxSyncedPlaybackClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // The server owns this one: the listeners check the synced switch.
        wiring.whenInstalled(JukeboxSyncedPlayback.ENTRY, ClientJukeboxSync::register);
    }
}
