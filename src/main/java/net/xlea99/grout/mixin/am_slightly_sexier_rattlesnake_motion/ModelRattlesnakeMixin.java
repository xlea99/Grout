package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake_motion;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.SnakeBody;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.SnakeMotion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hides the walk amount from Alex's Mobs' {@code setupAnim}, which takes away only its side-to-side swing (its coil, rattle,
 * bite, tongue and head turn stay), then adds {@link SnakeMotion}. Hooks the bridge method taking any {@link Entity}, so that
 * nothing here names Alex's Mobs' classes.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelRattlesnake")
public abstract class ModelRattlesnakeMixin {
    @Shadow @Final private AdvancedModelBox body;
    @Shadow @Final private AdvancedModelBox tail1;
    @Shadow @Final private AdvancedModelBox tail2;
    @Shadow @Final private AdvancedModelBox neck1;
    @Shadow @Final private AdvancedModelBox neck2;
    @Shadow @Final private AdvancedModelBox head;

    @Unique
    private final SnakeMotion.Pose grout$pose = new SnakeMotion.Pose();

    @ModifyVariable(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private float grout$noOwnSwing(float amount) {
        return 0F;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void grout$slither(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        EntityRattlesnakeAccessor snake = (EntityRattlesnakeAccessor) entity;
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        float curl = Mth.lerp(partialTick, snake.grout$prevCurlProgress(), snake.grout$curlProgress()) / 5F;
        float swingPerTick = 4F * (float) Mth.length(entity.getX() - entity.xo, entity.getZ() - entity.zo);
        float scale = ((LivingEntity) entity).isBaby() ? 0.35F : 1F;
        SnakeMotion.Pose pose = this.grout$pose;
        SnakeBody body = ((SnakeBody.Holder) entity).grout$snakeBody();
        SnakeMotion.apply(pose, body, age, entity.getId(), swingPerTick, curl, snake.grout$rattlingNow(), scale,
            netHeadYaw, headPitch);
        AdvancedModelBox[] parts = {this.body, this.neck1, this.neck2, this.head, this.tail1, this.tail2};
        for (int i = 0; i < SnakeMotion.PARTS; i++) {
            parts[i].rotateAngleX += pose.rx[i];
            parts[i].rotateAngleY += pose.ry[i];
            parts[i].rotateAngleZ += pose.rz[i];
            parts[i].rotationPointZ += pose.pz[i];
        }
    }
}
