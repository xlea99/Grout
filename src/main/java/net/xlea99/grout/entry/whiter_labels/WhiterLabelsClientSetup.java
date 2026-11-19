package net.xlea99.grout.entry.whiter_labels;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link WhiterLabels}. */
public final class WhiterLabelsClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(WhiterLabels.ENTRY, WhiterPaper::register);
    }
}
