package twilightforest.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Stub for Curios compatibility. Returns false/empty for all operations when Curios is not installed.
 */
public class CuriosCompat {

	public static boolean findAndConsumeCurio(Item item, Player player) {
		return false;
	}

	public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return false;
	}

	public static boolean isCurioEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return false;
	}
}