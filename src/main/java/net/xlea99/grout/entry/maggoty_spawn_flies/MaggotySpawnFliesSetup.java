package net.xlea99.grout.entry.maggoty_spawn_flies;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link MaggotySpawnFlies}. {@link Infestation} is kept apart because it names Spawn's classes, and this loads either way. */
public final class MaggotySpawnFliesSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(MaggotySpawnFlies.ENTRY, () -> {
            NeoForge.EVENT_BUS.addListener(MaggotySpawnFliesSetup::onPlayerTick);
            NeoForge.EVENT_BUS.addListener(MaggotySpawnFliesSetup::onDrops);
        });
    }

    private static void onDrops(LivingDropsEvent event) {
        Infestation.dropMaggots(event.getEntity(), event.getDrops());
    }

    private static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player)
            Infestation.tick(player);
    }
}
