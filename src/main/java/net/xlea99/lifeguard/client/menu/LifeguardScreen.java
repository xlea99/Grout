package net.xlea99.lifeguard.client.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.xlea99.lifeguard.boot.Boot;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Switch;
import org.lwjgl.glfw.GLFW;

/**
 * A mod's menu: its auto rule's setting across the top (Lifeguard: the profile bar; none for a plain mod), the filters on
 * the left, the entries in the middle, the selected entry's details on the right, Done and Cancel at the bottom. What
 * the mod's rule adds (badges, the bar's names, questions asked first) comes from its {@link MenuRule}.
 *
 * <p>Everything edits a draft. Done (or Esc, as in vanilla's own option screens) writes it to the config files and
 * Cancel throws it away; either way nothing changes until the next restart, and the status line says when one would.
 */
public final class LifeguardScreen extends Screen {
    private static final int BAR_TOP = 22;
    private static final int STATUS_TOP = 48;
    private static final int BODY_TOP = 62;
    // Without a setting there's no bar: the status line and the body move up into its place.
    private static final int NO_BAR_SHIFT = STATUS_TOP - BAR_TOP;
    private static final int FOOTER = 36;
    private static final int GAP = 6;

    private final Screen parent;
    private final MenuRule rule;
    private final MenuModel model = new MenuModel();
    private final List<Button> choiceButtons = new ArrayList<>();
    private final DetailsPane details = new DetailsPane(this);
    private final Filters filters;
    private final FilterPane filterPane;
    private int statusTop;
    private int bodyTop;
    private EntryList list;
    private Button resetButton;
    private Entry selected;
    private Component saveProblem;
    // "/" focuses the search box on key press; the character that follows must not land in it.
    private boolean swallowSlash;

    public LifeguardScreen(Screen parent, MenuRule rule) {
        super(Component.translatable(Boot.registry().owner().menuTitleKey()));
        this.parent = parent;
        this.rule = rule;
        this.filters = new Filters(rule);
        this.filterPane = new FilterPane(this, filters);
    }

    MenuModel model() {
        return model;
    }

    MenuRule rule() {
        return rule;
    }

    Font font() {
        return font;
    }

    Entry selected() {
        return selected;
    }

    @Override
    protected void init() {
        choiceButtons.clear();
        List<String> choices = model.ruleChoices();
        int shift = choices.isEmpty() ? NO_BAR_SHIFT : 0;
        statusTop = STATUS_TOP - shift;
        bodyTop = BODY_TOP - shift;
        int gap = 4;
        int buttonWidth = choices.isEmpty() ? 0 : Math.min(110, (width - 40 - gap * (choices.size() - 1)) / choices.size());
        int barLeft = (width - (buttonWidth * choices.size() + gap * (choices.size() - 1))) / 2;
        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i);
            choiceButtons.add(addRenderableWidget(Button.builder(rule.choiceName(choice), button -> pickChoice(choice))
                .bounds(barLeft + i * (buttonWidth + gap), BAR_TOP, buttonWidth, 20).build()));
        }

        // Three panes: filters, entries, details. The entries get whatever the other two leave.
        int bodyHeight = height - bodyTop - FOOTER;
        int filtersWidth = Math.max(96, Math.min(140, (int) (width * 0.2)));
        int detailsWidth = Math.max(150, Math.min(280, (int) (width * 0.3)));
        int listWidth = width - filtersWidth - detailsWidth - GAP * 4;
        filterPane.layout(GAP, bodyTop, filtersWidth, bodyHeight);
        // The column header is pinned above the rows; the list itself starts below it and scrolls under it.
        list = addRenderableWidget(new EntryList(minecraft, listWidth, bodyHeight - EntryList.HEADER_HEIGHT,
            bodyTop + EntryList.HEADER_HEIGHT, this));
        list.setX(GAP * 2 + filtersWidth);
        details.setBounds(width - detailsWidth - GAP, bodyTop, detailsWidth, bodyHeight);
        rebuildRows();
        details.show(selected);

        int footerWidth = 104;
        int footerLeft = width / 2 - (footerWidth * 3 + 8) / 2;
        resetButton = addRenderableWidget(Button.builder(Component.translatable("guppylib.menu.reset"),
            button -> confirmReset()).bounds(footerLeft, height - 28, footerWidth, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
            .bounds(footerLeft + footerWidth + 4, height - 28, footerWidth, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> minecraft.setScreen(parent))
            .bounds(footerLeft + (footerWidth + 4) * 2, height - 28, footerWidth, 20).build());
        refresh();
    }

    // --- choices that ask first ---------------------------------------------------------------------------------------

    /** A choice applies at once unless the rule asks first (Lifeguard: Off the Deep End names what it would turn on). */
    private void pickChoice(String choice) {
        MenuRule.Confirmation question = rule.confirmChoice(choice, model.wouldTurnOn(choice));
        if (question == null) {
            applyChoice(choice);
            return;
        }
        ask(question, () -> applyChoice(choice));
    }

    private void applyChoice(String choice) {
        Map<Entry, Boolean> before = activeNow();
        model.pickChoice(choice);
        startRipple(before);
        refresh();
    }

    /** Asks, and on yes does it; either way back to the menu. */
    private void ask(MenuRule.Confirmation question, Runnable onYes) {
        minecraft.setScreen(new ConfirmScreen(yes -> {
            if (yes)
                onYes.run();
            minecraft.setScreen(this);
        }, question.title(), question.message()));
    }

    /** One step of an entry's switch: forward from a left click or the space bar, backward from a right click. */
    void cycle(Entry entry, boolean forward) {
        setSwitch(entry, MenuModel.step(model.draft().setting(entry), forward));
    }

    /**
     * Every change to a switch comes through here, so a rule's question before landing on On (Lifeguard: a dangerous
     * entry, quoting its warning) is asked whichever way the player got there.
     */
    void setSwitch(Entry entry, Switch target) {
        if (model.locked(entry))
            return;
        MenuRule.Confirmation question = target == Switch.ON && model.draft().setting(entry) != Switch.ON
            ? rule.confirmOn(entry) : null;
        if (question != null) {
            ask(question, () -> {
                model.set(entry, target);
                refresh();
            });
            return;
        }
        model.set(entry, target);
        refresh();
    }

    private void confirmReset() {
        int count = model.resettableOverrides();
        if (count == 0)
            return;
        minecraft.setScreen(new ConfirmScreen(yes -> {
            if (yes) {
                Map<Entry, Boolean> before = activeNow();
                model.resetOverrides();
                startRipple(before);
                refresh();
            }
            minecraft.setScreen(this);
        }, Component.translatable("guppylib.menu.confirm.reset.title"), rule.resetMessage(count)));
    }

    // --- the ripple ---------------------------------------------------------------------------------------------------

    private record Ripple(long start, boolean on) {
    }

    private static final long RIPPLE_STEP = 35;
    private static final long RIPPLE_MAX_DELAY = 600;
    static final long RIPPLE_LENGTH = 350;

    private final Map<Entry, Ripple> ripples = new HashMap<>();

    private Map<Entry, Boolean> activeNow() {
        Map<Entry, Boolean> active = new HashMap<>();
        for (MenuModel.Line line : model.lines(filters))
            if (!line.isHeading())
                active.put(line.entry(), line.entry().activeUnder(model.draft()));
        return active;
    }

    /** Rows that flipped flash green (now on) or red (now off), one after another from the top. */
    private void startRipple(Map<Entry, Boolean> before) {
        ripples.clear();
        long now = Util.getMillis();
        int index = 0;
        for (MenuModel.Line line : model.lines(filters)) {
            if (line.isHeading())
                continue;
            Entry entry = line.entry();
            Boolean was = before.get(entry);
            boolean isOn = entry.activeUnder(model.draft());
            if (was != null && was != isOn)
                ripples.put(entry, new Ripple(now + Math.min(index * RIPPLE_STEP, RIPPLE_MAX_DELAY), isOn));
            index++;
        }
    }

    /** The ripple's overlay color for a row right now (alpha included), or 0 for none. */
    int rippleColor(Entry entry) {
        Ripple ripple = ripples.get(entry);
        if (ripple == null)
            return 0;
        long age = Util.getMillis() - ripple.start();
        if (age < 0)
            return 0;
        if (age >= RIPPLE_LENGTH) {
            ripples.remove(entry);
            return 0;
        }
        int alpha = (int) (0x70 * (1 - age / (double) RIPPLE_LENGTH));
        return (alpha << 24) | (ripple.on() ? 0x40C060 : 0xD04040);
    }

    /** While any filter is on, collapsing does nothing (the model shows every match): arrows show open and don't click. */
    boolean collapseIgnored() {
        return filters.any();
    }

    void rebuildRows() {
        list.show(model.lines(filters));
    }

    void filtersChanged() {
        rebuildRows();
    }

    void addFilterWidget(AbstractWidget widget) {
        addRenderableWidget(widget);
    }

    /** Shows an entry in the details pane; with reveal, also opens its collapsed parents and scrolls the list to it. */
    void select(Entry entry, boolean reveal) {
        selected = entry;
        if (reveal) {
            model.expandTo(entry);
            rebuildRows();
            list.reveal(entry);
        }
        details.show(entry);
    }

    void addDetailWidget(AbstractWidget widget) {
        addRenderableWidget(widget);
    }

    void removeDetailWidget(AbstractWidget widget) {
        removeWidget(widget);
    }

    /** Re-labels what depends on the draft: the setting's buttons (the picked one highlighted). Rows redraw themselves. */
    void refresh() {
        Optional<String> shown = model.shownChoice();
        List<String> choices = model.ruleChoices();
        for (int i = 0; i < choices.size(); i++) {
            MutableComponent name = rule.choiceName(choices.get(i)).copy();
            if (shown.isPresent() && shown.get().equals(choices.get(i)))
                name = Component.literal("▶ ").append(name).append(" ◀")
                    .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);
            choiceButtons.get(i).setMessage(name);
        }
        resetButton.active = model.resettableOverrides() > 0;
        saveProblem = null;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 8, 0xFFFFFFFF);
        graphics.drawCenteredString(font, status(), width / 2, statusTop, 0xFFB0B0B0);
        list.renderHeader(graphics, bodyTop, mouseX, mouseY);
        filterPane.render(graphics, mouseX, mouseY);
        details.render(graphics, mouseX, mouseY);
    }

    /** The panes' backdrops go under every widget, so they don't dim the sliders and search box drawn on them. */
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        filterPane.renderBackdrop(graphics);
        details.renderBackdrop(graphics);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && (filterPane.click(mouseX, mouseY) || details.click(mouseX, mouseY) || list.headerClick(mouseX, mouseY)))
            return true;
        if (button == 1 && list.rightClick(mouseX, mouseY))
            return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return filterPane.scroll(mouseX, mouseY, scrollY) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    /**
     * The keyboard: "/" jumps to search; up and down move the selection through the visible entries; left and right
     * collapse and expand it; space cycles its switch. While typing in search, keys go to the search box.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EditBox search = filterPane.search();
        if (search != null && search.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_ENTER) {
                search.setFocused(false);
                setFocused(null);
                moveSelection(selected == null ? 0 : 1);
                return true;
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        switch (keyCode) {
            case GLFW.GLFW_KEY_SLASH -> {
                setFocused(search);
                swallowSlash = true;
                return true;
            }
            case GLFW.GLFW_KEY_UP -> {
                moveSelection(-1);
                return true;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                moveSelection(1);
                return true;
            }
            case GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT -> {
                if (selected != null && !selected.shownBelow().isEmpty() && !collapseIgnored()
                    && model.collapsed(selected) == (keyCode == GLFW.GLFW_KEY_RIGHT)) {
                    model.toggleCollapsed(selected);
                    rebuildRows();
                }
                return true;
            }
            case GLFW.GLFW_KEY_SPACE -> {
                if (selected != null) {
                    cycle(selected, true);
                    return true;
                }
            }
            default -> {
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (swallowSlash && character == '/') {
            swallowSlash = false;
            return true;
        }
        swallowSlash = false;
        return super.charTyped(character, modifiers);
    }

    /** Moves the selection by one visible entry (headings skipped), or to the first one when nothing is selected. */
    private void moveSelection(int step) {
        List<Entry> visible = new ArrayList<>();
        for (MenuModel.Line line : model.lines(filters))
            if (!line.isHeading())
                visible.add(line.entry());
        if (visible.isEmpty())
            return;
        int at = visible.indexOf(selected);
        int next = at < 0 ? 0 : Math.max(0, Math.min(visible.size() - 1, at + step));
        selected = visible.get(next);
        details.show(selected);
        list.reveal(selected);
    }

    private Component status() {
        if (saveProblem != null)
            return saveProblem;
        MutableComponent line;
        if (model.hasRuleSetting()) {
            Component choice = model.shownChoice()
                .map(rule::choiceName)
                .orElseGet(() -> Component.translatable("guppylib.menu.status.mixed",
                    rule.choiceName(model.draft().ruleChoice(Side.CLIENT)), rule.choiceName(model.draft().ruleChoice(Side.SERVER))));
            line = Component.translatable("guppylib.menu.status", choice, model.activeCount(), model.overrides());
        } else {
            line = Component.translatable("guppylib.menu.status.plain", model.activeCount(), model.overrides());
        }
        if (model.following())
            line.append(" · ").append(Component.translatable("guppylib.menu.following"));
        Optional<String> broken = model.brokenFile(Side.SERVER).or(() -> model.brokenFile(Side.CLIENT));
        if (broken.isPresent())
            line.append(" · ").append(Component.translatable("guppylib.menu.broken", broken.get()).withStyle(ChatFormatting.RED));
        else if (model.restartPending())
            line.append(" · ").append(Component.translatable("guppylib.menu.restart").withStyle(ChatFormatting.YELLOW));
        return line;
    }

    /**
     * Esc and Done both save, as vanilla's option screens do. A failed save keeps the menu open and says why. When the
     * saved state differs from what's running, the restart summary comes next; otherwise straight back.
     */
    @Override
    public void onClose() {
        Optional<String> problem = model.save();
        if (problem.isPresent()) {
            saveProblem = Component.translatable("guppylib.menu.save_failed", problem.get()).withStyle(ChatFormatting.RED);
            return;
        }
        MenuModel.Summary summary = model.summary();
        minecraft.setScreen(summary.empty() ? parent : new RestartSummaryScreen(this, parent, summary));
    }
}
