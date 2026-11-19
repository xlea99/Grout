package net.xlea99.grout.entry.ac_iris_biome_light;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link AcIrisBiomeLight}. */
public final class AcIrisBiomeLightClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(AcIrisBiomeLight.ENTRY, BiomeFog::register);
    }
}
