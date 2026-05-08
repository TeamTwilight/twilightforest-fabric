package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFSounds;
import twilightforest.util.WorldUtil;

public class TimeLogCoreBlock extends SpecialMagicLogBlock {
	public TimeLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableTimeCore;
	}

	@Override
	@SuppressWarnings("unchecked")
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		int numticks = 8 * 3 * this.tickRate();

		for (int i = 0; i < numticks; i++) {
			BlockPos dPos = WorldUtil.randomOffset(rand, pos, TFConfig.timeCoreRange);
			BlockState state = level.getBlockState(dPos);

			if (!state.is(BlockTagGenerator.TIME_CORE_EXCLUDED)) {
				if (state.isRandomlyTicking()) {
					state.randomTick(level, dPos, rand);
				}

				BlockEntity entity = level.getBlockEntity(dPos);
				if (entity != null) {
					BlockEntityTicker<BlockEntity> ticker = state.getTicker(level, (BlockEntityType<BlockEntity>) entity.getType());
					if (ticker != null) {
						ticker.tick(level, dPos, state, entity);
					}
				}
			}
		}
	}

	@Override
	protected void playSound(net.minecraft.world.level.Level level, BlockPos pos, RandomSource rand) {
		level.playSound(null, pos, TFSounds.TIME_CORE, SoundSource.BLOCKS, 0.35F, 0.5F);
	}
}
