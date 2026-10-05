package twilightforest.datagen.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.WorldClock;
import twilightforest.TFCommon;

public class TFWorldClockGenerator {

	public static final ResourceKey<WorldClock> TWILIGHT_FOREST = ResourceKey.create(Registries.WORLD_CLOCK, TFCommon.prefix("twilight_forest"));

	public static void bootstrap(BootstrapContext<WorldClock> context) {
		TFCommon.LOGGER.info("Bootstrap called for world clock...");
		context.register(TWILIGHT_FOREST, new WorldClock());
	}
}