package twilightforest.item;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;

public class IceSwordItem extends Item {

	public IceSwordItem(Properties properties) {
		super(properties);
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		ApplyFrostedEffect.doChillAuraEffect(target, 200, 2, true);
		ParticlePacket particlePacket = new ParticlePacket();
		for (int i = 0; i < 20; i++) {
			particlePacket.queueParticle(TFParticleType.SNOW, false, false,
				target.getX() + (target.getRandom().nextGaussian() * target.getBbWidth() * 0.5),
				target.getY() + target.getBbHeight() * 0.5F + (target.getRandom().nextGaussian() * target.getBbHeight() * 0.5),
				target.getZ() + (target.getRandom().nextGaussian() * target.getBbWidth() * 0.5),
				0, 0, 0);
		}
		for (ServerPlayer player : PlayerLookup.tracking(target)) {
			ServerPlayNetworking.send(player, particlePacket);
		}
	}
}