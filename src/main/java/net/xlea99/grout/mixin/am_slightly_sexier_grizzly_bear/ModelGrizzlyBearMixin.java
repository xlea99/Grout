package net.xlea99.grout.mixin.am_slightly_sexier_grizzly_bear;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.xlea99.grout.entry.am_slightly_sexier_grizzly_bear.GrizzlyGait;
import net.xlea99.grout.entry.am_slightly_sexier_grizzly_bear.GrizzlyMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * On all fours, hides the walk amount from Alex's Mobs' {@code setupAnim}, which takes its own walk away and nothing else, then adds
 * {@link GrizzlyMotion} after it. These hook the bridge method taking any {@link Entity}, through which renderers reach the real one,
 * so that nothing here names Alex's Mobs' classes: Grout isn't built against it.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelGrizzlyBear")
public abstract class ModelGrizzlyBearMixin {
    @Shadow @Final public AdvancedModelBox body;
    @Shadow @Final public AdvancedModelBox midbody;
    @Shadow @Final public AdvancedModelBox head;
    @Shadow @Final public AdvancedModelBox left_ear;
    @Shadow @Final public AdvancedModelBox right_ear;
    @Shadow @Final public AdvancedModelBox left_leg;
    @Shadow @Final public AdvancedModelBox right_leg;
    @Shadow @Final public AdvancedModelBox left_arm;
    @Shadow @Final public AdvancedModelBox right_arm;

    @Unique
    private float grout$amount;
    @Unique
    private float grout$fours;

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float grout$noOwnWalk(float amount, @Local(argsOnly = true) Entity entity) {
        EntityGrizzlyBearAccessor bear = (EntityGrizzlyBearAccessor) entity;
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float sit = Mth.lerp(partialTick, bear.grout$prevSitProgress(), bear.grout$sitProgress());
        float stand = Mth.lerp(partialTick, bear.grout$prevStandProgress(), bear.grout$standProgress());
        this.grout$amount = amount;
        this.grout$fours = 1F - Math.clamp(Math.max(sit, stand) / 10F, 0F, 1F);
        return amount * (1F - this.grout$fours);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void grout$bearMotion(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        GrizzlyMotion.apply(this.body, this.midbody, this.head, this.left_ear, this.right_ear, this.left_leg, this.right_leg, this.left_arm, this.right_arm,
            ((GrizzlyGait.Holder) entity).grout$gait(), limbSwing, this.grout$amount, age, entity.getId(), this.grout$fours);
    }
}
