package net.xlea99.grout.mixin.jukebox_playlists;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xlea99.grout.entry.jukebox_playlists.PlaylistJukebox;
import net.xlea99.grout.entry.jukebox_playlists.Playlists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A jukebox plays its playlist ({@link Playlists}): when a song finishes, the next one comes in after a short silence. A
 * jukebox with a disc in it ticks, so the wait is counted here; it and the playlist's place are saved with the jukebox.
 */
@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin implements PlaylistJukebox {
    @Unique
    private int grout$nextTrackIn = -1;
    @Unique
    private int grout$cursor;
    @Unique
    private final List<String> grout$played = new ArrayList<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private static void grout$countDown(Level level, BlockPos pos, BlockState state, JukeboxBlockEntity jukebox,
                                            CallbackInfo ci) {
        if (((PlaylistJukebox) jukebox).grout$trackDue())
            Playlists.nextTrack(jukebox);
    }

    @Override
    public boolean grout$trackDue() {
        // Something already started a song (a disc put in by hand or by a hopper while it waited).
        if (this.grout$nextTrackIn > 0 && ((JukeboxBlockEntity) (Object) this).getSongPlayer().isPlaying())
            this.grout$nextTrackIn = -1;
        if (this.grout$nextTrackIn > 0 && --this.grout$nextTrackIn == 0) {
            this.grout$nextTrackIn = -1;
            return true;
        }
        return false;
    }

    @Override
    public void grout$waitForNextTrack(int ticks) {
        this.grout$nextTrackIn = ticks;
    }

    @Override
    public void grout$cancelNextTrack() {
        this.grout$nextTrackIn = -1;
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void grout$savePlaylist(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        if (this.grout$nextTrackIn > 0)
            tag.putInt("grout_next_track_in", this.grout$nextTrackIn);
        if (this.grout$cursor != 0)
            tag.putInt("grout_cursor", this.grout$cursor);
        if (!this.grout$played.isEmpty()) {
            ListTag played = new ListTag();
            this.grout$played.forEach(id -> played.add(StringTag.valueOf(id)));
            tag.put("grout_played", played);
        }
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void grout$loadPlaylist(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        this.grout$nextTrackIn = tag.contains("grout_next_track_in") ? tag.getInt("grout_next_track_in") : -1;
        this.grout$cursor = tag.getInt("grout_cursor");
        this.grout$played.clear();
        tag.getList("grout_played", Tag.TAG_STRING).forEach(id -> this.grout$played.add(id.getAsString()));
    }

    @Override
    public int grout$cursor() {
        return this.grout$cursor;
    }

    @Override
    public void grout$setCursor(int slot) {
        this.grout$cursor = slot;
    }

    @Override
    public List<String> grout$played() {
        return this.grout$played;
    }
}
