package twilightforest.asm.hooks.event;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFItems;
import twilightforest.item.MinotaurAxeItem;

public final class ToolEventHooks {
	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	public static float doKnightmetalToolLogic(LivingEntity target, DamageSource source, float originalDamage, float newDamage) {
		if (target.level().isClientSide() || !(source.getDirectEntity() instanceof LivingEntity living)) {
			return newDamage;
		}

		ItemStack weapon = living.getMainHandItem();
		if (target.getArmorValue() > 0) {
			if (!weapon.is(TFItems.KNIGHTMETAL_PICKAXE) && !weapon.is(TFItems.KNIGHTMETAL_SWORD)) {
				return newDamage;
			}

			int bonusDamage;
			if (target.getArmorCoverPercentage() > 0) {
				bonusDamage = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
			} else {
				bonusDamage = KNIGHTMETAL_BONUS_DAMAGE;
			}

			((ServerLevel) target.level()).getChunkSource().sendToTrackingPlayers(target, new ClientboundAnimatePacket(target, 5));
			return newDamage + bonusDamage;
		}

		if (weapon.is(TFItems.KNIGHTMETAL_AXE)) {
			((ServerLevel) target.level()).getChunkSource().sendToTrackingPlayers(target, new ClientboundAnimatePacket(target, 5));
			return originalDamage + KNIGHTMETAL_BONUS_DAMAGE;
		}

		return newDamage;
	}

	public static float addExtraAxeChargingDamage(LivingEntity target, DamageSource source, float newDamage) {
		if (target.level().isClientSide() || !(source.getDirectEntity() instanceof LivingEntity attacker) || !attacker.isSprinting()) {
			return newDamage;
		}

		ItemStack weapon = attacker.getMainHandItem();
		if (!(weapon.getItem() instanceof MinotaurAxeItem)) {
			return newDamage;
		}

		((ServerLevel) target.level()).getChunkSource().sendToTrackingPlayers(target, new ClientboundAnimatePacket(target, 5));
		return newDamage + MINOTAUR_AXE_BONUS_DAMAGE;
	}
}