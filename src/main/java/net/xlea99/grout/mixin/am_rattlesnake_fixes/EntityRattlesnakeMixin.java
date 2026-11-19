package net.xlea99.grout.mixin.am_rattlesnake_fixes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The rattle's sound plays when {@code loopSoundTick} is 0 and the count runs to 50 and round again, but only while rattling,
 * and nothing puts it back to 0 when a rattle ends: the next rattle started wherever the last left off, silent for up to 50
 * ticks. Not rattling, it's back to 0, so a rattle's sound starts with it. Targeted by name.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public abstract class EntityRattlesnakeMixin {
    @Shadow
    private int loopSoundTick;

    @Shadow
    public abstract boolean isRattling();

    @Inject(method = "tick", at = @At("HEAD"))
    private void grout$rattleSoundFromTheStart(CallbackInfo ci) {
        if (!this.isRattling())
            this.loopSoundTick = 0;
    }
}
