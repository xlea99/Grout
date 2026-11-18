package net.xlea99.lifeguard.core;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * What a switch set to Auto means, for one mod's entries. The {@link #PLAIN plain rule} follows each entry's own
 * {@link Entry#defaultOn default}; a mod may bring its own instead, along with a setting per side that the rule reads
 * (one value from a fixed list, kept in that side's config file and synced from the server like the switches).
 *
 * <p>A rule is asked at startup, before Minecraft's classes are safe to load (it decides which mixins load), so it may
 * only read its own data and the entry's declaration. It rides with the mod's {@link ModSpec} for that reason.
 */
public interface AutoRule {
    /** Auto = the entry's own default; no setting. */
    AutoRule PLAIN = new AutoRule() {
        @Override
        public Setting setting() {
            return null;
        }

        @Override
        public boolean autoOn(Entry entry, String choice) {
            return entry.defaultOn();
        }

        @Override
        public String toString() {
            return "AutoRule.PLAIN";
        }
    };

    /** The per-side setting this rule reads, or null when it reads none. */
    Setting setting();

    /** Whether Auto means on for {@code entry}, given its side's {@code choice} of the setting (null without one). */
    boolean autoOn(Entry entry, String choice);

    /**
     * What's wrong with how {@code entry} was declared, as far as this rule is concerned, or null: checked at the
     * freeze, with every other declaration problem.
     */
    default String check(Entry entry) {
        return null;
    }

    // --- its words in the config file (English, like the rest of that file) ------------------------------------------

    /** What Auto follows, for the file's header: "Auto follows <this>; on and off override it." */
    default String autoFollows() {
        return "each entry's own default";
    }

    /** A word or two about the entry for its summary line ("Feature · <this> · needs nothing"), or null for none. */
    default String summaryTag(Entry entry) {
        return null;
    }

    /** The line saying what Auto does for this entry, given its side's {@code choice}. */
    default String autoNote(Entry entry, String choice) {
        return "auto = " + (autoOn(entry, choice) ? "on" : "off");
    }

    /**
     * A rule's setting: its key in the config file, the values it takes (in the order a player sees them) and the one
     * a fresh file starts with.
     */
    record Setting(String key, List<String> choices, String defaultChoice) {
        private static final Pattern NAME = Pattern.compile("[a-z][a-z0-9_]*");

        public Setting {
            Objects.requireNonNull(key);
            choices = List.copyOf(choices);
            Objects.requireNonNull(defaultChoice);
            if (!NAME.matcher(key).matches())
                throw new IllegalArgumentException("setting key '" + key + "' isn't lowercase letters, digits and _");
            if (choices.isEmpty())
                throw new IllegalArgumentException(key + ": a setting needs at least one choice");
            if (new HashSet<>(choices).size() != choices.size())
                throw new IllegalArgumentException(key + ": the same choice twice in " + choices);
            if (!choices.contains(defaultChoice))
                throw new IllegalArgumentException(key + ": the default '" + defaultChoice + "' isn't one of " + choices);
        }

        public boolean accepts(String choice) {
            return choices.contains(choice);
        }
    }
}
