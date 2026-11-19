package net.xlea99.grout.mixin.quark_hollow_log_paths;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Hollow logs (and Quark's other hollow pillars) are not walkable-through for land pathfinding. Neither Quark, Zeta's pillar
 * nor vanilla's rotated pillar overrides {@code isPathfindable}, so vanilla's default (anything but a full cube is open) let
 * pathfinders plan straight through a log lying in the way: in a trial a cow stood against a row of them for good. Extends
 * {@code RotatedPillarBlock} only to add the override. Targeted by name to match Grout's other Quark mixins.
 */
@Mixin(targets = "org.violetmoon.quark.content.building.block.HollowPillarBlock")
public abstract class HollowPillarBlockMixin extends RotatedPillarBlock {
    private HollowPillarBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return type != PathComputationType.LAND && super.isPathfindable(state, type);
    }
}
