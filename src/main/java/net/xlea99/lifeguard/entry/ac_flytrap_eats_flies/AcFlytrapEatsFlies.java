package net.xlea99.lifeguard.entry.ac_flytrap_eats_flies;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AcFlytrapEatsFlies {
    /**
     * Alex's Caves' Flytrap opens and shuts by itself and catches nothing. Anything carrying Spawn's flies that walks through an open one now sometimes gets one snapped off it, shutting the
     * trap for a while. Named flies are safe. (Alex's Caves 2.0.2, Spawn 4.0.8.)
     */
    public static final Entry ENTRY = Entries.compat("ac_flytrap_eats_flies", Taste.LIGHT).home("alexscaves")
        .requiresMods("alexscaves", "spawn")
        .side(Side.SERVER).restart(Restart.GAME);

    private AcFlytrapEatsFlies() {
    }
}
