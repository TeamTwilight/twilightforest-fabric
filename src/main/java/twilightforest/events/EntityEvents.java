package twilightforest.events;

import com.mojang.authlib.GameProfile;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityDamageEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityDamageEvents.HurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.event.common.ItemCraftedCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.enchantment.ChillAuraEnchantment;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFStats;
import twilightforest.item.FieryArmorItem;
import twilightforest.item.YetiArmorItem;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class EntityEvents {

	private static final boolean SHIELD_PARRY_MOD_LOADED = FabricLoader.getInstance().isModLoaded("parry");

	public static void init() {
		ItemCraftedCallback.EVENT.register(EntityEvents::onCrafting);
		LivingEntityDamageEvents.HURT.register(EntityEvents::entityHurts);
		LivingEntityDamageEvents.HURT.register(EntityEvents::onLivingHurtEvent);
		UseBlockCallback.EVENT.register(EntityEvents::createSkullCandle);
		PlayerBlockBreakEvents.BEFORE.register(EntityEvents::onCasketBreak);
		io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents.PROJECTILE_IMPACT.register(EntityEvents::onParryProjectile);
		PlayerEvents.ADVANCEMENT_GRANT.register(EntityEvents::alertPlayerCastleIsWIP);
		LivingEntityEvents.TICK.register(EntityEvents::onLivingTickEvent);
		LivingEntityEvents.JUMP.register(EntityEvents::onLivingJumpEvent);
	}

	public static void alertPlayerCastleIsWIP(Player player, Advancement advancement) {
		if (advancement.getId().equals(TwilightForestMod.prefix("progression_end"))) {
			player.sendSystemMessage(Component.translatable("gui.twilightforest.progression_end.message", Component.translatable("gui.twilightforest.progression_end.discord").withStyle(style -> style.withColor(ChatFormatting.BLUE).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/twilightforest")))));
		}
	}

	public static void entityHurts(HurtEvent event) {
		LivingEntity living = event.damaged;
		DamageSource source = event.damageSource;
		Entity trueSource = source.getEntity();

		// fire react and chill aura
		if (source.getEntity() != null && trueSource != null && event.damageAmount > 0) {
			int fireLevel = getGearCoverage(living, false) * 5;
			int chillLevel = getGearCoverage(living, true);

			if (fireLevel > 0 && living.getRandom().nextInt(25) < fireLevel && !trueSource.fireImmune()) {
				trueSource.setSecondsOnFire(fireLevel / 2);
			}

			if (trueSource instanceof LivingEntity target) {
				ChillAuraEnchantment.doChillAuraEffect(target, chillLevel * 5 + 5, chillLevel, chillLevel > 0);
			}
		}

		// triple bow strips invulnerableTime
		if (source.getMsgId().equals("arrow") && trueSource instanceof Player player) {

			if (player.getItemInHand(player.getUsedItemHand()).is(TFItems.TRIPLE_BOW.get())) {
				living.invulnerableTime = 0;
			}
		}
	}

	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public static boolean onCasketBreak(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te) {
		Block block = state.getBlock();
		UUID checker;
		if (block == TFBlocks.KEEPSAKE_CASKET.get()) {
			if (te instanceof KeepsakeCasketBlockEntity casket) {
				checker = casket.playeruuid;
			} else checker = null;
			if (checker != null) {
				if (!((KeepsakeCasketBlockEntity) te).isEmpty()) {
					if (!player.hasPermissions(3) || !player.getGameProfile().getId().equals(checker)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void onCrafting(Player player, ItemStack itemStack, Container inventory) {
		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		if (itemStack.is(Items.OAK_PLANKS) && itemStack.getCount() == 64 && inventory.countItem(TFBlocks.GIANT_LOG.get().asItem()) > 0) {
			player.getInventory().placeItemBackInInventory(new ItemStack(Items.OAK_PLANKS, 64));
			player.getInventory().placeItemBackInInventory(new ItemStack(Items.OAK_PLANKS, 64));
			player.getInventory().placeItemBackInInventory(new ItemStack(Items.OAK_PLANKS, 64));
		}
	}

	public static void onLivingHurtEvent(HurtEvent event) {
		LivingEntity living = event.damaged;
		if (living != null) {
			Optional.ofNullable(living.getEffect(TFMobEffects.FROSTY.get())).ifPresent(mobEffectInstance -> {
				if (event.damageSource.is(DamageTypes.FREEZE)) {
					event.damageAmount = event.damageAmount + (float)(mobEffectInstance.getAmplifier() / 2);
				} else if (event.damageSource.is(DamageTypeTags.IS_FIRE)) {
					living.removeEffect(TFMobEffects.FROSTY.get());
					mobEffectInstance.amplifier -= 1;
					if (mobEffectInstance.amplifier >= 0) living.addEffect(mobEffectInstance);
				}
			});
		}
	}

	// Parrying
	public static boolean onParryProjectile(final Projectile projectile, HitResult hitResult) {
		if (!projectile.getCommandSenderWorld().isClientSide() && !SHIELD_PARRY_MOD_LOADED && (TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get() || projectile instanceof ITFProjectile)) {

			if (hitResult instanceof EntityHitResult result) {
				Entity entity = result.getEntity();

				if (entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isBlocking() && entityBlocking.getUseItem().getUseDuration() - entityBlocking.getUseItemRemainingTicks() <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicks.get()) {
						projectile.setOwner(entityBlocking);
						Vec3 rebound = entityBlocking.getLookAngle();
						projectile.shoot(rebound.x(), rebound.y(), rebound.z(), 1.1F, 0.1F);  // reflect faster and more accurately
						if (projectile instanceof AbstractHurtingProjectile hurting) {
							hurting.xPower = rebound.x() * 0.1D;
							hurting.yPower = rebound.y() * 0.1D;
							hurting.zPower = rebound.z() * 0.1D;
						}

						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the player is attempting to create a skull candle
	 */
	// I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	// Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	public static InteractionResult createSkullCandle(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		BlockPos pos = hitResult.getBlockPos();
		BlockState state = world.getBlockState(pos);
		if (!TFConfig.COMMON_CONFIG.disableSkullCandles.get()) {
			if (stack.is(ItemTags.CANDLES) && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !player.isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock skull && BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) skull.getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.SKELETON_SKULL_CANDLE.get());
						}
						case WITHER_SKELETON -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.WITHER_SKELE_SKULL_CANDLE.get());
						}
						case PLAYER -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.PLAYER_SKULL_CANDLE.get());
						}
						case ZOMBIE -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.ZOMBIE_SKULL_CANDLE.get());
						}
						case CREEPER -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.CREEPER_SKULL_CANDLE.get());
						}
						case PIGLIN -> {
							if (wall) makeWallSkull(stack, pos, world, TFBlocks.PIGLIN_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(stack, pos, world, TFBlocks.PIGLIN_SKULL_CANDLE.get());
						}
						default -> {
							return InteractionResult.PASS;
						}
					}
					if (!player.getAbilities().instabuild) stack.shrink(1);
					player.swing(hand);
					if (player instanceof ServerPlayer)
						player.awardStat(TFStats.SKULL_CANDLES_MADE.get());
					//this is to prevent anything from being placed afterwords
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	private static void makeFloorSkull(ItemStack stack, BlockPos pos, Level level, Block newBlock) {
		GameProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(pos, newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
						.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)),
				AbstractSkullCandleBlock.candleToCandleColor(stack.getItem()).getValue(), 1));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(ItemStack stack, BlockPos pos, Level level, Block newBlock) {
		GameProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(pos, newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(stack.getItem()).getValue(), 1));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	/**
	 * Add up the number of armor pieces the player is wearing (either fiery or yeti)
	 */
	public static int getGearCoverage(LivingEntity entity, boolean yeti) {
		int amount = 0;

		for (ItemStack armor : entity.getArmorSlots()) {
			if (!armor.isEmpty() && (yeti ? armor.getItem() instanceof YetiArmorItem : armor.getItem() instanceof FieryArmorItem)) {
				amount++;
			}
		}

		return amount;
	}

	public static void onLivingTickEvent(LivingEntity living) {
		if (living != null && canSpawnCloudParticles(living)) {
			CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, false);
		}
	}

	public static boolean canSpawnCloudParticles(LivingEntity living) {
		if (living.getDeltaMovement().x == 0.0D && living.getDeltaMovement().z == 0.0D && living.getRandom().nextInt(20) != 0) return false;
		return living.tickCount % 2 == 0 && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock;
	}

	public static void onLivingJumpEvent(LivingEntity living) {
		if (living != null && living.level().isClientSide() && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock) {
			for (int i = 0; i < 12; i++) CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, true);
		}
	}
}
