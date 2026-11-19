package net.xlea99.grout.entry.zipline_chain_pass_through;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class ZiplineChainPassThrough {
    /**
     * None: Zipline means a player holding a pickaxe to pass through chains, so they can drop onto a span from above and
     * jump off through it, but it checks for a class, {@code ChainCollisionEntity}, that Reconnectible Chains no longer has
     * (user-seen 2026-11-05). This passes them through while the attachment is held out and during the cooldown a release
     * starts, not just for holding one, so chains can still be walked with a pickaxe in hand. Runs on both sides, so it's
     * runtime-gated: see {@link ChainPassing}. (Zipline 1.5.3, Chains 2.5.0.)
     */
    public static final Entry ENTRY = Entries.compat("zipline_chain_pass_through", Taste.NONE).home("zipline")
        .requiresMods("zipline", "connectiblechains")
        .side(Side.SERVER).restart(Restart.GAME);

    private ZiplineChainPassThrough() {
    }
}
