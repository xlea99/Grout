package net.xlea99.grout.mixin.ac_iris_biome_light;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import java.util.ArrayDeque;
import java.util.Map;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.xlea99.grout.entry.ac_iris_biome_light.BiomeLight;
import net.xlea99.grout.entry.ac_iris_biome_light.UnbuiltSections;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Builds the sections around and above the camera that it hasn't looked at, in Alex's Caves' cave biomes under a shaderpack. A pack's
 * shadow map only holds built sections, and Sodium builds what the camera sees, so a cave's ceiling doesn't block the sun until
 * the player has looked up: the light on the ground changes when they do. Normally a fine optimization. Queued ahead of Sodium's
 * own deferred builds, nearest first, since a sunlit floor that snaps dark is the thing to avoid, but under its budget, and only
 * sections with no mesh yet. Sodium clears a section's pending flag on submitting it, so queuing one twice builds it once.
 * Targeted by name: see {@link UnbuiltSections}.
 */
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager")
public abstract class RenderSectionManagerMixin {
    /** A pack's usual shadow distance, 192 blocks, in sections. */
    @Unique
    private static final int grout$RADIUS = 12;
    /** While there's something to build. Sodium replaces its queues whenever its view updates, so these have to keep coming. */
    @Unique
    private static final int grout$FRAMES_BETWEEN_BUSY_SCANS = 4;
    @Unique
    private static final int grout$FRAMES_BETWEEN_IDLE_SCANS = 20;
    @Unique
    private static final int grout$MOST_PER_SCAN = 32;

    @Shadow
    @Final
    private Long2ReferenceMap<Object> sectionByPosition;
    @Shadow
    private Map<Object, ArrayDeque<Object>> taskLists;
    @Shadow
    @Final
    private ClientLevel level;

    @Unique
    private int grout$framesUntilScan;

    @Inject(method = "updateChunks", at = @At("HEAD"))
    private void grout$buildUnseenSections(boolean updateImmediately, CallbackInfo ci) {
        if (--this.grout$framesUntilScan > 0 || ShadowRenderingState.areShadowsCurrentlyBeingRendered() || !BiomeLight.needsCeilingBuilt())
            return;
        this.grout$framesUntilScan = grout$FRAMES_BETWEEN_IDLE_SCANS;
        ArrayDeque<Object> queue = this.taskLists.get(UnbuiltSections.queueKey());
        if (queue == null)
            return;
        BlockPos camera = Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition();
        int centerX = SectionPos.blockToSectionCoord(camera.getX());
        int centerY = SectionPos.blockToSectionCoord(camera.getY());
        int centerZ = SectionPos.blockToSectionCoord(camera.getZ());
        Object[] found = new Object[grout$MOST_PER_SCAN];
        int count = 0;
        // Outward in square rings, so the ceiling right above comes first.
        for (int ring = 0; ring <= grout$RADIUS && count < found.length; ring++)
            for (int x = -ring; x <= ring && count < found.length; x++)
                for (int z = -ring; z <= ring && count < found.length; z++) {
                    if (Math.max(Math.abs(x), Math.abs(z)) != ring)
                        continue;
                    for (int y = centerY - 1; y < this.level.getMaxSection() && count < found.length; y++) {
                        Object section = this.sectionByPosition.get(SectionPos.asLong(centerX + x, y, centerZ + z));
                        if (section != null && UnbuiltSections.waiting(section))
                            found[count++] = section;
                    }
                }
        // Last to first, so the nearest ends up at the very front.
        for (int i = count - 1; i >= 0; i--)
            queue.addFirst(found[i]);
        if (count > 0)
            this.grout$framesUntilScan = grout$FRAMES_BETWEEN_BUSY_SCANS;
    }
}
