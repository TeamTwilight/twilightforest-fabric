package twilightforest.block;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import twilightforest.enums.Diagonals;

import org.jetbrains.annotations.Nullable;

public class SpiralBrickBlock extends Block {

	public static final EnumProperty<Diagonals> DIAGONAL = EnumProperty.create("diagonal", Diagonals.class);
	public static final EnumProperty<Direction.Axis> AXIS_FACING = EnumProperty.create("axis", Direction.Axis.class);

	public SpiralBrickBlock() {
		super(Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 10.0F).sound(SoundType.STONE).noOcclusion());
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIAGONAL, Diagonals.BOTTOM_RIGHT).setValue(AXIS_FACING, Direction.Axis.X));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS_FACING, DIAGONAL);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (context.isSecondaryUseActive()) {
			//if sneaking, place on the y axis with glazed terracotta logic
			return this.defaultBlockState()
					.setValue(AXIS_FACING, Direction.Axis.Y)
					.setValue(DIAGONAL, convertVerticalDirectionToDiagonal(context.getHorizontalDirection()));
		} else {
			//otherwise, place on the x and z with stair logic
			return this.defaultBlockState()
					.setValue(AXIS_FACING, context.getHorizontalDirection().getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X) //this is insanely jank but for some reason it rotates the wrong way normally. Might be a model issue but im too lazy to fix it, so this works
					.setValue(DIAGONAL, getHorizontalDiagonalFromPlayerPlacement(context.getPlayer(), context.getHorizontalDirection(), context.getClickLocation().y - (double) context.getClickedPos().getY() > 0.5D));
		}
	}

	private static Diagonals convertVerticalDirectionToDiagonal(Direction facing) {
		return switch (facing) {
			default -> Diagonals.TOP_RIGHT;
			case SOUTH -> Diagonals.BOTTOM_LEFT;
			case EAST -> Diagonals.TOP_LEFT;
			case WEST -> Diagonals.BOTTOM_RIGHT;
		};
	}

	private static Diagonals getHorizontalDiagonalFromPlayerPlacement(LivingEntity placer, Direction facing, boolean upper) {
		return switch (facing) {
			case NORTH, EAST -> Diagonals.getDiagonalFromUpDownLeftRight(placer.getDirection() != facing, upper);
			case SOUTH, WEST -> Diagonals.getDiagonalFromUpDownLeftRight(placer.getDirection() == facing, upper);
			default -> Diagonals.BOTTOM_RIGHT;
		};

	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (rot == Rotation.NONE) return state;

		Direction.Axis axis = state.getValue(AXIS_FACING);

		if (axis == Direction.Axis.Y) {
			return state.setValue(DIAGONAL, Diagonals.rotate(state.getValue(DIAGONAL), rot));
		} else {
			if (rot == Rotation.CLOCKWISE_180 || (axis == Direction.Axis.X && rot == Rotation.COUNTERCLOCKWISE_90) || (axis == Direction.Axis.Z && rot == Rotation.CLOCKWISE_90))
				state = state.setValue(DIAGONAL, Diagonals.mirror(state.getValue(DIAGONAL), Mirror.LEFT_RIGHT));

			return rot.ordinal() % 2 == 0 ? state : state.setValue(AXIS_FACING, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
		}
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(DIAGONAL, Diagonals.mirrorOn(state.getValue(AXIS_FACING), state.getValue(DIAGONAL), mirror));
	}

}
