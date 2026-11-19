package net.xlea99.grout.entry.enchanting_tables_keep_lapis;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;

/** What {@link TableLapis} needs from an enchanting menu (implemented on {@code EnchantmentMenu} by a mixin). */
public interface LapisMenu {
    /** The menu's table: a real position on the server, NULL on the client. */
    ContainerLevelAccess grout$access();

    /** Slot 0 the item, slot 1 the lapis. */
    Container grout$enchantSlots();

    /** Whether this menu's lapis slot mirrors its table's stored lapis (set when the menu was filled). */
    boolean grout$linked();

    void grout$setLinked(boolean linked);
}
