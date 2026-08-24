package twilightforest.asm.hooks;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItems;
import twilightforest.item.EnderBowItem;
import twilightforest.item.MinotaurAxeItem;

// TODO [Fabric] : Integrate these hooks into mixins once the project compiles and can be tested
public final class ToolEventHooks {
	/*
	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	private void onEnderBowHit(ProjectileImpactEvent evt) {
		Projectile arrow = evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
			&& evt.getRayTraceResult() instanceof EntityHitResult result
			&& result.getEntity() instanceof LivingEntity living
			&& arrow.getOwner() != result.getEntity() && !result.getEntity().is(Tags.EntityTypes.BOSSES)) {

			if (arrow.getPersistentData().contains(EnderBowItem.KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.invulnerableTime = 40;
				player.level().broadcastEntityEvent(player, (byte) 46);
				if (living.isPassenger() && living.getVehicle() != null) {
					player.startRiding(living.getVehicle(), true);
					living.stopRiding();
				}
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.setYRot(sourceYaw);
				living.setXRot(sourcePitch);
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.level().broadcastEntityEvent(player, (byte) 46);
				if (playerVehicle != null) {
					living.startRiding(playerVehicle, true);
					player.stopRiding();
				}
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	private void doKnightmetalToolLogic(LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity target = event.getEntity();

			DamageContainer container = event.getContainer();
			if (!target.level().isClientSide() && container.getSource().getDirectEntity() instanceof LivingEntity living) {
				ItemStack weapon = living.getMainHandItem();

				if (!weapon.isEmpty()) {
					if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE.get()) || weapon.is(TFItems.KNIGHTMETAL_SWORD.get()))) {
						if (target.getArmorCoverPercentage() > 0) {
							int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
							container.setNewDamage(container.getNewDamage() + moreBonus);
						} else {
							container.setNewDamage(container.getNewDamage() + KNIGHTMETAL_BONUS_DAMAGE);
						}
						// enchantment attack sparkles
						((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
					} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE.get())) {
						container.setNewDamage(container.getOriginalDamage() + KNIGHTMETAL_BONUS_DAMAGE);
						// enchantment attack sparkles
						((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
					}
				}
			}
		}
	}

	private void addExtraAxeChargingDamage(LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity target = event.getEntity();
			DamageContainer container = event.getContainer();
			if (!target.level().isClientSide() && container.getSource().getDirectEntity() instanceof LivingEntity living && living.isSprinting()) {
				ItemStack weapon = living.getMainHandItem();
				if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
					container.setNewDamage(container.getNewDamage() + MINOTAUR_AXE_BONUS_DAMAGE);
					// enchantment attack sparkles
					((ServerLevel) target.level()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
	}
	 */
}