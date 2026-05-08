package twilightforest.asmhooks;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;

public class PlayerHooks {

	public static float getFoodExhaustion(float original, Player player) {
		ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
		Float divisor = chestStack.get(TFDataComponents.EFFICIENT_EATER);
		if (!TravellersModifiersManager.isModifierActive(player, chestStack, TravellersModifiersManager.EFFICIENT_EATER_MODIFIER) || divisor == null) {
			return original;
		}
		return original * (1.0F / divisor);
	}

	public static void straightAheadNullify(AbstractClientPlayer player) {
		AttributeInstance attributeInstance = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null) {
			return;
		}
		AttributeModifier modifier = attributeInstance.getModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION);
		double multiplier = modifier == null ? 1.0D : modifier.amount() + 1.0D;
		TFDataAttachments.set(player, TFDataAttachments.TEMPORARY_SAVED_STRAIGHT_AHEAD, multiplier);
		attributeInstance.removeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION);
	}

	public static void straightAheadRestore(AbstractClientPlayer player) {
		if (!(player instanceof LocalPlayer)) {
			return;
		}
		AttributeInstance attributeInstance = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null) {
			return;
		}
		double multiplier = TFDataAttachments.get(player, TFDataAttachments.TEMPORARY_SAVED_STRAIGHT_AHEAD);
		attributeInstance.addTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}
}
