package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.enums.BossVariant;
import twilightforest.tileentity.spawner.BossSpawnerTileEntity;

import javax.annotation.Nullable;

public class BossSpawnerBlock extends BaseEntityBlock {

	private final BossVariant boss;

	protected BossSpawnerBlock(BlockBehaviour.Properties props, BossVariant variant) {
		super(props);
		boss = variant;
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return boss.getType().create(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> type) {
		return createTickerHelper(type, boss.getType(), BossSpawnerTileEntity::tick);
	}

//	@Override
//	public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
//		return state.getDestroySpeed(world, pos) >= 0f;
//	}
}
