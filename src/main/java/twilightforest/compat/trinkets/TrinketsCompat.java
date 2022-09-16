package twilightforest.compat.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketEnums;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import dev.emi.trinkets.api.event.TrinketDropCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twilightforest.block.CritterBlock;
import twilightforest.compat.trinkets.renderer.CharmOfKeepingRenderer;
import twilightforest.compat.trinkets.renderer.CharmOfLife1NecklaceRenderer;
import twilightforest.compat.trinkets.renderer.CharmOfLife2NecklaceRenderer;
import twilightforest.compat.trinkets.renderer.CurioHeadRenderer;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.item.CuriosCharmItem;
import twilightforest.item.SkullCandleItem;
import twilightforest.item.TrophyItem;

public class TrinketsCompat {

	public static void init() {
		TrinketDropCallback.EVENT.register(TrinketsCompat::keepCurios);
		RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, object) -> {
			if(object instanceof BlockItem blockItem && blockItem.getBlock() instanceof CritterBlock)
				TrinketsCompat.setupCuriosCapability(object);
			if (object instanceof TrophyItem)
				TrinketsCompat.setupCuriosCapability(object);
			if (object instanceof CuriosCharmItem)
				TrinketsCompat.setupCuriosCapability(object);
			if (object instanceof SkullCandleItem)
				TrinketsCompat.setupCuriosCapability(object);
		});
	}

	public static void setupCuriosCapability(Item item) {
		TrinketsApi.registerTrinket(item, new Trinket() {
			@Override
			public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
				entity.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
			}
		});
	}

	//if we have any curios and die with a charm of keeping on us, keep our curios instead of dropping them
	public static TrinketEnums.DropRule keepCurios(TrinketEnums.DropRule rule, ItemStack stack, SlotReference ref, LivingEntity entity) {
		if (entity instanceof Player player) {
			CompoundTag playerData = CharmEvents.getPlayerData(player);
			if (!player.level.isClientSide() && playerData.contains(CharmEvents.CHARM_INV_TAG) && !playerData.getList(CharmEvents.CHARM_INV_TAG, 10).isEmpty()) {
				//Keep all Curios items
				return TrinketEnums.DropRule.KEEP;
			}
		}
		return rule;
	}

	public static void registerCurioRenderers(Minecraft client) {
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_1.get(), CharmOfLife1NecklaceRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_2.get(), CharmOfLife2NecklaceRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_1.get(), CharmOfKeepingRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_2.get(), CharmOfKeepingRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_3.get(), CharmOfKeepingRenderer::render);

		TrinketRendererRegistry.registerRenderer(TFBlocks.NAGA_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.LICH_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.MINOSHROOM_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.HYDRA_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.UR_GHAST_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.ALPHA_YETI_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.SNOW_QUEEN_TROPHY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.QUEST_RAM_TROPHY.get().asItem(), CurioHeadRenderer::render);

		TrinketRendererRegistry.registerRenderer(TFBlocks.CICADA.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.FIREFLY.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.MOONWORM.get().asItem(), CurioHeadRenderer::render);

		TrinketRendererRegistry.registerRenderer(TFBlocks.CREEPER_SKULL_CANDLE.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.PLAYER_SKULL_CANDLE.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.SKELETON_SKULL_CANDLE.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get().asItem(), CurioHeadRenderer::render);
		TrinketRendererRegistry.registerRenderer(TFBlocks.ZOMBIE_SKULL_CANDLE.get().asItem(), CurioHeadRenderer::render);
	}

	public static boolean isTrophyCurioEquipped(LivingEntity entity) {
		return entity.getComponent(TrinketsApi.TRINKET_COMPONENT).isEquipped(stack -> stack.getItem() instanceof TrophyItem);
	}

	public static boolean isSkullCurioEquipped(LivingEntity entity) {
		return entity.getComponent(TrinketsApi.TRINKET_COMPONENT).isEquipped(stack -> stack.getItem() instanceof SkullCandleItem);
	}
}
