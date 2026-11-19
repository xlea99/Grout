package net.xlea99.grout.entry.cc_head_vision;

import net.xlea99.guppylib.core.Entry;
import net.xlea99.guppylib.core.Restart;
import net.xlea99.guppylib.core.Side;
import net.xlea99.grout.core.Entries;
import net.xlea99.grout.core.Taste;

/**
 * Wearing Caverns & Chasms' heads colors your view, like a creeper head: all assets, riding Supplementaries' mob head
 * effects ({@code mob_head_effects/*.json} -> a post shader; Supps' client option "mob_head_shaders" must be on). The
 * pack holds {@code assets/grout/mob_head_effects} and the shaders they name.
 */
public final class CcHeadVision {
    /** Only what you see. */
    public static final Entry ENTRY = Entries.feature("grout:cc_head_vision", Taste.STRONG).home("caverns_and_chasms")
        .requiresMods("caverns_and_chasms", "supplementaries")
        .side(Side.CLIENT).restart(Restart.GAME)
        .resourcePack();

    private CcHeadVision() {
    }
}
