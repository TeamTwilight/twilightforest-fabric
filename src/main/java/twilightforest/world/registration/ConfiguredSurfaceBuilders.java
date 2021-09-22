package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.surfacebuilders.FillingSurfaceBuilder;
import twilightforest.world.components.surfacebuilders.GlacierSurfaceBuilder;

public class ConfiguredSurfaceBuilders {
	public static final FillingSurfaceBuilder.FillingSurfaceBuilderConfig DEADROCK_CONFIG = new FillingSurfaceBuilder.FillingSurfaceBuilderConfig(BlockConstants.WEATHERED_DEADROCK, BlockConstants.CRACKED_DEADROCK, BlockConstants.CRACKED_DEADROCK, BlockConstants.DEADROCK);
	public static final SurfaceBuilderBaseConfiguration HIGHLANDS_CONFIG = new SurfaceBuilderBaseConfiguration(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.SAND);
	public static final SurfaceBuilderBaseConfiguration SNOW_CONFIG = new SurfaceBuilderBaseConfiguration(BlockConstants.SNOW, BlockConstants.SNOW, BlockConstants.PACKED_ICE);

	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> CONFIGURED_GRAVEL = SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_GRAVEL);
	public static final GlacierSurfaceBuilder.GlacierSurfaceConfig GLACIER_CONFIG = new GlacierSurfaceBuilder.GlacierSurfaceConfig(() -> CONFIGURED_GRAVEL, 30, 2, BlockConstants.PACKED_ICE, BlockConstants.ICE);

	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> CONFIGURED_HIGHLANDS = TwilightSurfaceBuilders.HIGHLANDS.configured(HIGHLANDS_CONFIG);
	public static final ConfiguredSurfaceBuilder<FillingSurfaceBuilder.FillingSurfaceBuilderConfig> CONFIGURED_PLATEAU = TwilightSurfaceBuilders.DEADROCK_FILLING.configured(DEADROCK_CONFIG);
	public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> CONFIGURED_SNOW = SurfaceBuilder.DEFAULT.configured(SNOW_CONFIG);
	public static final ConfiguredSurfaceBuilder<GlacierSurfaceBuilder.GlacierSurfaceConfig> CONFIGURED_GLACIER = TwilightSurfaceBuilders.GLACIER.configured(GLACIER_CONFIG);

	public static void registerConfigurations(Registry<ConfiguredSurfaceBuilder<?>> registry) {
		Registry.register(registry, TwilightForestMod.prefix("coarse_podzol_surface"), ConfiguredSurfaceBuilders.CONFIGURED_HIGHLANDS);
		Registry.register(registry, TwilightForestMod.prefix("deadrock_filler"), ConfiguredSurfaceBuilders.CONFIGURED_PLATEAU);
		Registry.register(registry, TwilightForestMod.prefix("heavy_snow"), ConfiguredSurfaceBuilders.CONFIGURED_SNOW);
		Registry.register(registry, TwilightForestMod.prefix("all_gravel"), ConfiguredSurfaceBuilders.CONFIGURED_GRAVEL);
		Registry.register(registry, TwilightForestMod.prefix("glacier"), ConfiguredSurfaceBuilders.CONFIGURED_GLACIER);
	}
}