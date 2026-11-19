package net.xlea99.grout.mixin.am_slightly_sexier_gorilla_motion;

import net.xlea99.grout.entry.am_slightly_sexier_gorilla_motion.GorillaGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public abstract class EntityGorillaMixin implements GorillaGait.Holder {
    @Unique
    private final GorillaGait grout$gait = new GorillaGait();

    @Override
    public GorillaGait grout$gait() {
        return this.grout$gait;
    }
}
