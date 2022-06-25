package twilightforest.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;

public class TrophyItem extends StandingAndWallBlockItem {

	public TrophyItem(Block floorBlock, Block wallBlock, FabricItemSettings properties) {
		super(floorBlock, wallBlock, properties.equipmentSlot(stack -> EquipmentSlot.HEAD));
	}
}