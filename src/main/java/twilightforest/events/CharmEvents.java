package twilightforest.events;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import twilightforest.TFMain;
import twilightforest.block.KeepsakeCasketBlock;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.config.TFConfig;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.tags.TFItemTags;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;

public class CharmEvents {

	public static final String CHARM_INV_TAG = "TFCharmInventory";
	public static final String CASKET_DAMAGE_TAG = "CasketDamage";
	public static final String CONSUMED_CHARM_TAG = "CharmStack";

	private static final List<EquipmentSlot> ARMOR_SLOTS = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

	public static void init() {
		ServerLivingEntityEvents.ALLOW_DEATH.register(CharmEvents::applyCharmOfLife);
		ServerLivingEntityEvents.ALLOW_DEATH.register(CharmEvents::applyKeepingAndCasket);
		ServerPlayerEvents.AFTER_RESPAWN.register(CharmEvents::returnItemsOnRespawn);
	}

	private static boolean applyCharmOfLife(LivingEntity living, net.minecraft.world.damagesource.DamageSource source, float amount) {
		//ensure our player is real and in survival before attempting anything
		if (living.level().isClientSide() || !(living instanceof Player player) ||
				player.isCreative() || player.isSpectator()) return true;

		// Executes if the player had charms
		return !handleCharmOfLife(player);
	}

	private static boolean applyKeepingAndCasket(LivingEntity living, net.minecraft.world.damagesource.DamageSource source, float amount) {
		//ensure our player is real and in survival before attempting anything
		if (living.level().isClientSide() || !(living instanceof Player player) ||
				player.isCreative() || player.isSpectator()) return true;

		if (!((net.minecraft.server.level.ServerLevel) living.level()).getGameRules().get(GameRules.KEEP_INVENTORY)) {
			// Did the player recover? No? Let's give them their stuff based on the keeping charms
			handleCharmOfKeeping(player);

			// Then let's store the rest of their stuff in the casket
			stockKeepsakeCasket(player);
		}
		return true;
	}

	private static void returnItemsOnRespawn(ServerPlayer oldPlayer, ServerPlayer serverPlayer, boolean endConquered) {
		if (!endConquered) {
			returnStoredItems(serverPlayer);
		}
	}

	/**
	 * Maybe we kept some stuff for the player!
	 */
	private static void returnStoredItems(Player player) {

		TFMain.LOGGER.debug("Player {} ({}) respawned and received items held in storage", player.getName().getString(), player.getUUID());

		//check if our tag is in the persistent player data. If so, copy that inventory over to our own. Cloud storage at its finest!
		CompoundTag playerData = getPlayerData(player);
		if (!player.level().isClientSide() && playerData.contains(CHARM_INV_TAG)) {
			ListTag tagList = playerData.getListOrEmpty(CHARM_INV_TAG);
			TFItemStackUtils.loadNoClear(player.registryAccess(), tagList, player.getInventory());
			playerData.getListOrEmpty(CHARM_INV_TAG).clear();
			playerData.remove(CHARM_INV_TAG);
		}

		// spawn effect thingers
		if (playerData.contains(CONSUMED_CHARM_TAG)) {
			ItemStack stack = ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, (CompoundTag) playerData.get(CONSUMED_CHARM_TAG)).result().orElse(ItemStack.EMPTY);

			if (player instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, new SpawnCharmPacket(stack, TFSounds.CHARM_KEEP.unwrapKey().orElseThrow()));
				serverPlayer.awardStat(TFStats.KEEPING_CHARMS_ACTIVATED);
			}
			playerData.remove(CONSUMED_CHARM_TAG);
		}
	}

	private static boolean handleCharmOfLife(Player player) {
		boolean charm2 = TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_2, getPlayerData(player), false) || hasCharmCurio(TFItems.CHARM_OF_LIFE_2, player);
		boolean charm1 = !charm2 && (TFItemStackUtils.consumeInventoryItem(player, TFItems.CHARM_OF_LIFE_1, getPlayerData(player), false) || hasCharmCurio(TFItems.CHARM_OF_LIFE_1, player));

		if (charm2 || charm1) {
			if (charm1) {
				player.setHealth(8);
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
			}

			if (charm2) {
				player.setHealth(player.getMaxHealth());

				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 3));
				player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 0));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 0));
			}

			if (player instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, new SpawnCharmPacket(new ItemStack(charm1 ? TFItems.CHARM_OF_LIFE_1 : TFItems.CHARM_OF_LIFE_2), TFSounds.CHARM_LIFE.unwrapKey().orElseThrow()));
				serverPlayer.awardStat(TFStats.LIFE_CHARMS_ACTIVATED);
			}

			return true;
		}

		return false;
	}

	private static void handleCharmOfKeeping(Player player) {
		//create a fake inventory to organize our kept inventory in
		Inventory keepInventory = new Inventory(player, player.equipment);
		ListTag tagList = new ListTag();

		if (!applyCharm(TFItems.CHARM_OF_KEEPING_3, keepInventory, player, player.getInventory().items)) {
			if (!applyCharm(TFItems.CHARM_OF_KEEPING_2, keepInventory, player, player.getInventory().items.subList(0, 9))) {
				int i = player.getInventory().getSelectedSlot();
				if (Inventory.isHotbarSlot(i)) {
					applyCharm(TFItems.CHARM_OF_KEEPING_1, keepInventory, player, NonNullList.of(player.getInventory().items.get(i)));
				}
			}
		}

		//keep all items in the kept_on_death tag. This allows modpacks to support other items to keep on death
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack stack = player.getInventory().items.get(i);
			if (stack.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.items.set(i, stack.copy());
				player.getInventory().items.set(i, ItemStack.EMPTY);
			}
		}

		for (int i = 0; i < ARMOR_SLOTS.size(); i++) {
			ItemStack armor = player.equipment.get(ARMOR_SLOTS.get(i));
			if (armor.is(TFItemTags.KEPT_ON_DEATH)) {
				keepInventory.equipment.set(ARMOR_SLOTS.get(i), armor.copy());
				player.equipment.set(ARMOR_SLOTS.get(i), ItemStack.EMPTY);
			}
		}

		if (getOffhand(player).is(TFItemTags.KEPT_ON_DEATH)) {
			keepInventory.equipment.set(EquipmentSlot.OFFHAND, getOffhand(player).copy());
			setOffhand(player, ItemStack.EMPTY);
		}

		//take our fake inventory and save it to the persistent player data.
		//by saving it there we can guarantee we will always get all of our items back, even if the player logs out and back in.
		if (!keepInventory.isEmpty()) {
			saveInventoryToTag(keepInventory, tagList);
			getPlayerData(player).put(CHARM_INV_TAG, tagList);
		}
	}

	private static boolean applyCharm(Item charm, Inventory keptInventory, Player player, List<ItemStack> inventorySlots) {
		List<ItemStack> mergedCheck = new ArrayList<>(inventorySlots);
		//merge armor and offhand into check slots since theyll always be kept by a charm
		mergedCheck.addAll(getArmorItems(player));
		mergedCheck.add(getOffhand(player));
		//first, check all affected slots to make sure they arent empty.
		//filter out the charm so it doesnt count towards keeping items if its the only thing we are holding
		if (mergedCheck.stream().filter(stack -> !stack.is(charm)).allMatch(ItemStack::isEmpty)) return false;

		//do we even have a charm? No? Then stop operation
		if (!TFItemStackUtils.consumeInventoryItem(player, charm, getPlayerData(player), true) && !hasCharmCurio(charm, player)) return false;

		boolean keptACasket = keepWholeListAndCheckCasket(keptInventory.items, inventorySlots, charm == TFItems.CHARM_OF_KEEPING_3);
		keptACasket = keepWholeListAndCheckCasket(keptInventory.items, getArmorItems(player), keptACasket);
		keepWholeListAndCheckCasket(keptInventory.items, List.of(getOffhand(player)), keptACasket);

		return true;
	}

	private static void stockKeepsakeCasket(Player player) {
		//make sure we are still actually holding onto items before trying to place a casket
		if (player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && !stack.is(TFItems.KEEPSAKE_CASKET))) {
			boolean casketConsumed = TFItemStackUtils.consumeInventoryItem(player, TFBlocks.KEEPSAKE_CASKET, getPlayerData(player), false);

			if (!casketConsumed)
				return;

			Level level = player.level();
			BlockPos.MutableBlockPos pos = player.blockPosition().mutable();

			if (pos.getY() < level.dimensionType().minY() + 2) {
				pos.setY(level.dimensionType().minY() + 2);
			} else {
				int logicalHeight = player.level().dimensionType().logicalHeight();

				if (pos.getY() > logicalHeight) {
					pos.setY(logicalHeight - 1);
				}
			}

			pos.move(0, -1, 0);

			do {
				pos.move(0, 1, 0);
			} while (!level.getBlockState(pos).canBeReplaced());

			BlockPos immutablePos = pos.immutable();
			FluidState fluidState = level.getFluidState(immutablePos);

			int damage = getPlayerData(player).getIntOr(CASKET_DAMAGE_TAG, 0);
			BlockState setState = TFBlocks.KEEPSAKE_CASKET.defaultBlockState()
				.setValue(BlockLoggingEnum.MULTILOGGED, BlockLoggingEnum.getFromFluid(fluidState.getType()))
				.setValue(KeepsakeCasketBlock.BREAKAGE, damage)
				.setValue(KeepsakeCasketBlock.FACING, Direction.from2DDataValue(level.getRandom().nextInt(3)));

			if (player.getRandom().nextFloat() <= 0.15F) {
				if (damage >= 2) {
					setState = TFBlocks.SKULL_CHEST.withPropertiesOf(setState);
					TFMain.LOGGER.debug("{}'s Casket damage value was too high, placing Skull Chest instead", player.getName().getString());
				} else {
					damage = damage + 1;
					setState = TFBlocks.KEEPSAKE_CASKET.withPropertiesOf(setState).setValue(KeepsakeCasketBlock.BREAKAGE, damage);
					TFMain.LOGGER.debug("{}'s Casket was randomly damaged, applying new damage", player.getName().getString());
				}
			}

			if (!level.setBlockAndUpdate(immutablePos, setState)) {
				TFMain.LOGGER.error("Could not place Keepsake Casket at {}", pos);
				return;
			}

			if (!(level.getBlockEntity(immutablePos) instanceof SkullChestBlockEntity casket)) {
				TFMain.LOGGER.error("Failed to set Keepsake Casket data at {}", pos);
				return;
			}

			if (TFConfig.casketUUIDLocking) {
				//make it so only the player who died can open the chest if our config allows us
				casket.owner = ResolvableProfile.createResolved(player.getGameProfile());
			} else {
				casket.owner = null;
			}

			//some names are way too long for the casket so we'll cut them down
			String modifiedName = player.getName().getString().substring(0, Math.min(12, player.getName().getString().length()));
			casket.name = (Component.literal(modifiedName + "'s " + (level.getRandom().nextInt(1000) == 0 ? "Costco Casket" : casket.getDisplayName().getString())));

			int casketCapacity = casket.getContainerSize();
			List<ItemStack> list = new ArrayList<>(casketCapacity);
			NonNullList<ItemStack> filler = NonNullList.withSize(4, ItemStack.EMPTY);

			// lets add our inventory exactly how it was on us
			list.addAll(TFItemStackUtils.sortArmorForCasket(player));
			for (EquipmentSlot slot : ARMOR_SLOTS) player.equipment.set(slot, ItemStack.EMPTY);
			list.addAll(filler);
			list.add(getOffhand(player));
			setOffhand(player, ItemStack.EMPTY);
			list.addAll(TFItemStackUtils.sortInvForCasket(player));
			player.getInventory().items.clear();

			casket.setItems(NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[casketCapacity])));
			getPlayerData(player).remove(CASKET_DAMAGE_TAG);
		} else {
			//inventory is empty minus the casket: put the casket into the kept inventory
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				if (player.getInventory().getItem(i).is(TFItems.KEEPSAKE_CASKET)) {
					Inventory tmp = new Inventory(player, player.equipment);
					loadInventoryFromTag(tmp, getPlayerData(player).getListOrEmpty(CHARM_INV_TAG));
					tmp.add(player.getInventory().getItem(i).copy());
					player.getInventory().setItem(i, ItemStack.EMPTY);
					ListTag saved = new ListTag();
					saveInventoryToTag(tmp, saved);
					getPlayerData(player).put(CHARM_INV_TAG, saved);
				}
			}
		}
	}

	private static ItemStack getOffhand(Player player) {
		return player.equipment.get(EquipmentSlot.OFFHAND);
	}

	private static void setOffhand(Player player, ItemStack stack) {
		player.equipment.set(EquipmentSlot.OFFHAND, stack);
	}

	private static List<ItemStack> getArmorItems(Player player) {
		return ARMOR_SLOTS.stream().map(slot -> player.equipment.get(slot)).toList();
	}

	private static void saveInventoryToTag(Inventory inventory, ListTag tagList) {
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			ItemStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, stack).result().ifPresent(tagList::add);
		}
	}

	private static void loadInventoryFromTag(Inventory inventory, ListTag tagList) {
		for (int i = 0; i < tagList.size() && i < inventory.getContainerSize(); i++) {
			ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, tagList.get(i)).result().ifPresent(stack -> inventory.setItem(i, stack));
		}
	}

	public static CompoundTag getPlayerData(Player player) {
		return ((AttachmentTarget) player).getAttachedOrCreate(TFDataAttachments.CHARM_PERSISTENT, CompoundTag::new);
	}

	//transfers a list of items to another
	private static boolean keepWholeListAndCheckCasket(NonNullList<ItemStack> transferTo, List<ItemStack> transferFrom, boolean skipCasketCheck) {
		boolean keptCasket = false;
		for (int i = 0; i < transferFrom.size(); i++) {
			var item = transferFrom.get(i).copy();
			if (skipCasketCheck || (!item.is(TFItems.KEEPSAKE_CASKET) || keptCasket)) {
				transferTo.set(i, item);
				transferFrom.set(i, ItemStack.EMPTY);
			} else {
				keptCasket = true;
				if (item.getCount() > 1) {
					item.shrink(1);
					transferTo.set(i, item);
					transferFrom.set(i, item.copyWithCount(1));
				}
			}
		}
		return keptCasket || skipCasketCheck;
	}

	private static boolean hasCharmCurio(Item item, Player player) {
		// The curios integration is not ported to Fabric yet.
		return false;
	}
}
