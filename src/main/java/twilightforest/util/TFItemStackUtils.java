package twilightforest.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import twilightforest.TwilightForestMod;

import java.util.Collections;
import java.util.function.Predicate;

public class TFItemStackUtils {

	public static int damage = 0;

	//TODO: PORT IF NEEDED?
//	@Deprecated
//	public static boolean consumeInventoryItem(LivingEntity living, final Predicate<ItemStack> matcher, final int count) {
//		TwilightForestMod.LOGGER.warn("consumeInventoryItem accessed! Forge requires the player to be alive before we can access this cap. This cap is most likely being accessed for an Afterdeath Charm!");
//
//		return living.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(inv -> {
//			int innerCount = count;
//			boolean consumedSome = false;
//
//			for (int i = 0; i < inv.getSlots() && innerCount > 0; i++) {
//				ItemStack stack = inv.getStackInSlot(i);
//				if (matcher.test(stack)) {
//					ItemStack consumed = inv.extractItem(i, innerCount, false);
//					innerCount -= consumed.getCount();
//					consumedSome = true;
//				}
//			}
//
//			//TODO: Baubles is dead, replace with curios
//			/*if (TFCompat.BAUBLES.isActivated() && living instanceof EntityPlayer) {
//				consumedSome |= Baubles.consumeInventoryItem((EntityPlayer) living, matcher, count);
//			}*/
//
//			return consumedSome;
//		}).orElse(false);
//	}

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

	//TODO: IE Compat
//	public static boolean hasToolMaterial(ItemStack stack, IItemTier material) {
//
//		Item item = stack.getItem();
//
//		// see TileEntityFurnace.getItemBurnTime
//		if (item instanceof ToolItem && material.toString().equals(((ToolItem)item).getToolMaterialName())) {
//			return true;
//		}
//		if (item instanceof SwordItem && material.toString().equals(((SwordItem)item).getToolMaterialName())) {
//			return true;
//		}
//		if (item instanceof HoeItem && material.toString().equals(((HoeItem)item).getMaterialName())) {
//			return true;
//		}
//
//		return false;
//	}

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
