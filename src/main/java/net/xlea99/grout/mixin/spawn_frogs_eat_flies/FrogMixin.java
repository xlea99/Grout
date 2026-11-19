package net.xlea99.grout.mixin.spawn_frogs_eat_flies;

import net.minecraft.world.entity.animal.frog.Frog;
import net.xlea99.grout.entry.spawn_frogs_eat_flies.FrogSnack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** After the frog's brain has had its turn, on the server. */
@Mixin(Frog.class)
public abstract class FrogMixin implements FrogSnack.Snacker {
    @Unique
    private FrogSnack grout$snack;

    @Override
    public boolean grout$snacking() {
        return this.grout$snack != null && this.grout$snack.active();
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void grout$snack(CallbackInfo ci) {
        if (this.grout$snack == null)
            this.grout$snack = new FrogSnack((Frog) (Object) this);
        this.grout$snack.tick();
    }
}
