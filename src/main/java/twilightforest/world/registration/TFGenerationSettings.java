package twilightforest.world.registration;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;

public class TFGenerationSettings {
	@Deprecated // Used in places where we can't access the sea level FIXME Resolve
	public static final int SEALEVEL = 0;

	public static final ResourceLocation DIMENSION = TwilightForestMod.prefix("twilight_forest");
	public static final ResourceKey<LevelStem> WORLDGEN_KEY = ResourceKey.create(Registries.LEVEL_STEM, DIMENSION);
	public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registries.DIMENSION, DIMENSION);

	// Checks if the world is linked by the default Twilight Portal.
	// If you want to check if the world is a Twilight world, use usesTwilightChunkGenerator instead
	// Only use this method if you need to know if a world is a destination for portals!
	public static boolean isTwilightPortalDestination(Level level) {
		return DIMENSION.equals(level.dimension().location());
	}

	// Checks if the world is a qualified Twilight world by checking against its namespace or if it's a portal destination
	@Environment(EnvType.CLIENT)
	public static boolean isTwilightWorldOnClient(Level level) {
		return TwilightForestMod.ID.equals(Minecraft.getInstance().level.dimension().location().getNamespace()) || isTwilightPortalDestination(level);
	}

	// Checks if the world is *a* Twilight world on the Server side.
	public static boolean usesTwilightChunkGenerator(ServerLevel level) {
		return level.getChunkSource().getGenerator() instanceof ChunkGeneratorTwilight;
	}
}
