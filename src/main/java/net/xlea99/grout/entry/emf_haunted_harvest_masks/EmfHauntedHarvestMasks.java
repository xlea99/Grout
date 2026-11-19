package net.xlea99.grout.entry.emf_haunted_harvest_masks;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class EmfHauntedHarvestMasks {
    /** Only how the mask is drawn. */
    public static final Entry ENTRY = Entries.compat("grout:emf_haunted_harvest_masks", Taste.NONE).home("hauntedharvest")
        .requiresMods("hauntedharvest", "entity_model_features")
        .side(Side.CLIENT).restart(Restart.GAME);

    private EmfHauntedHarvestMasks() {
    }
}
