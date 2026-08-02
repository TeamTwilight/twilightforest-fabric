package twilightforest.compat.trinkets;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Stub for Trinkets compatibility. Returns false/empty for all operations when Trinkets is not installed.
 */
public class TrinketsCompat {

	public static boolean findAndConsumeTrinket(Item item, Player player) {
		return false;
	}

	public static boolean isTrinketEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return false;
	}

	public static boolean isTrinketEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return false;
	}
}