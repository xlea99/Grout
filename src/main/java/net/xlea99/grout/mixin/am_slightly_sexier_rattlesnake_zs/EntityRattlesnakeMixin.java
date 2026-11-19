package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake_zs;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeSleep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A sleeping Rattlesnake breathes out Alex's Caves' sleep Zs, in the burst its Gummy Bears use (three, a few ticks apart,
 * every 50 ticks), from about where its head lies on the coil. Client only; the particle is looked up by id.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public abstract class EntityRattlesnakeMixin {
    @Unique
    private static final ResourceLocation SLEEP = ResourceLocation.fromNamespaceAndPath("alexscaves", "sleep");

    /** Where the Zs start, off the snake's feet in blocks: up, and forward along its body (a baby's are scaled down). */
    @Unique
    private static final double UP = 0.3, FORWARD = 0.2, RISE = 0.1;

    @Unique
    private static ParticleOptions grout$zs;

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$snore(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        // Asleep as its mind says, through Sleepy eyes (which this entry requires, so every snake carries it).
        if (!self.level().isClientSide || !((SnakeSleep.Holder) this).grout$sleep().asleep)
            return;
        int beat = (self.tickCount + self.getId()) % 50;
        if (beat != 2 && beat != 10 && beat != 18)
            return;
        if (grout$zs == null && BuiltInRegistries.PARTICLE_TYPE.get(SLEEP) instanceof ParticleOptions options)
            grout$zs = options;
        if (grout$zs == null)
            return;
        double scale = self.isBaby() ? 0.35 : 1.0;
        Vec3 at = self.position().add(new Vec3(0, UP * scale, FORWARD * scale).yRot(-self.yBodyRot * ((float) Math.PI / 180F)));
        self.level().addParticle(grout$zs, at.x, at.y, at.z, 0, RISE, 0);
    }
}
