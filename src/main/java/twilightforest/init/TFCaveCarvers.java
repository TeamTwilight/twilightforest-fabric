package twilightforest.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TFCommon;
import twilightforest.world.components.NoiseCarverWallProvider;
import twilightforest.world.components.TFCavesCarver;

import java.util.List;

//this was all put into 1 class because it seems like a waste to have it in 2
public class TFCaveCarvers {
	public static final TFCavesCarver TF_CAVES = register("tf_caves", new TFCavesCarver(
		CaveCarverConfiguration.CODEC,
		false,
		new NoiseCarverWallProvider(
			6972119253061020355L,
			new NormalNoise.NoiseParameters(0, 1.0),
			0.5f,
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
	public static final TFCavesCarver HIGHLAND_CAVES = register("highland_caves",
		new TFCavesCarver(
			CaveCarverConfiguration.CODEC,
			true,
			(random, _) -> {
				WeightedList<BlockState> highlandWalls = WeightedList.<BlockState>builder()
					.add(TFBlocks.TROLLSTEINN.defaultBlockState(), 1)
					.add(Blocks.STONE.defaultBlockState(), 3)
					.build();

				return highlandWalls.getRandomOrThrow(random);
			}
		)
	);

	public static final ResourceKey<ConfiguredWorldCarver<?>> TFCAVES_CONFIGURED = registerKey("tf_caves");
	public static final ResourceKey<ConfiguredWorldCarver<?>> HIGHLANDCAVES_CONFIGURED = registerKey("highland_caves");

	private static ResourceKey<ConfiguredWorldCarver<?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_CARVER, TFCommon.prefix(name));
	}

	private static <T extends WorldCarver<?>> T register(String name, T carver) {
		return Registry.register(
			BuiltInRegistries.CARVER,
			TFCommon.prefix(name),
			carver
		);
	}

	public static void init() {
		TFCommon.LOGGER.info("Initializing cave carvers...");
	}
}