package twilightforest.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class WearableItem extends BlockItem {
    public WearableItem(Block block, FabricItemSettings props) {
        super(block, props.equipmentSlot(stack -> EquipmentSlot.HEAD));
    }
}
