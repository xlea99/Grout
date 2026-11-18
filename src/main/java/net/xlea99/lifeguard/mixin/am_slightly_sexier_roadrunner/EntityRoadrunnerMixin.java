package net.xlea99.lifeguard.mixin.am_slightly_sexier_roadrunner;

import net.xlea99.lifeguard.entry.am_slightly_sexier_roadrunner.RoadrunnerGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRoadrunner")
public abstract class EntityRoadrunnerMixin implements RoadrunnerGait.Holder {
    @Unique
    private final RoadrunnerGait lifeguard$gait = new RoadrunnerGait();

    @Shadow
    public abstract boolean isMeep();

    @Override
    public RoadrunnerGait lifeguard$gait() {
        return this.lifeguard$gait;
    }

    @Override
    public boolean lifeguard$isMeep() {
        return this.isMeep();
    }
}
