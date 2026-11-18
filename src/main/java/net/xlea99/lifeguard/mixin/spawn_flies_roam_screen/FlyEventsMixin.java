package net.xlea99.lifeguard.mixin.spawn_flies_roam_screen;

import com.ninni.spawn.client.events.FlyEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.util.RandomSource;
import net.xlea99.lifeguard.entry.spawn_flies_roam_screen.FlyWander;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Runs just after a fly has picked its next place in the inventory panel, where it asks how long to wait before picking again, and picks again: a {@link FlyWander}
 * hop, or for a scared one, anywhere on screen. That branch isn't reached when it's following the cursor to food, so that stays as it is.
 */
@Mixin(FlyEvents.class)
public abstract class FlyEventsMixin {
    /** How far a scared fly stays from the screen's edges, in GUI pixels. */
    @Unique
    private static final float LIFEGUARD_MARGIN = 8F;

    @Inject(method = "updateFly", at = @At(value = "INVOKE", target = "Lcom/ninni/spawn/client/events/FlyEvents$Fly;getMovingInterval(Lnet/minecraft/util/RandomSource;)I"))
    private static void lifeguard$wander(FlyEvents.Fly fly, Minecraft mc, AbstractContainerScreen<?> screen, float partialTick, boolean followMouse, double mouseX, double mouseY, CallbackInfo ci) {
        FlyAccessor accessor = (FlyAccessor) (Object) fly;
        RandomSource random = mc.level.random;
        if (accessor.lifeguard$scaredTime() > 0) {
            accessor.lifeguard$setTargetX(LIFEGUARD_MARGIN + random.nextFloat() * (screen.width - 2F * LIFEGUARD_MARGIN));
            accessor.lifeguard$setTargetY(LIFEGUARD_MARGIN + random.nextFloat() * (screen.height - 2F * LIFEGUARD_MARGIN));
            return;
        }
        int left = screen.getGuiLeft();
        int top = screen.getGuiTop();
        float[] next = FlyWander.next(accessor.lifeguard$x(), accessor.lifeguard$y(), FlyWander.pull(fly.getUuid()), screen.width, screen.height,
            left, top, left + screen.getXSize(), top + screen.getYSize(), random::nextDouble);
        accessor.lifeguard$setTargetX(next[0]);
        accessor.lifeguard$setTargetY(next[1]);
    }
}
