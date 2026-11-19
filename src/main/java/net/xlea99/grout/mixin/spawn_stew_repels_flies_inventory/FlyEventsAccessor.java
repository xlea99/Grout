package net.xlea99.grout.mixin.spawn_stew_repels_flies_inventory;

import com.ninni.spawn.client.events.FlyEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlyEvents.Fly.class)
public interface FlyEventsAccessor {
    @Accessor("x")
    float grout$stewX();

    @Accessor("y")
    float grout$stewY();

    @Accessor("targetX")
    float grout$stewTargetX();

    @Accessor("targetY")
    float grout$stewTargetY();

    @Accessor("targetX")
    void grout$setStewTargetX(float x);

    @Accessor("targetY")
    void grout$setStewTargetY(float y);

    @Accessor("scaredTime")
    int grout$stewScaredTime();

    @Accessor("scaredTime")
    void grout$setStewScaredTime(int time);

    @Accessor("dead")
    boolean grout$stewDead();
}
