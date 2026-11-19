package net.xlea99.grout.entry.ac_thornwood_very_slightly_lighter_planks;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;
import net.xlea99.grout.client.SpecRecolors;

/** Client setup for {@link AcThornwoodVerySlightlyLighterPlanks}. */
public final class AcThornwoodVerySlightlyLighterPlanksClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(AcThornwoodVerySlightlyLighterPlanks.ENTRY, () -> SpecRecolors.register(
            AcThornwoodVerySlightlyLighterPlanks.ENTRY.name(), SpecRecolors.spec("ac_thornwood"), "alexscaves"));
    }
}
