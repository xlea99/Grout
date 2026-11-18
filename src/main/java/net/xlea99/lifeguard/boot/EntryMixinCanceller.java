package net.xlea99.lifeguard.boot;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import java.util.List;
import net.xlea99.lifeguard.core.Entry;
import net.xlea99.lifeguard.core.Library;
import org.slf4j.Logger;

/** Switches off the other mods' mixins an active entry cancels ({@link Entry#cancelsMixins}), as each is applied. */
public final class EntryMixinCanceller implements MixinCanceller {
    private static final Logger LOGGER = Library.LOGGER;

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        for (Entry entry : Boot.registry().entries()) {
            if (entry.cancels(mixinClassName)) {
                LOGGER.info("{} {}: switched off {}", Boot.registry().owner().name(), entry.id(), mixinClassName);
                return true;
            }
        }
        return false;
    }
}
