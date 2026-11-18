package net.xlea99.lifeguard.mixin.am_slightly_sexier_grizzly_bear;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear")
public interface EntityGrizzlyBearAccessor {
    @Accessor("prevSitProgress")
    float lifeguard$prevSitProgress();

    @Accessor("sitProgress")
    float lifeguard$sitProgress();

    @Accessor("prevStandProgress")
    float lifeguard$prevStandProgress();

    @Accessor("standProgress")
    float lifeguard$standProgress();
}
