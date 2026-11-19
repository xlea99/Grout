package net.xlea99.grout.mixin.am_slightly_sexier_rattlesnake_motion;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.SnakeBody;
import net.xlea99.grout.entry.am_slightly_sexier_rattlesnake_motion.SnakeCues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Carries each Rattlesnake's {@link SnakeBody} and keeps how far up its head should be and whether it's asleep, as
 * {@link SnakeCues} has it: its mind's word when something tells it, else up whenever anything is near enough to notice,
 * and never asleep. Coiled and resting, no tongue flicks. Client only; it never names the mind.
 */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRattlesnake")
public abstract class EntityRattlesnakeMixin extends Animal implements SnakeBody.Holder {
    /** Below this far up, a coiled snake that isn't rattling is resting: Alex's random tongue flicks are held off. */
    @Unique
    private static final float RESTING = 0.5F;

    /** Tasting the air: a one in this many chance a tick of a flick, lasting this many ticks and up to this many more (Alex's: 1 in 15, 10 to 30). */
    @Unique
    private static final int TASTE_CHANCE = 5, TASTE_LEAST = 5, TASTE_SPREAD = 6;

    @Unique
    private final SnakeBody grout$snakeBody = new SnakeBody();

    @Unique
    private float grout$coilYaw = Float.NaN;

    private EntityRattlesnakeMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public SnakeBody grout$snakeBody() {
        return this.grout$snakeBody;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void grout$notice(CallbackInfo ci) {
        if (!this.level().isClientSide)
            return;
        SnakeBody body = this.grout$snakeBody;
        float told = SnakeCues.told(this);
        if (!Float.isNaN(told)) {
            body.head = told;
            body.asleep = SnakeCues.asleep(this);
        } else {
            body.asleep = false;
            if ((this.tickCount + this.getId()) % 10 == 0)
                body.head = SnakeCues.anyoneNear(this) ? 1F : 0F;
        }
        EntityRattlesnakeAccessor snake = (EntityRattlesnakeAccessor) this;
        boolean curled = snake.grout$curledNow();
        // The tongue: none resting coiled or asleep; tasting the air, quick and often, while it watches or is on the move;
        // otherwise Alex's own now and then.
        boolean moving = this.getX() != this.xo || this.getZ() != this.zo;
        if (body.asleep || curled && !snake.grout$rattlingNow() && body.head < RESTING)
            snake.grout$setTongueTicks(0);
        else if ((body.head >= RESTING || moving) && snake.grout$tongueTicks() == 0 && this.random.nextInt(TASTE_CHANCE) == 0)
            snake.grout$setTongueTicks(TASTE_LEAST + this.random.nextInt(TASTE_SPREAD));
        // Coiled, the body holds where it faced: only the head and neck turn to look. (Left alone, the body turns after the
        // head whenever it turns far, here as on any still mob, so the coil spun on the spot.)
        if (curled && this.getTarget() == null) {
            if (Float.isNaN(this.grout$coilYaw))
                this.grout$coilYaw = this.yBodyRot;
            this.yBodyRot = this.grout$coilYaw;
            this.yBodyRotO = this.grout$coilYaw;
        } else {
            this.grout$coilYaw = Float.NaN;
        }
    }
}
