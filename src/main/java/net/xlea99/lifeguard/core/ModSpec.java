package net.xlea99.lifeguard.core;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Who a set of entries belongs to: the mod that declares them. Its files, its mixins and its texts are all named after
 * it, so the machinery never hardcodes a mod. Each mod hands its own to {@code Boot.start}.
 *
 * @param id         the mod id, which also names its config files, lang file, mixin config and entry texts
 * @param name       its display name, for the log and the config files' headers
 * @param mixinRoot  the package its entries' mixins live under, ending in a dot ({@code net.example.mymod.mixin.})
 * @param autoRule   what Auto means for its entries: {@link AutoRule#PLAIN} (each entry's own default) or its own rule.
 *                   Here because it's asked as early as the mixin plugin, which only has this description to go on.
 */
public record ModSpec(String id, String name, String mixinRoot, AutoRule autoRule) {
    private static final Pattern ID = Pattern.compile("[a-z][a-z0-9_]*");

    public ModSpec {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(mixinRoot);
        Objects.requireNonNull(autoRule);
        if (!ID.matcher(id).matches())
            throw new IllegalArgumentException("mod id '" + id + "' isn't lowercase letters, digits and _");
        if (!mixinRoot.endsWith("."))
            throw new IllegalArgumentException("mixin root '" + mixinRoot + "' must end in a dot");
    }

    /** The mixin config in its jar: {@code <id>.mixins.json}. */
    public String mixinConfig() {
        return id + ".mixins.json";
    }

    /** Its English texts in its jar: {@code /assets/<id>/lang/en_us.json}. */
    public String langResource() {
        return "/assets/" + id + "/lang/en_us.json";
    }

    /** {@code <id>-server.toml}: the settings a server decides. */
    public String serverConfigFile() {
        return id + "-server.toml";
    }

    /** {@code <id>-client.toml}: each player's own settings. */
    public String clientConfigFile() {
        return id + "-client.toml";
    }

    /** The start of its entries' text keys: {@code <id>.entry.}. */
    public String entryKeyPrefix() {
        return id + ".entry.";
    }

    /** Its menu's title, in its own lang file: {@code <id>.menu.title}. */
    public String menuTitleKey() {
        return id + ".menu.title";
    }

    /** The menu heading over its own entries (those about no other mod): {@code <id>.menu.group.<id>}. */
    public String ownGroupKey() {
        return id + ".menu.group." + id;
    }

    /** Another mod's display name, for its entries that need that mod: {@code <id>.mod.<mod id>}. */
    public String modNameKey(String modId) {
        return id + ".mod." + modId;
    }
}
