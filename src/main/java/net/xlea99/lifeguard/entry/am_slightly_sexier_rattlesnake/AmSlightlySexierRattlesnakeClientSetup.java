package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.xlea99.lifeguard.boot.EntrySetup;
import net.xlea99.lifeguard.boot.Wiring;

/** Client setup for {@link AmSlightlySexierRattlesnake#MOTION}: its sleeping texture, made again at every resource reload. */
public final class AmSlightlySexierRattlesnakeClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(AmSlightlySexierRattlesnake.MOTION, modBus -> modBus.addListener((RegisterClientReloadListenersEvent event) ->
            event.registerReloadListener((ResourceManagerReloadListener) SleepingRattlesnakeTexture::rebuild)));
    }
}
