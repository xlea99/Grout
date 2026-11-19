package net.xlea99.grout.mixin.moonlight_registration_races;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicResourcesProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Makes registering a Moonlight dynamic resource provider take turns, so the merged pack is always registered.
 *
 * <p>Every Moonlight mod registers the provider that generates its assets from its mod constructor, and NeoForge constructs
 * mods in parallel. With Moonlight's {@code merge_dynamic_resource_packs} on (its default), a client provider joins one
 * merged pack: {@code MoonlightClient.mergePackSupplier} adds it (locked), then, outside the lock, hands the merged pack back
 * to be registered only if it now holds exactly one provider. Two mods' first providers arriving together can both see two,
 * and nobody registers the merged pack: every Moonlight mod's generated textures are missing for that launch (vertical slabs,
 * Every Compat blocks, drawn as the purple and black missing texture). Proven on the real classes: 3 in a million paired
 * registrations lost it, none with the two steps locked together. The whole registration now runs one mod at a time.
 */
@Mixin(RegHelper.class)
public abstract class RegHelperMixin {

    @Unique
    private static final Object GROUT$LOCK = new Object();

    @WrapMethod(method = "registerDynamicResourceProvider")
    private static void grout$oneAtATime(DynamicResourcesProvider provider, Operation<Void> original) {
        synchronized (GROUT$LOCK) {
            original.call(provider);
        }
    }
}
