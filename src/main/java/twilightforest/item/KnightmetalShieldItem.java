package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.item.ToolActionCheckingItem;
import io.github.fabricators_of_create.porting_lib.util.ToolAction;
import io.github.fabricators_of_create.porting_lib.util.ToolActions;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;
import java.util.List;

import twilightforest.data.tags.ItemTagGenerator;

public class KnightmetalShieldItem extends ShieldItem implements ToolActionCheckingItem {

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

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        // TODO: PORT
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction) /*|| super.canPerformAction(stack, toolAction)*/;
    }
}
