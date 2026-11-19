package net.xlea99.grout.entry.spawn_stew_repels_flies;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link SpawnStewRepelsFlies}. {@link Repellent} is kept apart because it names Spawn's classes, and this loads either way. */
public final class SpawnStewRepelsFliesSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(SpawnStewRepelsFlies.ENTRY, () -> NeoForge.EVENT_BUS.addListener(SpawnStewRepelsFliesSetup::onPlayerTick));
    }

    private static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player)
            Repellent.tick(player);
    }
}
