package net.xlea99.lifeguard.mixin.am_slightly_sexier_roadrunner;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.xlea99.lifeguard.entry.am_slightly_sexier_roadrunner.RoadrunnerGait;
import net.xlea99.lifeguard.entry.am_slightly_sexier_roadrunner.RoadrunnerMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides the walk amount from Alex's Mobs' {@code setupAnim}, which takes its own walk and lean away and leaves its bite, idle wag and
 * flapping, then adds {@link RoadrunnerMotion} after it. A Roadrunner named Meep is left alone: its spinning legs run on that amount. These
 * hook the bridge method taking any {@link Entity}, through which renderers reach the real one, so that nothing here names Alex's Mobs'
 * classes: Lifeguard isn't built against it.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelRoadrunner")
public abstract class ModelRoadrunnerMixin {
    @Shadow @Final private AdvancedModelBox body;
    @Shadow @Final private AdvancedModelBox neck;
    @Shadow @Final private AdvancedModelBox beak;
    @Shadow @Final private AdvancedModelBox tail;
    @Shadow @Final private AdvancedModelBox left_wing;
    @Shadow @Final private AdvancedModelBox right_wing;
    @Shadow @Final private AdvancedModelBox left_leg;
    @Shadow @Final private AdvancedModelBox left_knee;
    @Shadow @Final private AdvancedModelBox left_foot;
    @Shadow @Final private AdvancedModelBox right_leg;
    @Shadow @Final private AdvancedModelBox right_knee;
    @Shadow @Final private AdvancedModelBox right_foot;

    @Unique
    private float lifeguard$amount;

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float lifeguard$noOwnWalk(float amount, @Local(argsOnly = true) Entity entity) {
        this.lifeguard$amount = amount;
        return ((RoadrunnerGait.Holder) entity).lifeguard$isMeep() ? amount : 0F;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void lifeguard$birdMotion(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        RoadrunnerGait.Holder bird = (RoadrunnerGait.Holder) entity;
        if (bird.lifeguard$isMeep())
            return;
        LivingEntity living = (LivingEntity) entity;
        RoadrunnerMotion.apply(this.body, this.neck, this.beak, this.tail, this.left_wing, this.right_wing,
            this.left_leg, this.left_knee, this.left_foot, this.right_leg, this.right_knee, this.right_foot,
            bird.lifeguard$gait(), limbSwing, this.lifeguard$amount, Mth.wrapDegrees(living.yBodyRot - living.yBodyRotO), age, entity.getId());
    }
}
