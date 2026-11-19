package net.xlea99.grout.mixin.am_slightly_sexier_crimson_mosquito_motion;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito.MosquitoBody;
import net.xlea99.grout.entry.am_slightly_sexier_crimson_mosquito.MosquitoMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds {@link MosquitoMotion} after Alex's Mobs' {@code setupAnim}, keeping everything of Alex's (its flight pose, its leg
 * flaps, its bloat, drinking and spitting) and taking over only the wings' beat while they beat. Hooks the bridge method taking
 * any {@link Entity}, so that nothing here names Alex's Mobs' classes.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelCrimsonMosquito")
public abstract class ModelCrimsonMosquitoMixin {
    @Shadow @Final private AdvancedModelBox body;
    @Shadow @Final private AdvancedModelBox head;
    @Shadow @Final private AdvancedModelBox tail;
    @Shadow @Final private AdvancedModelBox wingL;
    @Shadow @Final private AdvancedModelBox wingR;
    @Shadow @Final private AdvancedModelBox legsL;
    @Shadow @Final private AdvancedModelBox legsR;
    @Shadow @Final private AdvancedModelBox legL1;
    @Shadow @Final private AdvancedModelBox legR1;
    @Shadow @Final private AdvancedModelBox legL2;
    @Shadow @Final private AdvancedModelBox legR2;
    @Shadow @Final private AdvancedModelBox legL3;
    @Shadow @Final private AdvancedModelBox legR3;
    @Shadow @Final private AdvancedModelBox antennaL;
    @Shadow @Final private AdvancedModelBox antennaR;

    @Unique
    private final MosquitoMotion.Pose grout$pose = new MosquitoMotion.Pose();

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void grout$mosquitoMotion(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        EntityCrimsonMosquitoAccessor mosquito = (EntityCrimsonMosquitoAccessor) entity;
        LivingEntity living = (LivingEntity) entity;
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float fly = Mth.lerp(partialTick, mosquito.grout$prevFlyProgress(), mosquito.grout$flyProgress()) / 5F;
        boolean flapping = fly > 0F || mosquito.grout$randomWingFlapTick() > 0;
        float turn = Mth.wrapDegrees(living.yBodyRot - living.yBodyRotO);
        double vx = entity.getX() - entity.xo, vz = entity.getZ() - entity.zo;
        float yaw = living.yBodyRot * Mth.DEG_TO_RAD;
        float ahead = (float) (vx * -Mth.sin(yaw) + vz * Mth.cos(yaw));
        float climb = (float) (entity.getY() - entity.yo);
        float fullness = Math.clamp(mosquito.grout$bloodLevel() / 4F, 0F, 1F);
        boolean drinking = entity.isPassenger();
        float f = Math.min(fly, 1F);
        if (f > 0F) {
            // Alex's in-flight extras, which he switches on at full strength: grown in with the flight pose instead, and the
            // legs' in-and-out flap (offsets 1, 2, 2 front to back) cut down besides.
            this.body.rotationPointY += MosquitoMotion.bodyHeightCorrection(age, f, entity.getId());
            this.tail.rotateAngleX += MosquitoMotion.tailCorrection(age, f);
            this.legL1.rotateAngleZ += MosquitoMotion.legFlapCorrection(age, 1F, true, f);
            this.legR1.rotateAngleZ += MosquitoMotion.legFlapCorrection(age, 1F, false, f);
            this.legL2.rotateAngleZ += MosquitoMotion.legFlapCorrection(age, 2F, true, f);
            this.legR2.rotateAngleZ += MosquitoMotion.legFlapCorrection(age, 2F, false, f);
            this.legL3.rotateAngleZ += MosquitoMotion.legFlapCorrection(age, 2F, true, f);
            this.legR3.rotateAngleZ += MosquitoMotion.legFlapCorrection(age, 2F, false, f);
        }
        if (drinking)
            fly = 0F; // latched on: Alex's own pose, ours only pumps the belly

        MosquitoMotion.Pose pose = this.grout$pose;
        MosquitoMotion.apply(pose, ((MosquitoBody.Holder) entity).grout$mosquitoBody(), age, entity.getId(), fly, flapping && !drinking, turn, ahead,
            climb, fullness, drinking);
        AdvancedModelBox[] parts = {this.body, this.head, this.tail, this.wingL, this.wingR, this.legsL, this.legsR, this.legL1, this.legR1,
            this.antennaL, this.antennaR};
        for (int i = 0; i < MosquitoMotion.PARTS; i++) {
            parts[i].rotateAngleX += pose.rx[i];
            parts[i].rotateAngleY += pose.ry[i];
            parts[i].rotateAngleZ += pose.rz[i];
        }
        if (pose.wingsOwn) {
            this.wingL.rotateAngleZ = pose.wingLZ;
            this.wingR.rotateAngleZ = pose.wingRZ;
            if (f > 0F && f < 1F) {
                // Taking off or landing: the wings unfold out of Alex's folded pose as they take up their beat.
                float open = MosquitoMotion.wingsOpen(f), folded = 1F - open;
                this.wingL.rotateAngleZ *= open;
                this.wingR.rotateAngleZ *= open;
                this.wingL.rotateAngleX += MosquitoMotion.ALEX_WING_FOLD_X * folded;
                this.wingR.rotateAngleX += MosquitoMotion.ALEX_WING_FOLD_X * folded;
                this.wingL.rotateAngleY -= MosquitoMotion.ALEX_WING_FOLD_Y * folded;
                this.wingR.rotateAngleY += MosquitoMotion.ALEX_WING_FOLD_Y * folded;
            }
        }
        // Alex scales the belly by its blood every frame; ours rides on top of that.
        float bloat = 1F + mosquito.grout$bloodLevel() * 0.1F;
        float s = bloat * pose.tailScale;
        this.tail.setScale(s, s, s);
    }
}
