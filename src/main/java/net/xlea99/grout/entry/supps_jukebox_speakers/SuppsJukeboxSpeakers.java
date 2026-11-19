package net.xlea99.grout.entry.supps_jukebox_speakers;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;
import net.xlea99.grout.entry.jukebox_synced_playback.JukeboxSyncedPlayback;

public final class SuppsJukeboxSpeakers {
    /**
     * Strong: no precedent it extends, however small. Runtime-gated: the speaker's menu and puffs are the client's, its
     * messages and the range the server's, each asking the switch the server synced. See {@link JukeboxSpeakers}.
     */
    public static final Entry ENTRY = Entries.feature("supps_jukebox_speakers", Taste.STRONG).home("supplementaries")
        .requiresMods("supplementaries")
        .requiresEntries(JukeboxSyncedPlayback.ENTRY)
        .side(Side.SERVER).restart(Restart.GAME)
        .runtimeGated();

    private SuppsJukeboxSpeakers() {
    }
}
