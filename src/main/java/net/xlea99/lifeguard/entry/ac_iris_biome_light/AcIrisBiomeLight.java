package net.xlea99.lifeguard.entry.ac_iris_biome_light;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AcIrisBiomeLight {
    /**
     * None: Primordial Caves and Candy Cavity are meant to look bright with no real light, which Alex's Caves does in the
     * vanilla lightmap. Shaderpacks work out their own lighting and don't read it, so these draw as any other dark cave.
     * This has the pack see sky light there instead, at one fixed time of day, so its own lighting and fog do the work,
     * from the biome's own sky color. Thick biome fog, which packs don't read either, is drawn over their picture
     * ({@link BiomeFog}). Does nothing with no shaderpack on. (Alex's Caves 2.0.2, Iris 1.8.14-beta.1.)
     */
    public static final Entry ENTRY = Entries.compat("ac_iris_biome_light", Taste.NONE).home("alexscaves")
        .requiresMods("alexscaves", "iris", "sodium")
        .side(Side.CLIENT).restart(Restart.GAME);

    private AcIrisBiomeLight() {
    }
}
