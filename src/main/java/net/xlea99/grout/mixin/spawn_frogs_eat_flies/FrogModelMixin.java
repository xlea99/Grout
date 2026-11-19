package net.xlea99.grout.mixin.spawn_frogs_eat_flies;

import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.frog.Frog;
import net.xlea99.grout.entry.spawn_frogs_eat_flies.FlyOnTongueLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** After vanilla's animation has set the tongue's length for this frame. */
@Mixin(FrogModel.class)
public abstract class FrogModelMixin {
    @Shadow @Final private ModelPart tongue;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/frog/Frog;FFFFF)V", at = @At("TAIL"))
    private void grout$reach(Frog frog, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        FlyOnTongueLayer.reach(this.tongue, frog, age);
    }
}
