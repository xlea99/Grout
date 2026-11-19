package net.xlea99.grout.boot;

import java.util.List;
import net.xlea99.guppylib.boot.ModCatalog;
import net.xlea99.guppylib.core.FamilySpec;
import net.xlea99.guppylib.core.ModSpec;
import net.xlea99.grout.Grout;
import net.xlea99.grout.core.Tastes;

/**
 * Who Grout is, to the shared machinery: its family ({@link #FAMILY}: a family of one, with its config files, its menu
 * and its taste rule), the mod itself ({@link #SPEC}: its id, its name, where its entries' mixins live), and everything
 * it hands over at startup ({@link #CATALOG}: that and its declaration classes; its entries go into its family's
 * registry). Its own class, apart from {@link Grout}, because the mixin plugin reads it before Minecraft's classes are
 * safe to load ({@code MODID} is a constant, so naming it here loads nothing).
 */
public final class GroutSpec {
    public static final FamilySpec FAMILY = new FamilySpec(Grout.MODID, "Grout", Tastes.RULE,
        List.of(new FamilySpec.Member(Grout.MODID, "net.xlea99.grout.boot.GroutSpec")));

    public static final ModSpec SPEC = new ModSpec(Grout.MODID, "Grout", "net.xlea99.grout.mixin.", FAMILY);

    public static final ModCatalog CATALOG = new ModCatalog(SPEC, Catalog.declarations());

    private GroutSpec() {
    }
}
