package twilightforest.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.init.TFDataComponents;

public class GreaterFlaskItem extends BrittleFlaskItem {

	public GreaterFlaskItem(Item.Properties properties) {
		super(properties, net.minecraft.world.item.Items.GLASS_BOTTLE, true);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack itemstack = super.getDefaultInstance();
		itemstack.set(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY_UNBREAKABLE);
		return itemstack;
	}
}
