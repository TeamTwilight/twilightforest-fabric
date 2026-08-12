package carminite.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface IBeaconColoredBlock {
	private Block self() {
		return (Block) this;
	}

	@Nullable
	default Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		if (self() instanceof BeaconBeamBlock)
			return ((BeaconBeamBlock) self()).getColor().getTextureDiffuseColor();
		return null;
	}
}