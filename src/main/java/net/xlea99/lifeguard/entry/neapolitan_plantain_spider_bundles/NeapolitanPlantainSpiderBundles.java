package net.xlea99.lifeguard.entry.neapolitan_plantain_spider_bundles;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class NeapolitanPlantainSpiderBundles {
    /**
     * None: breaking a banana bundle is meant to sometimes release 1-3 plantain spiders, unless the tool has Silk Touch or
     * is shears. The check is backwards, so only Silk Touch tools release them; and the extra spiders add the same entity
     * again, which the level refuses, so there's never more than one. (Neapolitan 6.0.1.)
     */
    public static final Entry ENTRY = Entries.fix("neapolitan_plantain_spider_bundles", Taste.NONE).home("neapolitan")
        .requiresMods("neapolitan")
        .side(Side.SERVER).restart(Restart.GAME);

    private NeapolitanPlantainSpiderBundles() {
    }
}
