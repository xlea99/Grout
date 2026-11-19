package net.xlea99.grout.mixin.zipline_server_checks;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.xlea99.grout.entry.zipline_server_checks.ChainedRides;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Zipline's server mixin asks one helper, `zipline$isUsingZipline`, before relaxing each of the three movement checks;
 * this narrows its yes to "and the server finds a chain span here" ({@link ChainedRides}). Priority above Zipline's
 * (default 1000) so its helper exists when this applies. The move being handled is remembered for the helper's calls
 * inside `handleMovePlayer`, on the server thread only (the netty thread enters it first and bounces).
 */
@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1500)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Unique
    @Nullable
    private Vec3 grout$move;

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void grout$rememberMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (player.server.isSameThread())
            grout$move = new Vec3(packet.getX(player.getX()), packet.getY(player.getY()), packet.getZ(player.getZ()));
    }

    @Inject(method = "handleMovePlayer", at = @At("RETURN"))
    private void grout$forgetMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (player.server.isSameThread())
            grout$move = null;
    }

    @Dynamic(mixin = com.evandev.zipline.mixin.ServerGamePacketListenerImplMixin.class)
    @Inject(method = "zipline$isUsingZipline", at = @At("RETURN"), cancellable = true)
    private void grout$onlyOnAChain(Player rider, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && !ChainedRides.onAChain(rider, grout$move))
            cir.setReturnValue(false);
    }
}
