package twilightforest.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import twilightforest.capabilities.CapabilityList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class FortificationWandItem extends Item {

	protected FortificationWandItem(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(stack);
		}

		if (!world.isClientSide) {
			CapabilityList.SHIELD_CAPABILITY_COMPONENT_KEY.maybeGet(player).ifPresent(cap -> {
				cap.replenishShields();
				stack.hurt(1, world.random, null);
			});
		}

		if (!player.isCreative())
			player.getCooldowns().addCooldown(this, 1200);

		return InteractionResultHolder.success(stack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getDamageValue()));
	}
}
