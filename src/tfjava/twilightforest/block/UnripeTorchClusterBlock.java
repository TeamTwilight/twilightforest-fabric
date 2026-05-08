package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;

/**
 * 1:1 port of upstream {@code twilightforest.block.UnripeTorchClusterBlock} — TrollRoot
 * subclass that ripens to {@link TFBlocks#TROLLBER} once light level reaches 6.
 */
public class UnripeTorchClusterBlock extends TrollRootBlock {
	private static final int RIPEN_THRESHOLD = 6;

	public UnripeTorchClusterBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.getMaxLocalRawBrightness(pos) >= RIPEN_THRESHOLD) {
			level.setBlockAndUpdate(pos, TFBlocks.TROLLBER.get().defaultBlockState());
		}
	}
}
