package net.xlea99.grout.mixin.spawn_frogs_eat_flies;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.xlea99.grout.entry.spawn_frogs_eat_flies.FrogSnack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Vanilla's frog has no answer to this event of its own to hook, so this adds one. */
@Mixin(Frog.class)
public abstract class FrogTongueMixin extends Animal implements FrogSnack.Holder {
    @Unique
    private float grout$flyOnTongueAt = -1000F;

    private FrogTongueMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public float grout$flyOnTongueAt() {
        return this.grout$flyOnTongueAt;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == FrogSnack.FLY_ON_TONGUE_EVENT)
            this.grout$flyOnTongueAt = this.tickCount;
        else
            super.handleEntityEvent(id);
    }
}
