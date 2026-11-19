package net.xlea99.grout.entry.haunted_harvest_mask_limit;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.guppylib.core.SubValue;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class HauntedHarvestMaskLimit {
    /** 100 of 256 sees as much as a vanilla carved pumpkin (30.9% of the overlay clear); every face HH carves fits. */
    public static final SubValue.Int MAX_CARVED = SubValue.ofInt("max_carved", 100, 0, 255);
    /** Endermen check masks on the server; the client only shows the tooltip. */
    public static final Entry ENTRY = Entries.tweak("haunted_harvest_mask_limit", Taste.LIGHT).home("hauntedharvest")
        .requiresMods("hauntedharvest")
        .side(Side.SERVER).restart(Restart.GAME)
        .values(MAX_CARVED);

    private HauntedHarvestMaskLimit() {
    }
}
