package twilightforest.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import twilightforest.data.tags.ItemTagGenerator;

import javax.annotation.Nullable;
import java.util.List;

public class KnightmetalShieldItem extends ShieldItem {

    public KnightmetalShieldItem(Properties props) {
    	super(props);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(ItemTagGenerator.KNIGHTMETAL_INGOTS) || !repair.is(ItemTags.PLANKS) && super.isValidRepairItem(toRepair, repair);
    }
}
