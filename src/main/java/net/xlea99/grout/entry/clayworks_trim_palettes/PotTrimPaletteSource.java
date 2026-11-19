package net.xlea99.grout.entry.clayworks_trim_palettes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;
import net.xlea99.grout.Grout;
import net.xlea99.grout.entry.etcetera_shimmering_bismuth_trims.EtceteraShimmeringBismuthTrims;
import net.xlea99.grout.entry.etcetera_shimmering_bismuth_trims.ShimmerTrims;

/**
 * Pot trim sprites for materials with an armor palette but no pot palette. Pot palettes are four colors against armor's
 * eight, so each material names the four armor slots it uses. A material whose palette isn't there (its mod isn't
 * installed) adds nothing. Nothing is copied into Grout: the colors come from the installed mod each time.
 *
 * @param materials the name is the end of Clayworks' sprite name: the material's id, namespace_path, without minecraft_
 */
public record PotTrimPaletteSource(List<Material> materials) implements SpriteSource {
    public record Material(String name, ResourceLocation palette, List<Integer> slots) {
        private static final Codec<Material> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Material::name),
            ResourceLocation.CODEC.fieldOf("palette").forGetter(Material::palette),
            Codec.INT.listOf().fieldOf("slots").forGetter(Material::slots)
        ).apply(instance, Material::new));
    }

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Grout.MODID, "pot_trim_palettes");
    public static final MapCodec<PotTrimPaletteSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Material.CODEC.listOf().fieldOf("materials").forGetter(PotTrimPaletteSource::materials)
    ).apply(instance, PotTrimPaletteSource::new));
    private static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath("clayworks", "entity/decorated_pot_trim_palettes/trim_palette");
    private static final ResourceLocation BISMUTH = ResourceLocation.fromNamespaceAndPath("etcetera", "trims/color_palettes/bismuth");
    private static final FileToIdConverter PATTERNS = new FileToIdConverter("textures/entity/decorated_pot_trim_patterns", ".png");
    private static SpriteSourceType type;

    static void register(IEventBus modBus) {
        modBus.addListener((RegisterSpriteSourceTypesEvent event) -> {
            type = new SpriteSourceType(CODEC);
            event.register(ID, type);
        });
    }

    @Override
    public void run(ResourceManager manager, SpriteSource.Output output) {
        List<ResourceLocation> patterns = PATTERNS.listMatchingResources(manager).keySet().stream().map(TEXTURE_ID_CONVERTER::fileToId).toList();
        for (Material material : this.materials) {
            if (manager.getResource(TEXTURE_ID_CONVERTER.idToFile(material.palette())).isEmpty())
                continue;
            try {
                int[] key = ShimmerTrims.pixels(manager, KEY);
                int[] palette = picked(ShimmerTrims.pixels(manager, material.palette()), material.slots());
                ShimmerTrims.addStill(manager, output, patterns, material.name(), key, palette);
                // Bismuth shimmers like its armor trims do, replacing the still ones just made.
                if (material.palette().equals(BISMUTH) && EtceteraShimmeringBismuthTrims.ENTRY.active())
                    ShimmerTrims.addShimmering(manager, output, patterns, material.name(), key, palette);
            } catch (IOException | RuntimeException e) {
                Grout.LOGGER.error("Grout: couldn't make pot trims for {}; they stay a missing texture", material.name(), e);
            }
        }
    }

    private static int[] picked(int[] palette, List<Integer> slots) {
        int[] out = new int[slots.size()];
        for (int i = 0; i < out.length; i++)
            out[i] = palette[slots.get(i)];
        return out;
    }

    @Override
    public SpriteSourceType type() {
        return type;
    }
}
