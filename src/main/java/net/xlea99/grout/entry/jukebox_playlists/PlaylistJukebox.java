package net.xlea99.grout.entry.jukebox_playlists;

import java.util.List;

/** A jukebox's place in its playlist ({@link Playlists}). */
public interface PlaylistJukebox {
    /** The pool slot to look from for the next disc, in order. */
    int grout$cursor();

    void grout$setCursor(int slot);

    /** The songs (their ids) shuffled this round: none plays twice until all have. */
    List<String> grout$played();

    /** The next track comes in {@code ticks} from now, unless something plays first. */
    void grout$waitForNextTrack(int ticks);

    /** Counts the wait down (once a tick); true when the next track is due now. */
    boolean grout$trackDue();

    /** Forgets a track change waiting on the silence after a song (the radio was turned off). */
    void grout$cancelNextTrack();
}
