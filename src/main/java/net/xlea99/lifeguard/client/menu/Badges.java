package net.xlea99.lifeguard.client.menu;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Draws a {@link MenuRule.Badge}: a small colored box with its letter in white. Every box of a rule is as wide as its
 * widest letter needs, so the names after them line up.
 */
final class Badges {
    static final int HEIGHT = 11;

    private Badges() {
    }

    /** The box width for this rule's badges, or 0 when it has none. */
    static int width(Font font, MenuRule rule) {
        int widest = -1;
        for (MenuRule.Badge badge : rule.badges())
            widest = Math.max(widest, font.width(badge.letter()));
        return widest < 0 ? 0 : widest + 4;
    }

    static void draw(GuiGraphics graphics, Font font, MenuRule rule, MenuRule.Badge badge, int x, int y) {
        int width = width(font, rule);
        graphics.fill(x, y, x + width, y + HEIGHT, badge.color());
        graphics.drawString(font, badge.letter(), x + (width - font.width(badge.letter()) + 1) / 2, y + 2, 0xFFFFFFFF, false);
    }
}
