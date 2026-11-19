package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake_sleep;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SleepingRattlesnakeTexture;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeSleep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A sleeping Rattlesnake is drawn with its eyes closed ({@link SleepingRattlesnakeTexture}), as Alex's Mobs draws its other
 * sleepers. Works on Alex's own model as much as on our slithering: it only swaps the texture. Hooks the bridge method
 * taking any {@link Entity}, so that nothing here names Alex's Mobs' classes.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.client.render.RenderRattlesnake")
public abstract class RenderRattlesnakeMixin {
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    private void grout$eyesClosed(Entity entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (entity instanceof SnakeSleep.Holder holder && holder.grout$sleep().asleep && SleepingRattlesnakeTexture.ready())
            cir.setReturnValue(SleepingRattlesnakeTexture.SLEEPING);
    }
}
