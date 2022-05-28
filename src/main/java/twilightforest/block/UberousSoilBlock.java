package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.util.IPlantable;
import io.github.fabricators_of_create.porting_lib.util.PlantType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UberousSoilBlock extends Block implements BonemealableBlock {

	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public UberousSoilBlock(Properties props) {
		super(props);
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return ctx.getLevel().getBlockState(ctx.getClickedPos().above()).getMaterial().isSolid() ? Blocks.DIRT.defaultBlockState() : super.getStateForPlacement(ctx);
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, IPlantable plantable) {
		if (direction != Direction.UP)
			return false;
		PlantType plantType = plantable.getPlantType(world, pos.relative(direction));
		return plantType == PlantType.CROP || plantType == PlantType.PLAINS || plantType == PlantType.CAVE;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (fromPos.getY() == pos.getY() + 1) {
			BlockState above = world.getBlockState(fromPos);
			if (!(above.getBlock() instanceof BonemealableBlock bonemealableBlock && !above.is(TFBlocks.UBEROUS_SOIL.get()))) {
				if (above.getMaterial().isSolid())
					world.setBlockAndUpdate(pos, pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), world, pos));
				return;
			}

			BlockState newState = Blocks.DIRT.defaultBlockState();

			if (bonemealableBlock instanceof IPlantable iPlantable && iPlantable.getPlantType(world, fromPos) == PlantType.CROP)
				newState = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7);
			else if (bonemealableBlock instanceof MushroomBlock)
				newState = Blocks.MYCELIUM.defaultBlockState();
			else if (bonemealableBlock instanceof BushBlock)
				newState = Blocks.GRASS_BLOCK.defaultBlockState();
			else if (bonemealableBlock instanceof MossBlock mossBlock)
				newState = mossBlock.defaultBlockState();

			if (world instanceof ServerLevel serverLevel && bonemealableBlock instanceof MushgloomBlock mushgloomBlock) {
				/*
				  This seems a bit hacky, but it's the easiest way of letting the mushgloom only be grown by uberous soil
				  If we make it growable by bonemeal as well, just delete this if statement and update the appropriate method inside the mushgloom class
				 */
				world.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, world, pos));
				mushgloomBlock.growMushroom(serverLevel, fromPos, above, serverLevel.random);
				world.levelEvent(2005, fromPos, 0);
				return;
			}

			/*
			 The block must be set to a new one before we attempt to bonemeal the plant, otherwise, we can end up with an infinite block update loop
			 For example, if we try to grow a mushroom but there isn't enough room for it to grow. (For some reason mushroom code does a block update when failing to grow)
			 */
			world.setBlockAndUpdate(pos, pushEntitiesUp(state, newState, world, pos));

			if (world instanceof ServerLevel serverLevel) {
				MinecraftServer server = serverLevel.getServer();
				server.tell(new TickTask(server.getTickCount(), () -> {
					//We need to use a tick task so that plants that grow into tall variants don't just break upon growth
					for (int i = 0; i < 15; i++) BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), serverLevel, fromPos);
				}));
			}

			world.levelEvent(2005, fromPos, 0);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if(level.isClientSide && rand.nextInt(5) == 0) {
			for(Player player : level.players()) {
				if (player.getMainHandItem().getItem().equals(TFItems.MAGIC_BEANS.get()) || player.getOffhandItem().getItem().equals(TFItems.MAGIC_BEANS.get())) {
					for (int i = 0; i < 2; i++) {
						level.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + rand.nextDouble(), pos.getY() + 1.25D, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}
					break;
				}
			}
		}
	}

	@Override
	//check each side of the block, as well as above and below each of those positions for valid spots
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		for (Direction dir : Direction.values()) {
			if (dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = world.getBlockState(pos.relative(dir));
				if (
						!world.getBlockState(pos.relative(dir).above()).getMaterial().isSolid() &&
								(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
								!blockAt.is(TFBlocks.UBEROUS_SOIL.get())) {
					return true;

				} else if (
						!world.getBlockState(pos.relative(dir).above().above()).getMaterial().isSolid() &&
								(world.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
								!world.getBlockState(pos.relative(dir).above()).is(TFBlocks.UBEROUS_SOIL.get())) {
					return true;

				} else if (
						!world.getBlockState(pos.relative(dir)).getMaterial().isSolid() &&
								(world.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
								!world.getBlockState(pos.relative(dir).below()).is(TFBlocks.UBEROUS_SOIL.get())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level world, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	//check each side of the block, as well as above and below each of those positions to check for a place to put a block
	//the above and below checks allow the patch to jump to a new y level, makes spreading easier
	public void performBonemeal(ServerLevel world, Random rand, BlockPos pos, BlockState state) {
		List<Direction> directions = Arrays.asList(Direction.values());
		Collections.shuffle(directions);
		for(Direction dir: directions) {
			if(dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = world.getBlockState(pos.relative(dir));
				if (
						!world.getBlockState(pos.relative(dir).above()).getMaterial().isSolid() &&
						(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
						!blockAt.is(TFBlocks.UBEROUS_SOIL.get())) {

					world.setBlockAndUpdate(pos.relative(dir), this.defaultBlockState());
					break;
				} else if (
						!world.getBlockState(pos.relative(dir).above().above()).getMaterial().isSolid() &&
						(world.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
						!world.getBlockState(pos.relative(dir).above()).is(TFBlocks.UBEROUS_SOIL.get())) {

					world.setBlockAndUpdate(pos.relative(dir).above(), this.defaultBlockState());
					break;
				} else if (
						!world.getBlockState(pos.relative(dir)).getMaterial().isSolid() &&
						(world.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
						!world.getBlockState(pos.relative(dir).below()).is(TFBlocks.UBEROUS_SOIL.get())) {

					world.setBlockAndUpdate(pos.relative(dir).below(), this.defaultBlockState());
					break;
				}
			}
		}
	}
}
