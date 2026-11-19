package net.xlea99.lifeguard.boot;

import net.xlea99.guppylib.boot.EntryMixinPlugin;
import net.xlea99.guppylib.boot.ModCatalog;

/** Lifeguard's mixin plugin (named in {@code lifeguard.mixins.json}): the shared one, handed Lifeguard. */
public final class LifeguardMixinPlugin extends EntryMixinPlugin {
    @Override
    protected ModCatalog mod() {
        return LifeguardSpec.CATALOG;
    }
}
