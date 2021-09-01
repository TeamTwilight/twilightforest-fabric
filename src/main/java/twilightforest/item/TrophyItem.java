package twilightforest.item;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TrophyItem extends StandingAndWallBlockItem {

	public TrophyItem(Block floorBlockIn, Block wallBlockIn, Item.Properties builder) {
		super(floorBlockIn, wallBlockIn, builder);
	}

//	@Override
//	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
//		return armorType == EquipmentSlot.HEAD;
//	}
//
//	@Override
//	@Nullable
//	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
//		return EquipmentSlot.HEAD;
//	}
}
