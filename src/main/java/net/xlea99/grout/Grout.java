package net.xlea99.grout;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.xlea99.guppylib.boot.Boot;
import net.xlea99.guppylib.boot.Setups;
import net.xlea99.grout.boot.GroutSpec;
import net.xlea99.grout.magnet.Magnets;
import org.slf4j.Logger;

/**
 * Grout's entry point: its entries (handed to GuppyLib, which runs the shared machinery from its own mod class), then
 * every entry's own setup ({@link Setups}).
 */
@Mod(Grout.MODID)
public class Grout {
    public static final String MODID = "grout";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Grout(IEventBus modEventBus, ModContainer modContainer) {
        // The mixin plugin normally got here first; this only matters if it didn't.
        Boot.start(GroutSpec.CATALOG);
        Setups.runCommon(modEventBus);
        // Alex's Caves magnets for every block that is one (shared by several entries' metals): joins AC's after each
        // block's entry has said so.
        Magnets.register(modEventBus);
    }
}
