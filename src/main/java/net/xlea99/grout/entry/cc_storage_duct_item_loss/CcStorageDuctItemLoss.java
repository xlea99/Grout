package net.xlea99.grout.entry.cc_storage_duct_item_loss;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class CcStorageDuctItemLoss {
    /** Hoppers and pipes move items on the server. See duct/StorageDucts. */
    public static final Entry ENTRY = Entries.fix("grout:cc_storage_duct_item_loss", Taste.NONE).home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms")
        .side(Side.SERVER).restart(Restart.GAME)
        .sharesMixins("cc_storage_ducts", "caverns_and_chasms");

    private CcStorageDuctItemLoss() {
    }
}
