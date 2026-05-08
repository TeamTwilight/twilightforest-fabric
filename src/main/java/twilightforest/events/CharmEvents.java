package twilightforest.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import twilightforest.TwilightForestMod;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.config.TFConfig;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.item.CharmOfKeepingItem;
import twilightforest.item.CharmOfLifeItem;
import twilightforest.network.SpawnCharmPacket;

import java.util.List;

public final class CharmEvents {
	public static final String CHARM_INV_TAG = "TFCharmInventory";
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";

	private static boolean bootstrapped;

	private CharmEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
			handleOminousFireDeath(entity, source);
			if (!(entity instanceof Player player) || entity.level().isClientSide()
					|| player.isCreative() || player.isSpectator()) {
				return true;
			}
			if (handleCharmOfLife(player)) {
				return false;
			}
			if (!player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
				handleCharmOfKeeping(player);
				stockKeepsakeCasket(player);
			}
			return true;
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			if (!alive) {
				returnStoredItems(newPlayer);
			}
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			CompoundTag oldData = getPlayerData(oldPlayer);
			if (!oldData.isEmpty()) {
				TFDataAttachments.set(newPlayer, TFDataAttachments.CHARM_PLAYER_DATA, oldData.copy());
			}
		});
	}

	private static void handleOminousFireDeath(LivingEntity entity, DamageSource source) {
		if (!source.is(TFDamageTypes.OMINOUS_FIRE)) {
			return;
		}
		if (entity instanceof ServerPlayer player) {
			Zombie zombie = EntityType.ZOMBIE.create(player.level());
			if (zombie == null) {
				return;
			}
			TFDataAttachments.set(zombie, TFDataAttachments.ZOMBIFIED_PLAYER, player.getGameProfile());
			zombie.setCustomName(player.getName());
			zombie.copyPosition(player);
			zombie.setCanPickUpLoot(true);
			zombie.setBaby(false);
			zombie.finalizeSpawn(player.serverLevel(), player.level().getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.CONVERSION, null);
			player.level().addFreshEntity(zombie);
		} else {
			twilightforest.util.datamaps.EntityTransformation transformation = twilightforest.init.TFDataMaps.getOminousFire(entity.getType());
			if (transformation != null) {
				twilightforest.util.entities.EntityUtil.convertEntity(entity, transformation.result());
			}
		}
	}

	private static boolean handleCharmOfLife(Player player) {
		ItemStack charmStack = findBestLifeCharm(player);
		if (charmStack.isEmpty() || !(charmStack.getItem() instanceof CharmOfLifeItem charm)) {
			return false;
		}
		ItemStack consumed = charmStack.copyWithCount(1);
		charmStack.shrink(1);

		if (charm.charmTier() >= 2) {
			player.setHealth(player.getMaxHealth());
			player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
			player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
			player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
		} else {
			player.setHealth(8.0F);
			player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
		}

		if (player instanceof ServerPlayer serverPlayer) {
			sendCharmPacket(serverPlayer, consumed, TFSounds.CHARM_LIFE);
			serverPlayer.awardStat(net.minecraft.stats.Stats.CUSTOM.get(TFStats.LIFE_CHARMS_ACTIVATED));
		}
		return true;
	}

	private static ItemStack findBestLifeCharm(Player player) {
		ItemStack best = ItemStack.EMPTY;
		int bestTier = 0;
		for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			if (stack.getItem() instanceof CharmOfLifeItem charm && charm.charmTier() > bestTier) {
				best = stack;
				bestTier = charm.charmTier();
			}
		}
		for (ItemStack stack : CuriosCompat.findEquippedStacks(player, equipped -> equipped.getItem() instanceof CharmOfLifeItem)) {
			if (stack.getItem() instanceof CharmOfLifeItem charm && charm.charmTier() > bestTier) {
				best = stack;
				bestTier = charm.charmTier();
			}
		}
		return best;
	}

	private static void handleCharmOfKeeping(Player player) {
		Inventory keepInventory = new Inventory(player);
		ListTag tagList = new ListTag();

		if (!applyKeepingCharm(TFItems.CHARM_OF_KEEPING_3.get(), keepInventory, player, KeepingMode.ALL)) {
			if (!applyKeepingCharm(TFItems.CHARM_OF_KEEPING_2.get(), keepInventory, player, KeepingMode.HOTBAR)) {
				if (Inventory.isHotbarSlot(player.getInventory().selected)) {
					applyKeepingCharm(TFItems.CHARM_OF_KEEPING_1.get(), keepInventory, player, KeepingMode.HELD);
				}
			}
		}

		keepTaggedItems(player, keepInventory);

		if (!keepInventory.isEmpty()) {
			keepInventory.save(tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
			markPlayerData(player);
		}
	}

	private static boolean applyKeepingCharm(Item charmItem, Inventory keptInventory, Player player, KeepingMode mode) {
		if (!hasKeepableItems(player, mode)) {
			return false;
		}
		ItemStack consumed = consumeInventoryItem(player, charmItem, true);
		if (consumed.isEmpty()) {
			return false;
		}

		boolean keptCasket = false;
		if (mode == KeepingMode.ALL) {
			for (int i = 0; i < player.getInventory().items.size(); i++) {
				keptCasket = keepMainSlot(keptInventory, player, i, keptCasket);
			}
		} else if (mode == KeepingMode.HOTBAR) {
			for (int i = 0; i <= 8 && i < player.getInventory().items.size(); i++) {
				keptCasket = keepMainSlot(keptInventory, player, i, keptCasket);
			}
		} else {
			keptCasket = keepMainSlot(keptInventory, player, player.getInventory().selected, keptCasket);
		}
		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			keptCasket = keepArmorSlot(keptInventory, player, i, keptCasket);
		}
		keepOffhandSlot(keptInventory, player, keptCasket);
		getPlayerData(player).put(CONSUMED_CHARM_TAG, consumed.save(player.registryAccess(), new CompoundTag()));
		markPlayerData(player);
		return true;
	}

	private static boolean hasKeepableItems(Player player, KeepingMode mode) {
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			if ((mode == KeepingMode.ALL || mode == KeepingMode.HOTBAR && i <= 8 || mode == KeepingMode.HELD && i == player.getInventory().selected)
					&& isNotOnlyCharm(player.getInventory().items.get(i))) {
				return true;
			}
		}
		return player.getInventory().armor.stream().anyMatch(CharmEvents::isNotOnlyCharm)
				|| player.getInventory().offhand.stream().anyMatch(CharmEvents::isNotOnlyCharm);
	}

	private static boolean isNotOnlyCharm(ItemStack stack) {
		return !stack.isEmpty() && !(stack.getItem() instanceof CharmOfKeepingItem);
	}

	private static void keepTaggedItems(Player player, Inventory keptInventory) {
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.is(ItemTagGenerator.KEPT_ON_DEATH)) {
				keptInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}
		for (int i = 0; i < player.getInventory().armor.size(); i++) {
			ItemStack stack = player.getInventory().armor.get(i);
			if (stack.is(ItemTagGenerator.KEPT_ON_DEATH)) {
				keptInventory.armor.set(i, stack.copy());
				player.getInventory().armor.set(i, ItemStack.EMPTY);
			}
		}
		if (!player.getInventory().offhand.isEmpty() && player.getInventory().offhand.getFirst().is(ItemTagGenerator.KEPT_ON_DEATH)) {
			keptInventory.offhand.set(0, player.getInventory().offhand.getFirst().copy());
			player.getInventory().offhand.set(0, ItemStack.EMPTY);
		}
	}

	private static boolean keepMainSlot(Inventory keptInventory, Player player, int slot, boolean keptCasket) {
		ItemStack stack = player.getInventory().items.get(slot);
		if (stack.isEmpty()) return keptCasket;
		if (stack.is(TFItems.KEEPSAKE_CASKET.get()) && !keptCasket) {
			if (stack.getCount() > 1) {
				ItemStack kept = stack.copyWithCount(stack.getCount() - 1);
				keptInventory.items.set(slot, kept);
				stack.setCount(1);
			}
			return true;
		}
		keptInventory.items.set(slot, stack.copy());
		player.getInventory().items.set(slot, ItemStack.EMPTY);
		return keptCasket;
	}

	private static boolean keepArmorSlot(Inventory keptInventory, Player player, int slot, boolean keptCasket) {
		ItemStack stack = player.getInventory().armor.get(slot);
		if (stack.isEmpty()) return keptCasket;
		if (stack.is(TFItems.KEEPSAKE_CASKET.get()) && !keptCasket) {
			return true;
		}
		keptInventory.armor.set(slot, stack.copy());
		player.getInventory().armor.set(slot, ItemStack.EMPTY);
		return keptCasket;
	}

	private static void keepOffhandSlot(Inventory keptInventory, Player player, boolean keptCasket) {
		if (player.getInventory().offhand.isEmpty()) return;
		ItemStack stack = player.getInventory().offhand.getFirst();
		if (stack.isEmpty()) return;
		if (stack.is(TFItems.KEEPSAKE_CASKET.get()) && !keptCasket) {
			return;
		}
		keptInventory.offhand.set(0, stack.copy());
		player.getInventory().offhand.set(0, ItemStack.EMPTY);
	}

	private static void stockKeepsakeCasket(Player player) {
		if (!player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && !stack.is(TFItems.KEEPSAKE_CASKET.get()))) {
			keepLonelyCasket(player);
			return;
		}
		ItemStack casket = consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET.get().asItem(), false);
		if (casket.isEmpty()) {
			return;
		}

		Level level = player.level();
		BlockPos.MutableBlockPos pos = player.blockPosition().mutable();
		if (pos.getY() < level.getMinBuildHeight() + 2) {
			pos.setY(level.getMinBuildHeight() + 2);
		} else if (pos.getY() > level.getMaxBuildHeight() - 1) {
			pos.setY(level.getMaxBuildHeight() - 2);
		}

		pos.move(0, -1, 0);
		do {
			pos.move(0, 1, 0);
		} while (pos.getY() < level.getMaxBuildHeight() && !level.getBlockState(pos).canBeReplaced());

		BlockPos immutablePos = pos.immutable();
		FluidState fluidState = level.getFluidState(immutablePos);
		int damage = getPlayerData(player).contains(CASKET_DAMAGE_TAG) ? getPlayerData(player).getInt(CASKET_DAMAGE_TAG) : 0;
		BlockState setState = TFBlocks.KEEPSAKE_CASKET.get().defaultBlockState()
				.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType()))
				.setValue(KeepsakeCasketBlock.BREAKAGE, damage)
				.setValue(KeepsakeCasketBlock.FACING, Direction.from2DDataValue(level.getRandom().nextInt(4)));

		if (player.getRandom().nextFloat() <= 0.15F) {
			if (damage >= 2) {
				setState = TFBlocks.SKULL_CHEST.get().withPropertiesOf(setState);
				TwilightForestMod.LOGGER.debug("{}'s Casket damage value was too high, placing Skull Chest instead", player.getName().getString());
			} else {
				damage++;
				setState = TFBlocks.KEEPSAKE_CASKET.get().withPropertiesOf(setState).setValue(KeepsakeCasketBlock.BREAKAGE, damage);
				TwilightForestMod.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
			}
		}

		if (!level.setBlockAndUpdate(immutablePos, setState)) {
			TwilightForestMod.LOGGER.error("Could not place Keepsake Casket at {}", pos);
			return;
		}
		if (!(level.getBlockEntity(immutablePos) instanceof SkullChestBlockEntity casketBlockEntity)) {
			TwilightForestMod.LOGGER.error("Failed to set Keepsake Casket data at {}", pos);
			return;
		}
		casketBlockEntity.owner = TFConfig.casketUUIDLocking ? new ResolvableProfile(player.getGameProfile()) : null;

		NonNullList<ItemStack> contents = NonNullList.withSize(casketBlockEntity.getContainerSize(), ItemStack.EMPTY);
		int cursor = 0;
		cursor = moveListInto(contents, cursor, player.getInventory().armor);
		cursor += 4;
		cursor = moveListInto(contents, cursor, player.getInventory().offhand);
		moveListInto(contents, cursor, player.getInventory().items);
		casketBlockEntity.setItems(contents);
		casketBlockEntity.setChanged();
		getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		markPlayerData(player);
	}

	private static int moveListInto(NonNullList<ItemStack> contents, int cursor, List<ItemStack> source) {
		for (int i = 0; i < source.size() && cursor < contents.size(); i++, cursor++) {
			contents.set(cursor, source.get(i).copy());
			source.set(i, ItemStack.EMPTY);
		}
		return cursor;
	}

	private static void keepLonelyCasket(Player player) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.is(TFItems.KEEPSAKE_CASKET.get())) {
				Inventory tmp = new Inventory(player);
					if (getPlayerData(player).contains(CHARM_INV_TAG)) {
					tmp.load(getPlayerData(player).getList(CHARM_INV_TAG, Tag.TAG_COMPOUND));
				}
				tmp.add(stack.copy());
				player.getInventory().setItem(i, ItemStack.EMPTY);
				getPlayerData(player).put(CHARM_INV_TAG, tmp.save(new ListTag()));
				markPlayerData(player);
				return;
			}
		}
	}

	private static void returnStoredItems(ServerPlayer player) {
		CompoundTag playerData = getPlayerData(player);
		if (playerData.contains(CHARM_INV_TAG)) {
			Inventory restored = new Inventory(player);
			restored.load(playerData.getList(CHARM_INV_TAG, Tag.TAG_COMPOUND));
			mergeInventory(player, restored);
			playerData.remove(CHARM_INV_TAG);
		}
		if (playerData.contains(CONSUMED_CHARM_TAG)) {
			ItemStack stack = ItemStack.parseOptional(player.registryAccess(), (CompoundTag) playerData.get(CONSUMED_CHARM_TAG));
			sendCharmPacket(player, stack, TFSounds.CHARM_KEEP);
			player.awardStat(net.minecraft.stats.Stats.CUSTOM.get(TFStats.KEEPING_CHARMS_ACTIVATED));
			playerData.remove(CONSUMED_CHARM_TAG);
		}
		markPlayerData(player);
	}

	private static void mergeInventory(ServerPlayer player, Inventory restored) {
		for (int i = 0; i < restored.items.size(); i++) {
			mergeSlot(player, player.getInventory().items, i, restored.items.get(i));
		}
		for (int i = 0; i < restored.armor.size(); i++) {
			mergeSlot(player, player.getInventory().armor, i, restored.armor.get(i));
		}
		for (int i = 0; i < restored.offhand.size(); i++) {
			mergeSlot(player, player.getInventory().offhand, i, restored.offhand.get(i));
		}
	}

	private static void mergeSlot(ServerPlayer player, NonNullList<ItemStack> target, int slot, ItemStack stack) {
		if (stack.isEmpty()) return;
		if (target.get(slot).isEmpty()) {
			target.set(slot, stack.copy());
		} else if (!player.getInventory().add(stack.copy())) {
			player.drop(stack.copy(), false);
		}
	}

	private static ItemStack consumeInventoryItem(Player player, Item item, boolean saveConsumed) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.is(item)) {
				ItemStack consumed = stack.copyWithCount(1);
				stack.shrink(1);
				return consumed;
			}
		}
		return CuriosCompat.consumeCurio(item, player, saveConsumed);
	}

	public static CompoundTag getPlayerData(Player player) {
		return TFDataAttachments.get(player, TFDataAttachments.CHARM_PLAYER_DATA);
	}

	private static void markPlayerData(Player player) {
		TFDataAttachments.set(player, TFDataAttachments.CHARM_PLAYER_DATA, getPlayerData(player));
	}

	private static void sendCharmPacket(ServerPlayer player, ItemStack charm, SoundEvent sound) {
		ResourceKey<SoundEvent> key = ResourceKey.create(Registries.SOUND_EVENT, BuiltInRegistries.SOUND_EVENT.getKey(sound));
		ServerPlayNetworking.send(player, new SpawnCharmPacket(charm, key));
	}

	private enum KeepingMode {
		HELD,
		HOTBAR,
		ALL
	}
}
