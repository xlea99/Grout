package net.xlea99.lifeguard.entry.ac_iris_biome_light;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.xlea99.lifeguard.Lifeguard;
import net.xlea99.lifeguard.boot.BrokenEntries;
import net.xlea99.lifeguard.entry.sexy_runic_glints.Shaderpacks;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Alex's Caves' thick biome fog under a shaderpack ({@link AcIrisBiomeLight}). For Toxic Caves the fog is the biome, but it's
 * vanilla fog, and packs have their own and don't read vanilla's. So it's drawn over the finished picture, from the main
 * depth texture, which packs draw into: the same distances, color and curve vanilla would have used, as Alex's Caves set
 * them, so it doesn't depend on the pack. Only biomes that pull the fog in, which Alex's Caves marks with a nearness below
 * 1, and only in open air. Its nearness is read by name: Lifeguard isn't built against Alex's Caves.
 */
public final class BiomeFog {
    /** Below vanilla's nearness of 1 by this much, Candy Cavity's, the mildest that pulls the fog in, is the full fog. */
    private static final float FULL_PULL = 0.25F;

    @Nullable
    private static ShaderInstance shader;
    @Nullable
    private static final Field NEARNESS = nearness();
    /** From clip space to camera space, this frame's. */
    private static final Matrix4f CLIP_TO_CAMERA = new Matrix4f();
    private static float start;
    private static float end;
    private static boolean cylinder;
    private static boolean inAir;
    private static float red;
    private static float green;
    private static float blue;
    private static int colorOnly;

    private BiomeFog() {
    }

    static void register(IEventBus modBus) {
        modBus.addListener(RegisterShadersEvent.class, BiomeFog::registerShader);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, ViewportEvent.ComputeFogColor.class, BiomeFog::onFogColor);
        NeoForge.EVENT_BUS.addListener(RenderLevelStageEvent.class, BiomeFog::onStage);
    }

    private static void registerShader(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(Lifeguard.MODID, "ac_biome_fog"), DefaultVertexFormat.POSITION),
                loaded -> shader = loaded);
        } catch (IOException e) {
            BrokenEntries.mark(AcIrisBiomeLight.ENTRY, "its fog shader wouldn't load (" + e + ")");
        }
    }

    private static void onFogColor(ViewportEvent.ComputeFogColor event) {
        red = event.getRed();
        green = event.getGreen();
        blue = event.getBlue();
    }

    private static void onStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            CLIP_TO_CAMERA.set(event.getProjectionMatrix()).mul(event.getModelViewMatrix()).invert();
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            // The terrain fog vanilla is really using, after every mod has had its say. Alex's Caves' fog listener is at
            // the lowest priority, so a listener can't reliably go after it.
            start = RenderSystem.getShaderFogStart();
            end = RenderSystem.getShaderFogEnd();
            cylinder = RenderSystem.getShaderFogShape() == FogShape.CYLINDER;
            inAir = event.getCamera().getFluidInCamera() == FogType.NONE;
        }
    }

    /** Over the finished picture, once the pack's last pass has drawn it to the main target. */
    public static void draw() {
        float strength = strength();
        if (strength <= 0 || shader == null || !inAir || !Shaderpacks.active())
            return;
        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        // The main target's color alone: reading a depth texture still attached to the target being drawn to is undefined,
        // and it shows, in whole triangles of solid fog after a window resize.
        if (colorOnly == 0)
            colorOnly = GlStateManager.glGenFramebuffers();
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, colorOnly);
        GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, main.getColorTextureId(), 0);
        RenderSystem.viewport(0, 0, main.viewWidth, main.viewHeight);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderTexture(0, main.getDepthTextureId());
        shader.safeGetUniform("ClipToCamera").set(CLIP_TO_CAMERA);
        shader.safeGetUniform("BiomeFogStart").set(start);
        shader.safeGetUniform("BiomeFogEnd").set(end);
        shader.safeGetUniform("Cylinder").set(cylinder ? 1.0F : 0.0F);
        shader.safeGetUniform("BiomeFogColor").set(red, green, blue);
        shader.safeGetUniform("Strength").set(strength);
        // The shader asks for this blend too, but vanilla skips setting it if it thinks it already has, and packs turn
        // blending off behind its back: without this, every pixel is the fog color.
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        BufferBuilder quad = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        quad.addVertex(-1, -1, 0);
        quad.addVertex(1, -1, 0);
        quad.addVertex(1, 1, 0);
        quad.addVertex(-1, 1, 0);
        BufferUploader.drawWithShader(quad.buildOrThrow());
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        main.bindWrite(true);
    }

    private static float strength() {
        if (NEARNESS == null)
            return 0;
        try {
            return Math.clamp((1F - NEARNESS.getFloat(null)) / FULL_PULL, 0F, 1F);
        } catch (IllegalAccessException e) {
            BrokenEntries.mark(AcIrisBiomeLight.ENTRY, "Alex's Caves' fog nearness can't be read (" + e + ")");
            return 0;
        }
    }

    @Nullable
    private static Field nearness() {
        try {
            Field field = Class.forName("com.github.alexmodguy.alexscaves.client.event.ClientEvents").getDeclaredField("lastSampledFogNearness");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException | RuntimeException e) {
            BrokenEntries.mark(AcIrisBiomeLight.ENTRY, "Alex's Caves' ClientEvents.lastSampledFogNearness can't be read (" + e + ")");
            return null;
        }
    }
}
