package net.xlea99.grout.mixin.connectiblechains_collisions;

import com.evandev.connectiblechains.util.ChainCollisionIndex;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xlea99.grout.entry.connectiblechains_collisions.ConnectibleChainsCollisions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Adds chain shapes where a direct call of {@code collideBoundingBox} takes its colliders, for the box this call moves
 * through, as vanilla's {@code collectColliders} would. Above Lithium's priority, so it lands in Lithium's overwrite.
 */
@Mixin(value = Entity.class, priority = 1500)
public abstract class EntityMixin {
    @ModifyVariable(method = "collideBoundingBox", at = @At("HEAD"), argsOnly = true)
    private static List<VoxelShape> grout$addChains(List<VoxelShape> colliders, @Nullable Entity entity, Vec3 movement, AABB box, Level level) {
        if (!ConnectibleChainsCollisions.ENTRY.active())
            return colliders;
        List<VoxelShape> chains = ChainCollisionIndex.collect(level, box.expandTowards(movement));
        if (chains == null)
            return colliders;
        chains.addAll(colliders);
        return chains;
    }
}
