package net.xlea99.grout.entry.cc_extended_plated_bricks;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;
import net.xlea99.grout.block.PlatedBricks;

/** Setup for {@link CcExtendedPlatedBricks}. */
public final class CcExtendedPlatedBricksSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        // Each metal registers whenever its entry is installed, never by the switch: a world may hold them.
        wiring.always(PlatedBricks::register);
    }
}
