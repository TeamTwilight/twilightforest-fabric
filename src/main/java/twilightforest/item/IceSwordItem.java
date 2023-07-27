package twilightforest.item;

import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.enchantment.ChillAuraEnchantment;
import twilightforest.init.TFParticleType;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class IceSwordItem extends SwordItem implements CustomEnchantingBehaviorItem {

	public IceSwordItem(Tier toolMaterial, Properties properties) {
		super(toolMaterial, 3, -2.4F, properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		AtomicBoolean badEnchant = new AtomicBoolean();
		EnchantmentHelper.getEnchantments(book).forEach((enchantment, integer) -> {
			if (Objects.equals(Enchantments.FIRE_ASPECT, enchantment)) {
				badEnchant.set(true);
			}
		});

		return !badEnchant.get();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !Enchantments.FIRE_ASPECT.equals(enchantment) && CustomEnchantingBehaviorItem.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		if (result) {
			ChillAuraEnchantment.doChillAuraEffect(target, 200, 2, true);
			for (int i = 0; i < 20; i++) {
				((ServerLevel) target.level()).sendParticles(TFParticleType.SNOW.get(), target.getX(), target.getY() + target.getBbHeight() * 0.5F, target.getZ(), 1, target.getBbWidth() * 0.5, target.getBbHeight() * 0.5, target.getBbWidth() * 0.5, 0);
			}
		}

		return result;
	}
}