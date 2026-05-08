package twilightforest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.init.TFParticleType;

public class IceSwordItem extends SwordItem {

	public IceSwordItem(Tier toolMaterial, Properties properties) {
		super(toolMaterial, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (super.hurtEnemy(stack, target, attacker)) {
			ApplyFrostedEffect.doChillAuraEffect(target, 200, 2, true);

			if (target.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
				for (int i = 0; i < 20; i++) {
					serverLevel.sendParticles(TFParticleType.SNOW,
						target.getX() + (target.getRandom().nextGaussian() * target.getBbWidth() * 0.5),
						target.getY() + target.getBbHeight() * 0.5F + (target.getRandom().nextGaussian() * target.getBbHeight() * 0.5),
						target.getZ() + (target.getRandom().nextGaussian() * target.getBbWidth() * 0.5),
						1, 0, 0, 0, 0);
				}
			}

			return true;
		}
		return false;
	}
}
