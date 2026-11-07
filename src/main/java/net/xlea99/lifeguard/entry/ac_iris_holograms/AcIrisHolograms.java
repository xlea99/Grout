package net.xlea99.lifeguard.entry.ac_iris_holograms;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AcIrisHolograms {
    /**
     * None: under a shaderpack a hologram draws as the real mob, lit and solid. Alex's Caves draws it with vanilla's entity
     * shader into a target of its own, and a post effect makes that a hologram. Iris swaps that shader for the pack's when
     * the render type asks for it, which is before the target is bound, and the pack's program draws into the world
     * instead. (Alex's Caves 2.0.2, Iris 1.8.14-beta.1.)
     */
    public static final Entry ENTRY = Entries.compat("ac_iris_holograms", Taste.NONE).home("alexscaves")
        .requiresMods("alexscaves", "iris")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AcIrisHolograms() {
    }
}
