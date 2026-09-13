package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import twilightforest.TFCommon;

public class TFDimensionData {

	// Avoid at all costs. If you need SeaLevel info instead and are ServerSide, use WorldUtil.getGeneratorSeaLevel()
	// TODO How should we fix this for clients? Would need to sync serverlevel's sealevel to clients
	@Deprecated // FIXME Make private
	public static final int SEALEVEL = 0;

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TFCommon.prefix("twilight_forest_type"));
	public static final ResourceKey<LevelStem> TWILIGHT_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, TFDimension.DIMENSION);
	public static final ResourceKey<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TFCommon.prefix("twilight_noise_gen"));
	public static final ResourceKey<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TFCommon.prefix("skylight_noise_gen"));
}