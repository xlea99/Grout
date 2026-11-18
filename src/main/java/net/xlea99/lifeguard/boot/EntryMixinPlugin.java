package net.xlea99.lifeguard.boot;

import com.bawnorton.mixinsquared.canceller.MixinCancellerRegistrar;
import java.util.List;
import java.util.Set;
import net.xlea99.lifeguard.core.Library;
import net.xlea99.lifeguard.core.MixinGate;
import net.xlea99.lifeguard.core.ModSpec;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.slf4j.Logger;

/**
 * Decides, entry by entry, which of a mod's mixins exist this session (see {@link MixinGate} for the rule), and which of
 * other mods' mixins entries switch off ({@link EntryMixinCanceller}). Each mod's mixin config names its own subclass,
 * which only hands over the mod ({@link #mod}); it must stay free of Minecraft's classes, like everything at this stage.
 */
public abstract class EntryMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = Library.LOGGER;

    /** The mod whose mixin config names this plugin. */
    protected abstract ModCatalog mod();

    @Override
    public void onLoad(String mixinPackage) {
        ModSpec spec = mod().spec();
        if (!spec.mixinRoot().equals(mixinPackage + "."))
            throw new IllegalStateException(spec.mixinConfig() + "'s package is " + mixinPackage
                + ", but the gate expects " + spec.mixinRoot());
        Boot.start(mod());
        // Other mods' mixins are applied class by class long after every config's onLoad, so this is in time.
        MixinCancellerRegistrar.register(new EntryMixinCanceller());
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean apply = MixinGate.shouldApply(mixinClassName, mod().registry(), Boot.dedicatedServer());
        // The workspace's checks read this line's wording ("<mod> mixin <class> -> <target>: applied|skipped").
        LOGGER.debug("{} mixin {} -> {}: {}", mod().spec().name(), mixinClassName, targetClassName, apply ? "applied" : "skipped");
        return apply;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
