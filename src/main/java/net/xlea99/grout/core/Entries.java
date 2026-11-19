package net.xlea99.grout.core;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Families;
import net.xlea99.guppylib.core.Registry;
import net.xlea99.grout.boot.GroutSpec;

/**
 * Where Grout's entries are declared: {@code Entries.feature("grout:jukebox_playlists", Taste.LIGHT)} adds to Grout's
 * family's one registry, taste included ({@link Tastes}).
 */
public final class Entries {
    public static final Registry REGISTRY = Families.registry(GroutSpec.FAMILY);

    private Entries() {
    }

    public static Entry feature(String id, Taste taste) {
        return Tastes.feature(REGISTRY, id, taste);
    }

    public static Entry tweak(String id, Taste taste) {
        return Tastes.tweak(REGISTRY, id, taste);
    }

    public static Entry compat(String id, Taste taste) {
        return Tastes.compat(REGISTRY, id, taste);
    }

    public static Entry fix(String id, Taste taste) {
        return Tastes.fix(REGISTRY, id, taste);
    }
}
