package net.xlea99.grout.entry.am_grizzly_tempting;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

public final class AmGrizzlyTempting {
    /**
     * Wild Grizzly Bears follow someone holding the Gorilla's food (leaves, apples, bamboo, sugar cane), by a wrong tag on their tempt
     * goal. They follow their own {@code #alexsmobs:grizzly_foodstuffs} now, which that tag's own comment says they should.
     */
    public static final Entry ENTRY = Entries.fix("am_grizzly_tempting", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmGrizzlyTempting() {
    }
}
