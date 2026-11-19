package net.xlea99.grout.entry.clayworks_trim_palettes;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link ClayworksTrimPalettes}. */
public final class ClayworksTrimPalettesClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(ClayworksTrimPalettes.ENTRY, PotTrimPaletteSource::register);
    }
}
