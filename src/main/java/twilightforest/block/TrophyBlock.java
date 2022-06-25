package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import twilightforest.enums.BossVariant;

//[VanillaCopy] of SkullBlock but we add a shape for the ur-ghast and we use BossVariant instead of ISkullType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class TrophyBlock extends AbstractTrophyBlock {

	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	public static final VoxelShape GHAST_SHAPE = Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape YETI_X_SHAPE = Block.box(3.25D, 0.0D, 4.5D, 12.75D, 10.0D, 11.5D);
	protected static final VoxelShape YETI_Z_SHAPE = Block.box(4.5D, 0.0D, 3.25D, 11.5D, 10.0D, 12.75D);
	protected static final VoxelShape YETI_CORNER_SHAPE = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 10.0D, 11.5D);

	public TrophyBlock(BossVariant variant, int value) {
		super(variant, value, Properties.of(Material.DECORATION).instabreak());
		this.registerDefaultState(this.getStateDefinition().any().setValue(TrophyBlock.ROTATION, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		if (((AbstractTrophyBlock) state.getBlock()).getVariant() == BossVariant.UR_GHAST) {
			return GHAST_SHAPE;
		} else if (((AbstractTrophyBlock) state.getBlock()).getVariant() == BossVariant.ALPHA_YETI) {
			return switch (state.getValue(ROTATION)) {
				case 3, 4, 5, 11, 12, 13 -> YETI_Z_SHAPE;
				case 2, 6, 10, 14 -> YETI_CORNER_SHAPE;
				default -> YETI_X_SHAPE;
			};
		}
		return SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(ROTATION, Mth.floor(context.getRotation() * 16.0F / 360.0F + 0.5D) & 15);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), 16));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, POWERED);
	}
}
