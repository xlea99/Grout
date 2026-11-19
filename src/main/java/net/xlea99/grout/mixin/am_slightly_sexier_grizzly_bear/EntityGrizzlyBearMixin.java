package net.xlea99.grout.mixin.am_slightly_sexier_grizzly_bear;

import net.xlea99.grout.entry.am_slightly_sexier_grizzly_bear.GrizzlyGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear")
public abstract class EntityGrizzlyBearMixin implements GrizzlyGait.Holder {
    @Unique
    private final GrizzlyGait grout$gait = new GrizzlyGait();

    @Override
    public GrizzlyGait grout$gait() {
        return this.grout$gait;
    }
}
