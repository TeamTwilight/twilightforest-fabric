package twilightforest.compat;

import dev.emi.trinkets.TrinketsMain;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import twilightforest.block.CritterBlock;
import twilightforest.block.TFBlocks;
import twilightforest.compat.curios.CharmOfKeepingRenderer;
import twilightforest.compat.curios.CharmOfLife1NecklaceRenderer;
import twilightforest.compat.curios.CharmOfLife2NecklaceRenderer;
import twilightforest.compat.curios.CurioHeadRenderer;
import twilightforest.item.CuriosCharmItem;
import twilightforest.item.TFItems;
import twilightforest.item.TrophyItem;

public class CuriosCompat extends TFCompat {

	public CuriosCompat() {
		super("Curios");
	}

	@Override
	protected boolean preInit() {
		return true;
	}

	@Override
	protected void init() {
	}

	@Override
	protected void postInit() {
		RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, object) -> {
			if(object instanceof BlockItem blockItem && blockItem.getBlock() instanceof CritterBlock)
				CuriosCompat.setupCuriosCapability(object);
			if (object instanceof TrophyItem)
				CuriosCompat.setupCuriosCapability(object);
			if (object instanceof CuriosCharmItem)
				CuriosCompat.setupCuriosCapability(object);
		});
	}

	@Override
	protected void handleIMCs() {
//		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
//		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
	}

	@Override
	protected void initItems() {
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
//	public static void keepCurios(DropRulesEvent event) { TODO: PORT
//		if (event.getEntityLiving() instanceof Player player) {
//			CompoundTag playerData = TFEventListener.getPlayerData(player);
//			if (!player.level.isClientSide() && playerData.contains(TFEventListener.CHARM_INV_TAG) && !playerData.getList(TFEventListener.CHARM_INV_TAG, 10).isEmpty()) {
//				//Keep all Curios items
//				CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(modifiable -> {
//					for (int i = 0; i < modifiable.getSlots(); ++i) {
//						int finalI = i;
//						event.addOverride(stack -> stack == modifiable.getStackInSlot(finalI), ICurio.DropRule.ALWAYS_KEEP);
//					}
//				});
//			}
//		}
//	}

	public static void registerCurioRenderers(Minecraft client) {
//		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_1.get(), new CharmOfLife1NecklaceRenderer()::render);
//		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_2.get(), new CharmOfLife2NecklaceRenderer()::render);
//		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_1.get(), new CharmOfKeepingRenderer()::render);
//		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_2.get(), new CharmOfKeepingRenderer()::render);
//		TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_3.get(), new CharmOfKeepingRenderer()::render);

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
	}
}
