package net.xlea99.grout.entry.dye_the_world_glow_lights;

import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link DyeTheWorldGlowLights}. */
public final class DyeTheWorldGlowLightsClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(DyeTheWorldGlowLights.ENTRY, GlowLightsPack::register);
    }
}
