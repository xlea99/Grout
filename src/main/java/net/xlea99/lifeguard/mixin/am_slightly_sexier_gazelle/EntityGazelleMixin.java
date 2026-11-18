package net.xlea99.lifeguard.mixin.am_slightly_sexier_gazelle;

import net.xlea99.lifeguard.entry.am_slightly_sexier_gazelle.GazelleGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGazelle")
public abstract class EntityGazelleMixin implements GazelleGait.Holder {
    @Unique
    private final GazelleGait lifeguard$gait = new GazelleGait();

    @Shadow
    public abstract boolean isRunning();

    @Override
    public GazelleGait lifeguard$gait() {
        return this.lifeguard$gait;
    }

    @Override
    public boolean lifeguard$isRunning() {
        return this.isRunning();
    }
}
