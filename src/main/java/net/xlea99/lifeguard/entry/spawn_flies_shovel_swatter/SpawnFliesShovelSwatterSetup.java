package net.xlea99.lifeguard.entry.spawn_flies_shovel_swatter;

import net.neoforged.bus.api.IEventBus;
import net.xlea99.lifeguard.boot.EntrySetup;
import net.xlea99.lifeguard.boot.Wiring;

/** Setup for {@link SpawnFliesShovelSwatter}. */
public final class SpawnFliesShovelSwatterSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(SpawnFliesShovelSwatter.ENTRY, (IEventBus bus) -> bus.addListener(SwatPayload::register));
    }
}
