package twilightforest.events;

import com.mojang.authlib.GameProfile;
import io.github.fabricators_of_create.porting_lib.event.common.ItemCraftedCallback;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.ProjectileImpactCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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
import twilightforest.TFConfig;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.fan.FeatherFanFallCapability;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFStats;
import twilightforest.item.FieryArmorItem;
import twilightforest.item.MazebreakerPickItem;
import twilightforest.item.YetiArmorItem;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;

import java.util.UUID;

public class PlayerEvents {

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";
	private static final boolean globalParry = !FabricLoader.getInstance().isModLoaded("parry");

	public static void init() {
		ItemCraftedCallback.EVENT.register(PlayerEvents::onCrafting);
		LivingEntityEvents.ACTUALLY_HURT.register(PlayerEvents::entityHurts);
		UseBlockCallback.EVENT.register(PlayerEvents::createSkullCandle);
		PlayerBlockBreakEvents.BEFORE.register(PlayerEvents::onCasketBreak);
		ServerPlayerEvents.AFTER_RESPAWN.register(PlayerEvents::onPlayerRespawn);
		ProjectileImpactCallback.EVENT.register(PlayerEvents::throwableParry);
		EntityTrackingEvents.START_TRACKING.register(PlayerEvents::onStartTracking);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(PlayerEvents::playerPortals);
	}

	@SubscribeEvent
	public static void damageToolsExtra(BlockEvent.BreakEvent event) {
		ItemStack stack = event.getPlayer().getMainHandItem();
		if (event.getState().is(BlockTagGenerator.MAZESTONE) || event.getState().is(BlockTagGenerator.CASTLE_BLOCKS)) {
			if (stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, event.getPlayer(), (user) -> user.broadcastBreakEvent(InteractionHand.MAIN_HAND));
			}
		}
	}

	@SubscribeEvent
	public static void updateFeatherFanCap(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof Player player && player.getCapability(CapabilityList.FEATHER_FAN_FALLING).isPresent()) {
			player.getCapability(CapabilityList.FEATHER_FAN_FALLING).ifPresent(FeatherFanFallCapability::update);
		}
	}

	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {
		String damageType = damageSource.getMsgId();
		Entity trueSource = damageSource.getEntity();

		// fire aura
		if (living instanceof Player player && (damageType.equals("mob") || damageType.equals("player")) && trueSource != null) {
			int fireLevel = getFieryAuraLevel(player.getInventory());

			if (fireLevel > 0 && player.getRandom().nextInt(25) < fireLevel) {
				trueSource.setSecondsOnFire(fireLevel / 2);
			}
		}

		// chill aura
		if (living instanceof Player player && (damageType.equals("mob") || damageType.equals("player")) && trueSource instanceof LivingEntity living1) {
			int chillLevel = getChillAuraLevel(player.getInventory());

			if (chillLevel > 0) {
				living1.addEffect(new MobEffectInstance(TFMobEffects.FROSTY.get(), chillLevel * 5 + 5, chillLevel));
			}
		}

		// triple bow strips hurtResistantTime
		if (damageType.equals("arrow") && trueSource instanceof Player player) {

			if (player.getMainHandItem().getItem() == TFItems.TRIPLE_BOW.get() || player.getOffhandItem().getItem() == TFItems.TRIPLE_BOW.get()) {
				living.invulnerableTime = 0;
			}
		}
		return amount;
	}

	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public static boolean onCasketBreak(Level world, Player player, BlockPos pos, BlockState state, /* Nullable */ BlockEntity te) {
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

	public static void onPlayerRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
		if (alive) {
			updateCapabilities(newPlayer, newPlayer);
		}

		if (TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && newPlayer.getRespawnPosition() == null) {
			CompoundTag tagCompound = newPlayer.getExtraCustomData();
			CompoundTag playerData = tagCompound.getCompound("PlayerPersisted");
			playerData.putBoolean(NBT_TAG_TWILIGHT, false); // set to false so that the method works
			tagCompound.put("PlayerPersisted", playerData); // commit
			banishNewbieToTwilightZone(newPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public static void playerLogsIn(ServerPlayer player) {
		if (!player.getLevel().isClientSide()) {
			updateCapabilities(player, player);
			banishNewbieToTwilightZone(player);
		}
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	public static void playerPortals(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
		if (!player.getLevel().isClientSide()) {
			updateCapabilities(player, player);
		}
	}

	public static void onStartTracking(Entity trackedEntity, ServerPlayer player) {
		updateCapabilities(player, trackedEntity);
	}

	// Parrying
	public static boolean throwableParry(final Projectile projectile, HitResult hitResult) {
		if (!projectile.getCommandSenderWorld().isClientSide() && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (hitResult instanceof EntityHitResult result) {
				Entity entity = result.getEntity();

				if (entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksThrowable.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();
						projectile.shoot(playerVec3.x(), playerVec3.y(), playerVec3.z(), 1.1F, 0.1F);  // reflect faster and more accurately
						projectile.setOwner(entityBlocking);
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
			if (stack.is(ItemTags.CANDLES) && Registry.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !player.isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock skull && Registry.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
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
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
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
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(stack.getItem()).getValue(), 1));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		CapabilityList.SHIELDS.maybeGet(shielded).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.sendToClient(new UpdateShieldPacket(shielded, cap), clientTarget);
			}
		});
	}

	// Teleport first-time players to Twilight Forest
	private static void banishNewbieToTwilightZone(Player player) {
		CompoundTag tagCompound = player.getExtraCustomData();
		CompoundTag playerData = tagCompound.getCompound("PlayerPersisted");

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put("PlayerPersisted", playerData); // commit

		if (shouldBanishPlayer)
			TFPortalBlock.attemptSendEntity(player, true, TFConfig.COMMON_CONFIG.DIMENSION.portalForNewPlayerSpawn.get()); // See ya hate to be ya
	}

	/**
	 * Add up the number of fiery armor pieces the player is wearing, multiplied by 5
	 */
	public static int getFieryAuraLevel(Inventory inventory) {
		int modifier = 0;

		for (ItemStack armor : inventory.armor) {
			if (!armor.isEmpty() && armor.getItem() instanceof FieryArmorItem) {
				modifier += 5;
			}
		}

		return modifier;
	}

	/**
	 * Add up the number of yeti armor pieces the player is wearing, 0-4
	 */
	public static int getChillAuraLevel(Inventory inventory) {
		int modifier = 0;

		for (ItemStack armor : inventory.armor) {
			if (!armor.isEmpty() && armor.getItem() instanceof YetiArmorItem) {
				modifier++;
			}
		}

		return modifier;
	}
}
