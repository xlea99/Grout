package net.xlea99.grout.entry.jukebox_progress_signal;

/** A jukebox remembering its last progress reading, to tell comparators only when it moves on. */
public interface ProgressJukebox {
    int grout$lastProgress();

    void grout$setLastProgress(int progress);
}
