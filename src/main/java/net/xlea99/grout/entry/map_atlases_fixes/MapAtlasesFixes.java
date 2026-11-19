package net.xlea99.grout.entry.map_atlases_fixes;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class MapAtlasesFixes {
    /** The server makes the cartography and crafting results; clients only predict them. */
    public static final Entry ENTRY = Entries.fix("map_atlases_fixes", Taste.NONE).home("map_atlases")
        .requiresMods("map_atlases")
        .side(Side.SERVER).restart(Restart.GAME);

    private MapAtlasesFixes() {
    }
}
