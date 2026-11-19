package net.xlea99.grout.mixin.am_slightly_sexier_gorilla_motion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public interface EntityGorillaAccessor {
    @Accessor("prevSitProgress")
    float grout$prevSitProgress();

    @Accessor("sitProgress")
    float grout$sitProgress();

    @Accessor("prevStandProgress")
    float grout$prevStandProgress();

    @Accessor("standProgress")
    float grout$standProgress();

    @Invoker("getGorillaScale")
    float grout$gorillaScale();
}
