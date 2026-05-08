package twilightforest.asmhooks;

import net.minecraft.core.BlockPos;
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
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TravellersModifiersManager;

public class BlockHooks {

	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
			if (!level.hasChunkAt(pos)) {
				return false;
			}
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

	public static boolean keepSnowyStateForSnowloggableBlocks(boolean original, BlockState state) {
		return original || (state.getBlock() instanceof SnowLoggable && state.getValue(SnowLoggable.SNOW_LAYERS) > 0);
	}

	public static boolean leashFenceKnotSurvives(boolean original, LeashFenceKnotEntity entity) {
		if (original) {
			return true;
		}
		BlockState fenceState = entity.level().getBlockState(entity.blockPosition());
		return fenceState.is(TFBlocks.WROUGHT_IRON_FENCE.get()) && fenceState.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE;
	}

	public static boolean canMushroomSurviveNearTwilightPortal(boolean original, LevelReader level, BlockPos pos) {
		if (original) {
			return true;
		}
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x != 0 || z != 0) {
					if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL.get())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static float resetBlockFrictionWithUnrestrained(float original, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 0.6F : original;
	}

	public static boolean resetSlimeMomentumWithUnrestrained(boolean original, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) || original;
	}

	public static void stopBouncing(Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) && entity.getDeltaMovement().y() > -0.08D) {
			entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.max(0, entity.getDeltaMovement().y), entity.getDeltaMovement().z));
		}
	}
}
