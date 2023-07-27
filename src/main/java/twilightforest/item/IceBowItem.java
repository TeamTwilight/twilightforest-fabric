package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import io.github.fabricators_of_create.porting_lib.item.CustomArrowItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.entity.projectile.IceArrow;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class IceBowItem extends BowItem implements CustomArrowItem, CustomEnchantingBehaviorItem {

	public IceBowItem(Properties props) {
		super(props);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		AtomicBoolean badEnchant = new AtomicBoolean();
		EnchantmentHelper.getEnchantments(book).forEach((enchantment, integer) -> {
			if (Objects.equals(Enchantments.FLAMING_ARROWS, enchantment)) {
				badEnchant.set(true);
			}
		});

		return !badEnchant.get();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !Enchantments.FLAMING_ARROWS.equals(enchantment) && CustomEnchantingBehaviorItem.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow) {
		return new IceArrow(arrow.level(), arrow.getOwner());
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairWith) {
		return repairWith.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().is(BlockTags.ICE) || super.isValidRepairItem(toRepair, repairWith);
	}
}