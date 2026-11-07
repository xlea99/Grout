package net.xlea99.lifeguard.mixin.clayworks_pot_trim_saving;

import com.teamabnormals.clayworks.common.DecoratedPotTrim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Above Clayworks' priority, so its {@code setTrim} is there to hook. */
@Mixin(value = DecoratedPotBlockEntity.class, priority = 1500)
public abstract class DecoratedPotBlockEntityMixin {
    @Dynamic(value = "Added by Clayworks' DecoratedPotBlockEntityMixin", mixin = com.teamabnormals.clayworks.core.mixin.DecoratedPotBlockEntityMixin.class)
    @Inject(method = "setTrim", at = @At("RETURN"))
    private void lifeguard$saveAndSend(DecoratedPotTrim trim, CallbackInfo ci) {
        BlockEntity pot = (BlockEntity) (Object) this;
        Level level = pot.getLevel();
        if (level == null || level.isClientSide)
            return;
        pot.setChanged();
        BlockState state = pot.getBlockState();
        level.sendBlockUpdated(pot.getBlockPos(), state, state, 3);
    }
}
