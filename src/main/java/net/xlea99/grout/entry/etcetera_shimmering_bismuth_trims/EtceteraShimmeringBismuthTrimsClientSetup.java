package net.xlea99.grout.entry.etcetera_shimmering_bismuth_trims;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link EtceteraShimmeringBismuthTrims}. */
public final class EtceteraShimmeringBismuthTrimsClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(EtceteraShimmeringBismuthTrims.ENTRY, ShimmerTrims::register);
    }
}
