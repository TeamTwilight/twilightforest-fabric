package twilightforest.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.Nullable;
import java.util.Random;

public class FireReactEnchantment extends LootOnlyEnchantment {

	public FireReactEnchantment(Rarity rarity) {
		super(rarity, EnchantmentCategory.ARMOR, new EquipmentSlot[]{
				EquipmentSlot.HEAD, EquipmentSlot.CHEST,
				EquipmentSlot.LEGS, EquipmentSlot.FEET
		});
	}

	@Override
	public boolean canEnchant(ItemStack pStack) {
		return pStack.getItem() instanceof ArmorItem || super.canEnchant(pStack);
	}

	@Override
	public int getMinCost(int pEnchantmentLevel) {
		return 5 + (pEnchantmentLevel - 1) * 9;
	}

	@Override
	public int getMaxCost(int pEnchantmentLevel) {
		return this.getMinCost(pEnchantmentLevel) + 15;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public void doPostHurt(LivingEntity user, @Nullable Entity attacker, int level) {
		Random random = user.getRandom();
		if (attacker != null && shouldHit(level, random, attacker)) {
			attacker.setSecondsOnFire(2 + (random.nextInt(level) * 3));
		}
	}

	public static boolean shouldHit(int level, Random pRnd, Entity attacker) {
		if (level <= 0 || attacker.fireImmune()) {
			return false;
		} else {
			return pRnd.nextFloat() < 0.15F * (float)level;
		}
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		return super.checkCompatibility(other) && other != TFEnchantments.CHILL_AURA.get() && other != Enchantments.THORNS;
	}
}
