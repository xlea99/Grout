package net.xlea99.grout.mixin.spawn_flies_roam_screen;

import com.ninni.spawn.client.events.FlyEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlyEvents.Fly.class)
public interface FlyAccessor {
    @Accessor("x")
    float grout$x();

    @Accessor("y")
    float grout$y();

    @Accessor("targetX")
    void grout$setTargetX(float x);

    @Accessor("targetY")
    void grout$setTargetY(float y);

    @Accessor("scaredTime")
    int grout$scaredTime();
}
