package net.xlea99.lifeguard;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.xlea99.guppylib.boot.Boot;
import net.xlea99.guppylib.boot.Setups;
import net.xlea99.lifeguard.boot.LifeguardSpec;
import net.xlea99.lifeguard.magnet.Magnets;
import org.slf4j.Logger;

/**
 * Lifeguard's entry point: its entries (handed to GuppyLib, which runs the shared machinery from its own mod class), then
 * every entry's own setup ({@link Setups}).
 */
@Mod(Lifeguard.MODID)
public class Lifeguard {
    public static final String MODID = "lifeguard";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Lifeguard(IEventBus modEventBus, ModContainer modContainer) {
        // The mixin plugin normally got here first; this only matters if it didn't.
        Boot.start(LifeguardSpec.CATALOG);
        Setups.runCommon(modEventBus);
        // Alex's Caves magnets for every block that is one (shared by several entries' metals): joins AC's after each
        // block's entry has said so.
        Magnets.register(modEventBus);
    }
}
