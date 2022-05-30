package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TFSounds;
import twilightforest.util.WorldUtil;

import java.util.Random;

public class TimeLogCoreBlock extends SpecialMagicLogBlock {

	public TimeLogCoreBlock(Properties props) {
		super(props);
	}

	/**
	 * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
	 */
	@Override
	@SuppressWarnings("unchecked") // Vanilla also makes this dirty cast on block entity tickers, poor mojank design.
	void performTreeEffect(Level world, BlockPos pos, Random rand) {
		int numticks = 8 * 3 * this.tickRate();

		for (int i = 0; i < numticks; i++) {

			BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16);

			BlockState state = world.getBlockState(dPos);

			if (state.isRandomlyTicking()) {
				state.randomTick((ServerLevel) world, dPos, rand);
			}

			BlockEntity entity = world.getBlockEntity(dPos);
			if (entity != null) {
				BlockEntityTicker<BlockEntity> ticker = state.getTicker(world, (BlockEntityType<BlockEntity>) entity.getType());
				if (ticker != null)
					ticker.tick(world, dPos, state, entity);
			}
		}
	}

	@Override
	protected void playSound(Level level, BlockPos pos, Random rand) {
		level.playSound(null, pos, TFSounds.TIME_CORE, SoundSource.BLOCKS, 0.1F, 0.5F);
	}
}
