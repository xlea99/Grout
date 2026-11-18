package net.xlea99.lifeguard.boot;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.xlea99.lifeguard.core.AutoRule;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Library;
import net.xlea99.lifeguard.core.ModSpec;
import net.xlea99.lifeguard.core.Registry;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.config.EntryConfig;
import net.xlea99.lifeguard.core.config.Texts;
import org.slf4j.Logger;

/**
 * A mod's startup: declarations, validation, config files. Runs once, as early as anything of ours runs (the mod's mixin
 * plugin calls it before any mixin is decided), so everything after it can trust that entries resolve. Everything named
 * after the mod (its lang file, its config files, its wording) comes from the {@link ModSpec} it's started with.
 *
 * <p>This runs before Minecraft's classes are safe to load. It may use the mod loader's early classes, and nothing from
 * Minecraft itself; the workspace tests check that.
 */
public final class Boot {
    private static final Logger LOGGER = Library.LOGGER;

    private static volatile boolean started;
    private static volatile ModCatalog mod;
    private static EntryConfig clientConfig;
    private static EntryConfig serverConfig;
    private static Set<String> loadedRegistryShapers = Set.of();

    private Boot() {
    }

    /**
     * The registry-shaping entries this game loaded at startup, by id. Fixed for the session: switching one needs a
     * restart, so this, not the live switch, is what this game's registries actually look like.
     */
    public static Set<String> loadedRegistryShapers() {
        return loadedRegistryShapers;
    }

    /** The mod this game started ({@link #start}; set as soon as it claims its registry). */
    public static ModCatalog mod() {
        ModCatalog current = mod;
        if (current == null)
            throw new IllegalStateException("no mod has started yet: Boot.start(mod) comes first");
        return current;
    }

    /** Its registry: every entry the machinery serves. */
    public static Registry registry() {
        return mod().registry();
    }

    /** Every entry that declares a resource pack ships one: a missing folder is a bug in the mod, caught at startup. */
    private static void checkResourcePacks(ModCatalog mod) {
        Registry registry = mod.registry();
        List<String> missing = new ArrayList<>();
        for (Entry entry : registry.entries())
            if (entry.hasResourcePack() && mod.resource("/" + entry.resourcePackPath() + "/pack.mcmeta") == null)
                missing.add(entry.id() + " (/" + entry.resourcePackPath() + "/pack.mcmeta)");
        if (!missing.isEmpty())
            throw new IllegalStateException(registry.owner().name() + " entries declare a resource pack the jar doesn't have: " + missing);
    }

    /** Every recipe edit an entry declares ships its file: a missing one is a bug in the mod, caught at startup. */
    private static void checkRecipeEdits(ModCatalog mod) {
        Registry registry = mod.registry();
        List<String> missing = new ArrayList<>();
        for (Entry entry : registry.entries())
            for (String recipeId : entry.recipeEdits())
                if (mod.resource("/" + entry.recipeEditPath(recipeId)) == null)
                    missing.add(entry.id() + " (/" + entry.recipeEditPath(recipeId) + ")");
        if (!missing.isEmpty())
            throw new IllegalStateException(registry.owner().name() + " entries declare recipe edits the jar doesn't have: " + missing);
    }

    /**
     * Starts a mod's entries, from what it hands over ({@link ModCatalog}). Called by the mod's mixin plugin and again by
     * its mod class; runs once.
     */
    public static synchronized void start(ModCatalog given) {
        if (started) {
            // The same mod again is fine; another one is a bug, and this says so.
            if (!mod.equals(given))
                throw new IllegalStateException("started for " + mod.spec().id() + ", not " + given.spec().id());
            return;
        }
        ModSpec spec = given.spec();
        Registry registry = given.registry();
        registry.claim(spec);
        // From here on the machinery's own steps below (the preflight marking entries broken, say) can ask Boot for it.
        mod = given;
        given.load();

        Map<String, String> lang = readLang(given);
        LoadingModList mods = LoadingModList.get();
        registry.setModPresence(modId -> mods.getModFileById(modId) != null);
        // Throws, listing every problem, if a declaration is wrong: a crash here is a bug in the mod, found early.
        registry.freeze(lang.keySet());
        checkResourcePacks(given);
        checkRecipeEdits(given);

        Texts texts = new Texts(lang, Boot::modName);
        Path dir = FMLPaths.CONFIGDIR.get();
        serverConfig = open(spec, new EntryConfig(dir.resolve(spec.serverConfigFile()), Side.SERVER, registry, texts));
        // A dedicated server never reads or writes the client file: those choices belong to each player.
        if (FMLLoader.getDist().isClient())
            clientConfig = open(spec, new EntryConfig(dir.resolve(spec.clientConfigFile()), Side.CLIENT, registry, texts));

        // Every mixin that would apply is checked against what it hooks; an entry whose mixins don't fit is broken (off)
        // from here on, before anything below reads active() for good.
        MixinPreflight.run(given, dedicatedServer());

        // Mixins are decided right after this, from these same answers, so this is exactly what loaded.
        Set<String> shaping = new LinkedHashSet<>();
        for (Entry entry : registry.entries())
            if (entry.isRegistryShaping() && entry.active())
                shaping.add(entry.id());
        loadedRegistryShapers = Collections.unmodifiableSet(shaping);

        started = true;
        LOGGER.info("{}: {}", spec.name(), summary(registry));
    }

    /** The client's own file. Empty on a dedicated server. */
    public static Optional<EntryConfig> clientConfig() {
        return Optional.ofNullable(clientConfig);
    }

    public static EntryConfig serverConfig() {
        return serverConfig;
    }

    public static boolean dedicatedServer() {
        return !FMLLoader.getDist().isClient();
    }

    private static Map<String, String> readLang(ModCatalog mod) {
        ModSpec spec = mod.spec();
        String lang = spec.langResource();
        try (InputStream json = mod.openResource(lang)) {
            if (json == null)
                throw new IllegalStateException(spec.name() + "'s " + lang + " is missing from the jar");
            return Texts.readLang(json);
        } catch (IOException e) {
            throw new IllegalStateException(spec.name() + " couldn't read " + lang, e);
        }
    }

    private static EntryConfig open(ModSpec spec, EntryConfig config) {
        String name = config.file().getFileName().toString();
        String mod = spec.name();
        try {
            List<String> warnings = config.load();
            for (String warning : warnings)
                LOGGER.warn("{} {}: {}", mod, name, warning);
            if (config.broken())
                LOGGER.error("{} {} can't be read and was left untouched; defaults are in use this session", mod, name);
            else if (config.save())
                LOGGER.info("{} wrote {}", mod, name);
        } catch (IOException e) {
            // Reading or writing the file failed outright (permissions, a locked file): defaults this session.
            LOGGER.error("{} couldn't read or write {}; defaults are in use this session", mod, name, e);
        }
        return config;
    }

    /** The installed mod's display name, or null when it isn't installed (Texts then falls back to our lang file). */
    private static String modName(String modId) {
        for (ModInfo mod : LoadingModList.get().getMods())
            if (mod.getModId().equals(modId))
                return mod.getDisplayName();
        return null;
    }

    private static String summary(Registry registry) {
        int active = 0, inert = 0;
        for (Entry entry : registry.entries()) {
            if (!entry.installed())
                inert++;
            else if (entry.active())
                active++;
        }
        StringBuilder out = new StringBuilder();
        int total = registry.entries().size();
        out.append(total).append(total == 1 ? " entry, " : " entries, ").append(active).append(" active, ")
            .append(inert).append(" missing a mod");
        if (!registry.broken().isEmpty())
            out.append(", ").append(registry.broken().size()).append(" BROKEN");
        AutoRule.Setting setting = registry.rule().setting();
        if (setting != null) {
            out.append(" · server ").append(setting.key()).append(' ').append(registry.ruleChoice(Side.SERVER));
            if (clientConfig != null)
                out.append(" · client ").append(setting.key()).append(' ').append(registry.ruleChoice(Side.CLIENT));
        }
        return out.toString();
    }
}
