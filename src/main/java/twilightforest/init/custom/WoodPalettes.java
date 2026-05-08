package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.util.WoodPalette;

import java.util.Locale;

public final class WoodPalettes {
    public static final Codec<Holder<WoodPalette>> CODEC =
            RegistryFileCodec.create(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC, false);

    public static final ResourceKey<WoodPalette> OAK = makeKey(ResourceLocation.withDefaultNamespace("oak"));
    public static final ResourceKey<WoodPalette> SPRUCE = makeKey(ResourceLocation.withDefaultNamespace("spruce"));
    public static final ResourceKey<WoodPalette> BIRCH = makeKey(ResourceLocation.withDefaultNamespace("birch"));
    public static final ResourceKey<WoodPalette> JUNGLE = makeKey(ResourceLocation.withDefaultNamespace("jungle"));
    public static final ResourceKey<WoodPalette> ACACIA = makeKey(ResourceLocation.withDefaultNamespace("acacia"));
    public static final ResourceKey<WoodPalette> DARK_OAK = makeKey(ResourceLocation.withDefaultNamespace("dark_oak"));
    public static final ResourceKey<WoodPalette> CRIMSON = makeKey(ResourceLocation.withDefaultNamespace("crimson"));
    public static final ResourceKey<WoodPalette> WARPED = makeKey(ResourceLocation.withDefaultNamespace("warped"));
    public static final ResourceKey<WoodPalette> VANGROVE = makeKey(ResourceLocation.withDefaultNamespace("mangrove"));

    public static final ResourceKey<WoodPalette> TWILIGHT_OAK = makeKey("twilight_oak");
    public static final ResourceKey<WoodPalette> CANOPY = makeKey("canopy");
    public static final ResourceKey<WoodPalette> MANGROVE = makeKey("mangrove");
    public static final ResourceKey<WoodPalette> DARKWOOD = makeKey("darkwood");
    public static final ResourceKey<WoodPalette> TIMEWOOD = makeKey("timewood");
    public static final ResourceKey<WoodPalette> TRANSWOOD = makeKey("transwood");
    public static final ResourceKey<WoodPalette> MINEWOOD = makeKey("minewood");
    public static final ResourceKey<WoodPalette> SORTWOOD = makeKey("sortwood");

    private WoodPalettes() {
    }

    private static ResourceKey<WoodPalette> makeKey(String name) {
        return makeKey(TwilightForestMod.prefix(name.toLowerCase(Locale.ROOT)));
    }

    private static ResourceKey<WoodPalette> makeKey(ResourceLocation name) {
        return ResourceKey.create(TFRegistries.Keys.WOOD_PALETTES, name);
    }

    public static void bootstrap(BootstrapContext<WoodPalette> context) {
        context.register(OAK, new WoodPalette(Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_PRESSURE_PLATE, TFBlocks.OAK_BANISTER.get()));
        context.register(SPRUCE, new WoodPalette(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PRESSURE_PLATE, TFBlocks.SPRUCE_BANISTER.get()));
        context.register(BIRCH, new WoodPalette(Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PRESSURE_PLATE, TFBlocks.BIRCH_BANISTER.get()));
        context.register(JUNGLE, new WoodPalette(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PRESSURE_PLATE, TFBlocks.JUNGLE_BANISTER.get()));
        context.register(ACACIA, new WoodPalette(Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PRESSURE_PLATE, TFBlocks.ACACIA_BANISTER.get()));
        context.register(DARK_OAK, new WoodPalette(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PRESSURE_PLATE, TFBlocks.DARK_OAK_BANISTER.get()));
        context.register(CRIMSON, new WoodPalette(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_BUTTON, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_FENCE_GATE, Blocks.CRIMSON_PRESSURE_PLATE, TFBlocks.CRIMSON_BANISTER.get()));
        context.register(WARPED, new WoodPalette(Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS, Blocks.WARPED_SLAB, Blocks.WARPED_BUTTON, Blocks.WARPED_FENCE, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_PRESSURE_PLATE, TFBlocks.WARPED_BANISTER.get()));
        context.register(VANGROVE, new WoodPalette(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_BUTTON, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_FENCE_GATE, Blocks.MANGROVE_PRESSURE_PLATE, TFBlocks.VANGROVE_BANISTER.get()));

        context.register(TWILIGHT_OAK, new WoodPalette(TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.TWILIGHT_OAK_FENCE_GATE.get(), TFBlocks.TWILIGHT_OAK_PRESSURE_PLATE.get(), TFBlocks.TWILIGHT_OAK_BANISTER.get()));
        context.register(CANOPY, new WoodPalette(TFBlocks.CANOPY_PLANKS.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.CANOPY_FENCE_GATE.get(), TFBlocks.CANOPY_PRESSURE_PLATE.get(), TFBlocks.CANOPY_BANISTER.get()));
        context.register(MANGROVE, new WoodPalette(TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.MANGROVE_FENCE_GATE.get(), TFBlocks.MANGROVE_PRESSURE_PLATE.get(), TFBlocks.MANGROVE_BANISTER.get()));
        context.register(DARKWOOD, new WoodPalette(TFBlocks.DARK_PLANKS.get(), TFBlocks.DARK_STAIRS.get(), TFBlocks.DARK_SLAB.get(), TFBlocks.DARK_BUTTON.get(), TFBlocks.DARK_FENCE.get(), TFBlocks.DARK_FENCE_GATE.get(), TFBlocks.DARK_PRESSURE_PLATE.get(), TFBlocks.DARK_BANISTER.get()));
        context.register(TIMEWOOD, new WoodPalette(TFBlocks.TIME_PLANKS.get(), TFBlocks.TIME_STAIRS.get(), TFBlocks.TIME_SLAB.get(), TFBlocks.TIME_BUTTON.get(), TFBlocks.TIME_FENCE.get(), TFBlocks.TIME_FENCE_GATE.get(), TFBlocks.TIME_PRESSURE_PLATE.get(), TFBlocks.TIME_BANISTER.get()));
        context.register(TRANSWOOD, new WoodPalette(TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE_GATE.get(), TFBlocks.TRANSFORMATION_PRESSURE_PLATE.get(), TFBlocks.TRANSFORMATION_BANISTER.get()));
        context.register(MINEWOOD, new WoodPalette(TFBlocks.MINING_PLANKS.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.MINING_FENCE_GATE.get(), TFBlocks.MINING_PRESSURE_PLATE.get(), TFBlocks.MINING_BANISTER.get()));
        context.register(SORTWOOD, new WoodPalette(TFBlocks.SORTING_PLANKS.get(), TFBlocks.SORTING_STAIRS.get(), TFBlocks.SORTING_SLAB.get(), TFBlocks.SORTING_BUTTON.get(), TFBlocks.SORTING_FENCE.get(), TFBlocks.SORTING_FENCE_GATE.get(), TFBlocks.SORTING_PRESSURE_PLATE.get(), TFBlocks.SORTING_BANISTER.get()));
    }
}
