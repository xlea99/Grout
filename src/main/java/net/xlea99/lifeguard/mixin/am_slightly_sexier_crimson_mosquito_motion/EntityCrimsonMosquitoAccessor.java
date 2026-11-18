package net.xlea99.lifeguard.mixin.am_slightly_sexier_crimson_mosquito_motion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito")
public interface EntityCrimsonMosquitoAccessor {
    @Accessor("prevFlyProgress")
    float lifeguard$prevFlyProgress();

    @Accessor("flyProgress")
    float lifeguard$flyProgress();

    @Accessor("randomWingFlapTick")
    int lifeguard$randomWingFlapTick();

    @Invoker("getBloodLevel")
    int lifeguard$bloodLevel();
}
