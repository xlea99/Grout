package net.xlea99.lifeguard.mixin.am_slightly_sexier_bone_serpent_jaw;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.xlea99.lifeguard.entry.am_slightly_sexier_bone_serpent.SerpentMaw;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hooks the bridge method taking any {@link Entity}, so that nothing here names Alex's Mobs' classes: Lifeguard isn't built against it. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.model.ModelBoneSerpentHead")
public abstract class ModelBoneSerpentHeadMixin {
    @Shadow @Final private AdvancedModelBox head;
    @Shadow @Final private AdvancedModelBox headtop;
    @Shadow @Final private AdvancedModelBox jaw;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("TAIL"))
    private void lifeguard$unhinge(Entity entity, float limbSwing, float amount, float age, float netHeadYaw, float headPitch, CallbackInfo ci) {
        Mob serpent = (Mob) entity;
        float partialTick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        ((SerpentMaw.Holder) entity).lifeguard$maw().apply(this.head, this.headtop, this.jaw,
            serpent.isAggressive(), serpent.isInLava() || serpent.isInWater(), serpent.getAttackAnim(partialTick), age);
    }
}
