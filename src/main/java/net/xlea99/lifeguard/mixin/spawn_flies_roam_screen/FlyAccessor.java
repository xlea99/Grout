package net.xlea99.lifeguard.mixin.spawn_flies_roam_screen;

import com.ninni.spawn.client.events.FlyEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlyEvents.Fly.class)
public interface FlyAccessor {
    @Accessor("x")
    float lifeguard$x();

    @Accessor("y")
    float lifeguard$y();

    @Accessor("targetX")
    void lifeguard$setTargetX(float x);

    @Accessor("targetY")
    void lifeguard$setTargetY(float y);

    @Accessor("scaredTime")
    int lifeguard$scaredTime();
}
