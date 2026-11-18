package net.xlea99.lifeguard.entry.am_gazelle_step_height;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.xlea99.lifeguard.boot.EntrySetup;
import net.xlea99.lifeguard.boot.Wiring;

/** Setup for {@link AmGazelleStepHeight}. */
public final class AmGazelleStepHeightSetup implements EntrySetup {
    private static final ResourceLocation GAZELLE = ResourceLocation.fromNamespaceAndPath("alexsmobs", "gazelle");
    /** Just over a block: the move code jumps for any rise higher than this. */
    private static final double STEP_HEIGHT = 1.1;

    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(AmGazelleStepHeight.ENTRY, (IEventBus modBus) -> modBus.addListener(AmGazelleStepHeightSetup::onAttributes));
    }

    @SuppressWarnings("unchecked")
    private static void onAttributes(EntityAttributeModificationEvent event) {
        BuiltInRegistries.ENTITY_TYPE.getOptional(GAZELLE)
            .ifPresent(type -> event.add((EntityType<? extends LivingEntity>) type, Attributes.STEP_HEIGHT, STEP_HEIGHT));
    }
}
