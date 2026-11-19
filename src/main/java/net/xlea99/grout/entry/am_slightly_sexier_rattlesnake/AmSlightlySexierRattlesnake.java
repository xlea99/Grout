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
    public static final Entry ENTRY = Entries.tweak("grout:am_slightly_sexier_rattlesnake", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * The client half of {@link #ENTRY}, Sleepy eyes: the mind tells each client how far up a snake's head is and whether it
     * sleeps (an entity event, {@link SnakeMind}), and this keeps it ({@link SnakeSleep}) and closes a sleeping snake's eyes (a
     * sleeping texture made from its own, {@link SleepingRattlesnakeTexture}). On Alex's own model as much as on the
     * slithering, which it also tells ({@link SleepCues}). Without our server entry there, nobody tells it and it does nothing.
     * Its own client entry because client hooks are decided by the client's own switches (2026-11-19).
     */
    public static final Entry SLEEP = Entries.tweak("grout:am_slightly_sexier_rattlesnake_sleep", Taste.LIGHT).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.CLIENT).restart(Restart.GAME);

    /**
     * A sleeping Rattlesnake breathes out Alex's Caves' sleep particles, the little Zs its Gummy Bears sleep with, in the same
     * burst of three every two and a half seconds, from where its head lies. Only drawn on the client, from the sleep
     * {@link #SLEEP} keeps, so it sends nothing and adds nothing to any registry; it needs no slithering (2026-11-19: the
     * user's "requires the animations" dropped once the sleep moved to the behavior side). Its own entry because it needs
     * Alex's Caves too.
     */
    public static final Entry ZS = Entries.tweak("grout:am_slightly_sexier_rattlesnake_zs", Taste.LIGHT).shownUnder(ENTRY)
        .requiresMods("alexsmobs", "alexscaves").requiresEntries(SLEEP)
        .usesIds("particle_type", "alexscaves:sleep")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierRattlesnake() {
    }
}
