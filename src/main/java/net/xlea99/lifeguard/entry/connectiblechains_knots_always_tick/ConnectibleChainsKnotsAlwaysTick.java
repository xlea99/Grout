package net.xlea99.lifeguard.entry.connectiblechains_knots_always_tick;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class ConnectibleChainsKnotsAlwaysTick {
    /**
     * None: a chain's collision expires 3 ticks after the knot that owns it last ticked, and on the client Entity Culling
     * skips the tick of any entity it culls. This puts knots on its tick exceptions. Read, no symptom proven: turning tick
     * culling off didn't change the one inconsistency it was suspected of (2026-11-05). (Chains 2.5.0, Entity Culling 1.11.3.)
     */
    public static final Entry ENTRY = Entries.compat("connectiblechains_knots_always_tick", Taste.NONE).home("connectiblechains")
        .requiresMods("connectiblechains", "entityculling")
        .usesIds("entity_type", "connectiblechains:chain_knot")
        .side(Side.CLIENT).restart(Restart.GAME);

    private ConnectibleChainsKnotsAlwaysTick() {
    }
}
