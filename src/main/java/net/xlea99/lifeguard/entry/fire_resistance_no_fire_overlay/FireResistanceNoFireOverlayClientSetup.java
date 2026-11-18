package net.xlea99.lifeguard.entry.fire_resistance_no_fire_overlay;

import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.xlea99.lifeguard.boot.EntrySetup;
import net.xlea99.lifeguard.boot.Wiring;

/** Client setup for {@link FireResistanceNoFireOverlay}. */
public final class FireResistanceNoFireOverlayClientSetup implements EntrySetup {
    @Override
    public void setup(Wiring wiring) {
        wiring.whenActive(FireResistanceNoFireOverlay.ENTRY, () -> NeoForge.EVENT_BUS.addListener(FireResistanceNoFireOverlayClientSetup::onScreenEffect));
    }

    private static void onScreenEffect(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE && event.getPlayer().hasEffect(MobEffects.FIRE_RESISTANCE))
            event.setCanceled(true);
    }
}
