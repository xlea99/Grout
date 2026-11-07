package net.xlea99.lifeguard.entry.clayworks_trim_palettes;

import net.xlea99.lifeguard.boot.EntrySetup;
import net.xlea99.lifeguard.boot.Wiring;

/** Client setup for {@link ClayworksTrimPalettes}. */
public final class ClayworksTrimPalettesClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(ClayworksTrimPalettes.ENTRY, PotTrimPaletteSource::register);
    }
}
