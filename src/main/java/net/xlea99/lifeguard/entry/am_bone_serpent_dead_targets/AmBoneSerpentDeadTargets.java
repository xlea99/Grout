package net.xlea99.lifeguard.entry.am_bone_serpent_dead_targets;

import net.xlea99.lifeguard.core.Entries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Restart;
import net.xlea99.lifeguard.core.Side;
import net.xlea99.lifeguard.core.Taste;

public final class AmBoneSerpentDeadTargets {
    /**
     * A Bone Serpent's leap only asks whether it has a target, not whether that target is alive. A target from one of its own targeting
     * goals gets cleared when it dies; one set directly, by another serpent's alert or by another mod, never does, and it leaps at the
     * spot forever. It lets go of a dead target now. Alex's Mobs issue (ISSUES #171).
     */
    public static final Entry ENTRY = Entries.fix("am_bone_serpent_dead_targets", Taste.NONE).home("alexsmobs")
        .requiresMods("alexsmobs")
        .side(Side.SERVER).restart(Restart.GAME);

    private AmBoneSerpentDeadTargets() {
    }
}
