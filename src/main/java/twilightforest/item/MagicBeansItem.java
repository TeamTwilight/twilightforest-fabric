package twilightforest.item;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import javax.annotation.Nonnull;

public class MagicBeansItem extends Item {

	protected MagicBeansItem(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		Block blockAt = world.getBlockState(pos).getBlock();

		int minY = pos.getY() + 1;
		int maxY = Math.max(pos.getY() + 100, (int) (getCloudHeight() + 40));
		if (pos.getY() < maxY && blockAt == TFBlocks.uberous_soil) {
			if (!world.isClientSide) {
				ItemStack is = player.getItemInHand(context.getHand());
				is.shrink(1);
				makeHugeStalk(world, pos, minY, maxY);
				if (player instanceof ServerPlayer) {
					player.awardStat(Stats.ITEM_USED.get(this));

					//fallback if the other part doesnt work since its inconsistent
					PlayerAdvancements advancements = ((ServerPlayer) player).getAdvancements();
					ServerAdvancementManager manager = ((ServerLevel)player.getCommandSenderWorld()).getServer().getAdvancements();
					Advancement advancement = manager.getAdvancement(TwilightForestMod.prefix("beanstalk"));
					if(advancement != null) {
						advancements.award(advancement, "use_beans");
					}
				}
			}

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	private float getCloudHeight() {
		return 128;
	}


	private void makeHugeStalk(Level world, BlockPos pos, int minY, int maxY) {
		float x = pos.getX();
		float z = pos.getZ();

		int yOffset = world.random.nextInt(100);

		float cScale = world.random.nextFloat() * 0.25F + 0.125F; // spiral tightness scaling
		float rScale = world.random.nextFloat() * 0.25F + 0.125F; // radius change scaling

		// offset x and z to make stalk start at origin
		float radius = 4F + Mth.sin((minY + yOffset) * rScale) * 3F; // make radius a little wavy
		x -= Mth.sin((minY + yOffset) * cScale) * radius;
		z -= Mth.cos((minY + yOffset) * cScale) * radius;

		// leaves!
		int nextLeafY = minY + 10 + world.random.nextInt(20);

		// make stalk
		boolean isClear = true;
		for (int dy = minY; dy < maxY && isClear; dy++) {
			// make radius a little wavy
			radius = 5F + Mth.sin((dy + yOffset) * rScale) * 2.5F;

			// find center of stalk
			float cx = x + Mth.sin((dy + yOffset) * cScale) * radius;
			float cz = z + Mth.cos((dy + yOffset) * cScale) * radius;


			float stalkThickness = 2.5F;

			// reduce thickness near top
			if (maxY - dy < 5) {
				stalkThickness *= (maxY - dy) / 5F;
			}

			int minX = Mth.floor(x - radius - stalkThickness);
			int maxX = Mth.ceil(x + radius + stalkThickness);
			int minZ = Mth.floor(z - radius - stalkThickness);
			int maxZ = Mth.ceil(z + radius + stalkThickness);

			// generate stalk
			for (int dx = minX; dx < maxX; dx++) {
				for (int dz = minZ; dz < maxZ; dz++) {
					if ((dx - cx) * (dx - cx) + (dz - cz) * (dz - cz) < stalkThickness * stalkThickness) {
						isClear &= this.tryToPlaceStalk(world, new BlockPos(dx, dy, dz));
					}
				}
			}

			// leaves?
			if (dy == nextLeafY) {
				// make leaf blob

				int lx = (int) (x + Mth.sin((dy + yOffset) * cScale) * (radius + stalkThickness));
				int lz = (int) (z + Mth.cos((dy + yOffset) * cScale) * (radius + stalkThickness));

				this.placeLeaves(world, new BlockPos(lx, dy, lz));

				nextLeafY = dy + 5 + world.random.nextInt(10);
			}
		}
	}

	private void placeLeaves(Level world, BlockPos pos) {
		// stalk at center
		world.setBlockAndUpdate(pos, TFBlocks.huge_stalk.defaultBlockState());

		// small squares
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				this.tryToPlaceLeaves(world, pos.offset(dx, -1, dz));
				this.tryToPlaceLeaves(world, pos.offset(dx, 1, dz));
			}
		}
		// larger square
		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				if (!((dx == 2 || dx == -2) && (dz == 2 || dz == -2))) {
					this.tryToPlaceLeaves(world, pos.offset(dx, 0, dz));
				}
			}
		}
	}

	/**
	 * Place the stalk block only if the destination is clear.  Return false if blocked.
	 */
	private boolean tryToPlaceStalk(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isAir() || state.getMaterial().isReplaceable() || (state.isAir() || state.is(BlockTags.LEAVES)) || BlockTags.LEAVES.contains(state.getBlock()) /*|| state.getBlock().canSustainLeaves(state, world, pos)*/) {
			world.setBlockAndUpdate(pos, TFBlocks.huge_stalk.defaultBlockState());
			return true;
		} else {
			return false;
		}
	}

	private void tryToPlaceLeaves(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isAir() || state.is(BlockTags.LEAVES)) {
			world.setBlock(pos, TFBlocks.beanstalk_leaves.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 2);
		}
	}
}
