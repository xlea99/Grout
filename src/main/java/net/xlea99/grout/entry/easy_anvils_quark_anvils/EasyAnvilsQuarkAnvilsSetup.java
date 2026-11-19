package net.xlea99.grout.entry.easy_anvils_quark_anvils;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link EasyAnvilsQuarkAnvils}. */
public final class EasyAnvilsQuarkAnvilsSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(EasyAnvilsQuarkAnvils.ENTRY, AnvilBreaks::register);
    }
}
