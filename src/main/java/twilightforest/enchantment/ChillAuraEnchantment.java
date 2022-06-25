package twilightforest.enchantment;

import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFMobEffects;

public class ChillAuraEnchantment extends LootOnlyEnchantment {

	public ChillAuraEnchantment(Rarity rarity) {
		super(rarity, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST,
				EquipmentSlot.LEGS, EquipmentSlot.FEET});
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		return stack.getItem() instanceof ArmorItem || super.canEnchant(stack);
	}

	@Override
	public int getMinCost(int level) {
		return 5 + (level - 1) * 9;
	}

	@Override
	public int getMaxCost(int level) {
		return this.getMinCost(level) + 15;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public void doPostHurt(LivingEntity user, Entity attacker, int level) {
		RandomSource random = user.getRandom();
		if (attacker != null && shouldHit(level, random)) {
			if (attacker instanceof LivingEntity entity) {
				if (!entity.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.FREEZE_IMMUNE_WEARABLES) &&
						!entity.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.FREEZE_IMMUNE_WEARABLES) &&
						!entity.getItemBySlot(EquipmentSlot.LEGS).is(ItemTags.FREEZE_IMMUNE_WEARABLES) &&
						!entity.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FREEZE_IMMUNE_WEARABLES)) {
					if (entity instanceof Player player && !player.isCreative()) {
						entity.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), 200, level - 1));
					}
				}
			}
		}
	}

	public static boolean shouldHit(int level, RandomSource pRnd) {
		if (level <= 0) {
			return false;
		} else {
			return pRnd.nextFloat() < 0.15F * (float) level;
		}
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		return super.checkCompatibility(other) && other != TFEnchantments.FIRE_REACT.get() && other != Enchantments.THORNS;
	}
}
