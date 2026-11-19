package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import java.util.UUID;

/**
 * Each Rattlesnake's own nature, worked out from its UUID, so it's the same every time it loads and the same on both sides
 * without sending anything. Three traits, each anywhere between its two ends:
 * <ul>
 * <li>temper, touchy to easygoing: a touchy snake hears further, works itself up faster and strikes from further off;</li>
 * <li>patience: how long it lies in wait before it moves on;</li>
 * <li>roaming: how far it goes when it does.</li>
 * </ul>
 *
 * @param nerve how fast what it senses works it up (1 is average)
 * @param hearing how far off it senses anything, blocks
 * @param strikeReach how close a threat must come, worked up to the full, before it strikes, blocks
 * @param patience how long it waits, times the average
 * @param roam how far it moves, times the average
 */
public record SnakeTemper(float nerve, float hearing, float strikeReach, float patience, float roam) {
    static final float NERVE_CALM = 0.7F, NERVE_TOUCHY = 1.4F;
    static final float HEARING_CALM = 10F, HEARING_TOUCHY = 13F;
    static final float STRIKE_CALM = 2.0F, STRIKE_TOUCHY = 3.0F;
    static final float PATIENCE_LEAST = 0.6F, PATIENCE_MOST = 1.6F;
    static final float ROAM_LEAST = 0.6F, ROAM_MOST = 1.5F;

    public static SnakeTemper of(UUID uuid) {
        long h = mix(uuid.getMostSignificantBits() ^ Long.rotateLeft(uuid.getLeastSignificantBits(), 29));
        float touchy = unit(h), patient = unit(h >>> 21), roaming = unit(h >>> 42);
        return new SnakeTemper(lerp(touchy, NERVE_CALM, NERVE_TOUCHY), lerp(touchy, HEARING_CALM, HEARING_TOUCHY),
            lerp(touchy, STRIKE_CALM, STRIKE_TOUCHY), lerp(patient, PATIENCE_LEAST, PATIENCE_MOST), lerp(roaming, ROAM_LEAST, ROAM_MOST));
    }

    /** SplitMix64's finisher: every bit of the UUID moves every bit of the result. */
    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }

    private static float unit(long bits) {
        return (bits & 0xFFFFF) / (float) 0xFFFFF;
    }

    private static float lerp(float t, float a, float b) {
        return a + (b - a) * t;
    }
}
