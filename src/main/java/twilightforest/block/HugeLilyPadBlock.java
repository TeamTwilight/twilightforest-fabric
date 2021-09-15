package twilightforest.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.enums.HugeLilypadPiece;

import java.util.List;

public class HugeLilyPadBlock extends BushBlock {

	public static final DirectionProperty FACING = TFHorizontalBlock.FACING;
	public static final EnumProperty<HugeLilypadPiece> PIECE = EnumProperty.create("piece", HugeLilypadPiece.class);
	private static final VoxelShape AABB = Shapes.create(new AABB(0, 0, 0, 1, 0.015625, 1));

	private boolean isSelfDestructing = false;

	protected HugeLilyPadBlock(Properties props) {
		super(props);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PIECE, HugeLilypadPiece.NW));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, PIECE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
		FluidState ifluidstate = worldIn.getFluidState(pos);
		return ifluidstate.getType() == Fluids.WATER;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		//TwilightForestMod.LOGGER.info("Destroying giant lilypad at {}, state {}", pos, state);

		if (!this.isSelfDestructing) {
			this.setGiantBlockToAir(world, pos, state);
		}
	}

	private void setGiantBlockToAir(Level world, BlockPos pos, BlockState state) {
		// this flag is not threadsafe
		this.isSelfDestructing = true;

		for (BlockPos check : this.getAllMyBlocks(pos, state)) {
			BlockState stateThere = world.getBlockState(check);
			if (stateThere.getBlock() == this) {
				world.destroyBlock(check, false);
			}
		}

		this.isSelfDestructing = false;
	}

	/**
	 * Get all 4 coordinates for all parts of this lily pad.
	 */
	public List<BlockPos> getAllMyBlocks(BlockPos pos, BlockState state) {
		List<BlockPos> pieces = Lists.newArrayListWithCapacity(4);
		if (state.getBlock() == this) {
			// find NW corner
			BlockPos nwPos = pos;
			switch (state.getValue(PIECE)) {
				case NE:
					nwPos = nwPos.west();
					break;
				case SE:
					nwPos = nwPos.north().west();
					break;
				case SW:
					nwPos = nwPos.north();
					break;
				default:
					break;
			}

			pieces.add(nwPos);
			pieces.add(nwPos.south());
			pieces.add(nwPos.east());
			pieces.add(nwPos.south().east());
		}

		return pieces;
	}

	@Override
	@Deprecated
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		super.entityInside(state, worldIn, pos, entityIn);

		if (entityIn instanceof Boat) {
			worldIn.destroyBlock(new BlockPos(pos), true);
		}
	}
}
