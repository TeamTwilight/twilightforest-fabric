package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.block.entity.TFSignBlockEntity;

import org.jetbrains.annotations.Nullable;

public class TFWallSignBlock extends WallSignBlock {
	public TFWallSignBlock(Properties properties, WoodType type) {
		super(properties, type);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFSignBlockEntity(pos, state);
	}
}
