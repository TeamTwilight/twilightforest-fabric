package twilightforest.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.components.item.SkullCandles;
import twilightforest.init.TFDataComponents;

import java.util.List;
import java.util.Map;

public class WallSkullCandleBlock extends AbstractSkullCandleBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final MapCodec<WallSkullCandleBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(SkullBlock.Type.CODEC.fieldOf("kind").forGetter(AbstractSkullCandleBlock::getType), propertiesCodec())
			.apply(instance, WallSkullCandleBlock::new)
	);
	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(
		Direction.NORTH, Block.box(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D),
		Direction.SOUTH, Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D),
		Direction.EAST, Block.box(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D),
		Direction.WEST, Block.box(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D)));
	private static final Map<Direction, VoxelShape> PIGLIN_AABBS = Maps.newEnumMap(ImmutableMap.of(
		Direction.NORTH, Block.box(3.0D, 4.0D, 8.0D, 13.0D, 12.0D, 16.0D),
		Direction.SOUTH, Block.box(3.0D, 4.0D, 0.0D, 13.0D, 12.0D, 8.0D),
		Direction.EAST, Block.box(0.0D, 4.0D, 3.0D, 8.0D, 12.0D, 13.0D),
		Direction.WEST, Block.box(8.0D, 4.0D, 3.0D, 16.0D, 12.0D, 13.0D)));
	private static final Int2ObjectMap<List<Vec3>> PARTICLE_OFFSETS = Util.make(() -> {
		Int2ObjectMap<List<Vec3>> map = new Int2ObjectOpenHashMap<>();
		map.defaultReturnValue(ImmutableList.of());
		map.put(1, ImmutableList.of(new Vec3(0.5D, 1.25D, 0.5D)));
		map.put(2, ImmutableList.of(new Vec3(0.375D, 1.19D, 0.5D), new Vec3(0.625D, 1.25D, 0.44D)));
		map.put(3, ImmutableList.of(new Vec3(0.5D, 1.063D, 0.625D), new Vec3(0.375D, 1.19D, 0.5D), new Vec3(0.56D, 1.25D, 0.44D)));
		map.put(4, ImmutableList.of(new Vec3(0.44D, 1.063D, 0.56D), new Vec3(0.625D, 1.19D, 0.56D), new Vec3(0.375D, 1.19D, 0.375D), new Vec3(0.56D, 1.25D, 0.375D)));
		return Int2ObjectMaps.unmodifiable(map);
	});

	public WallSkullCandleBlock(SkullBlock.Type type, Properties properties) {
		super(type, properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return this.getType() == SkullBlock.Types.PIGLIN ? PIGLIN_AABBS.get(state.getValue(FACING)) : AABBS.get(state.getValue(FACING));
	}

	@Override
	public Iterable<Vec3> getParticleOffsets(BlockState state, LevelAccessor accessor, BlockPos pos) {
		return PARTICLE_OFFSETS.get(state.getValue(CANDLES));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = this.defaultBlockState();
		BlockGetter getter = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		for (Direction dir : ctx.getNearestLookingDirections()) {
			if (dir.getAxis().isHorizontal()) {
				Direction opposite = dir.getOpposite();
				state = state.setValue(FACING, opposite);
				if (!getter.getBlockState(pos.relative(dir)).canBeReplaced(ctx)) {
					return state.setValue(LIGHTING, Lighting.NONE).setValue(CANDLES, ctx.getItemInHand().getOrDefault(TFDataComponents.SKULL_CANDLES, SkullCandles.DEFAULT).count());
				}
			}
		}
		return null;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		Direction dir = state.getValue(FACING);
		if (state.getValue(LIGHTING) != Lighting.NONE) {
			this.getParticleOffsets(state, level, pos).forEach(offset -> {
				Vec3 trueOffset = offset.add(pos.getX() - (float) dir.getStepX() * 0.25F, pos.getY(), pos.getZ() - (float) dir.getStepZ() * 0.25F);
				this.addParticlesAndSound(level, trueOffset.x(), trueOffset.y(), trueOffset.z(), rand, state.getValue(LIGHTING));
			});
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(FACING));
	}
}
