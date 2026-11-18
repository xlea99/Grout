package net.xlea99.lifeguard.entry.spawn_flies_roam_screen;

import java.util.UUID;
import java.util.function.DoubleSupplier;

/**
 * Where a calm fly goes next ({@link SpawnFliesRoamScreen}): a hop from where it is, not a place picked anew. A fly moves a fixed share of the distance left
 * each frame, so hops being short is what keeps it from launching, and always starting from where it is what keeps it from snapping back. Away from the middle of
 * the inventory panel its direction leans toward it, by how much depending on the fly: so most stay around it, and a few live out on the edges of the screen.
 */
public final class FlyWander {
    /** A usual hop, in GUI pixels. The panel is about 176 by 166, which is what this was to feel like. */
    private static final double HOP_LEAST = 40D;
    private static final double HOP_MOST = 120D;
    /** Now and then, a dart: the chance of one, and how far. */
    private static final double DART_CHANCE = 1D / 12D;
    private static final double DART_LEAST = 200D;
    private static final double DART_MOST = 400D;
    /** How hard each kind of fly leans home, against a random direction of length 1, and how far from the panel that's at full strength. */
    private static final double PULL_HOMEBODY = 1.5D;
    private static final double PULL_DRIFTER = 0.6D;
    private static final double PULL_EXPLORER = 0.1D;
    private static final double PULL_FULL_AT = 120D;
    /** The lean starts this far inside the panel's edges: from its edges they'd only hover in a ring around it. */
    private static final double PULL_INSET = 45D;
    /** Out of ten flies, how many are homebodies, and drifters. The rest are explorers. */
    private static final int HOMEBODIES = 6;
    private static final int DRIFTERS = 3;
    /** How far it stays from the screen's edges: a fly is about 16 across. */
    private static final double MARGIN = 8D;

    private FlyWander() {
    }

    /** Goes by the fly's UUID, which it keeps, so that one fly always acts the same way without anything being stored. */
    public static double pull(UUID fly) {
        int kind = Math.floorMod(fly.hashCode(), 10);
        return kind < HOMEBODIES ? PULL_HOMEBODY : kind < HOMEBODIES + DRIFTERS ? PULL_DRIFTER : PULL_EXPLORER;
    }

    /** @return where to go next: {x, y} */
    public static float[] next(double x, double y, double pull, double screenWidth, double screenHeight, double panelLeft, double panelTop, double panelRight, double panelBottom, DoubleSupplier random) {
        double length = random.getAsDouble() < DART_CHANCE ? lerp(random.getAsDouble(), DART_LEAST, DART_MOST) : lerp(random.getAsDouble(), HOP_LEAST, HOP_MOST);
        double angle = random.getAsDouble() * Math.PI * 2D;
        double directionX = Math.cos(angle);
        double directionY = Math.sin(angle);

        double toPanelX = Math.clamp(x, panelLeft + PULL_INSET, Math.max(panelLeft + PULL_INSET, panelRight - PULL_INSET)) - x;
        double toPanelY = Math.clamp(y, panelTop + PULL_INSET, Math.max(panelTop + PULL_INSET, panelBottom - PULL_INSET)) - y;
        double away = Math.hypot(toPanelX, toPanelY);
        if (away > 0D) {
            double lean = pull * Math.min(1D, away / PULL_FULL_AT);
            directionX += toPanelX / away * lean;
            directionY += toPanelY / away * lean;
            double size = Math.hypot(directionX, directionY);
            if (size > 0D) {
                directionX /= size;
                directionY /= size;
            }
        }
        return new float[]{(float) stayOn(x + directionX * length, screenWidth), (float) stayOn(y + directionY * length, screenHeight)};
    }

    /** A hop past an edge comes back off it, as if it had turned away. If it would still be off the other side, it just stops at the edge. */
    private static double stayOn(double place, double size) {
        double least = MARGIN;
        double most = size - MARGIN;
        double bounced = place < least ? least + (least - place) : place > most ? most - (place - most) : place;
        return Math.clamp(bounced, least, most);
    }

    private static double lerp(double amount, double from, double to) {
        return from + (to - from) * amount;
    }
}
