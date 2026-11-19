package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.world.entity.Mob;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.SnakeCues;

/**
 * Hands what Sleepy eyes knows ({@link SnakeSleep}) to the slithering ({@link SnakeCues}), so a snake's head lifts as far as
 * its mind says and it lies still asleep. The one place the behavior side names the motion side: when the suite splits,
 * this only loads with the motion mod installed. Client only.
 */
final class SleepCues implements SnakeCues.Source {
    @Override
    public float head(Mob snake) {
        return snake instanceof SnakeSleep.Holder holder ? holder.grout$sleep().head : Float.NaN;
    }

    @Override
    public boolean asleep(Mob snake) {
        return snake instanceof SnakeSleep.Holder holder && holder.grout$sleep().asleep;
    }
}
