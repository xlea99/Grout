package net.xlea99.grout.mixin.am_slightly_sexier_gazelle;

import net.xlea99.grout.entry.am_slightly_sexier_gazelle.GazelleGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGazelle")
public abstract class EntityGazelleMixin implements GazelleGait.Holder {
    @Unique
    private final GazelleGait grout$gait = new GazelleGait();

    @Shadow
    public abstract boolean isRunning();

    @Override
    public GazelleGait grout$gait() {
        return this.grout$gait;
    }

    @Override
    public boolean grout$isRunning() {
        return this.isRunning();
    }
}
