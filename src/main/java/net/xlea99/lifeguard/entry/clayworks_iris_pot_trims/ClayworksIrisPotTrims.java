package net.xlea99.lifeguard.entry.clayworks_iris_pot_trims;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class ClayworksIrisPotTrims {
    /**
     * None: with Iris loaded, shaderpack or not, a placed decorated pot draws no trim. Iris only keeps the draw order of
     * what it groups (entities and banners); ungrouped, a block entity's render types come back last used first, so the
     * trim draws before the pot's body and the body covers it, at the same depth. (Iris 1.8.14-beta.1, Clayworks 4.0.3.)
     */
    public static final Entry ENTRY = Entries.compat("clayworks_iris_pot_trims", Taste.NONE).home("clayworks")
        .requiresMods("clayworks", "iris")
        .side(Side.CLIENT).restart(Restart.GAME);

    private ClayworksIrisPotTrims() {
    }
}
