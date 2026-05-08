package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import twilightforest.init.TFSounds;

/**
 * 1:1 port of upstream {@code twilightforest.block.EncasedSmokerBlock} — TFSmokerBlock
 * subclass with redstone-driven ACTIVE toggle. Plays the smoker_start cue on transitions.
 */
public class EncasedSmokerBlock extends TFSmokerBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	public EncasedSmokerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (level.isClientSide()) return;

		boolean powered = level.hasNeighborSignal(pos);

		if (!state.getValue(ACTIVE) && powered) {
			level.setBlock(pos, state.setValue(ACTIVE, true), Block.UPDATE_ALL);
			level.playSound(null, pos, TFSounds.SMOKER_START, SoundSource.BLOCKS, 0.3F, 0.6F);
		}

		if (state.getValue(ACTIVE) && !powered) {
			level.setBlock(pos, state.setValue(ACTIVE, false), Block.UPDATE_ALL);
			level.playSound(null, pos, TFSounds.SMOKER_START, SoundSource.BLOCKS, 0.3F, 0.6F);
		}
	}
}
