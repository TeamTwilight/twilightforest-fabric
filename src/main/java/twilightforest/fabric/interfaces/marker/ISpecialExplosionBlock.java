package twilightforest.fabric.interfaces.marker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ISpecialExplosionBlock {
	default float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
		return ((Block) this).getExplosionResistance();
	}
}