package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.AmSlightlySexierRattlesnake;

public final class AmSlightlySexierRattlesnakeMotion {
    /**
     * Alex's Rattlesnake waggled side to side by one swing scaled by its speed, so it lay dead straight whenever it stopped and
     * barely wriggled when it crept. Now ({@link SnakeMotion}) it slithers: an S wave travels down its body as fast as it moves,
     * so the body follows the head's path while the head stays pointed where it's going; and it never lies straight, resting in
     * the S it stopped in, which slowly shifts, its head a little raised. Its coil eases in and out, and its tail lifts into
     * the rattle and back down rather than jumping. Coiled and resting, its head lies on its tail and lifts as far as it's
     * watching; asleep, it lies still and breathes slowly. What it's doing comes from {@link SnakeCues}: its mind's word through
     * Sleepy eyes ({@code grout:am_slightly_sexier_rattlesnake_sleep}) when that's on, else a guess (the head lifts for anything
     * near, and it never sleeps). Client side; it never names the mind, which another mod holds once the suite splits.
     *
     * <p>Shown under the behavior in Grout's menu: the one place this package names the behavior's, and it goes at the move,
     * where the mob heading replaces it (the user's mob-first menu, 2026-11-19).
     */
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_rattlesnake_motion", Taste.STRONG)
        .shownUnder(AmSlightlySexierRattlesnake.ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierRattlesnakeMotion() {
    }
}
