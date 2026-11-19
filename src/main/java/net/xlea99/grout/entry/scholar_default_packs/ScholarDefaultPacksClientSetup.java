package net.xlea99.grout.entry.scholar_default_packs;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link ScholarDefaultPacks}. */
public final class ScholarDefaultPacksClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(ScholarDefaultPacks.ENTRY, UnsavedPacks::register);
    }
}
