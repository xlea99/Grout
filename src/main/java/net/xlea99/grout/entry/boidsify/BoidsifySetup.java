package net.xlea99.grout.entry.boidsify;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;
import net.xlea99.grout.boids.Boids;

/** Setup for {@link Boidsify}. */
public final class BoidsifySetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(Boidsify.ENTRY, Boids::register);
    }
}
