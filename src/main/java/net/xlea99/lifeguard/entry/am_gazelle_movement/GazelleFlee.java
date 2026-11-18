package net.xlea99.lifeguard.entry.am_gazelle_movement;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

/** Where a fleeing Gazelle runs next ({@link AmGazelleMovement}): on ahead, roughly the way it's going, as long as that isn't back toward what it's fleeing. */
public final class GazelleFlee {
    /** How far either side of its heading it'll choose. */
    private static final float MOST_OFF_HEADING = 45F * Mth.DEG_TO_RAD;
    /** How far on, in blocks. */
    private static final int DISTANCE = 20;
    /** How far up or down the spot may be. */
    private static final int VERTICAL = 4;
    private static final int TRIES = 8;
    /** How fast it must already be going, squared, to have a heading at all. */
    private static final double MOVING = 0.04D;

    private GazelleFlee() {
    }

    /** @return null to leave it to Alex's Mobs' own choice: standing still, or nothing good ahead */
    @Nullable
    public static Vec3 ahead(PathfinderMob gazelle, LivingEntity threat) {
        if (gazelle.getDeltaMovement().horizontalDistanceSqr() < MOVING)
            return null;
        double awayX = gazelle.getX() - threat.getX();
        double awayZ = gazelle.getZ() - threat.getZ();
        float heading = gazelle.getYRot() * Mth.DEG_TO_RAD;
        for (int i = 0; i < TRIES; i++) {
            float yaw = heading + (gazelle.getRandom().nextFloat() * 2F - 1F) * MOST_OFF_HEADING;
            int x = Mth.floor(-Mth.sin(yaw) * DISTANCE);
            int z = Mth.floor(Mth.cos(yaw) * DISTANCE);
            if (x * awayX + z * awayZ < 0D)
                continue;
            int y = gazelle.getRandom().nextInt(VERTICAL * 2 + 1) - VERTICAL;
            BlockPos pos = LandRandomPos.generateRandomPosTowardDirection(gazelle, DISTANCE, false, new BlockPos(x, y, z));
            if (pos == null)
                continue;
            pos = LandRandomPos.movePosUpOutOfSolid(gazelle, pos);
            if (pos != null && !GoalUtils.isWater(gazelle, pos) && !GoalUtils.hasMalus(gazelle, pos))
                return Vec3.atBottomCenterOf(pos);
        }
        return null;
    }

    /**
     * Anywhere to run at all, for a Gazelle with nowhere to run directly away: Alex's Mobs' own choice gives up then, and it'd stand frightened
     * until something opened up. Cornered at the end of a spit of land, it bolts past what frightened it.
     */
    @Nullable
    public static Vec3 anywhere(PathfinderMob gazelle) {
        return LandRandomPos.getPos(gazelle, DISTANCE, VERTICAL);
    }

    /** What every Gazelle is made to have. */
    public interface Holder {
        boolean lifeguard$fleeing();
    }
}
