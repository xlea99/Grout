package net.xlea99.lifeguard.mixin.am_slightly_sexier_crocodile;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.xlea99.lifeguard.entry.am_slightly_sexier_crocodile.CrocodileGait;
import net.xlea99.lifeguard.entry.am_slightly_sexier_crocodile.CrocodileMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides the walk amount from Alex's Mobs' {@code setupAnim}, which takes its own walk and swim away and leaves basking, the swimming pose,
 * the lunge and the death roll, then adds {@link CrocodileMotion} after it. These hook the bridge method taking any {@link Entity}, through
 * which renderers reach the real one, so that nothing here names Alex's Mobs' classes: Lifeguard isn't built against it.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelCrocodile")
public abstract class ModelCrocodileMixin {
    @Shadow @Final private AdvancedModelBox body;
    @Shadow @Final private AdvancedModelBox neck;
    @Shadow @Final private AdvancedModelBox head;
    @Shadow @Final private AdvancedModelBox jaw;
    @Shadow @Final private AdvancedModelBox tail1;
    @Shadow @Final private AdvancedModelBox tail2;
    @Shadow @Final private AdvancedModelBox tail3;
    @Shadow @Final private AdvancedModelBox left_arm;
    @Shadow @Final private AdvancedModelBox left_hand;
    @Shadow @Final private AdvancedModelBox right_arm;
    @Shadow @Final private AdvancedModelBox right_hand;
    @Shadow @Final private AdvancedModelBox left_leg;
    @Shadow @Final private AdvancedModelBox left_foot;
    @Shadow @Final private AdvancedModelBox right_leg;
    @Shadow @Final private AdvancedModelBox right_foot;

    @Unique
    private float lifeguard$amount;

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float lifeguard$noOwnWalk(float amount) {
        this.lifeguard$amount = amount;
        return 0F;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void lifeguard$crocodileMotion(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        CrocodileGait.Holder crocodile = (CrocodileGait.Holder) entity;
        Mob mob = (Mob) entity;
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float basking = crocodile.lifeguard$basking(partialTick);
        float swimming = crocodile.lifeguard$swimming(partialTick);
        boolean sunGaping = basking > 0F && CrocodileMotion.sunGaping(mob);
        boolean lurking = swimming > 0.5F && CrocodileMotion.lurks(mob, mob.isAggressive(), mob.isVehicle(), crocodile.lifeguard$gait());
        CrocodileMotion.apply(this.body, this.neck, this.head, this.jaw, this.tail1, this.tail2, this.tail3,
            this.left_arm, this.left_hand, this.right_arm, this.right_hand, this.left_leg, this.left_foot, this.right_leg, this.right_foot,
            crocodile.lifeguard$gait(), mob.isAggressive(), CrocodileMotion.running(mob), mob.isVehicle(), mob.isBaby(), sunGaping, lurking, !CrocodileMotion.liesStraight(mob) ? 0F : crocodile.lifeguard$baskingType() == 1 ? -1F : 1F, basking, swimming,
            limbSwing, this.lifeguard$amount, age, entity.getId());
    }
}
