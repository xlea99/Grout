package net.xlea99.lifeguard.client.menu;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.xlea99.guppylib.client.menu.MenuRule;
import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Profile;
import net.xlea99.lifeguard.core.Taste;
import net.xlea99.lifeguard.core.Tastes;

/**
 * Lifeguard's tastes in the menu ({@link MenuRule}): a taste badge on every entry and a taste filter, the profile bar
 * across the top, and the two questions asked first (going Off the Deep End, turning on a dangerous entry).
 *
 * <p>Taste badges run cooler to hotter: gray N, steel-blue L, bright-blue S, muted red DE, red !. They keep to grays,
 * blues and reds; class badges run green to brown, so the two read apart.
 */
public final class TasteMenu implements MenuRule {
    public static final TasteMenu INSTANCE = new TasteMenu();

    private final Map<Taste, Badge> badges = new EnumMap<>(Taste.class);

    private TasteMenu() {
        for (Taste taste : Taste.values())
            badges.put(taste, new Badge(taste.id(), letter(taste), color(taste),
                Component.translatable("lifeguard.taste." + taste.id() + ".name"),
                Component.translatable("lifeguard.taste." + taste.id())));
    }

    private static String letter(Taste taste) {
        return switch (taste) {
            case NONE -> "N";
            case LIGHT -> "L";
            case STRONG -> "S";
            case DEEP_END -> "DE";
            case DANGEROUS -> "!";
        };
    }

    private static int color(Taste taste) {
        return switch (taste) {
            case NONE -> 0xFF5E5E5E;
            case LIGHT -> 0xFF5A7896;
            case STRONG -> 0xFF2F80FF;
            case DEEP_END -> 0xFF9A5A5A;
            case DANGEROUS -> 0xFFC8302E;
        };
    }

    // --- badges --------------------------------------------------------------------------------------------------------

    @Override
    public List<Badge> badges() {
        return List.copyOf(badges.values());
    }

    @Override
    public Badge badge(Entry entry) {
        return badges.get(Tastes.of(entry));
    }

    @Override
    public Component badgeFilterTitle() {
        return Component.translatable("lifeguard.menu.filters.taste");
    }

    // --- the profile ---------------------------------------------------------------------------------------------------

    @Override
    public Component choiceName(String choice) {
        return Component.translatable("lifeguard.profile." + choice);
    }

    @Override
    public Component settingChange(Side side, String before, String after) {
        return Component.translatable("lifeguard.menu.summary.profile",
            Component.translatable("lifeguard.side." + side.id()),
            choiceName(before),
            choiceName(after).copy().withStyle(ChatFormatting.YELLOW));
    }

    /** Off the Deep End names what it would turn on before it does; every other profile applies at once. */
    @Override
    public Confirmation confirmChoice(String choice, List<Entry> turningOn) {
        if (!choice.equals(Profile.OFF_THE_DEEP_END.id()) || turningOn.isEmpty())
            return null;
        MutableComponent names = Component.empty();
        int shown = Math.min(turningOn.size(), 8);
        for (int i = 0; i < shown; i++)
            names.append(i == 0 ? "" : ", ").append(Component.translatable(turningOn.get(i).nameKey()));
        if (turningOn.size() > shown)
            names.append(Component.translatable("lifeguard.menu.confirm.and_more", turningOn.size() - shown));
        return new Confirmation(Component.translatable("lifeguard.menu.confirm.deep_end.title"),
            Component.translatable("lifeguard.menu.confirm.deep_end.message", turningOn.size(), names));
    }

    /** Landing a dangerous entry on On asks first, quoting its warning: it's the only way such an entry is ever on. */
    @Override
    public Confirmation confirmOn(Entry entry) {
        if (Tastes.of(entry) != Taste.DANGEROUS)
            return null;
        return new Confirmation(Component.translatable("lifeguard.menu.confirm.dangerous.title",
            Component.translatable(entry.nameKey())), Component.translatable(entry.descKey()));
    }

    // --- words ---------------------------------------------------------------------------------------------------------

    @Override
    public Component autoReason(boolean on) {
        return Component.translatable(on ? "lifeguard.menu.details.why.profile_on" : "lifeguard.menu.details.why.profile_off");
    }

    @Override
    public Component switchTooltip() {
        return Component.translatable("lifeguard.menu.switch.tooltip");
    }

    @Override
    public Component resetMessage(int count) {
        return Component.translatable("lifeguard.menu.confirm.reset.message", count);
    }
}
