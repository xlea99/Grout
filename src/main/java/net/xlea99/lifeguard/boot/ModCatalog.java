package net.xlea99.lifeguard.boot;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import net.xlea99.lifeguard.core.ModSpec;
import net.xlea99.lifeguard.core.Registry;

/**
 * What a mod hands the machinery at startup ({@link Boot#start}): who it is, the registry its entries declare themselves
 * into, and the classes that declare them (each one's static initializer registers its entries; see each mod's catalog).
 * Built from constants only: the mixin plugin hands it over before Minecraft's classes are safe to load.
 *
 * <p>The mod's own files (its lang file, mixin config, resource packs, recipe edits) are read through one of its own
 * classes ({@link #resource}): a class of the machinery sees only its own jar's files once the two are separate mods.
 */
public record ModCatalog(ModSpec spec, Registry registry, List<Class<?>> declarations) {
    public ModCatalog {
        Objects.requireNonNull(spec);
        Objects.requireNonNull(registry);
        declarations = List.copyOf(declarations);
        if (declarations.isEmpty())
            throw new IllegalArgumentException(spec.name() + " hands over no declaration classes");
    }

    /** Runs each declaration class's static initializer, which is where its entries register. */
    void load() {
        for (Class<?> declaration : declarations) {
            try {
                Class.forName(declaration.getName(), true, declaration.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(spec.name() + " couldn't load its declarations in " + declaration.getName(), e);
            }
        }
    }

    /** A file in the mod's jar, by absolute path ({@code /assets/...}), or null when it isn't there. */
    public URL resource(String path) {
        return declarations.get(0).getResource(path);
    }

    /** The same, opened; null when it isn't there. */
    public InputStream openResource(String path) {
        return declarations.get(0).getResourceAsStream(path);
    }
}
