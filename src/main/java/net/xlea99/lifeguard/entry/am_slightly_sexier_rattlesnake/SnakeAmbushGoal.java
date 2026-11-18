package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * A grown Rattlesnake's day when it has nothing better to do, in four phases by the time of day, each meant to read at a
 * glance (the user, 2026-11-15: the old one "reads too dormant"):
 *
 * <ul>
 * <li><b>Morning</b> (dawn to {@code MORNING_TO}): up from its den; cool, it goes out into the sun and basks stretched out
 * ({@link SnakeWarmth}), otherwise it lies coiled near home.</li>
 * <li><b>Nap</b> (to {@code NAP_TO}): it finds cover nearby (a bush, a nook; {@link SnakeCover}) and sleeps there.</li>
 * <li><b>Hunt</b> (to {@code HUNT_TO}, the long one): it prowls from cover to cover, {@code HOP_LEAST} to {@code HOP_MOST}
 * blocks a hop (times its roaming, {@link SnakeTemper}), waiting a short while in each (times its patience) for dinner to
 * come by before moving on. Prey it notices it stalks ({@link SnakeStrikeGoal}).</li>
 * <li><b>Night</b> (to dawn): home to its den, and asleep. Its den is a spot it remembers (saved with it, {@link SnakeMind}):
 * a bush, a nook, a sheltered corner. Gone, no longer good, too far or unreachable, it picks a new one nearby.</li>
 * </ul>
 * Cold, it sleeps wherever it is unless the sun is out to warm it; too hot in the sun, it goes into shade. In a world with no
 * day it hunts and never sleeps but for the cold.
 *
 * <p>Alex's own curl times out after 75 ticks or more, so while it waits this holds that timeout off. It watches whatever
 * it's worked up about ({@link SnakeMind}); it claims the look so that Alex's stare at players and his random glances don't
 * run under it. A warning rattle keeps it coiled; a target (prey, a threat it goes for, whoever hurt it) ends all of this,
 * and Alex uncoils it. Ticked every tick, so its times are real ticks.
 */
public final class SnakeAmbushGoal extends Goal {
    public static final int PRIORITY = 3;
    /** The day's phases, in day time ticks (0 is sunrise). */
    public static final long MORNING_TO = 3000, NAP_TO = 9000, HUNT_TO = 18000, NIGHT_TO = 23000;
    /** Hunting: how long it waits in a spot, and how far a hop is (blocks, before its roaming). */
    static final int HUNT_WAIT_LEAST = 300, HUNT_WAIT_MOST = 700;
    static final int HOP_LEAST = 8, HOP_MOST = 20;
    /** A nap's cover is looked for this far; a den this far; a den further than {@code DEN_FAR} is given up for a new one. */
    static final int COVER_RANGE = 12, DEN_RANGE = 16, DEN_FAR = 64;
    /** The most ticks a move takes before it settles wherever it got to (a trip home takes longer). */
    static final int MOVE_GIVE_UP = 400, DEN_GIVE_UP = 900;
    /** The most ticks it takes slipping the last little way onto a spot. */
    static final int SETTLE_TICKS = 30;
    /** With no path to a spot, how soon it tries again. */
    static final int RETRY = 40;
    /**
     * Hunting hops: how many spots it remembers and passes over within {@code RECENT_NEAR} blocks of, a one in this many
     * chance it doesn't, how far its heading wobbles a hop (radians), and how far behind spots behind it rank.
     */
    static final int RECENT = 4, REVISIT_CHANCE = 7;
    static final double RECENT_NEAR = 3.0, BEHIND = 10.0;
    static final float HEADING_WOBBLE = 0.6F;
    /** Home is within this many blocks of its den. */
    static final double AT_DEN = 1.5;
    static final double SLUGGISH_SPEED = 0.7;
    /** Looking for sun or shade: how far, how many columns, and after none was found, how long before it looks again. */
    static final int SEEK_RANGE = 10, SEEK_AGAIN = 600;
    /** How often it asks itself what it should be doing. */
    static final int CHECK = 20;
    /** How long it must have been calm before it falls asleep. */
    static final int SLEEP_SETTLE = 400;
    /** Alex's timeout, put back when this ends. */
    static final int ALEX_CURL_TIME = 75;

    public enum Mode { WAIT, BASK, MOVE }

    public enum Phase { MORNING, NAP, HUNT, NIGHT }

    private enum Purpose { HOP, SUN, SHADE, COVER, DEN }

    private final PathfinderMob snake;
    private final RattlesnakeAccess access;
    private final SnakeMind.Holder mind;
    private Mode mode = Mode.WAIT;
    private Purpose purpose = Purpose.HOP;
    private Phase phase;
    private int timer;
    private int seekAgainAt;
    private int checkIn;
    /** Where it's going, and how long it has been settling into it. */
    private BlockPos goal;
    private int settling;
    /** Hunting: its heading (radians, NaN until its first hop) and the spots it last hopped to. Not saved. */
    private float heading = Float.NaN;
    private final java.util.ArrayDeque<BlockPos> recent = new java.util.ArrayDeque<>();

    public SnakeAmbushGoal(PathfinderMob snake, RattlesnakeAccess access, SnakeMind.Holder mind) {
        this.snake = snake;
        this.access = access;
        this.mind = mind;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public Mode mode() {
        return this.mode;
    }

    public Phase phase() {
        return this.phase;
    }

    /** Where it's going or last went. */
    public BlockPos goal() {
        return this.goal;
    }

    private SnakeMind m() {
        return mind.lifeguard$mind();
    }

    private SnakeWarmth warmth() {
        return m().warmth();
    }

    /** The phase of the day for its world now. */
    Phase phaseNow() {
        return warmth().noDay() ? Phase.HUNT : SnakeSchedule.phase(snake);
    }

    @Override
    public boolean canUse() {
        return !snake.isBaby() && snake.getTarget() == null && !snake.isPassenger() && !(snake instanceof Animal animal && animal.isInLove());
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        phase = null;
        checkIn = 0;
        lieInWait(huntWait());
    }

    @Override
    public void stop() {
        m().wake();
        access.lifeguard$setMaxCurlTime(ALEX_CURL_TIME);
        snake.getNavigation().stop();
        mode = Mode.WAIT;
    }

    @Override
    public void tick() {
        switch (mode) {
            case WAIT -> waiting();
            case BASK -> basking();
            case MOVE -> moving();
        }
    }

    private void waiting() {
        if (!access.lifeguard$isCurled())
            access.lifeguard$setCurled(true);
        access.lifeguard$setMaxCurlTime(Integer.MAX_VALUE / 2);
        watch();
        SnakeMind m = m();
        if (checkNow()) {
            Phase now = phaseNow();
            if (now != phase) {
                phase = now;
                startPhase(now);
                if (mode != Mode.WAIT)
                    return;
            }
            if (m.asleep()) {
                if (!restTime())
                    m.wake();
                return;
            }
            boolean sunHere = SnakeWarmth.sunlit(snake.level(), snake.blockPosition());
            if (warmth().wantsSun() && phase != Phase.NIGHT) {
                if (sunHere) {
                    bask();
                    return;
                }
                if (seek(Purpose.SUN))
                    return;
            } else if (warmth().wantsShade() && sunHere && seek(Purpose.SHADE)) {
                return;
            }
            if (m.calmTicks() >= SLEEP_SETTLE && restTime() && settledForSleep()) {
                m.sleep();
                return;
            }
        }
        if (m.asleep())
            return;
        if (phase == Phase.HUNT && !warmth().sluggish() && --timer <= 0)
            hop();
    }

    /** What it does as a phase begins. */
    private void startPhase(Phase now) {
        switch (now) {
            case MORNING, HUNT -> {
                m().wake();
                if (now == Phase.HUNT)
                    timer = Math.min(timer, snake.getRandom().nextInt(40, 200));
            }
            case NAP -> {
                if (SnakeCover.score(snake.level(), snake.blockPosition()) < SnakeCover.DEN) {
                    var spots = SnakeCover.nearest(snake, 0, COVER_RANGE, SnakeCover.TRY_PATHS, p -> SnakeCover.score(snake.level(), p) >= SnakeCover.DEN);
                    if (!spots.isEmpty() && goAny(spots, Purpose.COVER) == null)
                        phase = null; // no path yet: begin the phase again at the next check
                }
            }
            case NIGHT -> goHome();
        }
    }

    /** Whether it's a time to sleep: its nap and its night, and whenever it's cold (but for a sunny day: then it basks). */
    private boolean restTime() {
        SnakeWarmth w = warmth();
        if (w.sluggish())
            return !w.wantsSun();
        if (w.noDay())
            return false;
        return phase == Phase.NAP || phase == Phase.NIGHT;
    }

    /** At night it sleeps at home (or wherever it gave up getting home: that's home now). */
    private boolean settledForSleep() {
        if (phase != Phase.NIGHT)
            return true;
        BlockPos den = m().den();
        if (den == null || !den.closerToCenterThan(snake.position(), AT_DEN + 1)) {
            m().setDen(snake.blockPosition());
        }
        return true;
    }

    private void goHome() {
        m().wake();
        Level level = snake.level();
        BlockPos den = m().den();
        boolean good = den != null && den.distSqr(snake.blockPosition()) <= (double) DEN_FAR * DEN_FAR
            && SnakeCover.standable(level, den) && SnakeCover.score(level, den) >= SnakeCover.DEN;
        if (!good) {
            den = SnakeCover.score(level, snake.blockPosition()) >= SnakeCover.DEN ? snake.blockPosition()
                : SnakeCover.find(snake, 0, DEN_RANGE, true, p -> true);
            if (den == null)
                den = snake.blockPosition();
            m().setDen(den);
        }
        if (!den.closerToCenterThan(snake.position(), AT_DEN) && !go(den, Purpose.DEN))
            phase = null; // no path yet: set off home at the next check
    }

    private void basking() {
        snake.getNavigation().stop();
        access.lifeguard$setMaxCurlTime(Integer.MAX_VALUE / 2);
        // On guard while anything has it more than half watching; stretched out again once it calms.
        boolean guard = m().headLevel() > SnakeMind.HEAD_STEPS / 2;
        if (guard && !access.lifeguard$isCurled())
            access.lifeguard$setCurled(true);
        else if (!guard && access.lifeguard$isCurled() && !access.lifeguard$isRattling())
            access.lifeguard$setCurled(false);
        watch();
        if (checkNow()) {
            Phase now = phaseNow();
            if (!warmth().wantsSun() || now == Phase.NIGHT) {
                lieInWait(huntWait());
                phase = null; // pick the phase up afresh, so the nap or the trip home starts
            } else if (!SnakeWarmth.sunlit(snake.level(), snake.blockPosition()) && !seek(Purpose.SUN)) {
                lieInWait(huntWait());
            }
        }
    }

    private void moving() {
        // Alex curls a snake at random now and then, which would pin it mid-move.
        if (access.lifeguard$isCurled() && !access.lifeguard$isRattling())
            access.lifeguard$setCurled(false);
        boolean timedOut = ++timer > (purpose == Purpose.DEN ? DEN_GIVE_UP : MOVE_GIVE_UP);
        if (!snake.getNavigation().isDone() && !timedOut)
            return;
        // Its path done, the last little way onto the spot itself: slipping into the bush.
        if (!timedOut && goal != null && ++settling <= SETTLE_TICKS && SnakeCover.settleInto(snake, goal, speed()))
            return;
        if (purpose == Purpose.SUN && warmth().wantsSun() && SnakeWarmth.sunlit(snake.level(), snake.blockPosition())) {
            bask();
            return;
        }
        if (purpose == Purpose.DEN) {
            BlockPos den = m().den();
            if (den == null || !den.closerToCenterThan(snake.position(), AT_DEN + 1))
                m().setDen(snake.blockPosition()); // couldn't get home: this is home now
        }
        lieInWait(huntWait());
    }

    private int huntWait() {
        float ticks = snake.getRandom().nextInt(HUNT_WAIT_LEAST, HUNT_WAIT_MOST + 1) * m().temper(snake).patience();
        return Math.round(ticks);
    }

    /** True about every {@code CHECK} ticks (counted here: the goal's own tick, not the game's). */
    private boolean checkNow() {
        if (--checkIn > 0)
            return false;
        checkIn = CHECK;
        return true;
    }

    private void watch() {
        // Worked up about something: watch it. (The client lifts its head out of its rest as far as it's told.)
        LivingEntity watching = m().watching();
        if (watching != null)
            snake.getLookControl().setLookAt(watching, 20F, 20F);
    }

    private void lieInWait(int ticks) {
        mode = Mode.WAIT;
        timer = ticks;
        access.lifeguard$setCurled(true);
        access.lifeguard$setMaxCurlTime(Integer.MAX_VALUE / 2);
    }

    private void bask() {
        mode = Mode.BASK;
        snake.getNavigation().stop();
        if (!access.lifeguard$isRattling())
            access.lifeguard$setCurled(false);
    }

    /**
     * A hunting hop: on to the best spot nearby (cover first), a good stretch away, that it can get to. It works its way across
     * the land rather than back and forth (the user, 2026-11-16: "they have a tendency to ping pong"): it passes over spots
     * near its last {@code RECENT} (but for one hop in {@code REVISIT_CHANCE}), and among equally good spots it takes one ahead
     * of its heading over one behind; its heading wobbles up to {@code HEADING_WOBBLE} a hop and follows where it goes.
     */
    private void hop() {
        float roam = m().temper(snake).roam();
        if (Float.isNaN(heading))
            heading = snake.getRandom().nextFloat() * Mth.TWO_PI;
        heading += (snake.getRandom().nextFloat() * 2F - 1F) * HEADING_WOBBLE;
        boolean revisit = snake.getRandom().nextInt(REVISIT_CHANCE) == 0;
        double hx = Mth.cos(heading), hz = Mth.sin(heading);
        BlockPos from = snake.blockPosition();
        var spots = SnakeCover.candidates(snake, HOP_LEAST * roam, HOP_MOST * roam, SnakeCover.TRY_PATHS,
            p -> revisit || recent.stream().noneMatch(r -> r.closerThan(p, RECENT_NEAR)),
            p -> {
                double dx = p.getX() - from.getX(), dz = p.getZ() - from.getZ();
                boolean ahead = dx * hx + dz * hz >= 0;
                return (ahead ? 0 : BEHIND) + snake.getRandom().nextDouble();
            });
        if (spots.isEmpty()) {
            heading += Mth.PI; // boxed in: turn round
            lieInWait(huntWait() / 2);
            return;
        }
        BlockPos spot = goAny(spots, Purpose.HOP);
        if (spot == null) {
            lieInWait(RETRY);
            return;
        }
        heading = (float) Mth.atan2(spot.getZ() - from.getZ(), spot.getX() - from.getX());
        recent.addLast(spot);
        while (recent.size() > RECENT)
            recent.removeFirst();
    }

    /** Sets off for the first of {@code spots} it has a whole path to ({@link SnakeCover#goToFirstReachable}); null when none. */
    private BlockPos goAny(java.util.List<BlockPos> spots, Purpose why) {
        BlockPos spot = SnakeCover.goToFirstReachable(snake, spots, speed());
        if (spot != null)
            started(spot, why);
        return spot;
    }

    /** Goes to a spot nearby in the sun or out of it; false (and no looking again for a while) when there's none. */
    private boolean seek(Purpose kind) {
        if (snake.tickCount < seekAgainAt)
            return false;
        boolean wantSun = kind == Purpose.SUN;
        Level level = snake.level();
        BlockPos spot = SnakeCover.find(snake, 2, SEEK_RANGE, true, p -> SnakeWarmth.sunlit(level, p) == wantSun);
        if (spot == null) {
            seekAgainAt = snake.tickCount + SEEK_AGAIN;
            return false;
        }
        if (!go(spot, kind)) {
            seekAgainAt = snake.tickCount + RETRY;
            return false;
        }
        return true;
    }

    /** Sets off for {@code spot}; false, changing nothing, when there's no path (off the ground: try again later). */
    private boolean go(BlockPos spot, Purpose why) {
        if (!SnakeCover.goTo(snake, spot, speed()))
            return false;
        started(spot, why);
        return true;
    }

    private void started(BlockPos spot, Purpose why) {
        mode = Mode.MOVE;
        purpose = why;
        goal = spot;
        timer = 0;
        settling = 0;
        access.lifeguard$setMaxCurlTime(ALEX_CURL_TIME);
        access.lifeguard$setCurled(false);
    }

    private double speed() {
        return warmth().sluggish() ? SLUGGISH_SPEED : 1.0;
    }
}
