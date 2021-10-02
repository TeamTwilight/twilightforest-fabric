package twilightforest.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import twilightforest.TFConstants;
import twilightforest.TFEventListener;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.item.FullbrightBakedModel;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.data.ItemTagGenerator;
import twilightforest.item.TFItems;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.StaticTagHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class TFClientEvents {
	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);

	public static void showOptifineWarning(Screen screen) {
		if (TFClientSetup.optifinePresent && !TFClientSetup.optifineWarningShown && !TFClientSetup.CLIENT_CONFIG.disableOptifineNagScreen && screen instanceof TitleScreen) {
			TFClientSetup.optifineWarningShown = true;
			Minecraft.getInstance().setScreen(new OptifineWarningScreen(screen));
		}
	}

	public static void registerFabricEvents() {
		ClientTickEvents.START_CLIENT_TICK.register((client -> renderTick(client)));
		ClientTickEvents.END_CLIENT_TICK.register(client -> clientTick());
		ItemTooltipCallback.EVENT.register(((stack, context, lines) -> tooltipEvent(stack, lines)));
	}

	public static void registerModels() {
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(ShieldLayer.LOC));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(new ModelResourceLocation(TFConstants.prefix("trophy"), "inventory")));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(new ModelResourceLocation(TFConstants.prefix("trophy_minor"), "inventory")));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(new ModelResourceLocation(TFConstants.prefix("trophy_quest"), "inventory")));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(TFConstants.prefix("block/casket_obsidian")));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(TFConstants.prefix("block/casket_stone")));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManger, consumer) -> consumer.accept(TFConstants.prefix("block/casket_basalt")));
	}

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

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	//TODO: PORT
	/*
	public static void preOverlay(RenderGameOverlayEvent.PreLayer event) {
		if (event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT) {
			if (TFEventListener.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		}
	}
	 */

	/**
	 * Render effects in first-person perspective
	 */
	public static void renderWorldLast(float partialTicks) {

		if (!TFClientSetup.CLIENT_CONFIG.firstPersonEffects) return;

		Options settings = Minecraft.getInstance().options;
		if (settings.getCameraType() != CameraType.FIRST_PERSON || settings.hideGui) return;

		Entity entity = Minecraft.getInstance().getCameraEntity();
		if (entity instanceof LivingEntity) {
			EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
			if (renderer instanceof LivingEntityRenderer<?,?>) {
				for (RenderEffect effect : RenderEffect.VALUES) {
					if (effect.shouldRender((LivingEntity) entity, true)) {
						effect.render((LivingEntity) entity, ((LivingEntityRenderer<?,?>) renderer).getModel(), 0.0, 0.0, 0.0, partialTicks, true);
					}
				}
			}
		}
	}

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
		DimensionSpecialEffects info = DimensionSpecialEffects.EFFECTS.get(TFConstants.prefix("renderer"));

		// add weather box if needed
		if (!mc.isPaused() && mc.level != null && info instanceof TwilightForestRenderInfo twilightForestRenderInfo) {
			twilightForestRenderInfo.getWeatherRenderHandler().tick();
		}
	}

	@Environment(EnvType.CLIENT)
	private static final MutableComponent WIP_TEXT_0 = new TranslatableComponent("twilightforest.misc.wip0").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	@Environment(EnvType.CLIENT)
	private static final MutableComponent WIP_TEXT_1 = new TranslatableComponent("twilightforest.misc.wip1").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	@Environment(EnvType.CLIENT)
	private static final MutableComponent NYI_TEXT = new TranslatableComponent("twilightforest.misc.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	public static void tooltipEvent(ItemStack item, List<Component> tooltip) {
		/*
			There's some kinda crash here where the "Tag % used before it was bound" crash happens from
			StaticTagHelper$Wrapper.resolve() because the tag wrapped is null. I assume this crash happens because
			somehow the game attempts to load a tooltip for an item in the main menu or something upon
			resourcepack reload when the player has not loaded into a save. See Issue #1270 for crashlog
		*/
		boolean wip = (ItemTagGenerator.WIP instanceof StaticTagHelper.Wrapper<Item> wrappedWIP) && wrappedWIP.tag != null && item.is(wrappedWIP);
		// WIP takes precedence over NYI
		boolean nyi = !wip && (ItemTagGenerator.NYI instanceof StaticTagHelper.Wrapper<Item> wrappedNYI) && wrappedNYI.tag != null && item.is(wrappedNYI);

		if (!wip && !nyi)
			return;

		//if (item.getDisplayName() instanceof MutableComponent displayName)
		//	displayName/*.append(wip ? " [WIP]" : " [NYI]")*/.setStyle(displayName.getStyle().withColor(ChatFormatting.DARK_GRAY));

		if (wip) {
			tooltip.add(WIP_TEXT_0);
			tooltip.add(WIP_TEXT_1);
		} else {
			tooltip.add(NYI_TEXT);
		}
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
}
