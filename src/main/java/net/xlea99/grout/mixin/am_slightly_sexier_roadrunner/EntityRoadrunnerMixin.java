package net.xlea99.grout.mixin.am_slightly_sexier_roadrunner;

import net.xlea99.grout.entry.am_slightly_sexier_roadrunner.RoadrunnerGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Grout isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityRoadrunner")
public abstract class EntityRoadrunnerMixin implements RoadrunnerGait.Holder {
    @Unique
    private final RoadrunnerGait grout$gait = new RoadrunnerGait();

    @Shadow
    public abstract boolean isMeep();

    @Override
    public RoadrunnerGait grout$gait() {
        return this.grout$gait;
    }

    @Override
    public boolean grout$isMeep() {
        return this.isMeep();
    }
}
