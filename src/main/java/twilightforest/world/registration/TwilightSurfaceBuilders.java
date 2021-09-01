package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.surfacebuilders.FillingSurfaceBuilder;
import twilightforest.world.components.surfacebuilders.GlacierSurfaceBuilder;
import twilightforest.world.components.surfacebuilders.HighlandsSurfaceBuilder;

public class TwilightSurfaceBuilders {
	// Biomes are registered before surface builders and need the raw objects. So don't use DeferredRegister here.
	public static final FillingSurfaceBuilder DEADROCK_FILLING = new FillingSurfaceBuilder(FillingSurfaceBuilder.FillingSurfaceBuilderConfig.CODEC);
	public static final GlacierSurfaceBuilder GLACIER = new GlacierSurfaceBuilder(GlacierSurfaceBuilder.GlacierSurfaceConfig.CODEC);

	// TODO Can we move off of this and use surface features instead?
	public static final HighlandsSurfaceBuilder HIGHLANDS = new HighlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC);

	public static void register() {
		Registry.register(Registry.SURFACE_BUILDER, TwilightForestMod.prefix("plateau"), DEADROCK_FILLING);
		Registry.register(Registry.SURFACE_BUILDER, TwilightForestMod.prefix("glacier"), GLACIER);
		Registry.register(Registry.SURFACE_BUILDER, TwilightForestMod.prefix("highlands"), HIGHLANDS);
	}
}
