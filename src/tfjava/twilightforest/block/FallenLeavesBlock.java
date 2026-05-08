package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.client.particle.data.LeafParticleData;

public class FallenLeavesBlock extends TFPlantBlock {
	public static final int MAX_HEIGHT = 8;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final MapCodec<FallenLeavesBlock> CODEC = simpleCodec(FallenLeavesBlock::new);
	protected static final VoxelShape[] SHAPE_BY_LAYER = Util.make(new VoxelShape[9], shapes -> {
		shapes[0] = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.2D, 16.0D);
		for (int i = 1; i <= MAX_HEIGHT; i++) {
			shapes[i] = Block.box(0.0D, 0.0D, 0.0D, 16.0D, i * 2.0D, 16.0D);
		}
	});

	public FallenLeavesBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LAYERS, 1));
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).isFaceSturdy(reader, pos.below(), Direction.UP)
				|| reader.getFluidState(pos.below()).getType() == Fluids.WATER;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS) - 1];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
		return super.mayPlaceOn(state, getter, pos)
				|| ((getter.getFluidState(pos).getType() == Fluids.WATER || state.getBlock() instanceof IceBlock)
				&& getter.getFluidState(pos.above()).getType() == Fluids.EMPTY);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		int layers = state.getValue(LAYERS);
		boolean waterBelow = context.getLevel().getBlockState(context.getClickedPos().below()).liquid();
		if (waterBelow) {
			return false;
		}
		if (context.getItemInHand().is(this.asItem()) && layers < MAX_HEIGHT) {
			return !context.replacingClickedOnBlock() || context.getClickedFace() == Direction.UP;
		}
		return layers == 1;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = context.getLevel().getBlockState(context.getClickedPos());
		if (state.is(this)) {
			return state.setValue(LAYERS, Math.min(MAX_HEIGHT, state.getValue(LAYERS) + 1));
		}
		return super.getStateForPlacement(context);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(50) != 0) {
			return;
		}
		float dist = 10.0F;
		if (!level.canSeeSkyFromBelowWater(pos)) {
			for (int y = 0; y <= dist; y++) {
				if (level.getBlockState(pos.above(y)).is(BlockTags.LEAVES)) {
					dist = y;
					break;
				}
			}
			if (dist > 10.0F) {
				return;
			}
		}
		level.addParticle(fallenLeafData(random), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.entityInside(state, level, pos, entity);
		int layers = state.getValue(LAYERS);
		if (layers > 2) {
			entity.makeStuckInBlock(state, new Vec3(1.0D - 0.05D * (layers - 2), 1.0D, 1.0D - 0.05D * (layers - 2)));
		}
		if (!(entity instanceof LivingEntity) || (entity.getDeltaMovement().x() == 0.0D && entity.getDeltaMovement().z() == 0.0D) || !level.getRandom().nextBoolean()) {
			return;
		}
		double x = pos.getX() + level.getRandom().nextFloat();
		double y = pos.getY() + (2.0F / 16.0F) * (layers - 1);
		double z = pos.getZ() + level.getRandom().nextFloat();
		double dx = level.getRandom().nextFloat() * -0.5F * entity.getDeltaMovement().x();
		double dy = level.getRandom().nextFloat() * 0.5F + 0.25F;
		double dz = level.getRandom().nextFloat() * -0.5F * entity.getDeltaMovement().z();
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(fallenLeafData(level.getRandom()), x, y, z, 0, dx, dy, dz, 1.0D);
		} else {
			level.addParticle(fallenLeafData(level.getRandom()), x, y, z, dx, dy, dz);
		}
	}

	private static LeafParticleData fallenLeafData(RandomSource random) {
		int color = FoliageColor.getDefaultColor();
		int r = Mth.clamp(((color >> 16) & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
		int g = Mth.clamp(((color >> 8) & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
		int b = Mth.clamp((color & 0xFF) + random.nextInt(0x22) - 0x11, 0x00, 0xFF);
		return new LeafParticleData(r, g, b);
	}
}
