package net.xlea99.lifeguard.mixin.am_gazelle_movement;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;
import net.xlea99.lifeguard.entry.am_gazelle_movement.GazelleFlee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Other mobs use this goal too: only a Gazelle has the holder. Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic")
public abstract class AnimalAIHerdPanicMixin {
    @Shadow @Final protected PathfinderMob creature;
    @Shadow protected double randPosX;
    @Shadow protected double randPosY;
    @Shadow protected double randPosZ;

    @Inject(method = "findRandomPositionFrom", at = @At("HEAD"), cancellable = true)
    private void lifeguard$commitAhead(LivingEntity threat, CallbackInfoReturnable<Boolean> cir) {
        if (!(this.creature instanceof GazelleFlee.Holder))
            return;
        Vec3 ahead = GazelleFlee.ahead(this.creature, threat);
        if (ahead != null) {
            this.randPosX = ahead.x;
            this.randPosY = ahead.y;
            this.randPosZ = ahead.z;
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "findRandomPositionFrom", at = @At("RETURN"), cancellable = true)
    private void lifeguard$bolt(LivingEntity threat, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !(this.creature instanceof GazelleFlee.Holder))
            return;
        Vec3 anywhere = GazelleFlee.anywhere(this.creature);
        if (anywhere != null) {
            this.randPosX = anywhere.x;
            this.randPosY = anywhere.y;
            this.randPosZ = anywhere.z;
            cir.setReturnValue(true);
        }
    }
}
