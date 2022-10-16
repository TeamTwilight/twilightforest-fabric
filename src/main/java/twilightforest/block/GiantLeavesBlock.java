package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.block.ValidSpawnBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GiantLeavesBlock extends GiantBlock implements ValidSpawnBlock {

	public GiantLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter getter, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}
}
