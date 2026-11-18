package net.xlea99.lifeguard.boot;

import net.xlea99.lifeguard.Lifeguard;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.ModSpec;
import net.xlea99.lifeguard.core.Tastes;

/**
 * Who Lifeguard is, to the shared machinery: its id, its name, where its entries' mixins live and its taste rule
 * ({@link #SPEC}), and everything it hands over at startup ({@link #CATALOG}: that, its registry and its declaration
 * classes). Its own class, apart from {@link Lifeguard}, because the mixin plugin reads it before Minecraft's classes are
 * safe to load ({@code MODID} is a constant, so naming it here loads nothing).
 */
public final class LifeguardSpec {
    public static final ModSpec SPEC = new ModSpec(Lifeguard.MODID, "Lifeguard", "net.xlea99.lifeguard.mixin.", Tastes.RULE);

    public static final ModCatalog CATALOG = new ModCatalog(SPEC, Entries.REGISTRY, Catalog.declarations());

    private LifeguardSpec() {
    }
}
