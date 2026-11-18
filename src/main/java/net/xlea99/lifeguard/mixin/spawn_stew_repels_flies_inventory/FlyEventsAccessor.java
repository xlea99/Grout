package net.xlea99.lifeguard.mixin.spawn_stew_repels_flies_inventory;

import com.ninni.spawn.client.events.FlyEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlyEvents.Fly.class)
public interface FlyEventsAccessor {
    @Accessor("x")
    float lifeguard$stewX();

    @Accessor("y")
    float lifeguard$stewY();

    @Accessor("targetX")
    float lifeguard$stewTargetX();

    @Accessor("targetY")
    float lifeguard$stewTargetY();

    @Accessor("targetX")
    void lifeguard$setStewTargetX(float x);

    @Accessor("targetY")
    void lifeguard$setStewTargetY(float y);

    @Accessor("scaredTime")
    int lifeguard$stewScaredTime();

    @Accessor("scaredTime")
    void lifeguard$setStewScaredTime(int time);

    @Accessor("dead")
    boolean lifeguard$stewDead();
}
