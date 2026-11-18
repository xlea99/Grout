package net.xlea99.lifeguard.mixin.am_slightly_sexier_bone_serpent_jaw;

import net.xlea99.lifeguard.entry.am_slightly_sexier_bone_serpent.SerpentMaw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent")
public abstract class EntityBoneSerpentMixin implements SerpentMaw.Holder {
    @Unique
    private final SerpentMaw lifeguard$maw = new SerpentMaw();

    @Override
    public SerpentMaw lifeguard$maw() {
        return this.lifeguard$maw;
    }
}
