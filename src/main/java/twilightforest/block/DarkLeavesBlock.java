package twilightforest.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DarkLeavesBlock extends LeavesBlock {

	public DarkLeavesBlock(Properties properties) {
		super(properties);
		FlammableBlockRegistry.getDefaultInstance().add(this, getFlammability(), getFireSpreadSpeed());
	}

	public int getFlammability() {
		return 1;
	}

	public int getFireSpreadSpeed() {
		return 0;
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter getter, BlockPos pos) {
		return 15;
	}
}
