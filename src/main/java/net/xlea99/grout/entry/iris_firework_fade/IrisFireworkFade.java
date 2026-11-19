package net.xlea99.grout.entry.iris_firework_fade;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class IrisFireworkFade {
    /** Only how firework sparks are drawn, and only with no shaderpack on. */
    public static final Entry ENTRY = Entries.fix("iris_firework_fade", Taste.NONE)
        .home("iris")
        .requiresMods("iris")
        .side(Side.CLIENT).restart(Restart.GAME);

    private IrisFireworkFade() {
    }
}
