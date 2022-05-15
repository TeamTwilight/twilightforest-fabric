package twilightforest.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;

import java.util.Collections;
import java.util.function.Predicate;

public class TFItemStackUtils {

	public static int damage = 0;

	@Deprecated
	public static boolean consumeInventoryItem(Player living, final Predicate<ItemStack> matcher, final int count) {
		TwilightForestMod.LOGGER.warn("consumeInventoryItem accessed! Forge requires the player to be alive before we can access this cap. This cap is most likely being accessed for an Afterdeath Charm!");

		PlayerInventoryStorage inv = PlayerInventoryStorage.of(living);
		int innerCount = count;
		boolean consumedSome = false;

		for (int i = 0; i < inv.getSlots().size() && innerCount > 0; i++) {
			ItemStack stack = new ItemStack(inv.getSlot(i).getResource().getItem(), (int) inv.getSlot(i).getAmount());
			if (matcher.test(stack)) {
//				ItemStack consumed = inv.extract(i, innerCount, false); TODO: PORT
//				innerCount -= consumed.getCount();
//				consumedSome = true;
			}
		}

		return consumedSome;
	}

	public static boolean consumeInventoryItem(final Player player, final Item item) {
		return consumeInventoryItem(player.getInventory().armor, item) || consumeInventoryItem(player.getInventory().items, item) || consumeInventoryItem(player.getInventory().offhand, item);
	}

	public static boolean consumeInventoryItem(final NonNullList<ItemStack> stacks, final Item item) {
		for (ItemStack stack : stacks) {
			if (stack.getItem() == item) {
				stack.shrink(1);
				CompoundTag nbt = stack.getOrCreateTag();
				if (nbt.contains("BlockStateTag")) {
					CompoundTag damageNbt = nbt.getCompound("BlockStateTag");
					if (damageNbt.contains("damage")) {
						damage = damageNbt.getInt("damage");
					}
				}
				return true;
			}
		}

		return false;
	}

	public static NonNullList<ItemStack> sortArmorForCasket(Player player) {
		NonNullList<ItemStack> armor = player.getInventory().armor;
		Collections.reverse(armor);
		return armor;
	}

	public static NonNullList<ItemStack> sortInvForCasket(Player player) {
		NonNullList<ItemStack> inv = player.getInventory().items;
		NonNullList<ItemStack> sorted = NonNullList.create();
		//hotbar at the bottom
		sorted.addAll(inv.subList(9, 36));
		sorted.addAll(inv.subList(0, 9));

		return sorted;
	}

	public static NonNullList<ItemStack> splitToSize(ItemStack stack) {

		NonNullList<ItemStack> result = NonNullList.create();

		int size = stack.getMaxStackSize();

		while (!stack.isEmpty()) {
			result.add(stack.split(size));
		}

		return result;
	}

	public static boolean hasToolMaterial(ItemStack stack, Tier tier) {

		Item item = stack.getItem();

		// see TileEntityFurnace.getItemBurnTime
		if (item instanceof TieredItem tieredItem && tier.equals(tieredItem.getTier())) {
			return true;
		}
		if (item instanceof SwordItem sword && tier.equals(sword.getTier())) {
			return true;
		}
		if (item instanceof HoeItem hoe && tier.equals(hoe.getTier())) {
			return true;
		}

		return false;
	}

	public static void clearInfoTag(ItemStack stack, String key) {
		CompoundTag nbt = stack.getTag();
		if (nbt != null) {
			nbt.remove(key);
			if (nbt.isEmpty()) {
				stack.setTag(null);
			}
		}
	}
}
