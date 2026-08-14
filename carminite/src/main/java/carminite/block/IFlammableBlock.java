package carminite.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface IFlammableBlock {
	default boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
		return true;
	}

	static boolean onCaughtFireVanilla(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
		if (state.getBlock() instanceof IFlammableBlock flammableBlock) {
			return flammableBlock.onCaughtFire(state, level, pos, direction, igniter);
		} else if (state.getBlock() == Blocks.TNT) {
			return TntBlock.prime(level, pos);
		}
		return false;
	}
}