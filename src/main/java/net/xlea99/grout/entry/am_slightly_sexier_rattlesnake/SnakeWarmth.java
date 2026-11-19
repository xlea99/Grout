package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

/**
 * A Rattlesnake's body warmth, and the day it makes for it ({@link SnakeMind} looks after it). A snake has no warmth of its
 * own: it takes the warmth of where it lies, slowly. In the sun by day it warms toward a level set by how hot the biome is (a
 * desert's sun is too much for it); in shade by day, less; by night it cools, more in a cold biome; rain cools it a little.
 * A snake first seen takes the warmth of where it is.
 *
 * <ul>
 * <li>Cold (below {@code COLD}), it's sluggish: slower to work up, hearing less, hardly moving.</li>
 * <li>Cool by day, it wants the sun: below {@code BASK_START} it goes and basks until it's warmed to {@code BASK_DONE} (or
 * as warm as the sun here makes it).</li>
 * <li>Too hot (above {@code HOT}), it wants shade, until it has cooled to {@code COOLED}.</li>
 * <li>In its hunting phase (by its own clock, {@link SnakeSchedule}), unless cold, it's hunting: sharper.</li>
 * </ul>
 * In a world with no day (the Nether, the End) it keeps one even warmth and none of this happens.
 */
public final class SnakeWarmth {
    /** The warmth it heads for: in the sun, in shade by day, at night; each a base plus how hot the biome is (0 to 1). */
    static final float SUN_BASE = 0.6F, SUN_HEAT = 0.6F;
    static final float SHADE_BASE = 0.35F, SHADE_HEAT = 0.4F;
    static final float NIGHT_BASE = 0.15F, NIGHT_HEAT = 0.3F;
    static final float NO_DAY_BASE = 0.5F, NO_DAY_HEAT = 0.3F;
    static final float RAIN = 0.15F;
    /** How much of the way to it each look (every {@code SnakeMind.EVERY} ticks): warming in a minute or two, cooling slower. */
    static final float WARMING = 0.005F, COOLING = 0.003F;
    static final float COLD = 0.35F, BASK_START = 0.55F, BASK_DONE = 0.8F, HOT = 1.0F, COOLED = 0.85F;
    /** Cold, how much of its nerve it has at the least, and hunting, how much more. */
    static final float SLUGGISH_LEAST = 0.4F, HUNTING_NERVE = 1.2F;

    float warmth = Float.NaN;
    private boolean wantsSun, wantsShade, hunting, noDay;
    private float sunWarmth, heat;

    void update(PathfinderMob snake) {
        Level level = snake.level();
        BlockPos at = snake.blockPosition();
        float heat = Mth.clamp(level.getBiome(at).value().getBaseTemperature() / 2F, 0F, 1F);
        this.heat = heat;
        boolean noDay = level.dimensionType().hasFixedTime();
        this.noDay = noDay;
        this.sunWarmth = SUN_BASE + SUN_HEAT * heat;
        float target;
        if (noDay)
            target = NO_DAY_BASE + NO_DAY_HEAT * heat;
        else if (sunlit(level, at))
            target = this.sunWarmth;
        else if (level.isDay())
            target = SHADE_BASE + SHADE_HEAT * heat;
        else
            target = NIGHT_BASE + NIGHT_HEAT * heat;
        if (level.isRainingAt(at))
            target -= RAIN;
        if (Float.isNaN(this.warmth))
            this.warmth = target;
        this.warmth += (target - this.warmth) * (target > this.warmth ? WARMING : COOLING);
        float baskDone = Math.min(BASK_DONE, this.sunWarmth - 0.05F);
        boolean sunOut = !noDay && level.isDay() && !level.isRaining();
        this.wantsSun = sunOut && this.warmth < (this.wantsSun ? baskDone : BASK_START);
        this.wantsShade = !noDay && this.warmth > (this.wantsShade ? COOLED : HOT);
        this.hunting = !noDay && SnakeSchedule.phase(snake) == SnakeAmbushGoal.Phase.HUNT && !sluggish();
    }

    /** Whether this spot is in the sun: day, nothing above it at all (leaves above take some light: shade), no rain. */
    public static boolean sunlit(Level level, BlockPos at) {
        return level.isDay() && level.getBrightness(LightLayer.SKY, at) >= 15 && !level.isRainingAt(at);
    }

    public boolean sluggish() {
        return this.warmth < COLD;
    }

    public boolean wantsSun() {
        return this.wantsSun;
    }

    public boolean wantsShade() {
        return this.wantsShade;
    }

    public boolean hunting() {
        return this.hunting;
    }

    /** How much of its nerve it has: less when cold (down to {@code SLUGGISH_LEAST}), more hunting. */
    public float nerve() {
        if (Float.isNaN(this.warmth))
            return 1F;
        float share = Math.min(1F, this.warmth / COLD);
        float cold = Math.max(SLUGGISH_LEAST, share * share);
        return this.hunting ? cold * HUNTING_NERVE : cold;
    }

    public float warmth() {
        return this.warmth;
    }

    /** How hot its biome is, 0 to 1 (a desert's 2.0 is 1). */
    public float heat() {
        return this.heat;
    }

    /** Whether its world has no day (the Nether, the End). */
    public boolean noDay() {
        return this.noDay;
    }
}
