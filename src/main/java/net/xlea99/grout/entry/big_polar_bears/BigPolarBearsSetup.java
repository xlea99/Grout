package net.xlea99.grout.entry.big_polar_bears;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.xlea99.guppylib.boot.EntrySetup;
import net.xlea99.guppylib.boot.Wiring;

/** Setup for {@link BigPolarBears}. */
public final class BigPolarBearsSetup implements EntrySetup {
    /** Keep under about 1.42: past that, the hitbox's 1.4 passes 2 blocks and pathfinding treats the bear as a 3 block wide, 3 tall mob. */
    private static final double SCALE = 1.3;
    private static final double MAX_HEALTH = 70;
    private static final double ATTACK_DAMAGE = 8;

    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(BigPolarBears.ENTRY, (IEventBus modBus) -> modBus.addListener(BigPolarBearsSetup::onAttributes));
    }

    private static void onAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.POLAR_BEAR, Attributes.SCALE, SCALE);
        event.add(EntityType.POLAR_BEAR, Attributes.MAX_HEALTH, MAX_HEALTH);
        event.add(EntityType.POLAR_BEAR, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
    }
}
