package net.xlea99.grout.entry.spawn_flies_shovel_swatter;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xlea99.grout.Grout;

/**
 * A client saying it just swatted a fly with what's on its cursor. Spawn's flies live on the client while an inventory's open, so the swat itself is decided there. All this does is wear the
 * swatter, and only one the server agrees is on that player's cursor: the worst a made-up one can do is wear out that player's own shovel.
 */
public record SwatPayload() implements CustomPacketPayload {
    public static final Type<SwatPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Grout.MODID, "fly_swat"));
    public static final StreamCodec<ByteBuf, SwatPayload> STREAM_CODEC = StreamCodec.unit(new SwatPayload());
    public static final TagKey<Item> SWATTERS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Grout.MODID, "fly_swatters"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").optional().playToServer(TYPE, STREAM_CODEC, SwatPayload::receive);
    }

    private static void receive(SwatPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player)
            context.enqueueWork(() -> wear(player));
    }

    /** Vanilla's durability, so Unbreaking and the rest count, and nothing's lost in creative. */
    public static void wear(ServerPlayer player) {
        ItemStack carried = player.containerMenu.getCarried();
        if (!carried.is(SWATTERS))
            return;
        carried.hurtAndBreak(1, player.serverLevel(), player, item -> player.level().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 0.8F, 1F));
        player.containerMenu.broadcastChanges();
    }
}
