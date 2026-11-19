package net.xlea99.lifeguard.core;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Registry;

/**
 * Where Lifeguard's entries are declared: {@code Entries.feature("jukebox_playlists", Taste.LIGHT)} adds to Lifeguard's
 * one registry, taste included ({@link Tastes}).
 */
public final class Entries {
    public static final Registry REGISTRY = new Registry();

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
