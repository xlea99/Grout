package net.xlea99.grout.mixin.spawn_stew_repels_flies_inventory;

import com.ninni.spawn.client.events.FlyEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Every frame, before a fly moves, whatever's picked where it's going: Spawn or {@code spawn_flies_roam_screen}. Holding the stew does the opposite of holding rotten flesh, with nearby flies fleeing the cursor at their scared
 * speed. A slot holding it does the same within a circle about 5x5 slots across: flies inside flee out of it, and a place to go inside it is moved out. Spawn's own rotten flesh pull runs after this, and only if a fly isn't scared.
 */
@Mixin(FlyEvents.class)
public abstract class FlyEventsMixin {
    @Unique
    private static final TagKey<Item> GROUT_REPELLENTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath("grout", "fly_repellents"));
    /** How near the cursor a fly starts to flee, in GUI pixels, and how long past that it runs to. */
    @Unique
    private static final float GROUT_CURSOR_REACH = 80F;
    @Unique
    private static final float GROUT_FLEE_TO = 120F;
    /** How long each frame near the cursor keeps it scared, in frames. */
    @Unique
    private static final int GROUT_FLEE_SCARE = 10;
    /**
     * The circle around a slot holding it that flies keep out of, in GUI pixels from the slot's middle: 2.5 slots each way
     * (slots are 18 across), so about the 5x5 around it. A fly inside flees past it at its scared speed, the way it flees
     * the stew on the cursor, and a place to fly to inside it is moved out of it.
     */
    @Unique
    private static final float GROUT_SLOT_REACH = 45F;
    @Unique
    private static final float GROUT_SLOT_FLEE_TO = 65F;
    @Unique
    private static final float GROUT_SLOT_MIDDLE = 8F;
    @Unique
    private static final float GROUT_MARGIN = 8F;

    @Inject(method = "updateFly", at = @At("HEAD"))
    private static void grout$avoidStew(FlyEvents.Fly fly, Minecraft mc, AbstractContainerScreen<?> screen, float partialTick, boolean followMouse, double mouseX, double mouseY, CallbackInfo ci) {
        FlyEventsAccessor accessor = (FlyEventsAccessor) (Object) fly;
        if (accessor.grout$stewDead())
            return;
        for (Slot slot : screen.getMenu().slots) {
            if (slot.isActive() && slot.getItem().is(GROUT_REPELLENTS))
                grout$keepOut(accessor, screen, screen.getGuiLeft() + slot.x + GROUT_SLOT_MIDDLE, screen.getGuiTop() + slot.y + GROUT_SLOT_MIDDLE);
        }
        if (!screen.getMenu().getCarried().is(GROUT_REPELLENTS))
            return;
        float awayX = accessor.grout$stewX() - (float) mouseX;
        float awayY = accessor.grout$stewY() - (float) mouseY;
        float distance = (float) Math.hypot(awayX, awayY);
        if (distance >= GROUT_CURSOR_REACH)
            return;
        if (distance < 1F) {
            awayX = 1F;
            distance = 1F;
        }
        grout$flyTo(accessor, screen, (float) mouseX + awayX / distance * GROUT_FLEE_TO, (float) mouseY + awayY / distance * GROUT_FLEE_TO);
        accessor.grout$setStewScaredTime(Math.max(accessor.grout$stewScaredTime(), GROUT_FLEE_SCARE));
    }

    @Unique
    private static void grout$keepOut(FlyEventsAccessor accessor, AbstractContainerScreen<?> screen, float x, float y) {
        float awayX = accessor.grout$stewX() - x;
        float awayY = accessor.grout$stewY() - y;
        float away = (float) Math.hypot(awayX, awayY);
        if (away < GROUT_SLOT_REACH) {
            if (away < 1F) {
                awayX = 1F;
                away = 1F;
            }
            grout$flyTo(accessor, screen, x + awayX / away * GROUT_SLOT_FLEE_TO, y + awayY / away * GROUT_SLOT_FLEE_TO);
            accessor.grout$setStewScaredTime(Math.max(accessor.grout$stewScaredTime(), GROUT_FLEE_SCARE));
            return;
        }
        float offX = accessor.grout$stewTargetX() - x;
        float offY = accessor.grout$stewTargetY() - y;
        float distance = (float) Math.hypot(offX, offY);
        if (distance >= GROUT_SLOT_REACH)
            return;
        if (distance < 1F) {
            offX = 1F;
            distance = 1F;
        }
        grout$flyTo(accessor, screen, x + offX / distance * GROUT_SLOT_REACH, y + offY / distance * GROUT_SLOT_REACH);
    }

    @Unique
    private static void grout$flyTo(FlyEventsAccessor accessor, AbstractContainerScreen<?> screen, float x, float y) {
        accessor.grout$setStewTargetX(Math.clamp(x, GROUT_MARGIN, Math.max(GROUT_MARGIN, screen.width - GROUT_MARGIN)));
        accessor.grout$setStewTargetY(Math.clamp(y, GROUT_MARGIN, Math.max(GROUT_MARGIN, screen.height - GROUT_MARGIN)));
    }
}
