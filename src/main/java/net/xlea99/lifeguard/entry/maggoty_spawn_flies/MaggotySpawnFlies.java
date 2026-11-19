package net.xlea99.lifeguard.entry.maggoty_spawn_flies;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class MaggotySpawnFlies {
    /**
     * Strong: it eats a player's food. Spawn's flies are only a joke of their own, and Alex's Mobs' maggots only come from its fly and a few rewards. This
     * joins them: the flies a player carries lay maggots in their inventory, and maggots eat the food beside them, sometimes making another fly. Mobs that die with flies
     * drop about one maggot for every five. The tag
     * {@code lifeguard:flies_cant_eat} says what's safe. (Spawn 4.0.8, Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.feature("maggoty_spawn_flies", Taste.STRONG).home("spawn")
        .requiresMods("spawn", "alexsmobs")
        .usesIds("item", "alexsmobs:maggot")
        .side(Side.SERVER).restart(Restart.GAME);

    private MaggotySpawnFlies() {
    }
}
