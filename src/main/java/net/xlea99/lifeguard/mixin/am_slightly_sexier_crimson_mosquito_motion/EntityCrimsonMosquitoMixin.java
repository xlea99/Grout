package net.xlea99.lifeguard.mixin.am_slightly_sexier_crimson_mosquito_motion;

import net.minecraft.world.entity.Entity;
import net.xlea99.lifeguard.entry.am_slightly_sexier_crimson_mosquito.MosquitoBody;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Carries each Mosquito's {@link MosquitoBody}, and drops its flight pose the moment it latches on. Alex's model lifts a flying
 * Mosquito's body 10 pixels and eases that out over 5 ticks once it lands, while its latched-on pose snaps in at once: so a
 * Mosquito biting you showed up high and sank into place (the user, 2026-11-08). The flight progress is only drawn, so clearing
 * it on the client changes nothing else. Targeted by name: Lifeguard isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito")
public abstract class EntityCrimsonMosquitoMixin implements MosquitoBody.Holder {
    @Shadow
    public float flyProgress;
    @Shadow
    public float prevFlyProgress;

    @Unique
    private final MosquitoBody lifeguard$mosquitoBody = new MosquitoBody();

    @Override
    public MosquitoBody lifeguard$mosquitoBody() {
        return this.lifeguard$mosquitoBody;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void lifeguard$latchedPose(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self.level().isClientSide && self.isPassenger()) {
            this.flyProgress = 0F;
            this.prevFlyProgress = 0F;
        }
    }
}
