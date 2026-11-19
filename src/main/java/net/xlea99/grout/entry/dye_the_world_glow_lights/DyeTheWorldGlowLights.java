package net.xlea99.grout.entry.dye_the_world_glow_lights;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class DyeTheWorldGlowLights {
    /** Only models and textures. */
    public static final Entry ENTRY = Entries.compat("dye_the_world_glow_lights", Taste.NONE).home("snowyspirit")
        .requiresMods("snowyspirit", "dye_the_world")
        .side(Side.CLIENT).restart(Restart.GAME);

    private DyeTheWorldGlowLights() {
    }
}
