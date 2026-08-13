package carminite.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface ISpecialScaffoldingBlock {
	default boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		return state.is(Blocks.SCAFFOLDING);
	}
}