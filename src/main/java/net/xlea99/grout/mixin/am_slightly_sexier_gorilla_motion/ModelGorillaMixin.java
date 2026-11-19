package net.xlea99.grout.mixin.am_slightly_sexier_gorilla_motion;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.xlea99.grout.entry.am_slightly_sexier_gorilla_motion.GorillaGait;
import net.xlea99.grout.entry.am_slightly_sexier_gorilla_motion.GorillaMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides the walk amount from Alex's Mobs' {@code setupAnim}, which takes its own walk away and nothing else (its sit, stand,
 * ride and eating poses stay), then adds {@link GorillaMotion} after it. These hook the bridge method taking any
 * {@link Entity}, through which renderers reach the real one, so that nothing here names Alex's Mobs' classes.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelGorilla")
public abstract class ModelGorillaMixin {
    @Shadow @Final public AdvancedModelBox body;
    @Shadow @Final public AdvancedModelBox chest;
    @Shadow @Final public AdvancedModelBox head;
    @Shadow @Final public AdvancedModelBox leftArm;
    @Shadow @Final public AdvancedModelBox rightArm;
    @Shadow @Final public AdvancedModelBox leftLeg;
    @Shadow @Final public AdvancedModelBox rightLeg;

    @Unique
    private final GorillaMotion.Pose grout$pose = new GorillaMotion.Pose();
    @Unique
    private float grout$amount;

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float grout$noOwnWalk(float amount) {
        this.grout$amount = amount;
        return 0F;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void grout$gorillaMotion(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        EntityGorillaAccessor gorilla = (EntityGorillaAccessor) entity;
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float sit = Mth.lerp(partialTick, gorilla.grout$prevSitProgress(), gorilla.grout$sitProgress()) / 10F;
        float stand = Mth.lerp(partialTick, gorilla.grout$prevStandProgress(), gorilla.grout$standProgress()) / 10F;
        boolean rising = gorilla.grout$standProgress() > gorilla.grout$prevStandProgress();
        if (entity.isPassenger())
            sit = 1F; // a baby riding its parent: Alex's riding pose, nothing of ours
        boolean armsBusy = entity instanceof IAnimatedEntity animated && animated.getAnimation() != IAnimatedEntity.NO_ANIMATION;
        GorillaMotion.Pose pose = this.grout$pose;
        // Its real ground speed as limb swing (4 a block): vanilla's limb swing stops at 1 a tick, so a charge would out-run its legs.
        float swingPerTick = 4F * (float) Mth.length(entity.getX() - entity.xo, entity.getZ() - entity.zo);
        GorillaMotion.apply(pose, ((GorillaGait.Holder) entity).grout$gait(), limbSwing, this.grout$amount, swingPerTick, age,
            entity.getId(), gorilla.grout$gorillaScale(), sit, stand, rising, this.body.rotateAngleX, armsBusy);
        AdvancedModelBox[] parts = {this.body, this.chest, this.head, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg};
        for (int i = 0; i < GorillaMotion.PARTS; i++) {
            AdvancedModelBox part = parts[i];
            part.rotateAngleX += pose.rx[i];
            part.rotateAngleY += pose.ry[i];
            part.rotateAngleZ += pose.rz[i];
            part.rotationPointX += pose.px[i];
            part.rotationPointY += pose.py[i];
            part.rotationPointZ += pose.pz[i];
        }
        this.chest.setScale(pose.chestSwell, pose.chestSwell, 1F);
    }
}
