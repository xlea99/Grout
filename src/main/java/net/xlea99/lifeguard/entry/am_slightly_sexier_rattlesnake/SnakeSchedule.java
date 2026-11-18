package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import java.util.UUID;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake.SnakeAmbushGoal.Phase;

/**
 * Each Rattlesnake's own clock, so that a whole desert of them doesn't change phase in step (the user, 2026-11-16: "noon
 * comes and they move in LOCKSTEP to take their naps"). Its whole day runs early or late by its own shift (up to
 * {@code SHIFT} ticks either way, from its UUID: early birds and night owls), and each day every phase change moves by up to
 * {@code WOBBLE} more, differently each day. Worked out from its UUID and the day, so it's the same after a reload and on
 * both sides.
 */
public final class SnakeSchedule {
    static final int SHIFT = 2000, WOBBLE = 600;

    private SnakeSchedule() {
    }

    public static Phase phase(PathfinderMob snake) {
        long dayTime = snake.level().getDayTime();
        UUID id = snake.getUUID();
        long shifted = dayTime - shift(id);
        long day = Math.floorDiv(shifted, 24000L);
        long time = Math.floorMod(shifted, 24000L);
        if (time < SnakeAmbushGoal.MORNING_TO + wobble(id, day, 0) || time >= SnakeAmbushGoal.NIGHT_TO + wobble(id, day, 3))
            return Phase.MORNING;
        if (time < SnakeAmbushGoal.NAP_TO + wobble(id, day, 1))
            return Phase.NAP;
        return time < SnakeAmbushGoal.HUNT_TO + wobble(id, day, 2) ? Phase.HUNT : Phase.NIGHT;
    }

    /** Its own shift, ticks early (negative) or late. */
    static long shift(UUID id) {
        return Math.round(Mth.lerp(unit(mix(id.getLeastSignificantBits() ^ 0x5DEECE66DL)), -SHIFT, SHIFT));
    }

    /** That day's wobble of phase change {@code which}, ticks either way. */
    static long wobble(UUID id, long day, int which) {
        long h = mix(id.getMostSignificantBits() ^ day * 0x9E3779B97F4A7C15L ^ (long) which * 0xC2B2AE3D27D4EB4FL);
        return Math.round(Mth.lerp(unit(h), -WOBBLE, WOBBLE));
    }

    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }

    private static double unit(long bits) {
        return (bits & 0xFFFFF) / (double) 0xFFFFF;
    }
}
