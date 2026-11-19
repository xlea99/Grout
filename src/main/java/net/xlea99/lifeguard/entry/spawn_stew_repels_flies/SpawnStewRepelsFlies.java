package net.xlea99.lifeguard.entry.spawn_stew_repels_flies;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class SpawnStewRepelsFlies {
    /**
     * Strong: it changes what another mod's effect does. Alex's Mobs' Mosquito Repellent only ever kept off Crimson Mosquitoes. While it lasts, Spawn's flies now keep leaving a player, and any that land
     * leave at once. Named ones stay, as they do through fire and drowning. With {@code maggoty_spawn_flies} they lay nothing and hatch nothing, though maggots keep eating. (Spawn 4.0.8, Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.feature("spawn_stew_repels_flies", Taste.STRONG).home("spawn")
        .requiresMods("spawn", "alexsmobs")
        .usesIds("mob_effect", "alexsmobs:mosquito_repellent")
        .side(Side.SERVER).restart(Restart.GAME);

    /** Flies on an inventory screen flee what's in {@code lifeguard:fly_repellents} on the cursor, and flee the circle about 5x5 slots across around a slot holding it. Named ones too: he tolerates it, but he still hates it. */
    public static final Entry INVENTORY = Entries.feature("spawn_stew_repels_flies_inventory", Taste.NONE)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .side(Side.CLIENT).restart(Restart.GAME);

    private SpawnStewRepelsFlies() {
    }
}
