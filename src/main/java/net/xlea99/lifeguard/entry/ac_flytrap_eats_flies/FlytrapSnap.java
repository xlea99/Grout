package net.xlea99.lifeguard.entry.ac_flytrap_eats_flies;

import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.xlea99.lifeguard.compat.SpawnFlies;

/** What an open Flytrap does with something inside it ({@link AcFlytrapEatsFlies}). It shuts just as it does when it shuts by itself, and opens again on the same timer. */
public final class FlytrapSnap {
    /** The chance each tick something with flies spends inside an open one: a walk through takes several ticks. */
    private static final float SNAP_CHANCE = 0.05F;
    /** How long it stays shut, in ticks, as Alex's Caves has it. */
    private static final int SHUT_LEAST = 100;
    private static final int SHUT_MOST = 199;

    private FlytrapSnap() {
    }

    public static void inside(BlockState state, Level level, BlockPos pos, Entity entity, BooleanProperty open) {
        if (!(level instanceof ServerLevel server) || !(entity instanceof LivingEntity carrier) || !state.getValue(open)
            || server.getRandom().nextFloat() >= SNAP_CHANCE || SpawnFlies.edible(carrier).isEmpty())
            return;
        SpawnFlies.eatOne(carrier, server.getRandom());
        server.setBlock(pos, state.setValue(open, false), 2);
        server.scheduleTick(pos, state.getBlock(), Mth.nextInt(server.getRandom(), SHUT_LEAST, SHUT_MOST));
        server.playSound(null, pos, SpawnSoundEvents.FLY_DEATH.get(), SoundSource.BLOCKS, 1F, 1F);
    }
}
