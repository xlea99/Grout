package net.xlea99.grout.mixin.moonlight_registration_races;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.gui.ConfigScreenExtensions;
import net.mehvahdjukaar.moonlight.api.client.gui.widget.MediaButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Makes adding to Moonlight's config screen extras thread-safe.
 *
 * <p>Moonlight mods add these from their mod constructors (Snowy Spirit's season overlay, for one), and NeoForge constructs mods in parallel. Moonlight keeps
 * them in plain {@code HashMap}s, so two landing at once either crash the launch ({@code ConcurrentModificationException} in {@code computeIfAbsent}, seen
 * with Snowy Spirit 3.1.16) or quietly lose one. Additions now take turns. They're only read later, on one thread, once a config screen opens.
 */
@Mixin(ConfigScreenExtensions.class)
public abstract class ConfigScreenExtensionsMixin {
    @Unique
    private static final Object GROUT$LOCK = new Object();

    @WrapMethod(method = "registerOverlay")
    private static void grout$overlay(String modId, ConfigScreenExtensions.Overlay overlay, Operation<Void> original) {
        synchronized (GROUT$LOCK) {
            original.call(modId, overlay);
        }
    }

    @WrapMethod(method = "registerShowcase")
    private static void grout$showcase(String modId, ConfigScreenExtensions.Showcase showcase, Operation<Void> original) {
        synchronized (GROUT$LOCK) {
            original.call(modId, showcase);
        }
    }

    @WrapMethod(method = "registerIcon")
    private static void grout$icon(ResourceLocation id, Supplier<ItemStack> stack, Operation<Void> original) {
        synchronized (GROUT$LOCK) {
            original.call(id, stack);
        }
    }

    @WrapMethod(method = "registerLink")
    private static void grout$link(String modId, MediaButton.MediaIcon icon, String url, Operation<Void> original) {
        synchronized (GROUT$LOCK) {
            original.call(modId, icon, url);
        }
    }

    @WrapMethod(method = "registerFooterButton")
    private static void grout$footerButton(String modId, ConfigScreenExtensions.Side side, ConfigScreenExtensions.FooterButton factory, Operation<Void> original) {
        synchronized (GROUT$LOCK) {
            original.call(modId, side, factory);
        }
    }
}
