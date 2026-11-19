package net.xlea99.grout.entry.am_slightly_sexier_bone_serpent;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;

/** One Bone Serpent's jaws, over time ({@link AmSlightlySexierBoneSerpent#JAW}). Every number is one dial, in degrees or model pixels (16 to a block). */
public final class SerpentMaw {
    /** How far open it goes: all the way out of the lava, and only a threat swimming after something below it. */
    private static final float OPEN_LEAPING = 1F;
    private static final float OPEN_SWIMMING = 0.2F;
    /** How hard it's pulled open, and how fast that dies out: low enough that it overshoots a little. */
    private static final float PULL = 0.3F;
    private static final float DAMPING = 0.45F;
    /** The most time one update counts, so that a serpent coming back into view doesn't jump. */
    private static final float MOST_TICKS = 2F;

    private static final float JAW_DROP = 75F * Mth.DEG_TO_RAD;
    /** The jaw comes out of its socket as it drops, down and back, in pixels: this is the unhinging. */
    private static final float JAW_OUT_DOWN = 3F;
    private static final float JAW_OUT_BACK = 2F;
    private static final float TOP_LIFT = 30F * Mth.DEG_TO_RAD;
    /** The whole head rears back, showing its prey the inside of its mouth. */
    private static final float HEAD_REAR = 14F * Mth.DEG_TO_RAD;
    /** How its jaw shakes when it's wide open. */
    private static final float SHAKE = 2F * Mth.DEG_TO_RAD;
    private static final float SHAKE_SPEED = 2.3F;

    private float open;
    private float openSpeed;
    private float lastAge;

    /** @param bite how far through a bite, 0 to 1 and back to 0: vanilla's arm swing, which it has no arm to use for */
    public void apply(AdvancedModelBox head, AdvancedModelBox top, AdvancedModelBox jaw, boolean hunting, boolean inFluid, float bite, float age) {
        float target = hunting ? (inFluid ? OPEN_SWIMMING : OPEN_LEAPING) : 0F;
        float ticks = Math.clamp(age - this.lastAge, 0F, MOST_TICKS);
        this.lastAge = age;
        for (float left = ticks; left > 0F; left -= 0.25F) {
            float step = Math.min(left, 0.25F);
            this.openSpeed += (PULL * (target - this.open) - DAMPING * this.openSpeed) * step;
            this.open += this.openSpeed * step;
        }

        float shown = Math.max(0F, this.open) * (1F - Mth.sin(Mth.PI * Math.clamp(bite, 0F, 1F)));
        jaw.rotateAngleX += JAW_DROP * shown + SHAKE * shown * Mth.sin(age * SHAKE_SPEED);
        jaw.rotationPointY += JAW_OUT_DOWN * shown;
        jaw.rotationPointZ += JAW_OUT_BACK * shown;
        top.rotateAngleX -= TOP_LIFT * shown;
        head.rotateAngleX -= HEAD_REAR * shown;
    }

    /** What every Bone Serpent is made to have. */
    public interface Holder {
        SerpentMaw grout$maw();
    }
}
