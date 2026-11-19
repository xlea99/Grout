package net.xlea99.grout.mixin.am_slightly_sexier_crimson_mosquito_motion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito")
public interface EntityCrimsonMosquitoAccessor {
    @Accessor("prevFlyProgress")
    float grout$prevFlyProgress();

    @Accessor("flyProgress")
    float grout$flyProgress();

    @Accessor("randomWingFlapTick")
    int grout$randomWingFlapTick();

    @Invoker("getBloodLevel")
    int grout$bloodLevel();
}
