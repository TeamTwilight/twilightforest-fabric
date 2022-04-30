package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.ValidSpawnBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class GiantLeavesBlock extends GiantBlock implements ValidSpawnBlock {

	public GiantLeavesBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}
}
