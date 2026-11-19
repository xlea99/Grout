package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Rabbit;

/**
 * Prey has to come to it: a rabbit or a Jerboa within striking reach, in place of Alex's two goals that took them as targets
 * from 15 blocks off and chased them.
 */
public final class SnakePreyGoal extends NearestAttackableTargetGoal<LivingEntity> {
    public static final int PRIORITY = 1;
    static final ResourceLocation JERBOA = ResourceLocation.fromNamespaceAndPath("alexsmobs", "jerboa");

    public SnakePreyGoal(Mob snake) {
        super(snake, LivingEntity.class, 5, true, false, SnakePreyGoal::isPrey);
    }

    /**
     * Asleep, it doesn't notice prey until something has woken it. The search range is set afresh each time: vanilla fixes
     * it when the goal is made, before this snake could know it was hunting, so it stayed at striking reach (2026-11-15).
     */
    @Override
    public boolean canUse() {
        if (this.mob instanceof SnakeMind.Holder holder && (holder.grout$mind().asleep() || holder.grout$mind().groggy()))
            return false;
        this.targetConditions.range(getFollowDistance());
        return super.canUse();
    }

    static boolean isPrey(LivingEntity entity) {
        return entity instanceof Rabbit || JERBOA.equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));
    }

    /**
     * Searching, prey must be within striking reach; once it's the target, it's kept as far as the strike goal chases it (a
     * target goal drops its target beyond this distance).
     */
    @Override
    protected double getFollowDistance() {
        if (this.mob.getTarget() != null)
            return SnakeStrikeGoal.CHASE_RANGE;
        return this.mob instanceof SnakeMind.Holder holder && holder.grout$mind().warmth().hunting() ? STALK_RANGE : SnakeStrikeGoal.REACH;
    }

    /** Hunting, it notices prey this far off and stalks it ({@link SnakeStrikeGoal}); otherwise only prey that comes into reach. */
    static final double STALK_RANGE = 10.0;
}
