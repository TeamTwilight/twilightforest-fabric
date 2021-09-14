package twilightforest.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import twilightforest.TFEventListener;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.item.FullbrightBakedModel;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.extensions.IEffectsEx;
import twilightforest.item.TFItems;
import java.util.Map;
import java.util.Objects;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public class TFClientEvents {

	public static class ModBusEvents {

		public static void modelBake(Map<ResourceLocation, BakedModel> event) {
			fullbrightItem(event, TFItems.fiery_ingot);
			fullbrightItem(event, TFItems.fiery_boots);
			fullbrightItem(event, TFItems.fiery_chestplate);
			fullbrightItem(event, TFItems.fiery_helmet);
			fullbrightItem(event, TFItems.fiery_leggings);
			fullbrightItem(event, TFItems.fiery_pickaxe);
			fullbrightItem(event, TFItems.fiery_sword);
			fullbright(event, Registry.BLOCK.getKey(TFBlocks.fiery_block), "");
		}

		private static void fullbrightItem(Map<ResourceLocation, BakedModel> event, Item item) {
			fullbright(event, Objects.requireNonNull(Registry.ITEM.getKey(item)), "inventory");
		}

		private static void fullbright(Map<ResourceLocation, BakedModel> event, ResourceLocation rl, String state) {
			ModelResourceLocation mrl = new ModelResourceLocation(rl, state);
			event.put(mrl, new FullbrightBakedModel(event.get(mrl)));
		}

//		@SubscribeEvent
//		public static void texStitch(TextureStitchEvent.Pre evt) {
//			TextureAtlas map = evt.getMap();

		//FIXME bring back if you can get GradientMappedTexture working
		/*if (TFCompat.IMMERSIVEENGINEERING.isActivated()) {
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "revolvers/shaders/revolver_grip" ), IEShaderRegister.PROCESSED_REVOLVER_GRIP_LAYER, true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "revolvers/shaders/revolver_0"    ), IEShaderRegister.PROCESSED_REVOLVER_LAYER     , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/chemthrower_0"     ), IEShaderRegister.PROCESSED_CHEMTHROW_LAYER    , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/drill_diesel_0"    ), IEShaderRegister.PROCESSED_DRILL_LAYER        , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/railgun_0"         ), IEShaderRegister.PROCESSED_RAILGUN_LAYER      , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "items/shaders/shield_0"          ), IEShaderRegister.PROCESSED_SHIELD_LAYER       , true, EASY_GRAYSCALING_MAP ));
		//	map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", ""                                ), IEShaderRegister.PROCESSED_MINECART_LAYER     , true, EASY_GRAYSCALING_MAP ));
			map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", "blocks/shaders/balloon_0"        ), IEShaderRegister.PROCESSED_BALLOON_LAYER      , true, EASY_GRAYSCALING_MAP ));

			final String[] types = new String[]{ "1_0", "1_2", "1_4", "1_5", "1_6" };

			for (IEShaderRegister.CaseType caseType : IEShaderRegister.CaseType.everythingButMinecart()) {
				for (String type : types) {
					map.setTextureEntry(new GradientMappedTexture(
							IEShaderRegister.ModType.IMMERSIVE_ENGINEERING.provideTex(caseType, type),
							IEShaderRegister.ModType.TWILIGHT_FOREST.provideTex(caseType, type),
							true, EASY_GRAYSCALING_MAP
					));
				}
			}*/

			//TODO: Removed until Tinkers' Construct is available
		/*map.setTextureEntry( new MoltenFieryTexture   ( new ResourceLocation( "minecraft", "blocks/lava_still"  ), RegisterBlockEvent.moltenFieryStill                                        ));
		map.setTextureEntry( new MoltenFieryTexture   ( new ResourceLocation( "minecraft", "blocks/lava_flow"   ), RegisterBlockEvent.moltenFieryFlow                                         ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/lava_still"  ), RegisterBlockEvent.moltenKnightmetalStill, true, KNIGHTMETAL_GRADIENT_MAP  ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/lava_flow"   ), RegisterBlockEvent.moltenKnightmetalFlow , true, KNIGHTMETAL_GRADIENT_MAP  ));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/water_still" ), RegisterBlockEvent.essenceFieryStill     , true, FIERY_ESSENCE_GRADIENT_MAP));
		map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "minecraft", "blocks/water_flow"  ), RegisterBlockEvent.essenceFieryFlow      , true, FIERY_ESSENCE_GRADIENT_MAP));*/
//		}

		public static void registerFabricEvents() {
			ClientTickEvents.START_CLIENT_TICK.register((client -> renderTick(client)));
			ClientTickEvents.END_CLIENT_TICK.register(client -> clientTick());

		}

		//TODO: Fields are unused due to missing compat
	/*public static final GradientNode[] KNIGHTMETAL_GRADIENT_MAP = {
			new GradientNode(0.0f , 0xFF_33_32_32),
			new GradientNode(0.1f , 0xFF_6A_73_5E),
			new GradientNode(0.15f, 0xFF_80_8C_72),
			new GradientNode(0.3f , 0xFF_A3_B3_91),
			new GradientNode(0.6f , 0xFF_C4_D6_AE),
			new GradientNode(1.0f , 0xFF_E7_FC_CD)
	};

	public static final GradientNode[] FIERY_ESSENCE_GRADIENT_MAP = {
			new GradientNode(0.2f, 0xFF_3D_17_17),
			new GradientNode(0.8f, 0xFF_5C_0B_0B)
	};

	public static final GradientNode[] EASY_GRAYSCALING_MAP = {
		new GradientNode(0.0f, 0xFF_80_80_80),
		new GradientNode(0.5f, 0xFF_AA_AA_AA), // AAAAAAaaaaaaaaaaa
		new GradientNode(1.0f, 0xFF_FF_FF_FF)
	};*/


		public static void registerModels() {
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(ShieldLayer.LOC));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(TwilightForestMod.prefix("block/casket_obsidian")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(TwilightForestMod.prefix("block/casket_stone")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(TwilightForestMod.prefix("block/casket_basalt")));
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
//	@SubscribeEvent
//	public static void preOverlay(RenderGameOverlayEvent.Pre event) {
//		if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER) {
//			if (TFEventListener.isRidingUnfriendly(Minecraft.getInstance().player)) {
//				event.setCanceled(true);
//			}
//		}
//	}

	/**
	 * Render effects in first-person perspective
	 */
//	@SubscribeEvent
//	public static void renderWorldLast(RenderWorldLastEvent event) {
//
//		if (!TFConfig.CLIENT_CONFIG.firstPersonEffects) return;
//
//		Options settings = Minecraft.getInstance().options;
//		if (settings.getCameraType() != CameraType.FIRST_PERSON || settings.hideGui) return;
//
//		Entity entity = Minecraft.getInstance().getCameraEntity();
//		if (entity instanceof LivingEntity) {
//			EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
//			if (renderer instanceof LivingEntityRenderer<?,?>) {
//				for (RenderEffect effect : RenderEffect.VALUES) {
//					if (effect.shouldRender((LivingEntity) entity, true)) {
//						effect.render((LivingEntity) entity, ((LivingEntityRenderer<?,?>) renderer).getModel(), 0.0, 0.0, 0.0, event.getPartialTicks(), true);
//					}
//				}
//			}
//		}
//	}

	/**
	 * On the tick, we kill the vignette
	 */
	public static void renderTick(Minecraft minecraft) {
		// only fire if we're in the twilight forest
		if (minecraft.level != null && "twilightforest".equals(minecraft.level.dimension().location().getNamespace())) {
			// vignette
			if (minecraft.gui != null) {
				minecraft.gui.vignetteBrightness = 0.0F;
			}
		}//*/

		if (minecraft.player != null && TFEventListener.isRidingUnfriendly(minecraft.player)) {
			if (minecraft.gui != null) {
				minecraft.gui.setOverlayMessage(TextComponent.EMPTY, false);
			}
		}
	}

	public static void clientTick() {
		time++;

		Minecraft mc = Minecraft.getInstance();
		float partial = mc.getFrameTime();

		rotationTickerI = (rotationTickerI >= 359 ? 0 : rotationTickerI + 1);
		sineTickerI = (sineTickerI >= SINE_TICKER_BOUND ? 0 : sineTickerI + 1);

		rotationTicker = rotationTickerI + partial;
		sineTicker = sineTicker + partial;

		BugModelAnimationHelper.animate();
		DimensionSpecialEffects info = DimensionSpecialEffects.EFFECTS.get(TwilightForestMod.prefix("renderer"));

		// add weather box if needed
		if (!mc.isPaused() && mc.level != null && info instanceof TwilightForestRenderInfo) {
			TFWeatherRenderer weatherRenderer = ((IEffectsEx)info).getHandler();
			weatherRenderer.tick();
		}
	}

	@Environment(EnvType.CLIENT)
	private static final MutableComponent WIP_TEXT_0 = new TranslatableComponent("twilightforest.misc.wip0").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	@Environment(EnvType.CLIENT)
	private static final MutableComponent WIP_TEXT_1 = new TranslatableComponent("twilightforest.misc.wip1").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	@Environment(EnvType.CLIENT)
	private static final MutableComponent NYI_TEXT = new TranslatableComponent("twilightforest.misc.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

//	public static void tooltipEvent(ItemTooltipEvent event) {
//		ItemStack item = event.getItemStack();
//
//		// WIP takes precedence over NYI
//		boolean wip = item.is(ItemTagGenerator.WIP);
//		boolean nyi = !wip && item.is(ItemTagGenerator.NYI);
//
//		if (!wip && !nyi)
//			return;
//
//		//if (item.getDisplayName() instanceof MutableComponent displayName)
//		//	displayName/*.append(wip ? " [WIP]" : " [NYI]")*/.setStyle(displayName.getStyle().withColor(ChatFormatting.DARK_GRAY));
//
//		if (wip) {
//			event.getToolTip().add(WIP_TEXT_0);
//			event.getToolTip().add(WIP_TEXT_1);
//		} else {
//			event.getToolTip().add(NYI_TEXT);
//		}
//	}

	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);
}
