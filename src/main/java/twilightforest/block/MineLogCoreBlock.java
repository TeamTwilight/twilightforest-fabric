package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import twilightforest.TFConfig;
import twilightforest.init.TFSounds;
import twilightforest.item.OreMagnetItem;
import twilightforest.util.WorldUtil;

public class MineLogCoreBlock extends SpecialMagicLogBlock {

	public MineLogCoreBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.COMMON_CONFIG.MAGIC_TREES.disableMining.get();
	}

	/**
	 * The miner's tree generates the ore magnet effect randomly every second
	 */
	@Override
	void performTreeEffect(Level level, BlockPos pos, RandomSource rand) {
		BlockPos dPos = WorldUtil.randomOffset(rand, pos, TFConfig.COMMON_CONFIG.MAGIC_TREES.miningRange.get());
		int moved = OreMagnetItem.doMagnet(level, pos, dPos);

		if (moved > 0) {
			level.playSound(null, pos, TFSounds.MAGNET_GRAB.get(), SoundSource.BLOCKS, 0.1F, 1.0F);
		}
	}
}
