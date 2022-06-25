package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.ValidSpawnBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class GiantLeavesBlock extends GiantBlock implements ValidSpawnBlock {

	public GiantLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter getter, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}
}
