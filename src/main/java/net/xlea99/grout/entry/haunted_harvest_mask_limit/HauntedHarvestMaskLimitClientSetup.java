package net.xlea99.grout.entry.haunted_harvest_mask_limit;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link HauntedHarvestMaskLimit}. */
public final class HauntedHarvestMaskLimitClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // The server owns this one: the tooltip checks the synced value each time.
        wiring.whenInstalled(HauntedHarvestMaskLimit.ENTRY, HollowPumpkins::registerTooltip);
    }
}
