package net.xlea99.grout.entry.bucket_mob_flicker;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class BucketMobFlicker {
    /** Buckets let mobs out on the server. */
    public static final Entry ENTRY = Entries.fix("grout:bucket_mob_flicker", Taste.NONE).home(Entry.MINECRAFT)
        .side(Side.SERVER).restart(Restart.GAME);

    private BucketMobFlicker() {
    }
}
