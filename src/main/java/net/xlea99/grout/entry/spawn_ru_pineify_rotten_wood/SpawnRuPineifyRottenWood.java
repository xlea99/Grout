package net.xlea99.grout.entry.spawn_ru_pineify_rotten_wood;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SpawnRuPineifyRottenWood {
    /**
     * Deep End's call: rotten wood as a drained pine. Only textures, models and blockstates; the bark and stripped
     * side ship (Regions Unexplored's MIT art, recolored), so Regions Unexplored itself isn't needed.
     */
    public static final Entry ENTRY = Entries.tweak("grout:spawn_ru_pineify_rotten_wood", Taste.DEEP_END).home("spawn")
        .requiresMods("spawn")
        .side(Side.CLIENT).restart(Restart.GAME)
        .resourcePack();

    private SpawnRuPineifyRottenWood() {
    }
}
