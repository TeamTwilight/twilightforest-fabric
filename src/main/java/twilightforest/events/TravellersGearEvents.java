package twilightforest.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;

public final class TravellersGearEvents {
	private static boolean bootstrapped;

	private TravellersGearEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		ServerTickEvents.END_WORLD_TICK.register(level -> {
			for (net.minecraft.world.entity.Entity entity : level.getAllEntities()) {
				if (entity instanceof LivingEntity living) {
					updateMovementModifiers(living);
					updateOtherModifiers(living);
				}
			}
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (net.minecraft.server.level.ServerPlayer player : server.getPlayerList().getPlayers()) {
				tickPlayerModifiers(player);
			}
		});

		UseItemCallback.EVENT.register((player, level, hand) -> {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.has(DataComponents.FOOD) || level.isClientSide()) {
				return InteractionResultHolder.pass(stack);
			}
			if (TravellersModifiersManager.isEfficientEaterActive(player)) {
				player.getFoodData().eat(1, 0.4F);
			}
			return InteractionResultHolder.pass(stack);
		});
	}

	private static void updateMovementModifiers(LivingEntity living) {
		if (living instanceof Player player) {
			Boolean hasDoubleJump = null;
			if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
				hasDoubleJump = false;
			} else if (player.onGround() || player.isInLiquid() || player.onClimbable()) {
				hasDoubleJump = true;
			}

			if (hasDoubleJump != null && hasDoubleJump != TFDataAttachments.get(player, TFDataAttachments.HAS_DOUBLE_JUMP)) {
				TFDataAttachments.set(player, TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
				TFDataAttachments.set(player, TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
				AttributeInstance instance = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);
				if (instance != null) {
					instance.removeModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);
				}
			}

			if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.GRADUAL_GLIDE_MODIFIER)
				&& TFDataAttachments.get(player, TFDataAttachments.IS_GRADUALLY_GLIDING)) {
				TFDataAttachments.set(player, TFDataAttachments.IS_GRADUALLY_GLIDING, false);
			}

			TravellersGearLogic.travellersWingsSidestepCooldownSound(player);
			TravellersGearLogic.travellersStealth(player, stealthy -> stealthy.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false, false)));
			updateHighStep(player);
		}
	}

	private static void updateOtherModifiers(LivingEntity living) {
		TravellersGearLogic.travellersWingsGradualGlide(living);
		TravellersGearLogic.travellersBootsUnrestrained(living);
		TravellersGearLogic.travellersBootsSlimySolesBounce(living);
		TravellersGearLogic.travellersVestHaste(living);
		TravellersGearLogic.travellersWingsHighJump(living);
		TravellersGearLogic.travellersGearAutoRepair(living);
		TravellersGearLogic.travellersBootsStraightAhead(living);
		TravellersGearLogic.determineWingState(living);
	}

	private static void tickPlayerModifiers(net.minecraft.server.level.ServerPlayer player) {
		if (TravellersModifiersManager.isGradualGlideActive(player)
			&& !player.onGround() && !player.isInWater() && !player.onClimbable()
			&& !player.isFallFlying() && player.getDeltaMovement().y < -0.4D) {
			Vec3 movement = player.getDeltaMovement();
			player.setDeltaMovement(movement.x, -0.4D, movement.z);
			player.fallDistance = 0.0F;
		}

		if (player.getServer().getTickCount() % 4 == 0 && TravellersModifiersManager.isWaterWalkActive(player) && !player.isCrouching()) {
			freezeWaterUnder(player);
		}

		if (player.getServer().getTickCount() % 20 == 0) {
			if (TravellersModifiersManager.isRedThreadVisionActive(player)) {
				Vec3 here = player.position();
				net.minecraft.world.phys.AABB scan = new net.minecraft.world.phys.AABB(here, here).inflate(16.0D);
				for (LivingEntity target : player.serverLevel().getEntitiesOfClass(LivingEntity.class, scan, entity -> entity != player && entity.isAlive())) {
					target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0, true, false, false));
				}
			}
			if (TravellersModifiersManager.isSwiftSwimActive(player) && player.isInWater()) {
				player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 60, 0, true, false, true));
			}
			if (TravellersModifiersManager.isUnrestrainedActive(player) && player.isInWater()) {
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, true, false, true));
			}
		}
	}

	private static void updateHighStep(Player player) {
		if (!TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.STEP_UP_ABILITY)) {
			return;
		}
		AttributeInstance attribute = player.getAttribute(Attributes.STEP_HEIGHT);
		if (attribute == null) {
			return;
		}

		boolean shouldHaveHighStepModifier = !player.isCrouching();
		boolean hasHighStepModifier = attribute.hasModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP.id());
		if (!shouldHaveHighStepModifier && hasHighStepModifier) {
			attribute.removeModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
		}
		if (shouldHaveHighStepModifier && !hasHighStepModifier) {
			attribute.addPermanentModifier(TFAttributeModifiers.TRAVELLERS_HIGH_STEP);
		}
	}

	private static void freezeWaterUnder(net.minecraft.server.level.ServerPlayer player) {
		net.minecraft.core.BlockPos under = player.blockPosition().below();
		BlockState frosted = Blocks.FROSTED_ICE.defaultBlockState();
		net.minecraft.core.BlockPos.MutableBlockPos cursor = new net.minecraft.core.BlockPos.MutableBlockPos();
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				cursor.set(under.getX() + dx, under.getY(), under.getZ() + dz);
				BlockState here = player.serverLevel().getBlockState(cursor);
				if (here.getFluidState().is(net.minecraft.tags.FluidTags.WATER)
					&& here.getBlock() == Blocks.WATER
					&& here.getValue(LiquidBlock.LEVEL) == 0
					&& frosted.canSurvive(player.serverLevel(), cursor)
					&& player.serverLevel().isUnobstructed(frosted, cursor, CollisionContext.empty())) {
					player.serverLevel().setBlockAndUpdate(cursor, frosted);
					player.serverLevel().scheduleTick(cursor.immutable(), Blocks.FROSTED_ICE, Math.max(60, player.getRandom().nextInt(40) + 60));
				}
			}
		}
	}

	public static boolean isTravellersGear(ItemStack stack) {
		return stack.is(TFItems.TRAVELLERS_GOGGLES.get())
			|| stack.is(TFItems.TRAVELLERS_VEST.get())
			|| stack.is(TFItems.TRAVELLERS_GLOVES.get())
			|| stack.is(TFItems.TRAVELLERS_WINGS.get())
			|| stack.is(TFItems.TRAVELLERS_BELT.get())
			|| stack.is(TFItems.TRAVELLERS_BOOTS.get());
	}
}
