package net.xlea99.grout.entry.snowy_spirit_brighter_ginger;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class SnowySpiritBrighterGinger {
    /**
     * Only textures: Snowy Spirit's gingerbread and ginger in Alex's Caves' brighter, burnt orange (hue -12.5, saturation
     * x0.92, a little brighter; icing and candy bits map to themselves), from {@code grout:recolors/snowy_spirit_ginger.json}
     * applied to the player's own Snowy Spirit textures ({@code client/SpecRecolors}): its art can't ship, only be recolored.
     */
    public static final Entry ENTRY = Entries.tweak("snowy_spirit_brighter_ginger", Taste.DEEP_END).home("snowyspirit")
        .requiresMods("snowyspirit")
        .side(Side.CLIENT).restart(Restart.GAME);

    private SnowySpiritBrighterGinger() {
    }
}
