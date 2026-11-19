package net.xlea99.grout.entry.zipline_chain_pass_through;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Whether the player moving on this thread passes through chains: the client and the server each move players on their own. */
public final class ChainPassing {
    private static final TagKey<Item> ATTACHMENT = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("zipline", "attachment"));
    private static final ThreadLocal<Boolean> PASSING = ThreadLocal.withInitial(() -> false);

    private ChainPassing() {
    }

    public static void startMove(Player player) {
        PASSING.set(ZiplineChainPassThrough.ENTRY.active() && passes(player));
    }

    public static void endMove() {
        PASSING.set(false);
    }

    public static boolean passing() {
        return PASSING.get();
    }

    private static boolean passes(Player player) {
        if (player.isUsingItem() && player.getUseItem().is(ATTACHMENT))
            return true;
        return cooling(player, player.getMainHandItem()) || cooling(player, player.getOffhandItem());
    }

    private static boolean cooling(Player player, ItemStack stack) {
        return stack.is(ATTACHMENT) && player.getCooldowns().isOnCooldown(stack.getItem());
    }
}
