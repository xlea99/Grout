package net.xlea99.lifeguard.mixin.spawn_frogs_eat_flies;

import net.minecraft.world.entity.animal.frog.Frog;
import net.xlea99.lifeguard.entry.spawn_frogs_eat_flies.FrogSnack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** After the frog's brain has had its turn, on the server. */
@Mixin(Frog.class)
public abstract class FrogMixin implements FrogSnack.Snacker {
    @Unique
    private FrogSnack lifeguard$snack;

    @Override
    public boolean lifeguard$snacking() {
        return this.lifeguard$snack != null && this.lifeguard$snack.active();
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void lifeguard$snack(CallbackInfo ci) {
        if (this.lifeguard$snack == null)
            this.lifeguard$snack = new FrogSnack((Frog) (Object) this);
        this.lifeguard$snack.tick();
    }
}
