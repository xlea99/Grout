package net.xlea99.lifeguard.mixin.am_crimson_mosquito_fixes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito")
public interface EntityCrimsonMosquitoAccessor {
    @Invoker("isFlying")
    boolean lifeguard$isFlying();

    @Invoker("setFlying")
    void lifeguard$setFlying(boolean flying);
}
