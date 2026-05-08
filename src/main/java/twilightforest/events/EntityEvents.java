package twilightforest.events;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import twilightforest.TwilightForestMod;
import twilightforest.block.CloudBlock;
import twilightforest.block.SkullChestBlock;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.entity.passive.quest.QuestReloadListener;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.init.TFMobEffects;
import twilightforest.init.TFSounds;
import twilightforest.item.FieryArmorItem;
import twilightforest.item.YetiArmorItem;
import twilightforest.network.SyncQuestsPacket;

public final class EntityEvents {
	private static final String FINAL_CASTLE_INTERACTION_TAG = "final_castle_wip";
	private static final ResourceLocation GROUP_HEALTH_BOOST = TwilightForestMod.prefix("group_health_boost");
	private static boolean bootstrapped;

	private EntityEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new QuestReloadListener());
		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> syncQuestContext(player));
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof LivingEntity living && world instanceof ServerLevel serverLevel) {
				adjustEntityHealthInMultiplayerFights(living, serverLevel);
			}
		});
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damageTaken, blocked) -> {
			if (damageTaken > 0.0F && !blocked) {
				entityHurts(entity, source, damageTaken);
				addQualifiedGroupPlayerIfNeeded(entity, source);
			}
		});
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> grantGroupAdvancementIfNeeded(entity));
		PlayerBlockBreakEvents.BEFORE.register(EntityEvents::onCasketBreak);
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> attachLeadToWroughtFence(player, level.getBlockState(hitResult.getBlockPos()), hitResult.getBlockPos(), hand == null ? ItemStack.EMPTY : player.getItemInHand(hand)));
		AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> removeCastleTextIfAttacked(entity));
	}

	public static float modifyIncomingDamage(LivingEntity living, DamageSource source, float amount) {
		MobEffectInstance frosty = living.getEffect(TFMobEffects.FROSTY);
		if (frosty == null) {
			return amount;
		}
		if (source.typeHolder().is(DamageTypes.FREEZE)) {
			return amount + (frosty.getAmplifier() / 2.0F);
		}
		if (source.typeHolder().is(DamageTypeTags.IS_FIRE)) {
			int nextAmplifier = frosty.getAmplifier() - 1;
			int duration = frosty.getDuration();
			boolean ambient = frosty.isAmbient();
			boolean visible = frosty.isVisible();
			boolean showIcon = frosty.showIcon();
			living.removeEffect(TFMobEffects.FROSTY);
			if (nextAmplifier >= 0) {
				living.addEffect(new MobEffectInstance(TFMobEffects.FROSTY, duration, nextAmplifier, ambient, visible, showIcon));
			}
		}
		return amount;
	}

	private static void entityHurts(LivingEntity living, DamageSource source, float damageTaken) {
		Entity trueSource = source.getEntity();
		if (trueSource != null && damageTaken > 0.0F) {
			int fireLevel = getGearCoverage(living, false) * 5;
			int chillLevel = getGearCoverage(living, true);
			if (fireLevel > 0 && living.getRandom().nextInt(25) < fireLevel && !trueSource.fireImmune()) {
				trueSource.igniteForSeconds(fireLevel / 2);
			}
			if (trueSource instanceof LivingEntity target) {
				ApplyFrostedEffect.doChillAuraEffect(target, chillLevel * 5 + 5, chillLevel, chillLevel > 0);
			}
		}

		if ("arrow".equals(source.getMsgId()) && trueSource instanceof Player player && player.getItemInHand(player.getUsedItemHand()).is(TFItems.TRIPLE_BOW.get())) {
			living.invulnerableTime = 0;
		}
	}

	private static boolean onCasketBreak(net.minecraft.world.level.Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (!(state.getBlock() instanceof SkullChestBlock) || !(blockEntity instanceof SkullChestBlockEntity casket) || casket.owner == null || casket.isEmpty()) {
			return true;
		}
		if (player.hasPermissions(3) || player.getGameProfile().equals(casket.owner.gameProfile())) {
			return true;
		}
		casket.displayLockedInfo(player);
		return false;
	}

	private static InteractionResult attachLeadToWroughtFence(Player player, BlockState state, BlockPos pos, ItemStack stack) {
		if (!stack.is(Items.LEAD) || !state.is(TFBlocks.WROUGHT_IRON_FENCE.get()) || state.getValue(WroughtIronFenceBlock.POST) == WroughtIronFenceBlock.PostState.NONE) {
			return InteractionResult.PASS;
		}
		if (player.level().isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		InteractionResult result = LeadItem.bindPlayerMobs(player, player.level(), pos);
		return result.consumesAction() ? result : InteractionResult.PASS;
	}

	private static InteractionResult removeCastleTextIfAttacked(Entity target) {
		if (!(target.level() instanceof ServerLevel level) || !(target instanceof Interaction interaction) || !target.getTags().contains(FINAL_CASTLE_INTERACTION_TAG)) {
			return InteractionResult.PASS;
		}
		AABB bounds = interaction.getBoundingBox();
		level.getEntities(interaction, bounds, entity -> entity instanceof Display).forEach(Entity::discard);
		interaction.discard();
		return InteractionResult.SUCCESS;
	}

	private static void adjustEntityHealthInMultiplayerFights(LivingEntity entity, ServerLevel level) {
		if (!entity.getType().is(EntityTagGenerator.MULTIPLAYER_INCLUSIVE_ENTITIES) || !TFConfig.multiplayerFightAdjuster.adjustsHealth()) {
			return;
		}
		AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
		if (maxHealth == null || maxHealth.hasModifier(GROUP_HEALTH_BOOST)) {
			return;
		}
		int nearbyPlayers = level.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(32.0D, 10.0D, 32.0D),
			player -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.ENTITY_STILL_ALIVE).test(player)).size();
		if (nearbyPlayers <= 1) {
			return;
		}
		maxHealth.addPermanentModifier(new AttributeModifier(GROUP_HEALTH_BOOST, getHealthBasedOnDifficulty(level.getDifficulty()) * (nearbyPlayers - 1), AttributeModifier.Operation.ADD_VALUE));
		entity.setHealth(entity.getMaxHealth());
	}

	private static double getHealthBasedOnDifficulty(Difficulty difficulty) {
		return switch (difficulty) {
			case EASY -> 20.0D;
			case NORMAL -> 40.0D;
			case HARD -> 60.0D;
			default -> 0.0D;
		};
	}

	private static void addQualifiedGroupPlayerIfNeeded(LivingEntity entity, DamageSource source) {
		if (entity.getType().is(EntityTagGenerator.MULTIPLAYER_INCLUSIVE_ENTITIES) && source.getEntity() != null) {
			TFDataAttachments.get(entity, TFDataAttachments.MULTIPLAYER_FIGHT).maybeAddQualifiedPlayer(source.getEntity());
		}
	}

	private static void grantGroupAdvancementIfNeeded(LivingEntity entity) {
		if (((AttachmentTarget) entity).hasAttached(TFDataAttachments.MULTIPLAYER_FIGHT)) {
			TFDataAttachments.get(entity, TFDataAttachments.MULTIPLAYER_FIGHT).grantGroupAdvancement(entity);
		}
	}

	private static void syncQuestContext(ServerPlayer player) {
		if (ServerPlayNetworking.canSend(player, SyncQuestsPacket.TYPE)) {
			ServerPlayNetworking.send(player, new SyncQuestsPacket(QuestReloadListener.currentContext().getContext()));
		}
	}

	public static int getGearCoverage(LivingEntity entity, boolean yeti) {
		int amount = 0;
		for (ItemStack armor : entity.getArmorSlots()) {
			if (!armor.isEmpty() && (yeti ? armor.getItem() instanceof YetiArmorItem : armor.getItem() instanceof FieryArmorItem)) {
				amount++;
			}
		}
		return amount;
	}

	public static void addCloudJumpParticles(LivingEntity living) {
		if (living.level().isClientSide() && !living.isSpectator() && living.level().getBlockState(living.getOnPos()).getBlock() instanceof CloudBlock) {
			for (int i = 0; i < 12; i++) {
				CloudBlock.addEntityMovementParticles(living.level(), living.getOnPos(), living, true);
			}
		}
	}

	public static void alertPlayerCastleIsWIP(ServerPlayer player) {
		player.sendSystemMessage(Component.translatable("gui.twilightforest.progression_end.message"));
	}
}
