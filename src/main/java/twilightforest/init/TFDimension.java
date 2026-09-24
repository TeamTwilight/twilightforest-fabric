package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import twilightforest.TFCommon;

public class TFDimension {

	// Avoid at all costs. If you need SeaLevel info instead and are ServerSide, use WorldUtil.getGeneratorSeaLevel()
	// TODO How should we fix this for clients? Would need to sync serverlevel's sealevel to clients
	@Deprecated // FIXME Make private
	public static final int SEALEVEL = 0;

	public static final Identifier DIMENSION = TFCommon.prefix("twilight_forest");

	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);
	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TFCommon.prefix("twilight_forest_type"));
	public static final ResourceKey<LevelStem> TWILIGHT_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, TFDimension.DIMENSION);
	public static final ResourceKey<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TFCommon.prefix("twilight_noise_gen"));
	public static final ResourceKey<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TFCommon.prefix("skylight_noise_gen"));

	// Referenced by our DimensionType 'twilight_forest_type'.
	public static final Identifier DIMENSION_RENDERER = TFCommon.prefix("renderer");

	// Checks if the world is linked by the default Twilight Portal.
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level level) {
		return DIMENSION.equals(level.dimension().identifier());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	public static boolean isTwilightWorldOnClient(Level clientWorld) {
		return TFCommon.ID.equals(clientWorld.dimension().identifier().getNamespace()) || isTwilightPortalDestination(clientWorld);
	}
}