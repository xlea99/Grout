package net.xlea99.lifeguard.mixin.am_slightly_sexier_rattlesnake_motion;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.xlea99.guppylib.sync.RemoteServer;
import net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake.AmSlightlySexierRattlesnake;
import net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake.SnakeBody;
import net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake.SnakeMind;
import net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake.SnakeNotice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Carries each Rattlesnake's {@link SnakeBody} and keeps how far up its head should be: as the server tells it when our
 * server entry runs there ({@link SnakeMind}), else up whenever anything is near enough to notice; and whether it's asleep,
 * as the server tells it. Coiled and resting, no tongue flicks. Client only.
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
    private final SnakeBody lifeguard$snakeBody = new SnakeBody();

    @Unique
    private float lifeguard$coilYaw = Float.NaN;

    private EntityRattlesnakeMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public SnakeBody lifeguard$snakeBody() {
        return this.lifeguard$snakeBody;
    }

    /** Alex's Mobs has no answer to this event of its own to hook, so this adds one. Its numbers come from {@link SnakeMind}. */
    @Override
    public void handleEntityEvent(byte id) {
        int step = id - SnakeMind.HEAD_EVENT;
        if (step >= 0 && step <= SnakeMind.ASLEEP) {
            boolean asleep = step == SnakeMind.ASLEEP;
            this.lifeguard$snakeBody.asleep = asleep;
            this.lifeguard$snakeBody.told = asleep ? 0F : step / (float) SnakeMind.HEAD_STEPS;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void lifeguard$notice(CallbackInfo ci) {
        if (!this.level().isClientSide)
            return;
        SnakeBody body = this.lifeguard$snakeBody;
        if (lifeguard$serverTells()) {
            body.head = Float.isNaN(body.told) ? 0F : body.told;
        } else {
            body.asleep = false;
            if ((this.tickCount + this.getId()) % 10 == 0)
                body.head = SnakeNotice.nearest(this) != null ? 1F : 0F;
        }
        EntityRattlesnakeAccessor snake = (EntityRattlesnakeAccessor) this;
        boolean curled = snake.lifeguard$curledNow();
        // The tongue: none resting coiled or asleep; tasting the air, quick and often, while it watches or is on the move;
        // otherwise Alex's own now and then.
        boolean moving = this.getX() != this.xo || this.getZ() != this.zo;
        if (body.asleep || curled && !snake.lifeguard$rattlingNow() && body.head < RESTING)
            snake.lifeguard$setTongueTicks(0);
        else if ((body.head >= RESTING || moving) && snake.lifeguard$tongueTicks() == 0 && this.random.nextInt(TASTE_CHANCE) == 0)
            snake.lifeguard$setTongueTicks(TASTE_LEAST + this.random.nextInt(TASTE_SPREAD));
        // Coiled, the body holds where it faced: only the head and neck turn to look. (Left alone, the body turns after the
        // head whenever it turns far, here as on any still mob, so the coil spun on the spot.)
        if (curled && this.getTarget() == null) {
            if (Float.isNaN(this.lifeguard$coilYaw))
                this.lifeguard$coilYaw = this.yBodyRot;
            this.yBodyRot = this.lifeguard$coilYaw;
            this.yBodyRotO = this.lifeguard$coilYaw;
        } else {
            this.lifeguard$coilYaw = Float.NaN;
        }
    }

    /** Whether the server here runs our entry (its switch, as this client follows it), so it tells each snake's head. */
    @Unique
    private static boolean lifeguard$serverTells() {
        return AmSlightlySexierRattlesnake.ENTRY.active() && (Minecraft.getInstance().hasSingleplayerServer() || RemoteServer.following());
    }
}
