package net.xlea99.lifeguard.mixin.ac_flytrap_eats_flies;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.xlea99.lifeguard.entry.ac_flytrap_eats_flies.FlytrapSnap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * The Flytrap has no collision, so things walk right through it, and vanilla tells a block every tick that something is in its space. The Flytrap just never listened, so this adds the method.
 * Targeted by name: the class isn't public.
 */
@Mixin(targets = "com.github.alexmodguy.alexscaves.server.block.FlytrapBlock")
public abstract class FlytrapBlockMixin extends BushBlock {
    @Shadow @Final public static BooleanProperty OPEN;

    private FlytrapBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        FlytrapSnap.inside(state, level, pos, entity, OPEN);
    }
}
