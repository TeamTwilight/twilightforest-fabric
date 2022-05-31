package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;

import javax.annotation.Nullable;
import java.util.Random;

public class JarBlock extends Block implements SimpleWaterloggedBlock {

	private static final VoxelShape JAR = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
	private static final VoxelShape LID = Block.box(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	private static final VoxelShape AABB = Shapes.or(JAR, LID);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public JarBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemEntity jarStuff = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), this == TFBlocks.FIREFLY_JAR.get() ? TFBlocks.FIREFLY.get().asItem().getDefaultInstance() : TFBlocks.CICADA.get().asItem().getDefaultInstance());
		if (player.isShiftKeyDown()) {
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			jarStuff.spawnAtLocation(jarStuff.getItem());
			jarStuff.spawnAtLocation(Items.GLASS_BOTTLE);
			return InteractionResult.sidedSuccess(level.isClientSide());
		} else {
			if (player.getItemInHand(hand).is(Items.POPPY) && this == TFBlocks.FIREFLY_JAR.get()) {
				level.setBlockAndUpdate(pos, TFBlocks.FIREFLY_SPAWNER.get().defaultBlockState().setValue(AbstractParticleSpawnerBlock.RADIUS, 1));
				return InteractionResult.sidedSuccess(level.isClientSide());
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
		super.destroy(level, pos, state);
		if (level.isClientSide())
			Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.getLocation(), SoundSource.BLOCKS);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if (this == TFBlocks.FIREFLY_JAR.get()) {
			for (int i = 0; i < 2; i++) {
				double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
				double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
				double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

				level.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
			}
		} else {
			double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.2F);
			double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

			level.addParticle(ParticleTypes.NOTE, dx, dy, dz, 0, 0, 0);

			if (!TFConfig.CLIENT_CONFIG.silentCicadas.get() && level.getRandom().nextInt(75) == 0) {
				level.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, TFSounds.CICADA, SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.is(Fluids.WATER);
		return super.getStateForPlacement(context).setValue(WATERLOGGED, flag);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		return super.updateShape(state, facing, facingState, accessor, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
}
