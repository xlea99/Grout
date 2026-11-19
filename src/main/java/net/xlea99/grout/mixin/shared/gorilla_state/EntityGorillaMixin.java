package net.xlea99.grout.mixin.shared.gorilla_state;

import net.xlea99.grout.entry.am_slightly_sexier_gorilla.GorillaState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * {@link GorillaState} for every Gorilla, shared by the entries that read it ({@code am_slightly_sexier_gorilla},
 * {@code am_gorilla_silverbacks}): applied while either is. Targeted by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public abstract class EntityGorillaMixin implements GorillaState {
    @Shadow
    public boolean forcedSit;

    @Shadow
    public abstract boolean isSilverback();

    @Override
    public boolean grout$isSilverback() {
        return this.isSilverback();
    }

    @Override
    public boolean grout$forcedSit() {
        return this.forcedSit;
    }
}
