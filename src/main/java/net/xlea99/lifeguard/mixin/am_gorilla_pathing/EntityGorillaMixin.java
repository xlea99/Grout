package net.xlea99.lifeguard.mixin.am_gorilla_pathing;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla's ground pathfinder in place of Citadel's, and leaves impassable after Alex's constructor marks them free. Mob's
 * constructor asks for the navigation before Alex's sets its costs, so the leaves cost is put back at the end of it.
 * Targeted by name: Lifeguard isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityGorilla")
public abstract class EntityGorillaMixin {
    @Inject(method = "createNavigation", at = @At("HEAD"), cancellable = true)
    private void lifeguard$vanillaPaths(Level level, CallbackInfoReturnable<PathNavigation> cir) {
        cir.setReturnValue(new GroundPathNavigation((Mob) (Object) this, level));
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lifeguard$leavesAreWalls(CallbackInfo ci) {
        ((Mob) (Object) this).setPathfindingMalus(PathType.LEAVES, -1.0F);
    }
}
