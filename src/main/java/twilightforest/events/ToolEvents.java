package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.ProjectileImpactCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFItems;
import twilightforest.item.*;

public class ToolEvents {

	public static void init() {
		LivingEntityEvents.HURT.register(ToolEvents::onMinotaurAxeCharge);
		LivingEntityEvents.HURT.register(ToolEvents::onKnightmetalToolDamage);
		AttackEntityCallback.EVENT.register(ToolEvents::fieryToolSetFire);
		ProjectileImpactCallback.EVENT.register(ToolEvents::onEnderBowHit);
		BlockEvents.BLOCK_BREAK.register(ToolEvents::damageToolsExtra);
		OreMagnetItem.buildOreMagnetCache();

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> Storage.empty(), TFBlockEntities.KEEPSAKE_CASKET.get());
	}

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	public static InteractionHand INTERACTION_HAND;

	public static boolean onEnderBowHit(Projectile arrow, HitResult hitResult) {
		if (arrow.getOwner() instanceof Player player
				&& hitResult instanceof EntityHitResult result
				&& result.getEntity() instanceof LivingEntity living
				&& arrow.getOwner() != result.getEntity()) {

			if (arrow.getExtraCustomData().contains(EnderBowItem.KEY)) {
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
		return false;
	}

	public static InteractionResult fieryToolSetFire(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
		if (entity instanceof LivingEntity living && (living.getMainHandItem().is(TFItems.FIERY_SWORD.get()) || living.getMainHandItem().is(TFItems.FIERY_PICKAXE.get())) && !living.fireImmune()) {
			living.setSecondsOnFire(1);
		}
		return InteractionResult.PASS;
	}

	public static float onKnightmetalToolDamage(DamageSource source, LivingEntity target, float amount) {
		float newAmount = amount;

		if (!target.getLevel().isClientSide() && source.getDirectEntity() instanceof LivingEntity living) {
			ItemStack weapon = living.getMainHandItem();

			if (!weapon.isEmpty()) {
				if (target.getArmorValue() > 0 && (weapon.is(TFItems.KNIGHTMETAL_PICKAXE.get()) || weapon.is(TFItems.KNIGHTMETAL_SWORD.get()))) {
					if (target.getArmorCoverPercentage() > 0) {
						int moreBonus = (int) (KNIGHTMETAL_BONUS_DAMAGE * target.getArmorCoverPercentage());
						newAmount = newAmount + moreBonus;
					} else {
						newAmount = newAmount + KNIGHTMETAL_BONUS_DAMAGE;
					}
					// enchantment attack sparkles
					((ServerLevel) target.getLevel()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				} else if (target.getArmorValue() == 0 && weapon.is(TFItems.KNIGHTMETAL_AXE.get())) {
					newAmount = newAmount + KNIGHTMETAL_BONUS_DAMAGE;
					// enchantment attack sparkles
					((ServerLevel) target.getLevel()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				}
			}
		}
		return newAmount;
	}

	public static float onMinotaurAxeCharge(DamageSource damageSource, LivingEntity target, float amount) {
		Entity source = damageSource.getDirectEntity();
		if (!target.getLevel().isClientSide() && source instanceof LivingEntity living && source.isSprinting() && (damageSource.getMsgId().equals("player") || damageSource.getMsgId().equals("mob"))) {
			ItemStack weapon = living.getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof MinotaurAxeItem) {
				float newAmount = amount + MINOTAUR_AXE_BONUS_DAMAGE;
				// enchantment attack sparkles
				((ServerLevel) target.getLevel()).getChunkSource().broadcastAndSend(target, new ClientboundAnimatePacket(target, 5));
				return newAmount;
			}
		}
		return amount;
	}


	public static void damageToolsExtra(BlockEvents.BreakEvent event) {
		ItemStack stack = event.getPlayer().getMainHandItem();
		if (event.getState().is(BlockTagGenerator.MAZESTONE) || event.getState().is(BlockTagGenerator.CASTLE_BLOCKS)) {
			if (stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, event.getPlayer(), (user) -> user.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			}
		}
	}
// 	TODO: PORT (I'm too lazy to right now)
//	@SubscribeEvent
//	public static void onEntityInteract(PlayerInteractEvent event) {
//		if (event instanceof PlayerInteractEvent.EntityInteractSpecific entityInteractSpecific) {
//			checkEntityTooFar(entityInteractSpecific, entityInteractSpecific.getTarget(), entityInteractSpecific.getEntity(), entityInteractSpecific.getHand());
//		} else if (event instanceof PlayerInteractEvent.EntityInteract entityInteract) {
//			checkEntityTooFar(entityInteract, entityInteract.getTarget(), entityInteract.getEntity(), entityInteract.getHand());
//		} else if (event instanceof PlayerInteractEvent.RightClickBlock rightClickBlock) {
//			checkBlockTooFar(event, rightClickBlock.getEntity(), rightClickBlock.getHand());
//		} else if (event instanceof PlayerInteractEvent.RightClickItem rightClickItem) {
//			INTERACTION_HAND = rightClickItem.getHand();
//		}
//	}
//
//	private static void checkEntityTooFar(PlayerInteractEvent event, Entity target, Player player, InteractionHand hand) {
//		if (!event.isCanceled()) {
//			ItemStack heldStack = player.getItemInHand(hand);
//			if (hasGiantItemInOneHand(player) && !(heldStack.getItem() instanceof GiantItem) && hand == InteractionHand.OFF_HAND) {
//				UUID uuidForOppositeHand = GiantItem.GIANT_RANGE_MODIFIER;
//				AttributeInstance attackRange = player.getAttribute(ReachEntityAttributes.ATTACK_RANGE);
//				if (attackRange != null) {
//					AttributeModifier giantModifier = attackRange.getModifier(uuidForOppositeHand);
//					if (giantModifier != null) {
//						attackRange.removeModifier(giantModifier);
//						double range = player.getAttributeValue(ReachEntityAttributes.ATTACK_RANGE);
//						double trueReach = range == 0 ? 0 : range + (player.isCreative() ? 3 : 0); // Copied from IForgePlayer#getAttackRange().
//						boolean tooFar = !player.isCloseEnough(target, trueReach);
//						attackRange.addTransientModifier(giantModifier);
//						event.setCanceled(tooFar);
//					}
//				}
//			}
//		}
//	}
//
//	private static void checkBlockTooFar(PlayerInteractEvent event, Player player, InteractionHand hand) {
//		if (!event.isCanceled()) {
//			ItemStack heldStack = player.getItemInHand(hand);
//			if (hasGiantItemInOneHand(player) && !(heldStack.getItem() instanceof GiantItem) && hand == InteractionHand.OFF_HAND) {
//				UUID uuidForOppositeHand = GiantItem.GIANT_REACH_MODIFIER;
//				AttributeInstance reachDistance = player.getAttribute(ReachEntityAttributes.REACH);
//				if (reachDistance != null) {
//					AttributeModifier giantModifier = reachDistance.getModifier(uuidForOppositeHand);
//					if (giantModifier != null) {
//						reachDistance.removeModifier(giantModifier);
//						double reach = player.getAttributeValue(ReachEntityAttributes.REACH);
//						double trueReach = reach == 0 ? 0 : reach + (player.isCreative() ? 0.5 : 0); // Copied from IForgePlayer#getReachDistance().
//						boolean tooFar = player.pick(trueReach, 0.0F, false).getType() != HitResult.Type.BLOCK;
//						reachDistance.addTransientModifier(giantModifier);
//						event.setCanceled(tooFar);
//					}
//				}
//			}
//		}
//	}

	public static boolean hasGiantItemInOneHand(Player player) {
		ItemStack mainHandStack = player.getMainHandItem();
		ItemStack offHandStack = player.getOffhandItem();
		return (mainHandStack.getItem() instanceof GiantItem && !(offHandStack.getItem() instanceof GiantItem));
	}
}
