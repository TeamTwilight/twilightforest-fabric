package twilightforest.events;

import carminite.events.neoforge.*;
import carminite.network.ClientPacketDistributor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jspecify.annotations.Nullable;
import twilightforest.TFCommon;
import twilightforest.advancements.DrinkFromFlaskTrigger;
import twilightforest.block.*;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.components.item.SkullCandles;
import twilightforest.config.TFConfig;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.*;
import twilightforest.network.WipeOreMeterPacket;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.entities.OminousFireDamageSource;
import twilightforest.world.components.structures.SpawnIndexProvider;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.ValidatedSpawnLocations;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

public class EntityEvents {
	public static final EntityEvents INSTANCE = new EntityEvents();

	private final QuestingRamCurrentContext questingRamCurrentContext = QuestingRamCurrentContext.INSTANCE;

	private static final boolean SHIELD_PARRY_MOD_LOADED = FabricLoader.getInstance().isModLoaded("parry");

	public void ominousFireConversion(LivingDeathEvent event) {
		if (!event.isCanceled() && event.getSource().is(TFDamageTypes.OMINOUS_FIRE)) {
			if (event.getEntity() instanceof ServerPlayer player) {
				var zombie = EntityType.ZOMBIE.create(player.level(), EntitySpawnReason.CONVERSION);
				if (zombie != null) {
					zombie.setAttached(TFDataAttachments.ZOMBIFIED_PLAYER, player.getGameProfile());
					zombie.setCustomName(player.getName());
					zombie.copyPosition(player);
					zombie.setCanPickUpLoot(true);
					zombie.setBaby(false);
					zombie.finalizeSpawn(player.level(), player.level().getCurrentDifficultyAt(player.blockPosition()), EntitySpawnReason.CONVERSION, null);
					player.level().addFreshEntity(zombie);
				}
			} else if (event.getEntity().level() instanceof ServerLevel) {
				EntityType<?> result = OminousFireBlock.OMINOUS_FIRE.get(event.getEntity().getType());
				if (result != null) {
					EntityUtil.convertEntity(event.getEntity(), result);
				}
			}
		}
	}

	public boolean zombifiedPlayerAttacks(LivingEntity entity, DamageSource source, float amount) {
		if (!(source instanceof OminousFireDamageSource) && source.getEntity() instanceof Zombie zombie && zombie.hasAttached(TFDataAttachments.ZOMBIFIED_PLAYER)) {
			entity.hurt(new OminousFireDamageSource(source), amount);
			return false;
		}
		return true;
	}

	public void alertPlayerCastleIsWIP(AdvancementEvent.AdvancementEarnEvent event) {
		if (event.getAdvancement().id().equals(TFCommon.prefix("progression_end"))) {
			event.getEntity().sendSystemMessage(Component.translatable("gui.twilightforest.progression_end.message", Component.translatable("gui.twilightforest.progression_end.discord").withStyle(style -> style.withColor(ChatFormatting.BLUE).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent.OpenUrl(URI.create("https://discord.experiment115.com/"))))));
		}
	}

	public void attachLeadToWroughtFence(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());
		if (stack.is(Items.LEAD)) {
			BlockPos pos = event.getPos();
			BlockState state = event.getLevel().getBlockState(pos);
			if (state.is(TFBlocks.WROUGHT_IRON_FENCE) && state.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE) {
				if (!event.getLevel().isClientSide()) {
					LeadItem.bindPlayerMobs(player, event.getLevel(), event.getPos());
					event.setCanceled(true);
					event.setCancellationResult(InteractionResult.SUCCESS);
				}
			}
		}
	}

	public void wipeOreMeterOnLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack item = event.getItemStack();
		if (item.is(TFItems.ORE_METER) && (item.has(TFDataComponents.ORE_DATA) || item.has(TFDataComponents.ORE_FILTER))) {
			ClientPacketDistributor.sendToServer(new WipeOreMeterPacket(event.getHand()));
			item.remove(TFDataComponents.ORE_DATA);
			item.remove(TFDataComponents.ORE_FILTER);
			event.getLevel().playSound(event.getEntity(), event.getEntity().blockPosition(), TFSounds.ORE_METER_CLEAR.value(), SoundSource.PLAYERS, 1.25F, event.getLevel().getRandom().nextFloat() * 0.2F + 0.6F);
		}
	}

	public void entityHurts(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		Entity trueSource = source.getEntity();

		// fire react and chill aura
		if (source.getEntity() != null && trueSource != null && baseDamageTaken > 0) {
			int fireLevel = getGearCoverage(entity, false) * 5;
			int chillLevel = getGearCoverage(entity, true);

			if (fireLevel > 0 && entity.getRandom().nextInt(25) < fireLevel && !trueSource.fireImmune()) {
				trueSource.igniteForSeconds(fireLevel / 2);
			}

			if (trueSource instanceof LivingEntity target) {
				ApplyFrostedEffect.doChillAuraEffect(target, chillLevel * 5 + 5, chillLevel, chillLevel > 0);
			}
		}

		// triple bow strips invulnerableTime
		if (source.getMsgId().equals("arrow") && trueSource instanceof Player player) {

			if (player.getItemInHand(player.getUsedItemHand()).is(TFItems.TRIPLE_BOW)) {
				entity.invulnerableTime = 0;
			}
		}
	}

	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	public boolean onCasketBreak(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
		if (state.getBlock() instanceof SkullChestBlock) {
			BlockEntity te = level.getBlockEntity(pos);
			if (te instanceof SkullChestBlockEntity casket) {
				ResolvableProfile checker = casket.owner;
				if (checker != null && !casket.isEmpty()) {
//					if (!Commands.LEVEL_ADMINS.check(player.permissions()) || !player.getGameProfile().equals(checker.gameProfile())) {
//						event.setCanceled(true);
//					}
				}
			}
		}
		return true;
	}

	/*private void reduceFrostedEffectIfOnFire(LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity living = event.getEntity();
			Optional.ofNullable(living.getEffect(TFMobEffects.FROSTY)).ifPresent(mobEffectInstance -> {
				DamageContainer container = event.getContainer();
				if (container.getSource().typeHolder().is(DamageTypes.FREEZE)) {
					container.setNewDamage(container.getOriginalDamage() + (float) (mobEffectInstance.getAmplifier() / 2));
				} else if (container.getSource().typeHolder().is(DamageTypeTags.IS_FIRE)) {
					living.removeEffect(TFMobEffects.FROSTY);
					mobEffectInstance.amplifier -= 1;
					if (mobEffectInstance.amplifier >= 0) living.addEffect(mobEffectInstance);
				}
			});
		}
	}*/

	// Parrying
	public void onParryProjectile(ProjectileImpactEvent event) {
		final Projectile projectile = event.getProjectile();

//		if (!projectile.getCommandSenderWorld().isClientSide() && !SHIELD_PARRY_MOD_LOADED && (TFConfig.parryNonTwilightAttacks || projectile instanceof ITFProjectile)) {
//			if (event.getRayTraceResult() instanceof EntityHitResult result) {
//				Entity entity = result.getEntity();
//
//				if (entity instanceof LivingEntity entityBlocking) {
//					if (entityBlocking.isBlocking() && entityBlocking.getUseItem().getUseDuration(entityBlocking) - entityBlocking.getUseItemRemainingTicks() <= TFConfig.shieldParryTicks) {
//						projectile.deflect(ProjectileDeflection.AIM_DEFLECT, entityBlocking, entityBlocking, true);
//						event.setCanceled(true);
//					}
//				}
//			}
//		}
	}

	/**
	 * Checks if the player is attempting to create a skull candle
	 */
	// I wanted to make sure absolutely nothing broke, so I also check against the namespaces of the item to make sure theyre vanilla.
	// Worst case some stupid mod adds their own stuff to the minecraft namespace and breaks this, then you can disable this via config.
	public void createSkullCandle(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		if (!TFConfig.disableSkullCandles) {
			if (stack.is(ItemTags.CANDLES) && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !event.getEntity().isShiftKeyDown()) {
				if (state.getBlock() instanceof AbstractSkullBlock skull && BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) skull.getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeSkullCandle(event, TFBlocks.SKELETON_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.SKELETON_SKULL_CANDLE);
						}
						case WITHER_SKELETON -> {
							if (wall) makeSkullCandle(event, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.WITHER_SKELE_SKULL_CANDLE);
						}
						case PLAYER -> {
							if (wall) makeSkullCandle(event, TFBlocks.PLAYER_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.PLAYER_SKULL_CANDLE);
						}
						case ZOMBIE -> {
							if (wall) makeSkullCandle(event, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.ZOMBIE_SKULL_CANDLE);
						}
						case CREEPER -> {
							if (wall) makeSkullCandle(event, TFBlocks.CREEPER_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.CREEPER_SKULL_CANDLE);
						}
						case PIGLIN -> {
							if (wall) makeSkullCandle(event, TFBlocks.PIGLIN_WALL_SKULL_CANDLE);
							else makeSkullCandle(event, TFBlocks.PIGLIN_SKULL_CANDLE);
						}
						default -> {
							return;
						}
					}
					stack.consume(1, event.getEntity());
					event.getEntity().swing(event.getHand());
					if (event.getEntity() instanceof ServerPlayer)
						event.getEntity().awardStat(TFStats.SKULL_CANDLES_MADE);
					//this is to prevent anything from being placed afterwords
					event.setCanceled(true);
				}
			}
		}
	}

	public static void makeSkullCandle(PlayerInteractEvent.RightClickBlock event, Block newBlock) {
		ResolvableProfile profile = null;
		Level level = event.getLevel();
		if (level.getBlockEntity(event.getPos()) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, event.getPos(), SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(event.getPos(), newBlock.withPropertiesOf(level.getBlockState(event.getPos()))
			.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE));
		level.setBlockEntity(new SkullCandleBlockEntity(event.getPos(), newBlock.withPropertiesOf(level.getBlockState(event.getPos()))
			.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)));
		if (level.getBlockEntity(event.getPos()) instanceof SkullCandleBlockEntity sc) {
			sc.setCandleInfo(new SkullCandles(sc.getCandleInfo().count(), AbstractSkullCandleBlock.candleToCandleColor(event.getItemStack().getItem()).getValue()));
			sc.setOwnerProfile(profile);
			sc.setChanged();
		}
	}

	/**
	 * Add up the number of armor pieces the player is wearing (either fiery or yeti)
	 */
	public static int getGearCoverage(LivingEntity entity, boolean yeti) {
		int amount = 0;

//		for (ItemStack armor : entity.getArmorSlots()) {
//			if (!armor.isEmpty() && (yeti ? armor.getItem() instanceof YetiArmorItem : armor.getItem() instanceof FieryArmorItem)) {
//				amount++;
//			}
//		}

		return amount;
	}

	public void addCloudJumpParticles(LivingEvent.LivingJumpEvent event) {
		LivingEntity living = event.getEntity();
		if (living.level().isClientSide() && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock) {
			for (int i = 0; i < 12; i++)
				CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, true);
		}
	}

	private static int getSpawnListIndexAt(StructureStart start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos) && component instanceof SpawnIndexProvider indexProvider && indexProvider.getSpawnIndex() > highestFoundIndex) {
				highestFoundIndex = indexProvider.getSpawnIndex();
			}
		}
		return highestFoundIndex;
	}

	public static void gatherPotentialSpawns(StructureManager structureManager, MobCategory classification, BlockPos pos, Consumer<Weighted<MobSpawnSettings.SpawnerData>> consumer) {
		List<StructureStart> structureStarts = structureManager.startsForStructure(ChunkPos.containing(pos), s -> s instanceof ControlledSpawns);

		for (StructureStart start : structureStarts) {
			if (start.getStructure() instanceof ControlledSpawns landmark) {

				if (!start.isValid())
					continue;

				if (classification != MobCategory.MONSTER) {
					landmark.getSpawnableList(classification)
						.unwrap()
						.forEach(consumer);

					return;
				}

				if (start instanceof TFStructureStart s && s.isConquered())
					return;

				if (landmark instanceof ValidatedSpawnLocations validator && !validator.canSpawnMob(pos, start.getBoundingBox()))
					return;

				final int index = getSpawnListIndexAt(start, pos);
				if (index < 0)
					return;

				landmark.getSpawnableMonsterList(index)
					.unwrap()
					.forEach(consumer);

				return;
			}
		}
	}

	/*private void structureSpecialSpawns(LevelEvent.PotentialSpawns event) {
		if (!(event.getLevel() instanceof ServerLevel serverLevel))
			return;

		List<Weighted<MobSpawnSettings.SpawnerData>> potentialStructureSpawns = new ArrayList<>();

		gatherPotentialSpawns(
			serverLevel.structureManager(),
			event.getMobCategory(),
			event.getPos(),
			potentialStructureSpawns::add
		);

		if (!potentialStructureSpawns.isEmpty()) {
			List.copyOf(event.getSpawnerDataList()).forEach(event::removeSpawnerData);
			potentialStructureSpawns.forEach(event::addSpawnerData);
		}
	}*/

	public void removeCastleTextIfAttacked(AttackEntityEvent event) {
		// For clearing our Display text entities at the Final Castle Gazebo, there's no other way to remove them otherwise
		// The tag distinguishes our Interaction entities from other Mods' utilization
//		if (event.getTarget().level() instanceof ServerLevel level && event.getTarget() instanceof Interaction interaction
//			&& interaction.getTags().contains(FinalCastleBossGazeboComponent.INTERACTION_TAG)) {
//			AABB bounds = interaction.getBoundingBox();
//			level.getEntities(interaction, bounds, e -> e instanceof Display).forEach(Entity::discard);
//			interaction.discard();
//		}
	}

	/*private void adjustEntityHealthInMultiplayerFights(FinalizeSpawnEvent event) {
		if (event.getEntity().is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			if (TFConfig.multiplayerFightAdjuster.adjustsHealth()) {
				List<ServerPlayer> nearbyPlayers = event.getLevel().getEntitiesOfClass(ServerPlayer.class, event.getEntity().getBoundingBox().inflate(32, 10, 32), player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.ENTITY_STILL_ALIVE).test(player));
				if (nearbyPlayers.size() > 1 && event.getEntity().getAttribute(Attributes.MAX_HEALTH) != null) {
					event.getEntity().getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(TwilightForestMod.prefix("group_health_boost"), getHealthBasedOnDifficulty(event.getDifficulty().getDifficulty()) * (nearbyPlayers.size() - 1), AttributeModifier.Operation.ADD_VALUE));
				}
			}
		}
	}*/

	private static double getHealthBasedOnDifficulty(Difficulty difficulty) {
		return switch (difficulty) {
			case EASY -> 20.0D;
			case NORMAL -> 40.0D;
			case HARD -> 60.0D;
			default -> 0.0D;
		};
	}

	public void addQualifiedGroupPlayerIfNeeded(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
		if (entity.is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			var data = entity.getAttached(TFDataAttachments.MULTIPLAYER_FIGHT);
			if (source.getEntity() != null) {
				data.maybeAddQualifiedPlayer(source.getEntity());
			}
		}
	}

	public void grantGroupAdvancementIfNeeded(LivingDeathEvent event) {
		if (!event.isCanceled() && event.getEntity().hasAttached(TFDataAttachments.MULTIPLAYER_FIGHT)) {
			event.getEntity().getAttached(TFDataAttachments.MULTIPLAYER_FIGHT).grantGroupAdvancement(event.getEntity());
		}
	}

	public void lichBombsDontBlowUpItems(ExplosionEvent.Detonate event) {
		if (event.getExplosion().getDirectSourceEntity() instanceof LichBomb) {
			event.getAffectedEntities().removeIf(entity -> entity instanceof ItemEntity || entity instanceof LichBomb);
		}
	}

	/*private void handleQuestSyncing(OnDatapackSyncEvent event) {
		if (event.getPlayer() != null) {
			PacketDistributor.sendToPlayer(event.getPlayer(), new SyncQuestsPacket(this.questingRamCurrentContext.getContext()));
		} else {
			event.getPlayerList().getPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, new SyncQuestsPacket(this.questingRamCurrentContext.getContext())));
		}
	}*/

	public void resetFlaskLogic(AdvancementEvent.AdvancementEarnEvent event) {
		for (var criteria : event.getAdvancement().value().criteria().entrySet()) {
			if (criteria.getValue().trigger() instanceof DrinkFromFlaskTrigger) {
				event.getEntity().getAttached(TFDataAttachments.FLASK_DOSES).resetDoses();
				break;
			}
		}
	}

	public void handleLeashPathingOverrides(EntityJoinLevelEvent event) {
		if (!(event.getEntity() instanceof PathfinderMob mob && mob.hasAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE))) {
			return;
		}

		if (!mob.mayBeLeashed()) {
			mob.removeAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
		}
	}

	public void stopEndermenFromGrabbingBlocksInTF(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof EnderMan enderMan) {
			enderMan.goalSelector.getAvailableGoals().stream()
				.filter(g -> g.getGoal() instanceof EnderMan.EndermanTakeBlockGoal)
				.findAny()
				.ifPresent(g -> {
					enderMan.goalSelector.removeGoal(g.getGoal());
					enderMan.goalSelector.addGoal(g.getPriority(), new ExtendedEndermanTakeBlockGoal((EnderMan.EndermanTakeBlockGoal) g.getGoal(), enderMan));
				});
		}
	}

	static class ExtendedEndermanTakeBlockGoal extends EnderMan.EndermanTakeBlockGoal {

		private final EnderMan.EndermanTakeBlockGoal delegate;
		private final EnderMan enderman;

		public ExtendedEndermanTakeBlockGoal(EnderMan.EndermanTakeBlockGoal delegate, EnderMan enderman) {
			super(enderman);
			this.delegate = delegate;
			this.enderman = enderman;
		}

		@Override
		public boolean canUse() {
			return this.delegate.canUse() && !this.enderman.level().dimensionTypeRegistration().is(TFDimensionData.TWILIGHT_DIM_TYPE);
		}

		@Override
		public void tick() {
			this.delegate.tick();
		}
	}
}