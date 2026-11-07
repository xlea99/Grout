package net.xlea99.lifeguard.entry.ac_iris_biome_light;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.xlea99.lifeguard.boot.BrokenEntries;
import net.xlea99.lifeguard.entry.sexy_runic_glints.Shaderpacks;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2i;
import org.joml.Vector3d;

/**
 * What a shaderpack sees of sky light in Alex's Caves' bright biomes ({@link AcIrisBiomeLight}), scaled by the ambient light
 * Alex's Caves works out for the camera, which already blends across biome borders. Read by name: Lifeguard isn't built
 * against Alex's Caves.
 */
public final class BiomeLight {
    /** Alex's Caves' ambient light in its brightest biomes: this much reads as full sky light. */
    private static final float FULL_AMBIENT = 0.125F;
    /** Its faintest, Toxic Caves': any biome with ambient light this strong has no clock. */
    private static final float FAINTEST_AMBIENT = 0.01F;
    /** Iris's lightmap matrix ({@code BuiltinReplacementUniforms}): light levels in 0-240 to texture coordinates. */
    private static final float SCALE = 1F / 256F;
    private static final float CENTER = 0.03125F;
    /** Added to the sky coordinate, it takes sky light 0 to where sky light 15 is. */
    private static final float FULL_SKY = 240F / 256F;
    private static final int FULL_EYE_BRIGHTNESS = 240;
    /**
     * Sets how much of a pack's own haze wakes up, in biomes with a faint ambient. {@link BiomeFog} is the real fog, so a touch at
     * most: at full it's heavy and the wrong color.
     */
    private static final float MOST_EYE_BRIGHTNESS = 0.2828F;
    /** How much brighter a pack sees the sky color in the brightest biomes. Packs build ambient light from it, so this is their base brightness. */
    private static final double SKY_BOOST = 1.5;
    /**
     * Eye brightness in the brightest biomes. With none, a pack's cave fog fades the distance toward near black; with much, its
     * haze, built from {@link #SKY_BOOST}'s sky color, comes back heavy and the wrong color.
     */
    private static final float BRIGHT_EYE_BRIGHTNESS = 0.3F;
    /** Candy Cavity's own {@link #SKY_BOOST}. */
    private static final double CANDY_SKY_BOOST = 1.8;
    /**
     * How far Candy Cavity's sky color, as a pack sees it, goes from its strong pink toward Alex's Caves' gentle light tint for
     * the biome. A pack lights everything with the sky color, which dyes it all magenta and takes away the hot pink's contrast.
     */
    private static final double CANDY_TINT = 0.6;
    /** Takes some blue out of that tint: the frosting is hot pink, and with Alex's Caves' tint alone it lands on loud true pink. */
    private static final double CANDY_BLUE = 0.85;
    /** Candy Cavity's own {@link #BRIGHT_EYE_BRIGHTNESS}: it can take more, since its haze comes in the tinted sky color. */
    private static final float CANDY_EYE_BRIGHTNESS = 0.7F;
    /** Alex's Caves' light color's green in Candy Cavity, the only bright biome that changes it: how to tell how far in the camera is. */
    private static final double CANDY_GREEN = 0.9;
    /** The time of day a pack sees in these biomes, in ticks like {@code /time set}: they're always lit, so no clock. */
    private static final long PINNED_TIME = 6000;

    private static final Matrix4f MATRIX = new Matrix4f(
        SCALE, 0, 0, 0,
        0, SCALE, 0, 0,
        0, 0, SCALE, 0,
        CENTER, CENTER, CENTER, 1);

    @Nullable
    private static final VarHandle AMBIENT = proxyField("lastBiomeAmbientLightAmount", float.class);
    @Nullable
    private static final VarHandle AMBIENT_PREV = proxyField("lastBiomeAmbientLightAmountPrev", float.class);
    @Nullable
    private static final VarHandle LIGHT_COLOR = proxyField("lastBiomeLightColor", Vec3.class);
    @Nullable
    private static final VarHandle SKY_OVERRIDE = proxyField("acSkyOverrideAmount", float.class);

    private BiomeLight() {
    }

    /** Alex's Caves' ambient light for the camera. Render thread only. */
    private static float ambient() {
        if (AMBIENT == null || AMBIENT_PREV == null)
            return 0;
        float prev = (float) AMBIENT_PREV.get();
        float now = (float) AMBIENT.get();
        return prev + (now - prev) * Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
    }

    /**
     * How much of full sky light to add here: 0 outside these biomes. The square root, since vanilla's lightmap makes even a
     * tiny ambient count, and Toxic Caves' 0.01 taken straight is next to nothing in a pack.
     */
    private static float amount() {
        return (float) Math.sqrt(Math.clamp(ambient() / FULL_AMBIENT, 0F, 1F));
    }

    /** Iris's matrix with the sky coordinate raised. Packs clamp what they read, so where there's real sky light it just stays full. */
    public static Matrix4fc lightmapMatrix() {
        return MATRIX.m31(CENTER + amount() * FULL_SKY);
    }

    /** The light at the eye, which packs use to tell a cave from outdoors, for their fog and more. */
    public static Vector2i eyeBrightness(Vector2i real) {
        float full = Math.clamp(ambient() / FULL_AMBIENT, 0F, 1F);
        float amount = Math.max(Math.min(amount(), MOST_EYE_BRIGHTNESS) * (1F - full), Mth.lerp((float) candy(), BRIGHT_EYE_BRIGHTNESS, CANDY_EYE_BRIGHTNESS) * full);
        return amount > 0 ? new Vector2i(real.x, Math.max(real.y, Math.round(amount * FULL_EYE_BRIGHTNESS))) : real;
    }

    /** The sky color a pack sees, brighter by {@link #SKY_BOOST} toward the brightest biomes; cubed, so Toxic Caves' faint ambient adds next to nothing. */
    public static Vector3d skyColor(Vector3d real) {
        float full = Math.clamp(ambient() / FULL_AMBIENT, 0F, 1F);
        if (full <= 0 || LIGHT_COLOR == null)
            return real;
        Vec3 light = (Vec3) LIGHT_COLOR.get();
        double candy = candy();
        double luma = real.x * 0.2126 + real.y * 0.7152 + real.z * 0.0722;
        double tint = CANDY_TINT * candy;
        double boost = 1 + (Mth.lerp(candy, SKY_BOOST, CANDY_SKY_BOOST) - 1) * full * full * full;
        return new Vector3d(Mth.lerp(tint, real.x, luma * light.x), Mth.lerp(tint, real.y, luma * light.y), Mth.lerp(tint, real.z, luma * light.z * CANDY_BLUE)).mul(boost);
    }

    /**
     * Whether a pack's sun needs the ceiling in its shadow map here: any of Alex's Caves' cave biomes, which it marks with its sky
     * override. They're big enough to leak even with no sky light for a pack to see: the sun shows through wherever a ceiling isn't built.
     */
    public static boolean needsCeilingBuilt() {
        return SKY_OVERRIDE != null && (float) SKY_OVERRIDE.get() > 0 && Shaderpacks.active();
    }

    /** How far into Candy Cavity the camera is, 0 to 1. Only means that where there's ambient light: other biomes tint the light too. */
    private static double candy() {
        return LIGHT_COLOR == null ? 0 : Mth.clamp((1 - ((Vec3) LIGHT_COLOR.get()).y) / (1 - CANDY_GREEN), 0, 1);
    }

    /**
     * The rain a pack sees: none here. These caves are sealed, but with rain above, packs swap their ambient light for a fixed
     * rain color, so the biome's sky color stops reaching anything and the cave goes dark.
     */
    public static float rainStrength(float real) {
        return real * (1F - Math.clamp(ambient() / FAINTEST_AMBIENT, 0F, 1F));
    }

    /**
     * Where the sun is for a pack, moved the short way round to {@link #PINNED_TIME}. Packs tell day from night by the sun,
     * and with sky light they'd take the whole cave through a day and night. {@code worldTime} is left alone: it also runs
     * the animations packs time by it. Iris works this angle out twice, for its uniforms and its shadow camera; both come here.
     */
    public static float skyAngle(float real) {
        float amount = Math.clamp(ambient() / FAINTEST_AMBIENT, 0F, 1F);
        if (amount <= 0)
            return real;
        float pinned = Minecraft.getInstance().level.dimensionType().timeOfDay(PINNED_TIME);
        return Mth.positiveModulo(real + (Mth.positiveModulo(pinned - real + 0.5F, 1F) - 0.5F) * amount, 1F);
    }

    @Nullable
    private static VarHandle proxyField(String field, Class<?> type) {
        try {
            Class<?> proxy = Class.forName("com.github.alexmodguy.alexscaves.client.ClientProxy");
            return MethodHandles.publicLookup().findStaticVarHandle(proxy, field, type);
        } catch (ReflectiveOperationException e) {
            BrokenEntries.mark(AcIrisBiomeLight.ENTRY, "Alex's Caves' ClientProxy." + field + " can't be read (" + e + ")");
            return null;
        }
    }
}
