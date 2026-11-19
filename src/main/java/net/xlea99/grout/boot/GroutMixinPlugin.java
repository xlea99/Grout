package net.xlea99.grout.boot;

import net.xlea99.guppylib.boot.EntryMixinPlugin;
import net.xlea99.guppylib.boot.ModCatalog;

/** Grout's mixin plugin (named in {@code grout.mixins.json}): the shared one, handed Grout. */
public final class GroutMixinPlugin extends EntryMixinPlugin {
    @Override
    protected ModCatalog mod() {
        return GroutSpec.CATALOG;
    }
}
