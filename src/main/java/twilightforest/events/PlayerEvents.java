package twilightforest.events;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.capabilities.CapabilityList;
import twilightforest.entity.projectile.ITFProjectile;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFStats;
import twilightforest.item.FieryArmorItem;
import twilightforest.item.YetiArmorItem;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.UpdateShieldPacket;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class PlayerEvents {

	private static final String NBT_TAG_TWILIGHT = "twilightforest_banished";
	private static final boolean globalParry = !ModList.get().isLoaded("parry");

	@SubscribeEvent
	public static void entityHurts(LivingHurtEvent event) {
		LivingEntity living = event.getEntityLiving();
		DamageSource source = event.getSource();
		String damageType = source.getMsgId();
		Entity trueSource = source.getEntity();

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
	}

	@SubscribeEvent
	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public static void onCasketBreak(BlockEvent.BreakEvent event) {
		Block block = event.getState().getBlock();
		Player player = event.getPlayer();
		BlockEntity te = event.getWorld().getBlockEntity(event.getPos());
		UUID checker;
		if (block == TFBlocks.KEEPSAKE_CASKET.get()) {
			if (te instanceof KeepsakeCasketBlockEntity casket) {
				checker = casket.playeruuid;
			} else checker = null;
			if (checker != null) {
				if (!((KeepsakeCasketBlockEntity) te).isEmpty()) {
					if (!player.hasPermissions(3) || !player.getGameProfile().getId().equals(checker)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
		ItemStack itemStack = event.getCrafting();

		// if we've crafted 64 planks from a giant log, sneak 192 more planks into the player's inventory or drop them nearby
		if (itemStack.is(Items.OAK_PLANKS) && itemStack.getCount() == 64 && event.getInventory().countItem(TFBlocks.GIANT_LOG.get().asItem()) > 0) {
			Player player = event.getPlayer();
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.OAK_PLANKS, 64));
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.OAK_PLANKS, 64));
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayer serverPlayer)) return;
		if (event.isEndConquered()) {
			updateCapabilities(serverPlayer, serverPlayer);
		}

		if (TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && serverPlayer.getRespawnPosition() == null) {
			CompoundTag tagCompound = serverPlayer.getPersistentData();
			CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);
			playerData.putBoolean(NBT_TAG_TWILIGHT, false); // set to false so that the method works
			tagCompound.put(Player.PERSISTED_NBT_TAG, playerData); // commit
			banishNewbieToTwilightZone(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	@SubscribeEvent
	public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.getPlayer().getLevel().isClientSide() && event.getPlayer() instanceof ServerPlayer) {
			updateCapabilities((ServerPlayer) event.getPlayer(), event.getPlayer());
			banishNewbieToTwilightZone(event.getPlayer());
		}
	}

	/**
	 * When player changes dimensions, send the rule status if they're moving to the Twilight Forest
	 */
	@SubscribeEvent
	public static void playerPortals(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getPlayer().getLevel().isClientSide() && event.getPlayer() instanceof ServerPlayer player) {
			updateCapabilities(player, event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayer) event.getPlayer(), event.getTarget());
	}

	// Parrying
	@SubscribeEvent
	public static void throwableParry(ProjectileImpactEvent event) {
		final Projectile projectile = event.getProjectile();

		if (!projectile.getCommandSenderWorld().isClientSide() && globalParry &&
				(TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.parryNonTwilightAttacks.get()
						|| projectile instanceof ITFProjectile)) {

			if (event.getRayTraceResult() instanceof EntityHitResult result) {
				Entity entity = result.getEntity();

				if (event.getEntity() != null && entity instanceof LivingEntity entityBlocking) {
					if (entityBlocking.isDamageSourceBlocked(new DamageSource("parry_this") {
						@Override
						public Vec3 getSourcePosition() {
							return projectile.position();
						}
					}) && (entityBlocking.getUseItem().getItem().getUseDuration(entityBlocking.getUseItem()) - entityBlocking.getUseItemRemainingTicks()) <= TFConfig.COMMON_CONFIG.SHIELD_INTERACTIONS.shieldParryTicksThrowable.get()) {
						Vec3 playerVec3 = entityBlocking.getLookAngle();
						projectile.shoot(playerVec3.x(), playerVec3.y(), playerVec3.z(), 1.1F, 0.1F);  // reflect faster and more accurately
						projectile.setOwner(entityBlocking);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	/**
	 * Checks if the player is attempting to create a skull candle
	 */
	// I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	// Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	@SubscribeEvent
	public static void createSkullCandle(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		Level level = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		if (!TFConfig.COMMON_CONFIG.disableSkullCandles.get()) {
			if (stack.is(ItemTags.CANDLES) && ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().equals("minecraft") && !event.getPlayer().isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock skull && ForgeRegistries.BLOCKS.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) skull.getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeWallSkull(event, TFBlocks.SKELETON_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.SKELETON_SKULL_CANDLE.get());
						}
						case WITHER_SKELETON -> {
							if (wall) makeWallSkull(event, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.WITHER_SKELE_SKULL_CANDLE.get());
						}
						case PLAYER -> {
							if (wall) makeWallSkull(event, TFBlocks.PLAYER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.PLAYER_SKULL_CANDLE.get());
						}
						case ZOMBIE -> {
							if (wall) makeWallSkull(event, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.ZOMBIE_SKULL_CANDLE.get());
						}
						case CREEPER -> {
							if (wall) makeWallSkull(event, TFBlocks.CREEPER_WALL_SKULL_CANDLE.get());
							else makeFloorSkull(event, TFBlocks.CREEPER_SKULL_CANDLE.get());
						}
						default -> {
							return;
						}
					}
					if (!event.getPlayer().getAbilities().instabuild) stack.shrink(1);
					event.getPlayer().swing(event.getHand());
					if (event.getPlayer() instanceof ServerPlayer)
						event.getPlayer().awardStat(TFStats.SKULL_CANDLES_MADE.get());
					//this is to prevent anything from being placed afterwords
					event.setCanceled(true);
				}
			}
		}
	}

	private static void makeFloorSkull(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		GameProfile profile = null;
		Level level = event.getWorld();
		if (level.getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(event.getPos(), newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, level.getBlockState(event.getPos()).getValue(SkullBlock.ROTATION)));
		level.setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(SkullCandleBlock.ROTATION, level.getBlockState(event.getPos()).getValue(SkullBlock.ROTATION)),
				AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue(), 1));
		if (level.getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		GameProfile profile = null;
		Level level = event.getWorld();
		if (level.getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(event.getPos(), newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, level.getBlockState(event.getPos()).getValue(WallSkullBlock.FACING)));
		level.setBlockEntity(new SkullCandleBlockEntity(event.getPos(),
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, AbstractLightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, level.getBlockState(event.getPos()).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue(), 1));
		if (level.getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		shielded.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
			if (cap.shieldsLeft() > 0) {
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> clientTarget), new UpdateShieldPacket(shielded, cap));
			}
		});
	}

	// Teleport first-time players to Twilight Forest
	private static void banishNewbieToTwilightZone(Player player) {
		CompoundTag tagCompound = player.getPersistentData();
		CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);

		// getBoolean returns false, if false or didn't exist
		boolean shouldBanishPlayer = TFConfig.COMMON_CONFIG.DIMENSION.newPlayersSpawnInTF.get() && !playerData.getBoolean(NBT_TAG_TWILIGHT);

		playerData.putBoolean(NBT_TAG_TWILIGHT, true); // set true once player has spawned either way
		tagCompound.put(Player.PERSISTED_NBT_TAG, playerData); // commit

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
