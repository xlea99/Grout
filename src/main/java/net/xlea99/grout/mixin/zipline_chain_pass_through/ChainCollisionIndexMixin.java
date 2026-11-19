package net.xlea99.grout.mixin.zipline_chain_pass_through;

import com.evandev.connectiblechains.util.ChainCollisionIndex;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xlea99.grout.entry.zipline_chain_pass_through.ChainPassing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Every route to chain collision goes through this lookup; for a player passing through, it finds nothing. */
@Mixin(ChainCollisionIndex.class)
public abstract class ChainCollisionIndexMixin {
    @Inject(method = "collect", at = @At("HEAD"), cancellable = true)
    private static void grout$passThrough(Level level, AABB area, CallbackInfoReturnable<List<VoxelShape>> cir) {
        if (ChainPassing.passing())
            cir.setReturnValue(null);
    }
}
