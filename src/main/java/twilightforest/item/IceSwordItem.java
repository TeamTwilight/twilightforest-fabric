package twilightforest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.effect.MobEffectInstance;
import twilightforest.client.particle.TFParticleType;
import twilightforest.potions.TFPotions;

import net.minecraft.world.item.Item.Properties;

public class IceSwordItem extends SwordItem {

	public IceSwordItem(Tier toolMaterial, Properties props) {
		super(toolMaterial, 3, -2.4F, props);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		if (result) {
			if (!target.level.isClientSide) {
				target.addEffect(new MobEffectInstance(TFPotions.frosty, 20 * 10, 2));
			} else {
				target.level.addParticle(TFParticleType.SNOW, target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), target.getBbWidth() * 0.5, target.getBbHeight() * 0.5, target.getBbWidth() * 0.5);
			}
		}

		return result;
	}
}
