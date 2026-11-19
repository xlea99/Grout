package net.xlea99.lifeguard.entry.spawn_frogs_eat_flies;

import net.minecraft.client.model.FrogModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Client setup for {@link SpawnFrogsEatFlies}. */
public final class SpawnFrogsEatFliesClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(SpawnFrogsEatFlies.FLY_ON_TONGUE, (IEventBus bus) -> bus.addListener(SpawnFrogsEatFliesClientSetup::onAddLayers));
    }

    private static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer<Frog, FrogModel<Frog>> renderer = event.getRenderer(EntityType.FROG);
        if (renderer != null)
            renderer.addLayer(new FlyOnTongueLayer(renderer, event.getEntityModels()));
    }
}
