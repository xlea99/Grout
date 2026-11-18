package net.xlea99.lifeguard.mixin.spawn_flies_shovel_swatter;

import com.ninni.spawn.client.events.FlyEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.xlea99.lifeguard.entry.spawn_flies_shovel_swatter.SwatPayload;
import net.xlea99.lifeguard.sync.Payloads;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** The one place a left click hurts a fly. Spawn's own click eating means the shovel stays on the cursor, ready for the next one. */
@Mixin(FlyEvents.class)
public abstract class FlyEventsMixin {
    /** More than any fly has. */
    @Unique
    private static final int LIFEGUARD_SWAT = 1000;

    @ModifyArg(method = "onMouseRelease", at = @At(value = "INVOKE", target = "Lcom/ninni/spawn/client/events/FlyEvents$Fly;damage(ILnet/minecraft/client/Minecraft;)V"), index = 0)
    private static int lifeguard$swat(int damage) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof AbstractContainerScreen<?> screen) || !screen.getMenu().getCarried().is(SwatPayload.SWATTERS))
            return damage;
        SwatPayload swat = new SwatPayload();
        ClientPacketListener connection = minecraft.getConnection();
        if (connection != null && connection.hasChannel(swat))
            Payloads.sendToServer(swat);
        return LIFEGUARD_SWAT;
    }
}
