package net.xlea99.lifeguard.mixin.am_rattlesnake_coil_sync;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Alex's tick ends a coil once it has lasted {@code maxCurlTime} ticks, outside its server-only block, so the client ends its
 * own copy on its own timer (75 and up) and the server, whose coil hasn't changed, never sends it again. On the client the
 * timer is held off: the server ends coils, and that change syncs down. Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public abstract class EntityRattlesnakeMixin {
    @Shadow
    public int maxCurlTime;

    @Inject(method = "tick", at = @At("HEAD"))
    private void lifeguard$serverEndsCoils(CallbackInfo ci) {
        if (((Entity) (Object) this).level().isClientSide)
            this.maxCurlTime = Integer.MAX_VALUE / 2;
    }
}
