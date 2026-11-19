package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake_motion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public interface EntityRattlesnakeAccessor {
    @Accessor("prevCurlProgress")
    float grout$prevCurlProgress();

    @Accessor("curlProgress")
    float grout$curlProgress();

    @Accessor("randomToungeTick")
    void grout$setTongueTicks(int ticks);

    @Accessor("randomToungeTick")
    int grout$tongueTicks();

    @Invoker("isRattling")
    boolean grout$rattlingNow();

    @Invoker("isCurled")
    boolean grout$curledNow();
}
