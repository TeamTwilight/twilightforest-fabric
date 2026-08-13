package carminite.event;

import carminite.event.impl.CarminiteEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEvent extends CarminiteEvent {
	private final LevelAccessor level;
	private final BlockPos pos;
	private final BlockState state;

	public BlockEvent(LevelAccessor level, BlockPos pos, BlockState state) {
		this.pos = pos;
		this.level = level;
		this.state = state;
	}

	public LevelAccessor getLevel() {
		return level;
	}

	public BlockPos getPos() {
		return pos;
	}

	public BlockState getState() {
		return state;
	}
}