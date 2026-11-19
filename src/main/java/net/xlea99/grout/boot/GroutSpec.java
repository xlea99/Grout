package net.xlea99.grout.boot;

import net.xlea99.guppylib.boot.ModCatalog;
import net.xlea99.guppylib.core.ModSpec;
import net.xlea99.grout.Grout;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Tastes;

/**
 * Who Grout is, to the shared machinery: its id, its name, where its entries' mixins live and its taste rule
 * ({@link #SPEC}), and everything it hands over at startup ({@link #CATALOG}: that, its registry and its declaration
 * classes). Its own class, apart from {@link Grout}, because the mixin plugin reads it before Minecraft's classes are
 * safe to load ({@code MODID} is a constant, so naming it here loads nothing).
 */
public final class GroutSpec {
    public static final ModSpec SPEC = new ModSpec(Grout.MODID, "Grout", "net.xlea99.grout.mixin.", Tastes.RULE);

    public static final ModCatalog CATALOG = new ModCatalog(SPEC, Entries.REGISTRY, Catalog.declarations());

    private GroutSpec() {
    }
}
