package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import twilightforest.extensions.IBlockMethods;
import twilightforest.item.TFItems;

import java.util.Random;

public class UberousSoilBlock extends Block implements BonemealableBlock, IBlockMethods {

	public UberousSoilBlock(Properties props) {
		super(props);
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, IPlantable plantable) {
		if (direction != Direction.UP)
			return false;
		IPlantable.PlantType plantType = plantable.getPlantType(world, pos.relative(direction));
		return plantType == IPlantable.PlantType.CROP || plantType == IPlantable.PlantType.PLAINS || plantType == IPlantable.PlantType.CAVE;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		BlockState above = world.getBlockState(pos.above());
		Material aboveMaterial = above.getMaterial();

		if (aboveMaterial.isSolid()) {
			world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
		}

		if (above.getBlock() instanceof BonemealableBlock) {
			// apply bonemeal
			// I wanted to make a while loop that checks if the block above can be bonemealed or not and iterate the growing process until fully grown,
			// but putting isValidBonemealTarget in a while loop freezes the server. This will do for now I guess
			for(int i = 0; i < 15; i++) BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), world, pos.above());
			world.levelEvent(2005, pos.above(), 0);
			if(above.getBlock() instanceof CropBlock || above.getBlock() instanceof StemBlock) {
				world.setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7));
			}
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if(level.isClientSide && rand.nextInt(5) == 0) {
			for(Player player : level.players()) {
				if (player.getMainHandItem().getItem().equals(TFItems.magic_beans) || player.getOffhandItem().getItem().equals(TFItems.magic_beans)) {
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
								!blockAt.is(TFBlocks.uberous_soil)) {
					return true;

				} else if (
						!world.getBlockState(pos.relative(dir).above().above()).getMaterial().isSolid() &&
								(world.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
								!world.getBlockState(pos.relative(dir).above()).is(TFBlocks.uberous_soil)) {
					return true;

				} else if (
						!world.getBlockState(pos.relative(dir)).getMaterial().isSolid() &&
								(world.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
								!world.getBlockState(pos.relative(dir).below()).is(TFBlocks.uberous_soil)) {
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
		for(Direction dir: Direction.values()) {
			if(dir != Direction.UP && dir != Direction.DOWN) {
				BlockState blockAt = world.getBlockState(pos.relative(dir));
				if (
						!world.getBlockState(pos.relative(dir).above()).getMaterial().isSolid() &&
						(blockAt.is(BlockTags.DIRT) || blockAt.is(Blocks.FARMLAND)) &&
						!blockAt.is(TFBlocks.uberous_soil)) {

					world.setBlockAndUpdate(pos.relative(dir), this.defaultBlockState());
					break;
				} else if (
						!world.getBlockState(pos.relative(dir).above().above()).getMaterial().isSolid() &&
						(world.getBlockState(pos.relative(dir).above()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).above()).is(Blocks.FARMLAND)) &&
						!world.getBlockState(pos.relative(dir).above()).is(TFBlocks.uberous_soil)) {

					world.setBlockAndUpdate(pos.relative(dir).above(), this.defaultBlockState());
					break;
				} else if (
						!world.getBlockState(pos.relative(dir)).getMaterial().isSolid() &&
						(world.getBlockState(pos.relative(dir).below()).is(BlockTags.DIRT) || world.getBlockState(pos.relative(dir).below()).is(Blocks.FARMLAND)) &&
						!world.getBlockState(pos.relative(dir).below()).is(TFBlocks.uberous_soil)) {

					world.setBlockAndUpdate(pos.relative(dir).below(), this.defaultBlockState());
					break;
				}
			}
		}
	}
}
