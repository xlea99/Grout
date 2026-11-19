package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

/** The Rattlesnake mixin, so this entry's goals can reach Alex's members without being built against Alex's Mobs. */
public interface RattlesnakeAccess {
    boolean grout$isCurled();

    void grout$setCurled(boolean curled);

    boolean grout$isRattling();

    void grout$setRattling(boolean rattling);

    /** Alex uncurls a snake once it has been curled this many ticks (unless it's rattling); he resets it to 75 and up. */
    void grout$setMaxCurlTime(int ticks);
}
