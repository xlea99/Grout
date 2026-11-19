package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake_sleep;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeMind;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake.SnakeSleep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Carries each Rattlesnake's {@link SnakeSleep} and keeps it as its mind tells this client: how far up its head is, and
 * whether it's asleep. The mind tells it as a vanilla entity event ({@code SnakeMind.HEAD_EVENT} plus a step, or plus
 * {@code ASLEEP}), which Alex's Mobs has no answer to, so this adds one. Client only.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public abstract class EntityRattlesnakeMixin extends Animal implements SnakeSleep.Holder {
    @Unique
    private final SnakeSleep grout$sleep = new SnakeSleep();

    private EntityRattlesnakeMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public SnakeSleep grout$sleep() {
        return this.grout$sleep;
    }

    @Override
    public void handleEntityEvent(byte id) {
        int step = id - SnakeMind.HEAD_EVENT;
        if (step >= 0 && step <= SnakeMind.ASLEEP) {
            boolean asleep = step == SnakeMind.ASLEEP;
            this.grout$sleep.asleep = asleep;
            this.grout$sleep.head = asleep ? 0F : step / (float) SnakeMind.HEAD_STEPS;
        } else {
            super.handleEntityEvent(id);
        }
    }
}
