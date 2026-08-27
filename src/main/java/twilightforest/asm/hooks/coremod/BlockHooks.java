package twilightforest.asm.hooks.coremod;

import net.minecraft.core.BlockPos;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import twilightforest.block.CloudBlock;
import twilightforest.block.SnowLoggable;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TravellersModifiersManager;

public final class BlockHooks {
	private static final FoliageColorHandler foliageColorHandler = FoliageColorHandler.INSTANCE;

	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
			if (!level.hasChunkAt(pos)) return false; //do NOT try to load new chunks when checking for rain. This can cause deadlocks if mobs ry to spawn in unloaded areas and check for rain
			LevelChunk chunk = level.getChunkAt(pos);
			for (int y = pos.getY(); y < pos.getY() + TFConfig.commonCloudBlockPrecipitationDistance; y++) {
				BlockPos newPos = pos.atY(y);
				BlockState state = chunk.getBlockState(newPos);
				if (state.getBlock() instanceof CloudBlock cloudBlock && cloudBlock.getCurrentPrecipitation(newPos, level, level.getRainLevel(1.0F)).getLeft() == Biome.Precipitation.RAIN) {
					return true;
				}
				if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(state)) {
					return false;
				}
			}
		}
		return isRaining;
	}

	public static boolean keepSnowyStateForSnowloggableBlocks(boolean o, BlockState state) {
		return o || (state.getBlock() instanceof SnowLoggable && state.getValue(SnowLoggable.SNOW_LAYERS) > 0);
	}

	public static boolean leashFenceKnotSurvives(boolean o, LeashFenceKnotEntity entity) {
		if (o)
			return true; // Short-circuit to avoid an unnecessary #getBlockState call
		BlockState fenceState = entity.level().getBlockState(entity.getPos());
		return fenceState.is(TFBlocks.WROUGHT_IRON_FENCE) && fenceState.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE;
	}

	public static TriState modifySoilDecisionForMushroomBlockSurvivability(TriState o, LevelReader level, BlockPos pos) {
		if (o != TriState.DEFAULT)
			return o; // Short-circuit - We should not override non-default soil behaviour otherwise this would allow Mushrooms to survive on ALL blocks
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;
				if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL))
					return TriState.TRUE;
			}
		}
		return o;
	}

	public static int resolveFoliageColor(int o, Biome biome, double x, double z) {
		return foliageColorHandler.get(o, biome, x, z);
	}

	public static float resetBlockFrictionWithUnrestrained(float o, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 0.6F : o;
	}

	public static boolean resetSlimeMomentumWithUnrestrained(boolean o, Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return true; //dont return false here as the original check is looking that an entity is NOT stepping carefully
		}
		return o;
	}

	public static void stopBouncing(Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) && entity.getDeltaMovement().y() > -0.08)
			entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.max(0, entity.getDeltaMovement().y), entity.getDeltaMovement().z));
	}
}