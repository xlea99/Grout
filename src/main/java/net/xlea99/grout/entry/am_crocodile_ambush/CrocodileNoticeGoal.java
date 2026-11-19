package net.xlea99.grout.entry.am_crocodile_ambush;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

/**
 * When a Crocodile decides to strike, in place of Alex's Mobs' three target goals: it needs to see its target, and the closer it is the likelier it
 * goes for it, so that nobody can tell when it will. While it's resting, only something right at its jaws.
 */
public final class CrocodileNoticeGoal extends TargetGoal {
    /** How far it notices anything, and where it's near certain to strike, in blocks. A sneaking player counts as a bit further away. */
    public static final float RANGE = 10F;
    private static final float SURE_RANGE = 5F;
    /** Close enough to bite without running. */
    public static final float JAW_RANGE = 3.5F;
    /** How often it checks, in ticks. */
    private static final int CHECK_EVERY = 10;
    private static final TagKey<EntityType<?>> PREY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("alexsmobs", "crocodile_targets"));

    private final TamableAnimal crocodile;
    private final CrocodileAmbush ambush;
    private final TargetingConditions conditions = TargetingConditions.forCombat().selector(this::isPrey);
    private long nextCheck;
    private LivingEntity found;

    public CrocodileNoticeGoal(TamableAnimal crocodile, CrocodileAmbush ambush) {
        super(crocodile, false);
        this.crocodile = crocodile;
        this.ambush = ambush;
    }

    private boolean isPrey(LivingEntity entity) {
        if (this.crocodile.isTame())
            return entity instanceof Monster && !(entity instanceof Creeper);
        return entity instanceof Player ? this.crocodile.level().getDifficulty() != Difficulty.PEACEFUL : entity.getType().is(PREY);
    }

    @Override
    public boolean canUse() {
        long now = this.crocodile.level().getGameTime();
        if (now < this.nextCheck || this.crocodile.isBaby() || this.crocodile.isInLove() || this.ambush.busy() || this.ambush.wantsToRun() || !this.crocodile.getPassengers().isEmpty())
            return false;
        this.nextCheck = now + CHECK_EVERY;
        Animation animation = ((IAnimatedEntity) this.crocodile).getAnimation();
        if (animation != null && animation != IAnimatedEntity.NO_ANIMATION)
            return false;

        boolean mayRun = this.ambush.rested() && !(this.crocodile.isTame() && ((CrocodileAmbush.Holder) this.crocodile).grout$sitting());
        float range = mayRun ? RANGE : JAW_RANGE;
        this.found = null;
        double nearest = Double.MAX_VALUE;
        this.conditions.range(range);
        for (LivingEntity entity : this.crocodile.level().getNearbyEntities(LivingEntity.class, this.conditions, this.crocodile, this.crocodile.getBoundingBox().inflate(range))) {
            double distance = this.crocodile.distanceToSqr(entity);
            if (distance < nearest) {
                nearest = distance;
                this.found = entity;
            }
        }
        if (this.found == null)
            return false;
        float closeness = Mth.clamp((RANGE - (float) Math.sqrt(nearest)) / (RANGE - SURE_RANGE), 0F, 1F);
        return this.crocodile.getRandom().nextFloat() < closeness * closeness;
    }

    @Override
    public void start() {
        this.crocodile.setTarget(this.found);
        super.start();
    }
}
