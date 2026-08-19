package twilightforest.fabric.interfaces.marker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ISpecialRunningEffectsBlock {
	default boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
		return false;
	}
}