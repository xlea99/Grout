package net.xlea99.lifeguard.mixin.am_slightly_sexier_grizzly_bear;

import net.xlea99.lifeguard.entry.am_slightly_sexier_grizzly_bear.GrizzlyGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear")
public abstract class EntityGrizzlyBearMixin implements GrizzlyGait.Holder {
    @Unique
    private final GrizzlyGait lifeguard$gait = new GrizzlyGait();

    @Override
    public GrizzlyGait lifeguard$gait() {
        return this.lifeguard$gait;
    }
}
