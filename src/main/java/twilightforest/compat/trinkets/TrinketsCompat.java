package twilightforest.compat.trinkets;

import dev.emi.trinkets.api.*;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import io.github.fabricators_of_create.porting_lib.registry.DeferredBlock;
import io.github.fabricators_of_create.porting_lib.registry.DeferredItem;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.trinkets.model.CharmOfLifeNecklaceModel;
import twilightforest.compat.trinkets.renderer.CharmOfKeepingRenderer;
import twilightforest.compat.trinkets.renderer.CharmOfLifeNecklaceRenderer;
import twilightforest.compat.trinkets.renderer.TrinketHeadRenderer;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.network.CreateMovingCicadaSoundPacket;
import twilightforest.network.PacketDistributor;

import java.util.List;
import java.util.function.Predicate;

public class TrinketsCompat {
	public static void init() {
		registerTrinketsCapabilities();
		TrinketDropCallback.EVENT.register((rule, stack, ref, entity) -> {
			if (entity instanceof Player player) {
				CompoundTag playerData = CharmEvents.getPlayerData(player);
				if (!player.level().isClientSide() && playerData.contains(CharmEvents.CONSUMED_CHARM_TAG) && playerData.contains(CharmEvents.CHARM_INV_TAG) && !playerData.getList(CharmEvents.CHARM_INV_TAG, 10).isEmpty()) {
					TrinketComponent component = TrinketsApi.getTrinketComponent(player).orElse(null);
					if (component != null) {
						for (Tuple<SlotReference, ItemStack> tuple : component.getAllEquipped()) {
							if (tuple.getB() == stack) {
								return TrinketEnums.DropRule.KEEP;
							}
						}
					}
				}
			}
			return rule;
		});
	}

	public static void registerTrinketsCapabilities() {
		Trinket trinket = new Trinket() {
			@Override
			public Holder<SoundEvent> getEquipSound(ItemStack stack, SlotReference slot, LivingEntity entity) {
				return SoundEvents.ARMOR_EQUIP_GENERIC;
			}

			@Override
			public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
				if (!entity.getItemBySlot(EquipmentSlot.HEAD).is(TFBlocks.CICADA.get().asItem())) {
					if (stack.is(TFBlocks.CICADA.get().asItem()) && !entity.level().isClientSide()) {
						PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new CreateMovingCicadaSoundPacket(entity.getId()));
					}
				}
			}

			@Override
			public boolean canEquipFromUse(ItemStack stack, LivingEntity entity) {
				return true;
			}
		};

		for (DeferredItem<Item> item : List.of(TFItems.CHARM_OF_KEEPING_1, TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_LIFE_1, TFItems.CHARM_OF_LIFE_2,
			TFItems.NAGA_TROPHY, TFItems.LICH_TROPHY, TFItems.MINOSHROOM_TROPHY, TFItems.HYDRA_TROPHY, TFItems.KNIGHT_PHANTOM_TROPHY,
			TFItems.UR_GHAST_TROPHY, TFItems.ALPHA_YETI_TROPHY, TFItems.SNOW_QUEEN_TROPHY, TFItems.QUEST_RAM_TROPHY, TFItems.SKELETON_SKULL_CANDLE, TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.ZOMBIE_SKULL_CANDLE, TFItems.CREEPER_SKULL_CANDLE, TFItems.PLAYER_SKULL_CANDLE, TFItems.PIGLIN_SKULL_CANDLE)
		) {
			TrinketsApi.registerTrinket(item.value(), trinket);
		}

		for (DeferredBlock<Block> block : List.of(TFBlocks.CICADA, TFBlocks.FIREFLY, TFBlocks.MOONWORM)) {
			TrinketsApi.registerTrinket(block.asItem(), trinket);
		}
	}

	public static void registerTrinketLayers() {
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHARM_OF_LIFE, CharmOfLifeNecklaceModel::create);
	}

	public static void registerTrinketRenderers() {
		TrinketRendererRegistry.registerRenderer(
			TFItems.CHARM_OF_LIFE_1.get(),
			new CharmOfLifeNecklaceRenderer(
				FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.5F, 0.5F)
			)
		);
		TrinketRendererRegistry.registerRenderer(
			TFItems.CHARM_OF_LIFE_2.get(),
			new CharmOfLifeNecklaceRenderer(
				FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.9F, 0.0F)
			)
		);

		CharmOfKeepingRenderer keeping = new CharmOfKeepingRenderer();
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_1.get(), keeping);
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_2.get(), keeping);
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_3.get(), keeping);


		TrinketHeadRenderer head = new TrinketHeadRenderer();
		TrinketRendererRegistry.registerRenderer(TFItems.NAGA_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.LICH_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.MINOSHROOM_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.HYDRA_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.KNIGHT_PHANTOM_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.UR_GHAST_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.ALPHA_YETI_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.SNOW_QUEEN_TROPHY.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.QUEST_RAM_TROPHY.get(), head);

		TrinketRendererRegistry.registerRenderer(TFBlocks.CICADA.get().asItem(), head);
		TrinketRendererRegistry.registerRenderer(TFBlocks.FIREFLY.get().asItem(), head);
		TrinketRendererRegistry.registerRenderer(TFBlocks.MOONWORM.get().asItem(), head);

		TrinketRendererRegistry.registerRenderer(TFItems.CREEPER_SKULL_CANDLE.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.PIGLIN_SKULL_CANDLE.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.PLAYER_SKULL_CANDLE.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.SKELETON_SKULL_CANDLE.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), head);
		TrinketRendererRegistry.registerRenderer(TFItems.ZOMBIE_SKULL_CANDLE.get(), head);
	}

	public static boolean isTrinketEquipped(LivingEntity entity, Predicate<ItemStack> predicate) {
		return TrinketsApi.getTrinketComponent(entity)
			.map(component -> !component.getEquipped(predicate).isEmpty())
			.orElse(false);
	}

	public static boolean findAndConsumeTrinket(Item item, Player player) {
		return TrinketsApi.getTrinketComponent(player).map(component -> {
			for (var tuple : component.getEquipped(stack -> stack.is(item))) {
				ItemStack stack = tuple.getB();

				CharmEvents.getPlayerData(player)
					.put(CharmEvents.CONSUMED_CHARM_TAG, stack.save(player.registryAccess()));

				stack.shrink(1);
				return true;
			}
			return false;
		}).orElse(false);
	}
}