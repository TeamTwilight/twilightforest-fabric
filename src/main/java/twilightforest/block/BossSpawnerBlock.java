package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.block.EntityDestroyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.block.entity.spawner.BossSpawnerBlockEntity;
import twilightforest.enums.BossVariant;

import org.jetbrains.annotations.Nullable;

public class BossSpawnerBlock extends BaseEntityBlock implements EntityDestroyBlock {
	private static final VoxelShape CHUNGUS = Block.box(-4, -4, -4, 20, 20, 20);
	private final BossVariant boss;

	public BossSpawnerBlock(BlockBehaviour.Properties props, BossVariant variant) {
		super(props);
		this.boss = variant;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return boss.getType().create(pos, state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return CHUNGUS;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, boss.getType(), BossSpawnerBlockEntity::tick);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
		return state.getDestroySpeed(getter, pos) >= 0f;
	}
}
