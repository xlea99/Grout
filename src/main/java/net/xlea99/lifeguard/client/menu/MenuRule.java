package net.xlea99.lifeguard.client.menu;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.xlea99.lifeguard.core.AutoRule;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Side;

/**
 * The menu's side of a mod's {@link AutoRule}: its badges (if any), how its setting (if any) reads, what it asks before
 * a change, and its words about Auto. {@link #PLAIN} is the menu of a mod on the plain rule: no setting bar, no badges,
 * and Auto explained as each entry's own default.
 *
 * <p>A rule WITH a setting must name its choices and their changes ({@link #choiceName}, {@link #settingChange}); the
 * defaults refuse loudly rather than show a raw id.
 */
public interface MenuRule {
    MenuRule PLAIN = new MenuRule() {
    };

    /**
     * A small colored box with a letter, drawn before an entry's name. A rule's badges are also a section of the filter
     * column (in {@link #badges()} order): {@code name} labels that chip and the details line, {@code tooltip} explains
     * the badge on hover.
     */
    record Badge(String id, String letter, int color, Component name, Component tooltip) {
    }

    /** A question asked before a change: yes goes ahead. */
    record Confirmation(Component title, Component message) {
    }

    // --- badges --------------------------------------------------------------------------------------------------------

    /** Every badge this rule gives out, in filter order; empty for none. */
    default List<Badge> badges() {
        return List.of();
    }

    /** The entry's badge, or null. */
    default Badge badge(Entry entry) {
        return null;
    }

    /** The filter column's heading over the badges. */
    default Component badgeFilterTitle() {
        throw new UnsupportedOperationException("a rule with badges titles their filter section");
    }

    // --- the rule's setting, across the top (only asked when the AutoRule has one) -------------------------------------

    default Component choiceName(String choice) {
        throw new UnsupportedOperationException("a rule with a setting names its choices in the menu");
    }

    /** One line of the restart summary: this side's setting goes from {@code before} to {@code after}. */
    default Component settingChange(Side side, String before, String after) {
        throw new UnsupportedOperationException("a rule with a setting words its own changes in the restart summary");
    }

    /** Asked before picking {@code choice}, which would turn on {@code turningOn}; null applies it at once. */
    default Confirmation confirmChoice(String choice, List<Entry> turningOn) {
        return null;
    }

    // --- switches ------------------------------------------------------------------------------------------------------

    /** Asked before a switch lands on On (by any route); null switches it at once. */
    default Confirmation confirmOn(Entry entry) {
        return null;
    }

    // --- its words about Auto ------------------------------------------------------------------------------------------

    /** The details pane's "why" line for an entry on Auto. */
    default Component autoReason(boolean on) {
        return Component.translatable(on ? "guppylib.menu.details.why.default_on" : "guppylib.menu.details.why.default_off");
    }

    /** The switch's hover help. */
    default Component switchTooltip() {
        return Component.translatable("guppylib.menu.switch.tooltip");
    }

    /** Asked before Reset overrides puts {@code count} switches back to Auto. */
    default Component resetMessage(int count) {
        return Component.translatable("guppylib.menu.confirm.reset.message", count);
    }
}
