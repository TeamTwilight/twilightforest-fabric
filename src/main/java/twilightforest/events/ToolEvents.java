package twilightforest.events;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;
import twilightforest.item.EnderBowItem;
import twilightforest.item.MinotaurAxeItem;

import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ToolEvents {

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	@SubscribeEvent
	public static void onEnderBowHit(ProjectileImpactEvent evt) {
		Projectile arrow = evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
				&& evt.getRayTraceResult() instanceof EntityHitResult result
				&& result.getEntity() instanceof LivingEntity living
				&& arrow.getOwner() != result.getEntity()) {

			if (arrow.getPersistentData().contains(EnderBowItem.KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.invulnerableTime = 40;
				player.getLevel().broadcastEntityEvent(player, (byte) 46);
				if (living.isPassenger() && living.getVehicle() != null) {
					player.startRiding(living.getVehicle(), true);
					living.stopRiding();
				}
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.setYRot(sourceYaw);
				living.setXRot(sourcePitch);
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.getLevel().broadcastEntityEvent(player, (byte) 46);
				if (playerVehicle != null) {
					living.startRiding(playerVehicle, true);
					player.stopRiding();
				}
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public static void fieryToolSetFire(LivingAttackEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity living && (living.getMainHandItem().is(TFItems.FIERY_SWORD.get()) || living.getMainHandItem().is(TFItems.FIERY_PICKAXE.get())) && !event.getEntityLiving().fireImmune()) {
			event.getEntityLiving().setSecondsOnFire(1);
		}
	}

	@SubscribeEvent
	public static void onKnightmetalToolDamage(LivingHurtEvent event) {
		LivingEntity target = event.getEntityLiving();

		if (!target.getLevel().isClientSide() && event.getSource().getDirectEntity() instanceof LivingEntity living) {
			ItemStack weapon = living.getMainHandItem();

			if (!weapon.isEmpty()) {
				if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE.get()) || weapon.is(TFItems.KNIGHTMETAL_SWORD.get()))) {
					if (target.getArmorCoverPercentage() > 0) {
						int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
						event.setAmount(event.getAmount() + moreBonus);
					} else {
						event.setAmount(event.getAmount() + KNIGHTMETAL_BONUS_DAMAGE);
					}
					// enchantment attack sparkles
					((ServerLevel) target.getLevel()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE.get())) {
					event.setAmount(event.getAmount() + KNIGHTMETAL_BONUS_DAMAGE);
					// enchantment attack sparkles
					((ServerLevel) target.getLevel()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onMinotaurAxeCharge(LivingHurtEvent event) {
		LivingEntity target = event.getEntityLiving();
		Entity source = event.getSource().getDirectEntity();
		if (!target.getLevel().isClientSide() && source instanceof LivingEntity living && source.isSprinting() && (event.getSource().getMsgId().equals("player") || event.getSource().getMsgId().equals("mob"))) {
			ItemStack weapon = living.getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
				event.setAmount(event.getAmount() + MINOTAUR_AXE_BONUS_DAMAGE);
				// enchantment attack sparkles
				((ServerLevel) target.getLevel()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
			}
		}
	}
}
