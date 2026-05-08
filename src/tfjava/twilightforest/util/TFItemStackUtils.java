package twilightforest.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class TFItemStackUtils {
	private TFItemStackUtils() {
	}

	public static boolean hasInfoTag(ItemStack stack, String key) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		return customData != null && customData.contains(key);
	}

	public static void addInfoTag(ItemStack stack, String key) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		CompoundTag tag = customData == null ? new CompoundTag() : customData.copyTag();
		tag.putBoolean(key, true);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}

	public static void clearInfoTag(ItemStack stack, String key) {
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return;
		}
		CompoundTag tag = customData.copyTag();
		tag.remove(key);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}

	public static void giveOrDrop(net.minecraft.world.entity.player.Player player, ItemStack stack) {
		if (!player.getInventory().add(stack)) {
			player.drop(stack, false);
		}
	}

	public static void hurtButDontBreak(ItemStack stack, int amount, ServerLevel level, LivingEntity entity) {
		if (stack.getMaxDamage() <= 0 || stack.getDamageValue() >= stack.getMaxDamage() - 1) {
			return;
		}
		if (entity instanceof ServerPlayer serverPlayer) {
			stack.hurtAndBreak(amount, level, serverPlayer, item -> entity.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
		} else {
			stack.setDamageValue(Math.min(stack.getMaxDamage() - 1, stack.getDamageValue() + amount));
		}
		if (stack.getDamageValue() >= stack.getMaxDamage()) {
			stack.setDamageValue(stack.getMaxDamage() - 1);
		}
	}
}
