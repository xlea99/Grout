package net.xlea99.grout.mixin.jukebox_synced_playback;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.xlea99.grout.entry.jukebox_synced_playback.JukeboxSyncedPlayback;
import net.xlea99.grout.entry.jukebox_synced_playback.SyncedJukeboxSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** A jukebox's song is one that can start partway in and keep time with its jukebox. */
@Mixin(SimpleSoundInstance.class)
public abstract class SimpleSoundInstanceMixin {
    @Inject(method = "forJukeboxSong", at = @At("HEAD"), cancellable = true)
    private static void grout$syncedSong(SoundEvent sound, Vec3 pos, CallbackInfoReturnable<SimpleSoundInstance> cir) {
        if (JukeboxSyncedPlayback.ENTRY.active())
            cir.setReturnValue(SyncedJukeboxSound.create(sound, pos));
    }
}
