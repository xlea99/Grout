package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.SnakeCues;
import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/**
 * Client setup for {@link AmSlightlySexierRattlesnake#SLEEP}: its sleeping texture, made again at every resource reload, and
 * what it knows handed to the slithering ({@link SleepCues}), so the head lifts as far as the mind says.
 */
public final class AmSlightlySexierRattlesnakeClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(AmSlightlySexierRattlesnake.SLEEP, modBus -> modBus.addListener((RegisterClientReloadListenersEvent event) ->
            event.registerReloadListener((ResourceManagerReloadListener) SleepingRattlesnakeTexture::rebuild)));
        wiring.whenActive(AmSlightlySexierRattlesnake.SLEEP, () -> SnakeCues.set(new SleepCues()));
    }
}
