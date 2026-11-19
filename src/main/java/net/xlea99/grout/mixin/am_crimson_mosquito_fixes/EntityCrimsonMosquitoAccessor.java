package net.xlea99.grout.mixin.am_crimson_mosquito_fixes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito")
public interface EntityCrimsonMosquitoAccessor {
    @Invoker("isFlying")
    boolean grout$isFlying();

    @Invoker("setFlying")
    void grout$setFlying(boolean flying);
}
