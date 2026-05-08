package twilightforest.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFItems;
import twilightforest.item.MazebreakerPickItem;
import twilightforest.item.MinotaurAxeItem;
import twilightforest.item.OreMagnetItem;

public final class ToolEvents {
	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	private ToolEvents() {
	}

	public static void bootstrap() {
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			ItemStack stack = player.getMainHandItem();
			if (state.is(BlockTagGenerator.MAZEBREAKER_ACCELERATED) && stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, player, EquipmentSlot.MAINHAND);
			}
		});
		ServerLifecycleEvents.SERVER_STARTED.register(server -> OreMagnetItem.refreshOreCacheFromTags());
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
			if (success) {
				OreMagnetItem.refreshOreCacheFromTags();
			} else {
				OreMagnetItem.markOreCacheDirty();
			}
		});
	}

	public static float modifyIncomingDamage(LivingEntity target, DamageSource source, float amount) {
		if (target.level().isClientSide() || !(source.getDirectEntity() instanceof LivingEntity attacker)) {
			return amount;
		}

		ItemStack weapon = attacker.getMainHandItem();
		if (weapon.isEmpty()) {
			return amount;
		}

		float newAmount = amount;
		boolean sparkles = false;
		if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE.get()) || weapon.is(TFItems.KNIGHTMETAL_SWORD.get()))) {
			float armorCover = target.getArmorCoverPercentage();
			newAmount += armorCover > 0.0F ? (int) (KNIGHTMETAL_BONUS_DAMAGE * armorCover) : KNIGHTMETAL_BONUS_DAMAGE;
			sparkles = true;
		} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE.get())) {
			newAmount += KNIGHTMETAL_BONUS_DAMAGE;
			sparkles = true;
		}

		if (attacker.isSprinting() && weapon.getItem() instanceof MinotaurAxeItem) {
			newAmount += MINOTAUR_AXE_BONUS_DAMAGE;
			sparkles = true;
		}

		if (sparkles && target.level() instanceof ServerLevel serverLevel) {
			serverLevel.getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
		}
		return newAmount;
	}

	public static boolean shouldBlockEffect(LivingEntity entity, MobEffectInstance effectInstance) {
		return effectInstance.is(MobEffects.DIG_SLOWDOWN) && entity.isHolding(TFItems.POCKET_WATCH.get());
	}
}
