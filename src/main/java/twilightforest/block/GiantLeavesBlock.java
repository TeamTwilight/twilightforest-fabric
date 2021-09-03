package twilightforest.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import twilightforest.TFConfig;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GiantLeavesBlock extends GiantBlock {

	public GiantLeavesBlock(Properties props) {
		super(props);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return TFConfig.COMMON_CONFIG.PERFORMANCE.leavesLightOpacity;
	}

	//TODO: PORT
//	@Override
	public boolean canCreatureSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
		return false;
	}
}
