package net.xlea99.lifeguard.entry.connectiblechains_knots_always_tick;

import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.xlea99.guppylib.boot.BrokenEntries;
import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link ConnectibleChainsKnotsAlwaysTick}. Entity Culling is found by name, so none of it is compiled in. */
public final class ConnectibleChainsKnotsAlwaysTickClientSetup implements EntrySetup {
    private static final ResourceLocation KNOT = ResourceLocation.fromNamespaceAndPath("connectiblechains", "chain_knot");

    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(ConnectibleChainsKnotsAlwaysTick.ENTRY,
            modBus -> modBus.addListener(ConnectibleChainsKnotsAlwaysTickClientSetup::onClientSetup));
    }

    /** Queued: Entity Culling makes its instance in its own client setup listener, and those run in parallel with this one. */
    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ConnectibleChainsKnotsAlwaysTickClientSetup::addKnots);
    }

    @SuppressWarnings("unchecked")
    private static void addKnots() {
        try {
            Class<?> culling = Class.forName("dev.tr7zw.entityculling.EntityCullingModBase");
            Object instance = culling.getField("instance").get(null);
            Set<EntityType<?>> exceptions = (Set<EntityType<?>>) culling.getField("tickCullWhitelists").get(instance);
            exceptions.add(BuiltInRegistries.ENTITY_TYPE.get(KNOT));
        } catch (ReflectiveOperationException | RuntimeException e) {
            BrokenEntries.mark(ConnectibleChainsKnotsAlwaysTick.ENTRY, "Entity Culling's tick exceptions can't be reached (" + e + ")");
        }
    }
}
