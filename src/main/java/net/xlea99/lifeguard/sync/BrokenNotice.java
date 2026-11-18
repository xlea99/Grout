package net.xlea99.lifeguard.sync;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.xlea99.lifeguard.boot.Boot;
import net.xlea99.lifeguard.boot.BrokenEntries;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Library;

/**
 * Operators (and a singleplayer host) hear about {@link BrokenEntries broken entries} as they join: a log line is easy
 * to miss. Outside boot on purpose: boot runs in the mixin plugin, before Minecraft's classes may load.
 */
public final class BrokenNotice {
    private BrokenNotice() {
    }

    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        List<Entry> broken = Boot.registry().broken();
        if (broken.isEmpty() || !(event.getEntity() instanceof ServerPlayer player))
            return;
        MinecraftServer server = player.getServer();
        if (server == null || !player.hasPermissions(2) && !server.isSingleplayerOwner(player.getGameProfile()))
            return;
        MutableComponent names = Component.empty();
        for (int i = 0; i < broken.size(); i++) {
            if (i > 0)
                names.append(", ");
            names.append(Component.translatableWithFallback(broken.get(i).nameKey(), broken.get(i).id()));
        }
        // With a fallback: the player may not have the mod's lang file. The mod's name is an argument, so the line reads
        // in the player's language.
        player.sendSystemMessage(Component.translatableWithFallback("guppylib.chat.broken",
            "%1$s: broken this session, so off: %2$s. A mod they hook has changed; the log says what didn't fit.",
            Boot.registry().owner().name(), names).withStyle(ChatFormatting.RED));
    }
}
