package net.xlea99.grout.mixin.zipline_chain_pass_through;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.xlea99.grout.entry.zipline_chain_pass_through.ChainPassing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Says who's moving, for {@link ChainCollisionIndexMixin}: Chains' shape lookup isn't told. */
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "move", at = @At("HEAD"))
    private void grout$startMove(MoverType type, Vec3 movement, CallbackInfo ci) {
        if ((Object) this instanceof Player player)
            ChainPassing.startMove(player);
    }

    @Inject(method = "move", at = @At("RETURN"))
    private void grout$endMove(MoverType type, Vec3 movement, CallbackInfo ci) {
        if ((Object) this instanceof Player)
            ChainPassing.endMove();
    }
}
