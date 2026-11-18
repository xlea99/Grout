package net.xlea99.lifeguard.mixin.am_slightly_sexier_rattlesnake_motion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public interface EntityRattlesnakeAccessor {
    @Accessor("prevCurlProgress")
    float lifeguard$prevCurlProgress();

    @Accessor("curlProgress")
    float lifeguard$curlProgress();

    @Accessor("randomToungeTick")
    void lifeguard$setTongueTicks(int ticks);

    @Accessor("randomToungeTick")
    int lifeguard$tongueTicks();

    @Invoker("isRattling")
    boolean lifeguard$rattlingNow();

    @Invoker("isCurled")
    boolean lifeguard$curledNow();
}
