package net.xlea99.grout.entry.no_deadly_memes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.Grout;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class NoDeadlyMemes {
    /**
     * Deep End: memes in other mods that are loud references to the outside world, kill the player for no reason (a hardcore world can
     * end on one), or are just eye-rollingly stupid, are gone. A harmless joke isn't a deadly meme. Does nothing itself: it's the one
     * decision, and each mod's child only extends it, which is why they carry no taste of their own.
     */
    public static final Entry ENTRY = Entries.tweak("grout:no_deadly_memes", Taste.DEEP_END).home(Grout.MODID)
        .side(Side.SERVER).restart(Restart.GAME);

    /**
     * On April 1st, Alex's Mobs' adult Grizzly Bears, tame ones too, turn into Freddy Fazbear, stalk a player, black out their screen,
     * play a music box and kill them outright, whatever their armor ({@code GrizzlyBearAIAprilFools}). That goal never starts now.
     */
    public static final Entry ALEXS_MOBS = Entries.compat("grout:no_deadly_memes_alexs_mobs", Taste.NONE)
        .requiresEntries(ENTRY).shownUnder(ENTRY)
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private NoDeadlyMemes() {
    }
}
