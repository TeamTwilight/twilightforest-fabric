package twilightforest.compat.curios;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.network.CreateMovingCicadaSoundPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class CuriosCompat {
	private static boolean bootstrapped;

	private CuriosCompat() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		Trinket twilightTrinket = new TwilightTrinket();
		register(twilightTrinket,
			TFItems.CHARM_OF_KEEPING_1.get(), TFItems.CHARM_OF_KEEPING_2.get(), TFItems.CHARM_OF_KEEPING_3.get(),
			TFItems.CHARM_OF_LIFE_1.get(), TFItems.CHARM_OF_LIFE_2.get(),
			TFBlocks.NAGA_TROPHY.get().asItem(), TFBlocks.LICH_TROPHY.get().asItem(), TFBlocks.MINOSHROOM_TROPHY.get().asItem(),
			TFBlocks.HYDRA_TROPHY.get().asItem(), TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem(), TFBlocks.UR_GHAST_TROPHY.get().asItem(),
			TFBlocks.ALPHA_YETI_TROPHY.get().asItem(), TFBlocks.SNOW_QUEEN_TROPHY.get().asItem(), TFBlocks.QUEST_RAM_TROPHY.get().asItem(),
			TFBlocks.CICADA.get().asItem(), TFBlocks.FIREFLY.get().asItem(), TFBlocks.MOONWORM.get().asItem(),
			TFBlocks.SKELETON_SKULL_CANDLE.get().asItem(), TFBlocks.WITHER_SKELETON_SKULL_CANDLE.get().asItem(),
			TFBlocks.ZOMBIE_SKULL_CANDLE.get().asItem(), TFBlocks.CREEPER_SKULL_CANDLE.get().asItem(),
			TFBlocks.PLAYER_SKULL_CANDLE.get().asItem(), TFBlocks.PIGLIN_SKULL_CANDLE.get().asItem()
		);
	}

	public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return TrinketsApi.getTrinketComponent(entity)
			.map(component -> component.isEquipped(stackPredicate))
			.orElse(false);
	}

	public static boolean isCurioEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return isCurioEquipped(entity, stackPredicate);
	}

	public static boolean findAndConsumeCurio(Item item, Player player) {
		return !consumeCurio(item, player, true).isEmpty();
	}

	public static ItemStack consumeCurio(Item item, Player player, boolean saveConsumed) {
		return TrinketsApi.getTrinketComponent(player).map(component -> {
			final ItemStack[] found = {ItemStack.EMPTY};
			component.forEach((slot, stack) -> {
				if (found[0].isEmpty() && stack.is(item)) {
					found[0] = stack;
				}
			});
			ItemStack stack = found[0];
			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack consumed = stack.copyWithCount(1);
			if (saveConsumed) {
				CharmEvents.getPlayerData(player).put(CharmEvents.CONSUMED_CHARM_TAG, consumed.save(player.registryAccess(), new CompoundTag()));
			}
			stack.shrink(1);
			return consumed;
		}).orElse(ItemStack.EMPTY);
	}

	public static List<ItemStack> findEquippedStacks(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		List<ItemStack> stacks = new ArrayList<>();
		TrinketsApi.getTrinketComponent(entity).ifPresent(component ->
			component.forEach((slot, stack) -> {
				if (stackPredicate.test(stack)) {
					stacks.add(stack);
				}
			}));
		return stacks;
	}

	private static void register(Trinket trinket, Item... items) {
		for (Item item : items) {
			TrinketsApi.registerTrinket(item, trinket);
		}
	}

	private static void sendCicadaSound(LivingEntity living) {
		if (living.level().isClientSide()) return;
		CreateMovingCicadaSoundPacket packet = new CreateMovingCicadaSoundPacket(living.getId());
		for (net.minecraft.server.level.ServerPlayer watcher : PlayerLookup.tracking(living)) {
			if (ServerPlayNetworking.canSend(watcher, CreateMovingCicadaSoundPacket.TYPE)) {
				ServerPlayNetworking.send(watcher, packet);
			}
		}
		if (living instanceof net.minecraft.server.level.ServerPlayer self && ServerPlayNetworking.canSend(self, CreateMovingCicadaSoundPacket.TYPE)) {
			ServerPlayNetworking.send(self, packet);
		}
	}

	private static final class TwilightTrinket implements Trinket {
		@Override
		public Holder<SoundEvent> getEquipSound(ItemStack stack, SlotReference slot, LivingEntity entity) {
			return SoundEvents.ARMOR_EQUIP_GENERIC;
		}

		@Override
		public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
			if (stack.is(TFBlocks.CICADA.get().asItem()) && !entity.level().isClientSide()
					&& !entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).is(TFBlocks.CICADA.get().asItem())) {
				sendCicadaSound(entity);
			}
		}

		@Override
		public boolean canEquipFromUse(ItemStack stack, LivingEntity entity) {
			return true;
		}

		@Override
		public TrinketEnums.DropRule getDropRule(ItemStack stack, SlotReference slot, LivingEntity entity) {
			if (entity instanceof Player player) {
				CompoundTag playerData = CharmEvents.getPlayerData(player);
				if (!player.level().isClientSide() && playerData.contains(CharmEvents.CONSUMED_CHARM_TAG)
						&& playerData.contains(CharmEvents.CHARM_INV_TAG)
						&& !playerData.getList(CharmEvents.CHARM_INV_TAG, 10).isEmpty()) {
					return TrinketEnums.DropRule.KEEP;
				}
			}
			return TrinketEnums.DropRule.DEFAULT;
		}
	}
}
