package net.xlea99.lifeguard.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import net.xlea99.guppylib.core.AutoRule;
import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Registry;
import net.xlea99.guppylib.core.SettingsView;
import net.xlea99.guppylib.core.Side;

/**
 * Lifeguard's taste layer on top of the shared entry model, which knows nothing of tastes: each entry carries its
 * {@link Taste} in its slot for the owning mod's data, and Auto follows the side's {@link Profile} through
 * {@link #RULE}, the mod's own {@link AutoRule}.
 */
public final class Tastes {
    private static final Map<String, Profile> PROFILES = new LinkedHashMap<>();

    static {
        for (Profile profile : Profile.values())
            PROFILES.put(profile.id(), profile);
    }

    /** Auto = on when the side's profile reaches the entry's taste. The setting is the profile, {@code profile = ...}. */
    public static final AutoRule RULE = new AutoRule() {
        private final Setting setting = new Setting("profile", new ArrayList<>(PROFILES.keySet()), Profile.DEFAULT.id());

        @Override
        public Setting setting() {
            return setting;
        }

        @Override
        public boolean autoOn(Entry entry, String choice) {
            return PROFILES.get(choice).turnsOn(of(entry));
        }

        @Override
        public String check(Entry entry) {
            return entry.modData() instanceof Taste ? null : entry.id() + ": no taste declared";
        }

        @Override
        public String autoFollows() {
            return "the profile";
        }

        @Override
        public String summaryTag(Entry entry) {
            return of(entry).id().replace('_', ' ');
        }

        @Override
        public String autoNote(Entry entry, String choice) {
            if (of(entry) == Taste.DANGEROUS)
                return "auto = always off: only \"on\" turns a dangerous entry on";
            return "auto = " + (autoOn(entry, choice) ? "on" : "off") + " under this profile";
        }

        @Override
        public String toString() {
            return "Tastes.RULE";
        }
    };

    private Tastes() {
    }

    // --- declaring -----------------------------------------------------------------------------------------------------

    public static Entry feature(Registry registry, String id, Taste taste) {
        return tasted(registry.feature(id), taste);
    }

    public static Entry tweak(Registry registry, String id, Taste taste) {
        return tasted(registry.tweak(id), taste);
    }

    public static Entry compat(Registry registry, String id, Taste taste) {
        return tasted(registry.compat(id), taste);
    }

    public static Entry fix(Registry registry, String id, Taste taste) {
        return tasted(registry.fix(id), taste);
    }

    /**
     * Records the taste, and the plain default as what the default profile gives: the rule replaces that default, so
     * it's never read, but it's never wrong either.
     */
    private static Entry tasted(Entry entry, Taste taste) {
        return entry.modData(taste).defaultOn(Profile.DEFAULT.turnsOn(taste));
    }

    // --- reading -------------------------------------------------------------------------------------------------------

    /** The entry's taste. */
    public static Taste of(Entry entry) {
        if (entry.modData() instanceof Taste taste)
            return taste;
        throw new IllegalStateException(entry.id() + " has no taste: declare it through Entries or Tastes");
    }

    /** The profile a side runs, in any state (live or a draft). */
    public static Profile profile(SettingsView view, Side side) {
        return PROFILES.get(view.ruleChoice(side));
    }
}
