package net.xlea99.grout.mixin.am_slightly_sexier_bone_serpent_jaw;

import net.xlea99.grout.entry.am_slightly_sexier_bone_serpent.SerpentMaw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent")
public abstract class EntityBoneSerpentMixin implements SerpentMaw.Holder {
    @Unique
    private final SerpentMaw grout$maw = new SerpentMaw();

    @Override
    public SerpentMaw grout$maw() {
        return this.grout$maw;
    }
}
