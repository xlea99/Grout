package net.xlea99.grout.mixin.jukebox_progress_signal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xlea99.grout.entry.jukebox_progress_signal.JukeboxProgress;
import net.xlea99.grout.entry.jukebox_progress_signal.ProgressJukebox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Each tick a jukebox checks whether its progress reading moved on ({@link JukeboxProgress}), and tells comparators when
 * it has (about 15 times a song).
 */
@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin implements ProgressJukebox {
    @Unique
    private int grout$lastProgress = -1;

    @Inject(method = "tick", at = @At("TAIL"))
    private static void grout$progressMoved(Level level, BlockPos pos, BlockState state, JukeboxBlockEntity jukebox,
                                                CallbackInfo ci) {
        // Jukeboxes tick on both sides; comparators only read the server's.
        if (level.isClientSide)
            return;
        ProgressJukebox progressJukebox = (ProgressJukebox) jukebox;
        int progress = JukeboxProgress.of(jukebox);
        if (progress != progressJukebox.grout$lastProgress()) {
            progressJukebox.grout$setLastProgress(progress);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
        }
    }

    @Override
    public int grout$lastProgress() {
        return this.grout$lastProgress;
    }

    @Override
    public void grout$setLastProgress(int progress) {
        this.grout$lastProgress = progress;
    }
}
