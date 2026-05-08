package twilightforest.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.world.components.TFCavesCarver;

import java.util.List;

public final class TFCaveCarvers {
    public static final TFRegistryObject<WorldCarver<CaveCarverConfiguration>> TFCAVES = carver("tf_caves", new TFCavesCarver(
        CaveCarverConfiguration.CODEC,
        false,
        new NoiseProvider(
            6972119253061020355L,
            new NormalNoise.NoiseParameters(0, 1.0),
            0.5F,
            List.of(
                Blocks.DIRT.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                Blocks.ROOTED_DIRT.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                Blocks.COARSE_DIRT.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                Blocks.DIRT.defaultBlockState()
            )
        )
    ));
    public static final TFRegistryObject<WorldCarver<CaveCarverConfiguration>> HIGHLAND_CAVES = carver("highland_caves", new TFCavesCarver(
        CaveCarverConfiguration.CODEC,
        true,
        new WeightedStateProvider(
            new SimpleWeightedRandomList.Builder<BlockState>()
                .add(TFBlocks.TROLLSTEINN.value().defaultBlockState(), 1)
                .add(Blocks.STONE.defaultBlockState(), 3)
        )
    ));
    public static final ResourceKey<ConfiguredWorldCarver<?>> TFCAVES_CONFIGURED = configuredKey("tf_caves");
    public static final ResourceKey<ConfiguredWorldCarver<?>> HIGHLANDCAVES_CONFIGURED = configuredKey("highland_caves");

    private TFCaveCarvers() {
    }

    public static void bootstrap() {
        TFCAVES.get();
        HIGHLAND_CAVES.get();
    }

    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        context.register(TFCAVES_CONFIGURED, TFCAVES.value().configured(new CaveCarverConfiguration(
            0.1F,
            UniformHeight.of(VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(-8)),
            ConstantFloat.of(0.6F),
            VerticalAnchor.bottom(),
            blocks.getOrThrow(BlockTagGenerator.CARVER_REPLACEABLES),
            ConstantFloat.of(1.05F),
            ConstantFloat.of(1.05F),
            ConstantFloat.of(-0.7F)
        )));

        context.register(HIGHLANDCAVES_CONFIGURED, HIGHLAND_CAVES.value().configured(new CaveCarverConfiguration(
            1.0F,
            BiasedToBottomHeight.of(VerticalAnchor.absolute(8), VerticalAnchor.absolute(32), 16),
            ConstantFloat.of(0.6F),
            VerticalAnchor.bottom(),
            blocks.getOrThrow(BlockTagGenerator.CARVER_REPLACEABLES),
            UniformFloat.of(1.1F, 1.3F),
            ConstantFloat.of(1.1F),
            UniformFloat.of(-0.9F, -0.65F)
        )));
    }

    private static <C extends CaveCarverConfiguration> TFRegistryObject<WorldCarver<C>> carver(String path, WorldCarver<C> carver) {
        ResourceKey<WorldCarver<?>> key = ResourceKey.create(BuiltInRegistries.CARVER.key(), TwilightForestMod.prefix(path));
        @SuppressWarnings({"unchecked", "rawtypes"})
        WorldCarver<C> registered = (WorldCarver<C>) Registry.register(BuiltInRegistries.CARVER, key.location(), carver);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TFRegistryObject<WorldCarver<C>> holder = new TFRegistryObject(registered, key);
        return holder;
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> configuredKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, TwilightForestMod.prefix(path));
    }
}
