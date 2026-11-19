package net.xlea99.grout.mixin.am_crocodile_basking_poses;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Its constructor's one random number is this. Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrocodile")
public abstract class EntityCrocodileMixin {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int grout$bothPoses(int bound) {
        return 2;
    }
}
