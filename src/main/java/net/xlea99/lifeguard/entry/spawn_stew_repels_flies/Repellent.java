package net.xlea99.lifeguard.entry.spawn_stew_repels_flies;

import com.ninni.spawn.registry.SpawnAttachments;
import com.ninni.spawn.registry.SpawnParticles;
import com.ninni.spawn.registry.SpawnSoundEvents;
import com.ninni.spawn.server.gui.fly.FlyData;
import com.ninni.spawn.server.network.FlySyncPacket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.xlea99.lifeguard.sync.Payloads;

/**
 * What the effect does to a player's flies ({@link SpawnStewRepelsFlies}). Flies reach a player in several ways, through several of Spawn's own paths, so this doesn't stop any of them. It watches for flies it
 * hasn't seen and sends them off, which covers them all, and any later: it landed, smelled you, and left.
 */
public final class Repellent {
    private static final int CHECK_EVERY = 20;
    /** Between flies leaving, in ticks: 5 to 20 seconds. */
    private static final int LEAVE_LEAST = 100;
    private static final int LEAVE_MOST = 400;
    /** How fast one heads off, in blocks a tick, and how much of that is up. Spawn's particle wanders from there and lasts 2 to 4 seconds. */
    private static final double FLIES_OFF_AT = 0.2D;
    private static final double FLIES_UP = 0.5D;

    private static final DeferredHolder<MobEffect, MobEffect> EFFECT = DeferredHolder.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("alexsmobs", "mosquito_repellent"));
    private static final Map<ServerPlayer, Watch> WATCHED = new WeakHashMap<>();

    private Repellent() {
    }

    private static final class Watch {
        private final Set<UUID> known = new HashSet<>();
        private long nextLeave;
    }

    public static boolean on(ServerPlayer player) {
        return SpawnStewRepelsFlies.ENTRY.active() && player.hasEffect(EFFECT);
    }

    public static void tick(ServerPlayer player) {
        if (player.tickCount % CHECK_EVERY != 0)
            return;
        if (!player.isAlive() || !player.hasEffect(EFFECT)) {
            WATCHED.remove(player);
            return;
        }
        long now = player.level().getGameTime();
        List<FlyData> flies = player.hasData(SpawnAttachments.FLIES) ? player.getData(SpawnAttachments.FLIES).getFlies() : List.of();
        Watch watch = WATCHED.get(player);
        if (watch == null) {
            watch = new Watch();
            watch.nextLeave = now + nextWait(player);
            for (FlyData fly : flies)
                watch.known.add(fly.uuid);
            WATCHED.put(player, watch);
        }
        List<FlyData> leaving = new ArrayList<>();
        List<FlyData> mayLeave = new ArrayList<>();
        for (FlyData fly : flies) {
            if (fly.dead || fly.customName != null) {
                watch.known.add(fly.uuid);
                continue;
            }
            if (watch.known.add(fly.uuid))
                leaving.add(fly);
            else
                mayLeave.add(fly);
        }
        if (now >= watch.nextLeave) {
            watch.nextLeave = now + nextWait(player);
            if (!mayLeave.isEmpty())
                leaving.add(mayLeave.get(player.getRandom().nextInt(mayLeave.size())));
        }
        if (leaving.isEmpty())
            return;
        flies.removeAll(leaving);
        Payloads.send(player, new FlySyncPacket(flies));
        for (FlyData ignored : leaving)
            fliesOff(player);
    }

    private static int nextWait(ServerPlayer player) {
        return Mth.nextInt(player.getRandom(), LEAVE_LEAST, LEAVE_MOST);
    }

    /** With a count of 0, the packet's spread is the particle's actual direction: this is the one way to send a particle somewhere from the server. */
    private static void fliesOff(ServerPlayer player) {
        double angle = player.getRandom().nextDouble() * Mth.TWO_PI;
        player.serverLevel().sendParticles(SpawnParticles.FLY.get(), player.getX(), player.getY() + player.getBbHeight() * 0.6D, player.getZ(), 0, Math.cos(angle), FLIES_UP, Math.sin(angle), FLIES_OFF_AT);
        player.level().playSound(null, player.blockPosition(), SpawnSoundEvents.FLY_BUZZ.get(), SoundSource.PLAYERS, 0.5F, 1F);
    }
}
