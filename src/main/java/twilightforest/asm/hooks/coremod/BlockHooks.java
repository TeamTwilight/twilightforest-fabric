package twilightforest.asm.hooks.coremod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import twilightforest.block.CloudBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.config.TFConfig;
import twilightforest.init.custom.TravellersModifiersManager;

public final class BlockHooks {
	private static final FoliageColorHandler foliageColorHandler = FoliageColorHandler.INSTANCE;

	public static void stopBouncing(Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) && entity.getDeltaMovement().y() > -0.08)
			entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.max(0, entity.getDeltaMovement().y), entity.getDeltaMovement().z));
	}

	public static boolean resetSlimeMomentumWithUnrestrained(boolean o, Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return true;
		}
		return o;
	}

	public static float resetBlockFrictionWithUnrestrained(float o, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 0.6F : o;
	}

	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
			if (!level.hasChunkAt(pos)) return false;
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

	public static int resolveFoliageColor(int o, Biome biome, double x, double z) {
		return foliageColorHandler.get(o, biome, x, z);
	}
}