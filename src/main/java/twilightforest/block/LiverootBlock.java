package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class LiverootBlock extends Block {

	public LiverootBlock(Properties properties) {
		super(properties);
	}

	//TODO move to loot table
	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (stack.is(ItemTags.AXES)) {
			level.setBlockAndUpdate(pos, TFBlocks.ROOT_BLOCK.defaultBlockState());
			int amountOfRoots = 1;
			//fortune formula copied from ApplyBonusCount.OreDrops.calculateNewCount so it acts exactly like the loot table
			if (EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().twilightforest$holderOrThrow(Enchantments.FORTUNE), stack) > 0) {
				int i = level.getRandom().nextInt(EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().twilightforest$holderOrThrow(Enchantments.FORTUNE), stack) + 2) - 1;
				if (i < 0) {
					i = 0;
				}
				amountOfRoots = amountOfRoots * (i + 1);
			}
			ItemEntity liveroot = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFItems.LIVEROOT, amountOfRoots));
			level.addFreshEntity(liveroot);
			level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, result);
	}
}
