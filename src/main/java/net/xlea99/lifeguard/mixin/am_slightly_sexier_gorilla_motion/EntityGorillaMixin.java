package net.xlea99.lifeguard.mixin.am_slightly_sexier_gorilla_motion;

import net.xlea99.lifeguard.entry.am_slightly_sexier_gorilla.GorillaGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public abstract class EntityGorillaMixin implements GorillaGait.Holder {
    @Unique
    private final GorillaGait lifeguard$gait = new GorillaGait();

    @Override
    public GorillaGait lifeguard$gait() {
        return this.lifeguard$gait;
    }
}
