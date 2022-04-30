package twilightforest.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

import javax.annotation.Nullable;

public class FurnaceFuelItem extends BlockItem {
    public FurnaceFuelItem(Block block, Properties properties, int burn) {
        super(block, properties);
        FuelRegistry.INSTANCE.add(this, burn);
    }
}
