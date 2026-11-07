package net.xlea99.lifeguard.entry.connectiblechains_collisions;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class ConnectibleChainsCollisions {
    /**
     * None: with Reconnectible Chains, Lithium and Alex's Caves together, chains have no collision (proven 2026-11-05 by
     * turning off Lithium's {@code entity.collisions.movement}). Chains adds its shapes in {@code collectColliders} and in
     * Lithium's {@code appendEntityCollisions}. Lithium's overwrite of {@code collideBoundingBox} stops calling the first,
     * and only reaches the second from inside {@code Entity.collide}, which Alex's Caves cancels for every entity, calling
     * {@code collideBoundingBox} itself. Runs on both sides, so it's runtime-gated. (Chains 2.5.0, Lithium 0.15.4, AC 2.0.2.)
     */
    public static final Entry ENTRY = Entries.compat("connectiblechains_collisions", Taste.NONE).home("connectiblechains")
        .requiresMods("connectiblechains", "lithium", "alexscaves")
        .side(Side.SERVER).restart(Restart.GAME);

    private ConnectibleChainsCollisions() {
    }
}
