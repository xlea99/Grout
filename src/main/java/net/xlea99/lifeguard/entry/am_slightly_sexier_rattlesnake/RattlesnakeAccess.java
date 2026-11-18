package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

/** The Rattlesnake mixin, so this entry's goals can reach Alex's members without being built against Alex's Mobs. */
public interface RattlesnakeAccess {
    boolean lifeguard$isCurled();

    void lifeguard$setCurled(boolean curled);

    boolean lifeguard$isRattling();

    void lifeguard$setRattling(boolean rattling);

    /** Alex uncurls a snake once it has been curled this many ticks (unless it's rattling); he resets it to 75 and up. */
    void lifeguard$setMaxCurlTime(int ticks);
}
