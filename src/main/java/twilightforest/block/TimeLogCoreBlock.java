package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFSounds;
import twilightforest.util.WorldUtil;

public class TimeLogCoreBlock extends SpecialMagicLogBlock {

	public TimeLogCoreBlock(Properties properties) {
		super(properties);
	}

	/**
	 * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
	 */
	@Override
	@SuppressWarnings("unchecked")
	// Vanilla also makes this dirty cast on block entity tickers, poor mojank design.
	void performTreeEffect(Level level, BlockPos pos, RandomSource rand) {
		int numticks = 8 * 3 * this.tickRate();

		for (int i = 0; i < numticks; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16);

			BlockState state = level.getBlockState(dPos);

			if (state.isRandomlyTicking()) {
				state.randomTick((ServerLevel) level, dPos, rand);
			}

			BlockEntity entity = level.getBlockEntity(dPos);
			if (entity != null) {
				BlockEntityTicker<BlockEntity> ticker = state.getTicker(level, (BlockEntityType<BlockEntity>) entity.getType());
				if (ticker != null)
					ticker.tick(level, dPos, state, entity);
			}
		}
	}

	@Override
	protected void playSound(Level level, BlockPos pos, RandomSource rand) {
		level.playSound(null, pos, TFSounds.TIME_CORE.get(), SoundSource.BLOCKS, 0.1F, 0.5F);
	}
}
