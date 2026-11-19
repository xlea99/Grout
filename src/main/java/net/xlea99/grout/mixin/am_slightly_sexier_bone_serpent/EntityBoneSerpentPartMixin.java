package net.xlea99.grout.mixin.am_slightly_sexier_bone_serpent;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Moves a segment from behind where its parent faces to the same distance from its parent along the line it was already on, so that it's
 * only ever pulled along. Its height stays Alex's Mobs': one tick behind its parent's. Targeted by name: Grout isn't built against Alex's Mobs.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityBoneSerpentPart")
public abstract class EntityBoneSerpentPartMixin {
    @Shadow
    protected float radius;

    @Unique
    private double grout$oldX;
    @Unique
    private double grout$oldZ;

    @Shadow
    public abstract Entity getParent();

    @Inject(method = "tick", at = @At("HEAD"))
    private void grout$rememberPlace(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        this.grout$oldX = self.getX();
        this.grout$oldZ = self.getZ();
    }

    /** Right after Alex's Mobs placed it, in the branch that only runs on the server with a parent. */
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/entity/EntityBoneSerpentPart;markHurt()V"))
    private void grout$trailParent(CallbackInfo ci) {
        Entity parent = this.getParent();
        double fromX = this.grout$oldX - parent.xo;
        double fromZ = this.grout$oldZ - parent.zo;
        double distance = Math.sqrt(fromX * fromX + fromZ * fromZ);
        if (distance < 0.01D)
            return;
        Entity self = (Entity) (Object) this;
        self.setPos(parent.xo + fromX / distance * this.radius, self.getY(), parent.zo + fromZ / distance * this.radius);
    }

    /** It faces along the chain: where its parent is, and no longer where its parent faces. */
    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/entity/EntityBoneSerpentPart;setYRot(F)V"))
    private float grout$faceParent(float parentFacing) {
        Entity parent = this.getParent();
        Entity self = (Entity) (Object) this;
        double toX = parent.xo - self.getX();
        double toZ = parent.zo - self.getZ();
        return toX * toX + toZ * toZ < 0.0001D ? parentFacing : (float) (Mth.atan2(toZ, toX) * Mth.RAD_TO_DEG) - 90F;
    }
}
