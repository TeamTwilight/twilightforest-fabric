package twilightforest.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;

public abstract class CritterBlock extends BaseEntityBlock implements Equipable {
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final String CLIENT_SOUND_HOOKS = "com.codex.twilight.client.render.ClientCritterSoundHooks";

	private final VoxelShape downShape = Shapes.create(new AABB(0.2F, 0.85F, 0.2F, 0.8F, 1.0F, 0.8F));
	private final VoxelShape upShape = Shapes.create(new AABB(0.2F, 0.0F, 0.2F, 0.8F, 0.15F, 0.8F));
	private final VoxelShape northShape = Shapes.create(new AABB(0.2F, 0.2F, 0.85F, 0.8F, 0.8F, 1.0F));
	private final VoxelShape southShape = Shapes.create(new AABB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.15F));
	private final VoxelShape westShape = Shapes.create(new AABB(0.85F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F));
	private final VoxelShape eastShape = Shapes.create(new AABB(0.0F, 0.2F, 0.2F, 0.15F, 0.8F, 0.8F));

	protected CritterBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP));
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> this.downShape;
			case NORTH -> this.northShape;
			case SOUTH -> this.southShape;
			case WEST -> this.westShape;
			case EAST -> this.eastShape;
			default -> this.upShape;
		};
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction clicked = context.getClickedFace();
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		BlockState state = this.defaultBlockState()
			.setValue(FACING, clicked)
			.trySetValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

		if (this.canSurvive(state, context.getLevel(), context.getClickedPos())) {
			return state;
		}

		for (Direction direction : context.getNearestLookingDirections()) {
			state = this.defaultBlockState()
				.setValue(FACING, direction.getOpposite())
				.trySetValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
			if (this.canSurvive(state, context.getLevel(), context.getClickedPos())) {
				return state;
			}
		}

		return null;
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
		if (state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED)) {
			accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
		}

		if (state.getValue(FACING).getOpposite() == direction && !state.canSurvive(accessor, pos)) {
			return Blocks.AIR.defaultBlockState();
		}

		return super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		Direction facing = state.getValue(FACING);
		BlockPos restingPos = pos.relative(facing.getOpposite());
		return canSupportCenter(reader, restingPos, facing) || reader.getBlockState(restingPos).getBlock() instanceof LeavesBlock;
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (stack.getItem() == TFBlocks.MASON_JAR.get().asItem()) {
			ItemContainerContents contents = stack.getComponents().get(DataComponents.CONTAINER);
			if (contents != null && !contents.copyOne().isEmpty()) {
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			}

			if (this == TFBlocks.FIREFLY.get()) {
				captureCritter(stack, state, level, pos, player, Util.make(new ItemStack(TFBlocks.FIREFLY_JAR.get()), jar -> jar.set(TFDataComponents.JAR_LID, stack.get(TFDataComponents.JAR_LID))));
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}

			if (this == TFBlocks.CICADA.get()) {
				captureCritter(stack, state, level, pos, player, Util.make(new ItemStack(TFBlocks.CICADA_JAR.get()), jar -> jar.set(TFDataComponents.JAR_LID, stack.get(TFDataComponents.JAR_LID))));
				stopCicadaSoundIfClient(level);
				return ItemInteractionResult.sidedSuccess(level.isClientSide());
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	private static void captureCritter(ItemStack jarInput, BlockState state, Level level, BlockPos pos, Player player, ItemStack jarStack) {
		if (!player.hasInfiniteMaterials()) {
			jarInput.shrink(1);
		}

		player.getInventory().add(jarStack);
		level.setBlockAndUpdate(pos, state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if ((entity instanceof Projectile && !entity.getType().is(EntityTagGenerator.DONT_KILL_BUGS)) || entity instanceof FallingBlockEntity) {
			level.setBlockAndUpdate(pos, state.hasProperty(WATERLOGGED) && state.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
			stopCicadaSoundIfClient(level);

			level.playSound(null, pos, TFSounds.BUG_SQUISH, SoundSource.BLOCKS, 1.0F, 1.0F);

			ResourceKey<LootTable> squishLootTable = this.getSquishLootTable();
			if (level instanceof ServerLevel serverLevel && squishLootTable != null) {
				LootParams context = new LootParams.Builder(serverLevel)
					.withParameter(LootContextParams.BLOCK_STATE, state)
					.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
					.withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
					.create(LootContextParamSets.BLOCK);
				serverLevel.getServer().reloadableRegistries().getLootTable(squishLootTable).getRandomItems(context).forEach(stack -> popResource(serverLevel, pos, stack));
			}

			for (int i = 0; i < 50; i++) {
				boolean wallBug = state.getValue(FACING) != Direction.UP;
				level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()), true,
					pos.getX() + Mth.nextFloat(level.getRandom(), 0.25F, 0.75F),
					pos.getY() + (wallBug ? 0.5F : 0.0F),
					pos.getZ() + Mth.nextFloat(level.getRandom(), 0.25F, 0.75F),
					0.0D, 0.0D, 0.0D);
			}

			if (entity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer player) {
				player.awardStat(Stats.CUSTOM.get(TFStats.BUGS_SQUISHED));
				TFAdvancements.KILL_BUG.get().trigger(player, state);
			}
		}
	}

	private static void stopCicadaSoundIfClient(Level level) {
		if (!level.isClientSide()) {
			return;
		}

		try {
			Class.forName(CLIENT_SOUND_HOOKS).getMethod("stopCicada").invoke(null);
		} catch (ReflectiveOperationException ignored) {
			// Client hook is split-env; a missing hook should never break block logic.
		}
	}

	public abstract @Nullable ResourceKey<LootTable> getSquishLootTable();

	@Nullable
	@Override
	public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}
}
