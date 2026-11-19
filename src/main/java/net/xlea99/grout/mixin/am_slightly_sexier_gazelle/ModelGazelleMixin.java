package net.xlea99.grout.mixin.am_slightly_sexier_gazelle;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.xlea99.grout.entry.am_slightly_sexier_gazelle.GazelleGait;
import net.xlea99.grout.entry.am_slightly_sexier_gazelle.GazelleMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides the walk amount from Alex's Mobs' {@code setupAnim}, which takes its own walk and run away and leaves eating, flicks and looking,
 * then adds {@link GazelleMotion} after it. These hook the bridge method taking any {@link Entity}, through which renderers reach the real
 * one, so that nothing here names Alex's Mobs' classes: Grout isn't built against it.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelGazelle")
public abstract class ModelGazelleMixin {
    @Shadow @Final private AdvancedModelBox body;
    @Shadow @Final private AdvancedModelBox neck;
    @Shadow @Final private AdvancedModelBox head;
    @Shadow @Final private AdvancedModelBox earL;
    @Shadow @Final private AdvancedModelBox earR;
    @Shadow @Final private AdvancedModelBox tail;
    @Shadow @Final private AdvancedModelBox frontlegL;
    @Shadow @Final private AdvancedModelBox frontlegR;
    @Shadow @Final private AdvancedModelBox backlegL;
    @Shadow @Final private AdvancedModelBox backlegR;

    @Unique
    private float grout$amount;

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float grout$noOwnWalk(float amount) {
        this.grout$amount = amount;
        return 0F;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void grout$gazelleMotion(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        GazelleGait.Holder gazelle = (GazelleGait.Holder) entity;
        GazelleMotion.apply(this.body, this.neck, this.head, this.earL, this.earR, this.tail,
            this.frontlegL, this.frontlegR, this.backlegL, this.backlegR,
            gazelle.grout$gait(), gazelle.grout$isRunning(), ((LivingEntity) entity).isBaby(), limbSwing, this.grout$amount, age, entity.getId());
    }
}
