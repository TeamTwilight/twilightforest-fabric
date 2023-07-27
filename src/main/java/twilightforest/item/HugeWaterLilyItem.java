package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.util.BlockSnapshot;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class HugeWaterLilyItem extends PlaceOnWaterBlockItem {

	public HugeWaterLilyItem(Block block, Properties props) {
		super(block, props);
	}

	// [VanillaCopy] ItemLilyPad.onItemRightClick, edits noted
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		BlockHitResult raytraceresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			if (raytraceresult.getType() == HitResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();
				Direction direction = raytraceresult.getDirection();
				if (!level.mayInteract(player, blockpos) || !player.mayUseItemAt(blockpos.relative(direction), direction, itemstack)) {
					return InteractionResultHolder.fail(itemstack);
				}

				BlockPos blockpos1 = blockpos.above();
				BlockState blockstate = level.getBlockState(blockpos);
				FluidState ifluidstate = level.getFluidState(blockpos);
				if ((ifluidstate.getType() == Fluids.WATER || blockstate.is(BlockTags.ICE)) && level.isEmptyBlock(blockpos1)) {

					// special case for handling block placement with water lilies
					BlockSnapshot blocksnapshot = BlockSnapshot.create(level.dimension(), level, blockpos1);
					// TF - getBlock() instead of hardcoded lilypad
					level.setBlock(blockpos1, getBlock().defaultBlockState(), 11);
//					if (net.minecraftforge.event.ForgeEventFactory.onBlockPlace(player, blocksnapshot, net.minecraft.core.Direction.UP)) {
//						blocksnapshot.restore(true, false);
//						return InteractionResultHolder.fail(itemstack);
//					}

					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos1, itemstack);
					}

					if (!player.getAbilities().instabuild) {
						itemstack.shrink(1);
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					level.playSound(player, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResultHolder.success(itemstack);
				}
			}

			return InteractionResultHolder.fail(itemstack);
		}
	}
}