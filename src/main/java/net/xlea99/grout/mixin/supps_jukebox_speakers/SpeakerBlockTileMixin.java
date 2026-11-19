package net.xlea99.grout.mixin.supps_jukebox_speakers;

import net.mehvahdjukaar.supplementaries.common.block.tiles.SpeakerBlockTile;
import net.xlea99.grout.entry.supps_jukebox_speakers.JukeboxSpeakers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** A speaker touching a jukebox is its speaker, not a message board ({@link JukeboxSpeakers}). */
@Mixin(SpeakerBlockTile.class)
public abstract class SpeakerBlockTileMixin {
    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void grout$jukeboxSpeaker(CallbackInfo ci) {
        SpeakerBlockTile self = (SpeakerBlockTile) (Object) this;
        if (self.getLevel() != null && JukeboxSpeakers.linked(self.getLevel(), self.getBlockPos()))
            ci.cancel();
    }
}
