package twilightforest.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import twilightforest.TFMain;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.block.SkullChestBlock;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.block.entity.SkullChestBlockEntity;

import twilightforest.components.entity.MultiplayerInclusivityAttachment;
import twilightforest.config.TFConfig;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFDataMaps;
import twilightforest.init.TFDimensionData;
import twilightforest.init.TFDamageTypes;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;

import twilightforest.network.SyncQuestsPacket;
import twilightforest.network.WipeOreMeterPacket;
import twilightforest.util.entities.EntityUtil;
import twilightforest.util.datamaps.EntityTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EntityEvents {

	private static final boolean SHIELD_PARRY_MOD_LOADED = FabricLoader.getInstance().isModLoaded("parry");

	private static final QuestingRamCurrentContext questingRamCurrentContext = QuestingRamCurrentContext.INSTANCE;

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register(EntityEvents::ominousFireConversion);
		ServerLivingEntityEvents.AFTER_DEATH.register(EntityEvents::grantGroupAdvancementIfNeeded);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityEvents::zombifiedPlayerAttacks);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(EntityEvents::entityHurts);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(EntityEvents::addQualifiedGroupPlayerIfNeeded);

		UseBlockCallback.EVENT.register(EntityEvents::attachLeadToWroughtFence);
		UseBlockCallback.EVENT.register(EntityEvents::createSkullCandle);
		PlayerBlockBreakEvents.BEFORE.register(EntityEvents::onCasketBreak);
		AttackEntityCallback.EVENT.register(EntityEvents::removeCastleTextIfAttacked);

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(EntityEvents::handleQuestSyncing);

		ServerEntityEvents.ENTITY_LOAD.register(EntityEvents::handleLeashPathingOverrides);
		ServerEntityEvents.ENTITY_LOAD.register(EntityEvents::stopEndermenFromGrabbingBlocksInTF);

		// TODO [Fabric] remaining NeoForge hooks still need mixins:
		// wipeOreMeterOnLeftClick / structureSpecialSpawns / lichBombsDontBlowUpItems / resetFlaskLogic
		// (reduceFrostedEffectIfOnFire, onParryProjectile, addCloudJumpParticles and
		//  adjustEntityHealthInMultiplayerFights are now mixins in asm.mixin.event)
	}

	private static void ominousFireConversion(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source) {
		if (source.is(TFDamageTypes.OMINOUS_FIRE)) {
			EntityTransformation dataMap = TFDataMaps.OMINOUS_FIRE.get(entity.getType().builtInRegistryHolder());

			if (entity instanceof ServerPlayer player) {
				var zombie = EntityType.ZOMBIE.create(player.level(), net.minecraft.world.entity.EntitySpawnReason.CONVERSION);
				((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) zombie).setAttached(TFDataAttachments.ZOMBIFIED_PLAYER, player.getGameProfile());
				zombie.setCustomName(player.getName());
				zombie.copyPosition(player);
				zombie.setCanPickUpLoot(true);
				zombie.setBaby(false);
				zombie.finalizeSpawn((ServerLevel) player.level(), player.level().getCurrentDifficultyAt(player.blockPosition()), net.minecraft.world.entity.EntitySpawnReason.CONVERSION, null);
				player.level().addFreshEntity(zombie);
			} else if (dataMap != null && entity.level() instanceof ServerLevel) {
				EntityUtil.convertEntity(entity, dataMap.result());
			}
		}
	}

	private static boolean zombifiedPlayerAttacks(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source, float amount) {
		if (!(source instanceof twilightforest.util.entities.OminousFireDamageSource) && source.getEntity() instanceof Zombie zombie && ((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) zombie).hasAttached(TFDataAttachments.ZOMBIFIED_PLAYER)) {
			entity.hurt(new twilightforest.util.entities.OminousFireDamageSource(source), amount);
			return false;
		}
		return true;
	}

	private static InteractionResult attachLeadToWroughtFence(Player player, Level level, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(Items.LEAD)) {
			BlockPos pos = hitResult.getBlockPos();
			net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
			if (state.is(TFBlocks.WROUGHT_IRON_FENCE) && state.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE) {
				if (!level.isClientSide()) {
					net.minecraft.world.item.LeadItem.bindPlayerMobs(player, level, pos);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	private static void entityHurts(LivingEntity living, net.minecraft.world.damagesource.DamageSource source, float amount, float original, boolean blockedByShield) {
		Entity trueSource = source.getEntity();

		// fire react and chill aura
		if (source.getEntity() != null && trueSource != null && original > 0) {
			int fireLevel = getGearCoverage(living, false) * 5;
			int chillLevel = getGearCoverage(living, true);

			if (fireLevel > 0 && living.getRandom().nextInt(25) < fireLevel && !trueSource.fireImmune()) {
				trueSource.igniteForSeconds(fireLevel / 2);
			}

			if (trueSource instanceof LivingEntity target) {
				twilightforest.enchantment.ApplyFrostedEffect.doChillAuraEffect(target, chillLevel * 5 + 5, chillLevel, chillLevel > 0);
			}
		}

		// triple bow strips invulnerableTime
		if (source.getMsgId().equals("arrow") && trueSource instanceof Player player) {
			if (player.getItemInHand(player.getUsedItemHand()).is(TFItems.TRIPLE_BOW)) {
				living.invulnerableTime = 0;
			}
		}
	}

	//if our casket is owned by someone and that player isnt the one breaking it, stop them
	private static boolean onCasketBreak(Level level, Player player, BlockPos pos, net.minecraft.world.level.block.state.BlockState state, BlockEntity blockEntity) {
		if (state.getBlock() instanceof SkullChestBlock) {
			BlockEntity te = level.getBlockEntity(pos);
			if (te instanceof SkullChestBlockEntity casket) {
				net.minecraft.world.item.component.ResolvableProfile checker = casket.owner;
				if (checker != null && !casket.isEmpty()) {
					if (!net.minecraft.commands.Commands.LEVEL_ADMINS.check(player.permissions()) || !player.getGameProfile().equals(checker.partialProfile())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the player is attempting to create a skull candle
	 */
	private static InteractionResult createSkullCandle(Player player, Level level, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		BlockPos pos = hitResult.getBlockPos();
		net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
		if (!TFConfig.disableSkullCandles) {
			if (stack.is(net.minecraft.tags.ItemTags.CANDLES) && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("minecraft") && !player.isShiftKeyDown()) {
				if (state.getBlock() instanceof SkullBlock skull && BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
					SkullBlock.Types type = (SkullBlock.Types) skull.getType();
					boolean wall = state.getBlock() instanceof WallSkullBlock;
					switch (type) {
						case SKELETON -> {
							if (wall) makeSkullCandle(level, pos, stack, player, TFBlocks.SKELETON_WALL_SKULL_CANDLE);
							else makeSkullCandle(level, pos, stack, player, TFBlocks.SKELETON_SKULL_CANDLE);
						}
						case WITHER_SKELETON -> {
							if (wall) makeSkullCandle(level, pos, stack, player, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE);
							else makeSkullCandle(level, pos, stack, player, TFBlocks.WITHER_SKELE_SKULL_CANDLE);
						}
						case PLAYER -> {
							if (wall) makeSkullCandle(level, pos, stack, player, TFBlocks.PLAYER_WALL_SKULL_CANDLE);
							else makeSkullCandle(level, pos, stack, player, TFBlocks.PLAYER_SKULL_CANDLE);
						}
						case ZOMBIE -> {
							if (wall) makeSkullCandle(level, pos, stack, player, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE);
							else makeSkullCandle(level, pos, stack, player, TFBlocks.ZOMBIE_SKULL_CANDLE);
						}
						case CREEPER -> {
							if (wall) makeSkullCandle(level, pos, stack, player, TFBlocks.CREEPER_WALL_SKULL_CANDLE);
							else makeSkullCandle(level, pos, stack, player, TFBlocks.CREEPER_SKULL_CANDLE);
						}
						case PIGLIN -> {
							if (wall) makeSkullCandle(level, pos, stack, player, TFBlocks.PIGLIN_WALL_SKULL_CANDLE);
							else makeSkullCandle(level, pos, stack, player, TFBlocks.PIGLIN_SKULL_CANDLE);
						}
						default -> {
							return InteractionResult.PASS;
						}
					}
					stack.consume(1, player);
					player.swing(hand);
					if (player instanceof ServerPlayer)
						player.awardStat(TFStats.SKULL_CANDLES_MADE);
					//this is to prevent anything from being placed afterwords
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	private static void makeSkullCandle(Level level, BlockPos pos, ItemStack stack, Player player, Block newBlock) {
		net.minecraft.world.item.component.ResolvableProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull)
			profile = skull.getOwnerProfile();
		level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(pos, newBlock.withPropertiesOf(level.getBlockState(pos))
			.setValue(AbstractSkullCandleBlock.LIGHTING, twilightforest.block.LightableBlock.Lighting.NONE));
		level.setBlockEntity(new SkullCandleBlockEntity(pos, newBlock.withPropertiesOf(level.getBlockState(pos))
			.setValue(AbstractSkullCandleBlock.LIGHTING, twilightforest.block.LightableBlock.Lighting.NONE)));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			sc.setCandleInfo(new twilightforest.components.item.SkullCandles(sc.getCandleInfo().count(), AbstractSkullCandleBlock.candleToCandleColor(stack.getItem()).getValue()));
			sc.setOwnerProfile(profile);
			sc.setChanged();
		}
	}

	/**
	 * Add up the number of armor pieces the player is wearing (either fiery or yeti)
	 */
	public static int getGearCoverage(LivingEntity entity, boolean yeti) {
		int amount = 0;

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.isArmor()) {
				ItemStack armor = entity.getItemBySlot(slot);
				if (!armor.isEmpty() && (yeti ? armor.getItem() instanceof twilightforest.item.YetiArmorItem : armor.getItem() instanceof twilightforest.item.FieryArmorItem)) {
					amount++;
				}
			}
		}

		return amount;
	}

	private static InteractionResult removeCastleTextIfAttacked(Player player, Level level, InteractionHand hand, Entity target, EntityHitResult hitResult) {
		// For clearing our Display text entities at the Final Castle Gazebo, there's no other way to remove them otherwise
		// The tag distinguishes our Interaction entities from other Mods' utilization
		if (target.level() instanceof ServerLevel serverLevel && target instanceof Interaction interaction
			&& interaction.tags.contains(twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent.INTERACTION_TAG)) {
			AABB bounds = interaction.getBoundingBox();
			serverLevel.getEntities(interaction, bounds, e -> e instanceof net.minecraft.world.entity.Display).forEach(Entity::discard);
			interaction.discard();
		}
		return InteractionResult.PASS;
	}

	private static void addQualifiedGroupPlayerIfNeeded(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source, float amount, float original, boolean blockedByShield) {
		if (entity.is(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES)) {
			var data = ((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) entity).getAttached(TFDataAttachments.MULTIPLAYER_FIGHT);
			if (source.getEntity() != null) {
				data.maybeAddQualifiedPlayer(source.getEntity());
			}
		}
	}

	private static void grantGroupAdvancementIfNeeded(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source) {
		if (((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) entity).hasAttached(TFDataAttachments.MULTIPLAYER_FIGHT)) {
			((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) entity).getAttached(TFDataAttachments.MULTIPLAYER_FIGHT).grantGroupAdvancement(entity);
		}
	}

	private static void handleQuestSyncing(ServerPlayer player, boolean joined) {
		net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, new SyncQuestsPacket(questingRamCurrentContext.getContext()));
	}

	private static void handleLeashPathingOverrides(Entity entity, ServerLevel level) {
		if (!(entity instanceof PathfinderMob mob && ((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) mob).hasAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE))) {
			return;
		}

		if (!mob.mayBeLeashed()) {
			((net.fabricmc.fabric.api.attachment.v1.AttachmentTarget) mob).removeAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
		}
	}

	private static void stopEndermenFromGrabbingBlocksInTF(Entity entity, ServerLevel level) {
		if (entity instanceof EnderMan enderMan) {
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
