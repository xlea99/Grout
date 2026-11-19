package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmSlightlySexierRattlesnake {
    /**
     * Strong: it changes how another mod's mob lives. A Rattlesnake is an ambush hunter, but Alex's wandered about, curled now
     * and then for 15 to 27 seconds, chased rabbits and Jerboas from 15 blocks off, and crawled after a player to bite. Now a
     * grown one waits ({@link SnakeAmbushGoal}): coiled and silent for one to three minutes, then a short slither to a new spot,
     * and coiled again. Prey has to come to it: a rabbit or Jerboa that hops within reach, or anyone who steps within reach, gets
     * a lunge from the coil ({@link SnakeStrikeGoal}) with Alex's own bite. Prey it has gone for is dinner: it follows it, up to
     * 32 blocks, striking again in reach. Anyone else it doesn't chase, and once you're out of reach it lets you go. What it
     * does about a player follows how worked up it is ({@link SnakeMind}) rather than Alex's fixed rings (a warning within 5
     * blocks, a bite within 2): how close you are, how loud, whether you're coming at it, so you may be warned from 7 blocks
     * or sneak to 2. Each snake has its own temper, patience and roaming ({@link SnakeTemper}). (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_rattlesnake", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * Alex's Rattlesnake waggled side to side by one swing scaled by its speed, so it lay dead straight whenever it stopped and
     * barely wriggled when it crept. Now ({@link SnakeMotion}) it slithers: an S wave travels down its body as fast as it moves,
     * so the body follows the head's path while the head stays pointed where it's going; and it never lies straight, resting in
     * the S it stopped in, which slowly shifts, its head a little raised. Its coil eases in and out, and its tail lifts into
     * the rattle and back down rather than jumping. Coiled and resting, its head lies on its tail and lifts as far as the
     * server says it's watching; asleep, its eyes close (a sleeping texture made from its own, {@link SleepingRattlesnakeTexture})
     * and it breathes slowly. Client side: without our server entry there, the head lifts for anything near and it never sleeps.
     */
    public static final Entry MOTION = Entries.tweak("am_slightly_sexier_rattlesnake_motion", Taste.STRONG).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    /**
     * A sleeping Rattlesnake breathes out Alex's Caves' sleep particles, the little Zs its Gummy Bears sleep with, in the same
     * burst of three every two and a half seconds. Only drawn on the client, from the sleep {@link #MOTION} is told of, so it
     * sends nothing and adds nothing to any registry. Its own entry because it needs Alex's Caves too.
     */
    public static final Entry ZS = Entries.tweak("am_slightly_sexier_rattlesnake_zs", Taste.LIGHT).shownUnder(MOTION)
        .requiresMods("alexsmobs", "alexscaves").requiresEntries(MOTION)
        .usesIds("particle_type", "alexscaves:sleep")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierRattlesnake() {
    }
}
