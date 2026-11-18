package net.xlea99.lifeguard.boot;

/** Lifeguard's mixin plugin (named in {@code lifeguard.mixins.json}): the shared one, handed Lifeguard. */
public final class LifeguardMixinPlugin extends EntryMixinPlugin {
    @Override
    protected ModCatalog mod() {
        return LifeguardSpec.CATALOG;
    }
}
