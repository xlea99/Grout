package net.xlea99.grout.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.module.VariantChestsModule;

/**
 * Where a chest item's chest is drawn: the single chest texture file its renderer uses. Three families in Deep End
 * (read 2026-10-31): Every Compat's (both its {@code q/} and {@code abnww/} chests are its own CompatChestBlock, drawn
 * from {@code everycomp:entity/chest/<the item's path>}, a texture it makes at launch), Quark's variant chests, and
 * Blueprint's (Woodworks', C&C's). Each mod's classes load only when it's installed.
 */
public final class ChestTextures {
    /** A block chest's texture layout width. */
    private static final int CHEST_LAYOUT = 64;
    /**
     * A Woodworks closet's texture laid out as a chest's, as {from x, from y, w, h, to x, to y}. The closet is a back box
     * (14x14x11) and a door (14x14x4) overlapping by one line, which the back gives up, so its sides and ends come to the
     * chest's 14. Its 14 rows make the chest's 15: rows 0-9 the body, rows 9-13 the lid. The door is the chest's front.
     */
    private static final int[][] CLOSET_FACES = {
        {22, 29, 14, 10, 42, 33}, {22, 38, 14, 5, 42, 14},
        {11, 11, 14, 10, 14, 33}, {11, 20, 14, 5, 14, 14},
        {0, 29, 4, 10, 0, 33}, {0, 38, 4, 5, 0, 14}, {1, 11, 10, 10, 4, 33}, {1, 20, 10, 5, 4, 14},
        {25, 11, 10, 10, 28, 33}, {25, 20, 10, 5, 28, 14}, {18, 29, 4, 10, 38, 33}, {18, 38, 4, 5, 38, 14},
        {18, 25, 14, 4, 28, 0}, {25, 1, 14, 10, 28, 4},
        {4, 25, 14, 4, 14, 19}, {11, 1, 14, 10, 14, 23},
        {36, 11, 14, 14, 14, 0}, {36, 11, 14, 14, 28, 19}};

    private ChestTextures() {
    }

    /**
     * {@code image}, the texture {@link #of} gave for {@code item}, as everything reading a chest's drawing expects it: a
     * block chest's layout. Only closets (a chest with a door hinge: Woodworks', Every Compat's) need changing; for those
     * {@code image} is closed and a new one returned.
     */
    public static NativeImage asChest(Item item, NativeImage image) {
        if (!(item instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof ChestBlock chest)
            || !chest.defaultBlockState().hasProperty(BlockStateProperties.DOOR_HINGE))
            return image;
        try (image) {
            int scale = Math.max(1, image.getWidth() / CHEST_LAYOUT);
            NativeImage out = new NativeImage(image.getWidth(), image.getHeight(), true);
            for (int[] face : CLOSET_FACES)
                for (int y = 0; y < face[3] * scale; y++)
                    for (int x = 0; x < face[2] * scale; x++)
                        out.setPixelRGBA(face[4] * scale + x, face[5] * scale + y, image.getPixelRGBA(face[0] * scale + x, face[1] * scale + y));
            return out;
        }
    }

    @Nullable
    public static ResourceLocation of(Item item) {
        if (!(item instanceof BlockItem blockItem))
            return null;
        Block block = blockItem.getBlock();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id.getNamespace().equals("everycomp") && block instanceof ChestBlock)
            return ResourceLocation.fromNamespaceAndPath("everycomp", "textures/entity/chest/" + id.getPath() + ".png");
        ResourceLocation texture = ModList.get().isLoaded("quark") ? QuarkChests.of(block) : null;
        if (texture == null && ModList.get().isLoaded("blueprint"))
            texture = BlueprintChests.of(block);
        return texture;
    }

    /** As Quark's VariantChestRenderer: {@code quark:<folder>/<path>/normal} in the chest atlas. */
    private static final class QuarkChests {
        @Nullable
        static ResourceLocation of(Block block) {
            return block instanceof VariantChestsModule.IVariantChest chest
                ? Quark.asResource("textures/" + chest.getTextureFolder() + "/" + chest.getTexturePath() + "/normal.png") : null;
        }
    }

    /** As Blueprint's BlueprintChestBlockEntityRenderer: the materials registered under the block's chest name. */
    private static final class BlueprintChests {
        @Nullable
        static ResourceLocation of(Block block) {
            if (!(block instanceof com.teamabnormals.blueprint.core.api.IChestBlock chest))
                return null;
            var materials = com.teamabnormals.blueprint.client.BlueprintChestMaterials.getMaterials(chest.getChestMaterialsName());
            if (materials == null)
                return null;
            ResourceLocation texture = materials.singleMaterial().texture();
            return ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "textures/" + texture.getPath() + ".png");
        }
    }
}
