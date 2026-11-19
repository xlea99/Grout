package net.xlea99.grout.mixin.jukebox_record_scratch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.xlea99.grout.entry.jukebox_record_scratch.RecordScratch;
import net.xlea99.grout.entry.jukebox_record_scratch.ScratchedJukebox;
import net.xlea99.grout.entry.jukebox_synced_playback.JukeboxSync;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A scratched song holds its place: its clock (and so its heartbeat, redstone and ending) waits while a creeper by it is
 * still hissing and through the awkward silence after, then it carries on, and everyone in earshot is told at once so
 * the music comes back mid-song.
 */
@Mixin(JukeboxSongPlayer.class)
public abstract class JukeboxSongPlayerMixin implements ScratchedJukebox {
    @Shadow
    @Nullable
    private Holder<JukeboxSong> song;
    @Shadow
    private long ticksSinceSongStarted;
    @Shadow
    @Final
    private BlockPos blockPos;

    @Unique
    private boolean grout$scratched;
    /** Ticks of awkward silence left once nothing by it is hissing. */
    @Unique
    private int grout$silence;

    @Override
    public boolean grout$scratch() {
        if (this.grout$scratched)
            return false;
        this.grout$scratched = true;
        this.grout$silence = RecordScratch.AWKWARD_SILENCE;
        return true;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void grout$holdThePlace(LevelAccessor level, @Nullable BlockState state, CallbackInfo ci) {
        if (!this.grout$scratched)
            return;
        if (this.song == null || !(level instanceof ServerLevel server)) {
            this.grout$scratched = false;
            return;
        }
        if (RecordScratch.stillHissing(server, this.blockPos)) {
            this.grout$silence = RecordScratch.AWKWARD_SILENCE;
            ci.cancel();
            return;
        }
        if (this.grout$silence > 0) {
            this.grout$silence--;
            ci.cancel();
            return;
        }
        this.grout$scratched = false;
        JukeboxSync.heartbeat(server, this.blockPos, this.song, this.ticksSinceSongStarted, JukeboxSync.RANGE);
    }

    @Inject(method = {"play", "stop"}, at = @At("HEAD"))
    private void grout$forgetTheScratch(CallbackInfo ci) {
        this.grout$scratched = false;
    }
}
