package net.xlea99.lifeguard.entry.am_slightly_sexier_bone_serpent;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Taste;

public final class AmSlightlySexierBoneSerpent {
    /**
     * Strong: it changes how another mod's mob moves. Each of the Bone Serpent's segments was held rigidly behind where its parent faces,
     * so a turning head swung the next one sideways around itself, and so on down the body. Each now trails its parent like a real chain,
     * only ever pulled toward it, so that the body follows the path the head took. Hitboxes move with them, which is why this is on the
     * server. It also tells clients when the serpent is hunting and when it bites, by what vanilla already sends: that's for {@link #JAW}.
     * (Alex's Mobs 1.22.9.)
     */
    public static final Entry ENTRY = Entries.tweak("am_slightly_sexier_bone_serpent", Taste.STRONG).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    /** It has no attack animation, only a small wobble as it moves. Now its jaws unhinge when it leaps at its prey, and snap when it bites ({@link SerpentMaw}). */
    public static final Entry JAW = Entries.tweak("am_slightly_sexier_bone_serpent_jaw", Taste.NONE)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .side(Side.CLIENT).restart(Restart.GAME);

    private AmSlightlySexierBoneSerpent() {
    }
}
