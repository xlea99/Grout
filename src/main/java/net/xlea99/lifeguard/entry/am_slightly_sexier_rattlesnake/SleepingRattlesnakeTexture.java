package net.xlea99.lifeguard.entry.am_slightly_sexier_rattlesnake;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.xlea99.lifeguard.Lifeguard;

/**
 * A sleeping Rattlesnake's texture, eyes closed: the way Alex's Mobs draws its other sleepers (a {@code *_sleeping.png}, the
 * Tiger's, the Jerboa's), but the Rattlesnake has none, so it's made here from whatever Rattlesnake texture is loaded. Its eyes
 * are one dark pixel on each side of the head (2,26 and 9,26); each is painted the scale beside it, a shade darker, so the
 * eye reads closed and flush with the head (the user, 2026-11-16: little lid boxes stood out of the head "like an extra part
 * of the body"). A resource pack whose texture has no dark pixel there has its eyes elsewhere: then there's no sleeping
 * texture, and the eyes stay open rather than a wrong pixel painted. Made again at every resource reload. Client only.
 */
public final class SleepingRattlesnakeTexture {
    public static final ResourceLocation SLEEPING = ResourceLocation.fromNamespaceAndPath("lifeguard", "dynamic/rattlesnake_sleeping");
    static final ResourceLocation AWAKE = ResourceLocation.fromNamespaceAndPath("alexsmobs", "textures/entity/rattlesnake.png");
    /** Each eye pixel, and the scale pixels either side of it. */
    static final int[][] EYES = {{2, 26, 1, 3}, {9, 26, 8, 10}};
    /** An eye is darker than this (0-255, the brightest of red, green, blue); a closed one is its neighbors' average this much as bright. */
    static final int EYE_DARKEST = 80;
    static final float LID = 0.85F;

    private static volatile boolean ready;

    private SleepingRattlesnakeTexture() {
    }

    /** Whether there is a sleeping texture to draw (the loaded texture has its eyes where Alex's are). */
    public static boolean ready() {
        return ready;
    }

    public static void rebuild(ResourceManager manager) {
        ready = false;
        var resource = manager.getResource(AWAKE);
        if (resource.isEmpty())
            return;
        NativeImage image;
        try (InputStream in = resource.get().open()) {
            image = NativeImage.read(in);
        } catch (IOException e) {
            Lifeguard.LOGGER.warn("Lifeguard: couldn't read the Rattlesnake's texture for its sleeping eyes ({})", e.toString());
            return;
        }
        int scale = image.getWidth() / 64;
        for (int[] eye : EYES) {
            if (scale < 1 || brightest(image.getPixelRGBA(eye[0] * scale, eye[1] * scale)) >= EYE_DARKEST) {
                image.close();
                return; // eyes aren't where Alex's are: leave them open
            }
        }
        for (int[] eye : EYES) {
            for (int dx = 0; dx < scale; dx++) {
                for (int dy = 0; dy < scale; dy++) {
                    int a = image.getPixelRGBA(eye[2] * scale + dx, eye[1] * scale + dy), b = image.getPixelRGBA(eye[3] * scale + dx, eye[1] * scale + dy);
                    image.setPixelRGBA(eye[0] * scale + dx, eye[1] * scale + dy, lid(a, b));
                }
            }
        }
        Minecraft.getInstance().getTextureManager().register(SLEEPING, new DynamicTexture(image));
        ready = true;
    }

    /** NativeImage pixels are ABGR. */
    private static int brightest(int abgr) {
        return Math.max(FastColor.ABGR32.red(abgr), Math.max(FastColor.ABGR32.green(abgr), FastColor.ABGR32.blue(abgr)));
    }

    private static int lid(int a, int b) {
        int r = Math.round((FastColor.ABGR32.red(a) + FastColor.ABGR32.red(b)) / 2F * LID);
        int g = Math.round((FastColor.ABGR32.green(a) + FastColor.ABGR32.green(b)) / 2F * LID);
        int bl = Math.round((FastColor.ABGR32.blue(a) + FastColor.ABGR32.blue(b)) / 2F * LID);
        return FastColor.ABGR32.color(255, bl, g, r);
    }
}
