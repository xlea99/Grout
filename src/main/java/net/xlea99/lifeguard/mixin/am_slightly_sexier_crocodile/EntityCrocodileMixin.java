package net.xlea99.lifeguard.mixin.am_slightly_sexier_crocodile;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.xlea99.lifeguard.entry.am_slightly_sexier_crocodile.CrocodileGait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** Targeted by name: Lifeguard isn't built against Alex's Mobs. */
@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityCrocodile")
public abstract class EntityCrocodileMixin extends TamableAnimal implements CrocodileGait.Holder {
    @Shadow public float prevBaskingProgress;
    @Shadow public float baskingProgress;
    @Shadow public float prevSwimProgress;
    @Shadow public float swimProgress;
    @Shadow public int baskingType;

    @Unique
    private final CrocodileGait lifeguard$gait = new CrocodileGait();

    private EntityCrocodileMixin(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    /** Alex's Mobs has no answer to this event of its own to hook, so this adds one. Its number comes from {@code am_crocodile_ambush}. */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == CrocodileGait.MISSED_EVENT)
            this.lifeguard$gait.missed(this.tickCount);
        else
            super.handleEntityEvent(id);
    }

    @Override
    public CrocodileGait lifeguard$gait() {
        return this.lifeguard$gait;
    }

    @Override
    public int lifeguard$baskingType() {
        return this.baskingType;
    }

    @Override
    public float lifeguard$basking(float partialTick) {
        return Mth.lerp(partialTick, this.prevBaskingProgress, this.baskingProgress) / 10F;
    }

    @Override
    public float lifeguard$swimming(float partialTick) {
        return Mth.lerp(partialTick, this.prevSwimProgress, this.swimProgress) / 10F;
    }
}
