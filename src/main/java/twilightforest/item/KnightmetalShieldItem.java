package twilightforest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import io.github.fabricators_of_create.porting_lib.client_extensions.IClientItemExtensions;
import io.github.fabricators_of_create.porting_lib.tool.ItemAbilities;
import io.github.fabricators_of_create.porting_lib.tool.ItemAbility;
import io.github.fabricators_of_create.porting_lib.tool.extensions.VanillaItemAbilityItem;
import twilightforest.client.ISTER;
import twilightforest.data.tags.ItemTagGenerator;

import java.util.List;
import java.util.function.Consumer;

public class KnightmetalShieldItem extends ShieldItem implements VanillaItemAbilityItem {

	public KnightmetalShieldItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.is(ItemTagGenerator.KNIGHTMETAL_INGOTS) || !repair.is(ItemTags.PLANKS) && super.isValidRepairItem(toRepair, repair);
	}

	@Override
	public boolean port_lib$canPerformAction(ItemStack stack, ItemAbility toolAction) {
		return ItemAbilities.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
	}
}