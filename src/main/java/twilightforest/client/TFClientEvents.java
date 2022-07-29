package twilightforest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.event.client.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import twilightforest.TFConfig;
import twilightforest.TFEventListener;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.client.model.item.FullbrightBakedModel;
import twilightforest.client.model.item.TintIndexAwareFullbrightBakedModel;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.renderer.entity.ShieldLayer;
import twilightforest.client.renderer.tileentity.TwilightChestRenderer;
import twilightforest.compat.TrinketsCompat;
import twilightforest.compat.TFCompat;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.item.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Environment(EnvType.CLIENT)
public class TFClientEvents {

	public static void init() {
		ModBusEvents.registerModels();
		ModBusEvents.registerLoaders();
		WorldRenderEvents.LAST.register(TFClientEvents::renderWorldLast);
		ModelsBakedCallback.EVENT.register(ModBusEvents::modelBake);
		TextureStitchCallback.PRE.register(ModBusEvents::texStitch);
		RenderTickStartCallback.EVENT.register(TFClientEvents::renderTick);
		ClientTickEvents.END_CLIENT_TICK.register(TFClientEvents::clientTick);
		ItemTooltipCallback.EVENT.register(TFClientEvents::tooltipEvent);
		FOVModifierCallback.PARTIAL_FOV.register(TFClientEvents::FOVUpdate);
		LivingEntityRenderEvents.PRE.register((entity, renderer, partialRenderTick, matrixStack, buffers, light) -> {
			TFClientEvents.unrenderHeadWithTrophies(entity, renderer, partialRenderTick, matrixStack, buffers, light);
			return false;
		});
		LivingEntityRenderEvents.POST.register(TFClientEvents::unrenderHeadWithTrophies);
	}

	public static class ModBusEvents {
		public static void registerLoaders() {
			ModelLoadingRegistry.INSTANCE.registerResourceProvider(manager -> PatchModelLoader.INSTANCE);
		}

		@Deprecated // tterrag said this would become deprecated soon in favor of above method
		public static void modelBake(ModelManager manager, Map<ResourceLocation, BakedModel> models, ModelBakery loader) {
			// TODO Unhardcode, into using Model Deserializers and load from JSON instead
			fullbrightItem(models, TFItems.FIERY_INGOT);
			fullbrightItem(models, TFItems.FIERY_BOOTS);
			fullbrightItem(models, TFItems.FIERY_CHESTPLATE);
			fullbrightItem(models, TFItems.FIERY_HELMET);
			fullbrightItem(models, TFItems.FIERY_LEGGINGS);
			fullbrightItem(models, TFItems.FIERY_PICKAXE);
			fullbrightItem(models, TFItems.FIERY_SWORD);

			fullbrightItem(models, TFItems.RED_THREAD);

			fullbrightBlock(models, TFBlocks.FIERY_BLOCK);

			if(!FabricLoader.getInstance().isModLoaded("ctm")) {
				tintedFullbrightBlock(models, TFBlocks.PINK_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
				tintedFullbrightBlock(models, TFBlocks.BLUE_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
				tintedFullbrightBlock(models, TFBlocks.YELLOW_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
				tintedFullbrightBlock(models, TFBlocks.VIOLET_CASTLE_RUNE_BRICK, FullbrightBakedModel::disableCache);
			}

			if(FabricLoader.getInstance().isModLoaded(TFCompat.IE_ID)) {
//				IECompat.registerShaderModels(models);
			}
		}

		private static void fullbrightItem(Map<ResourceLocation, BakedModel> models, RegistryObject<Item> item) {
			fullbrightItem(models, item, f -> f);
		}

		private static void fullbrightItem(Map<ResourceLocation, BakedModel> models, RegistryObject<Item> item, UnaryOperator<FullbrightBakedModel> process) {
			fullbright(models, Objects.requireNonNull(item.getId()), "inventory", process);
		}

		private static void fullbrightBlock(Map<ResourceLocation, BakedModel> models, RegistryObject<Block> block) {
			fullbrightBlock(models, block, f -> f);
		}

		private static void fullbrightBlock(Map<ResourceLocation, BakedModel> models, RegistryObject<Block> block, UnaryOperator<FullbrightBakedModel> process) {
			fullbright(models, Objects.requireNonNull(block.getId()), "inventory", process);
			fullbright(models, Objects.requireNonNull(block.getId()), "", process);
		}

		private static void fullbright(Map<ResourceLocation, BakedModel> models, ResourceLocation rl, String state, UnaryOperator<FullbrightBakedModel> process) {
			ModelResourceLocation mrl = new ModelResourceLocation(rl, state);
			models.put(mrl, process.apply(new FullbrightBakedModel(models.get(mrl))));
		}

		private static void tintedFullbrightItem(Map<ResourceLocation, BakedModel> models, RegistryObject<Item> item) {
			tintedFullbrightItem(models, item, f -> f);
		}

		private static void tintedFullbrightItem(Map<ResourceLocation, BakedModel> models, RegistryObject<Item> item, UnaryOperator<FullbrightBakedModel> process) {
			tintedFullbright(models, Objects.requireNonNull(item.getId()), "inventory", process);
		}

		private static void tintedFullbrightBlock(Map<ResourceLocation, BakedModel> models, RegistryObject<Block> block) {
			tintedFullbrightBlock(models, block, f -> f);
		}

		private static void tintedFullbrightBlock(Map<ResourceLocation, BakedModel> models, RegistryObject<Block> block, UnaryOperator<FullbrightBakedModel> process) {
			tintedFullbright(models, Objects.requireNonNull(block.getId()), "inventory", process);
			tintedFullbright(models, Objects.requireNonNull(block.getId()), "", process);
		}

		private static void tintedFullbright(Map<ResourceLocation, BakedModel> models, ResourceLocation rl, String state, UnaryOperator<FullbrightBakedModel> process) {
			ModelResourceLocation mrl = new ModelResourceLocation(rl, state);
			models.put(mrl, process.apply(new TintIndexAwareFullbrightBakedModel(models.get(mrl))));
		}

		public static void texStitch(TextureAtlas map, Consumer<ResourceLocation> spriteAdder) {
			if (Sheets.CHEST_SHEET.equals(map.location()))
				TwilightChestRenderer.MATERIALS.values().stream()
						.flatMap(e -> e.values().stream())
						.map(Material::texture)
						.forEach(spriteAdder::accept);

			if (Sheets.BANNER_SHEET.equals(map.location()) || Sheets.SHIELD_SHEET.equals(map.location())) {
				for (BannerPattern pattern : BannerPattern.values()) {
					if (pattern.getFilename().startsWith(TwilightForestMod.ID)) {
						spriteAdder.accept(pattern.location(Sheets.BANNER_SHEET.equals(map.location())));
					}
				}
			}

			spriteAdder.accept(TwilightForestMod.prefix("block/mosspatch"));

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
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(ShieldLayer.LOC));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory")));

			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(TwilightForestMod.prefix("block/casket_obsidian")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(TwilightForestMod.prefix("block/casket_stone")));
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(TwilightForestMod.prefix("block/casket_basalt")));
		}
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
//	@SubscribeEvent TODO: PORT
//	public static void preOverlay(RenderGameOverlayEvent.PreLayer event) {
//		if (event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT) {
//			if (TFEventListener.isRidingUnfriendly(Minecraft.getInstance().player)) {
//				event.setCanceled(true);
//			}
//		}
//	}

	/**
	 * Render effects in first-person perspective
	 */
	public static void renderWorldLast(WorldRenderContext context) {

		if (!TFConfig.CLIENT_CONFIG.firstPersonEffects.get()) return;

		Options settings = Minecraft.getInstance().options;
		if (settings.getCameraType() != CameraType.FIRST_PERSON || settings.hideGui) return;

		Entity entity = Minecraft.getInstance().getCameraEntity();
		if (entity instanceof LivingEntity) {
			EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
			if (renderer instanceof LivingEntityRenderer<?,?>) {
				for (RenderEffect effect : RenderEffect.VALUES) {
					if (effect.shouldRender((LivingEntity) entity, true)) {
						effect.render((LivingEntity) entity, ((LivingEntityRenderer<?,?>) renderer).getModel(), 0.0, 0.0, 0.0, context.tickDelta(), true);
					}
				}
			}
		}
	}

	/**
	 * On the tick, we kill the vignette
	 */
	public static void renderTick() {
		Minecraft minecraft = Minecraft.getInstance();

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

	public static void clientTick(Minecraft client) {
		if (Minecraft.getInstance().isPaused()) return;
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
		if (!mc.isPaused() && mc.level != null && info instanceof TwilightForestRenderInfo tf) {
			TFWeatherRenderer weatherRenderer = tf.getWeatherRenderHandler();
			if (weatherRenderer instanceof TFWeatherRenderer)
				((TFWeatherRenderer) weatherRenderer).tick();
		}
	}

	private static final MutableComponent WIP_TEXT_0 = new TranslatableComponent("twilightforest.misc.wip0").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent WIP_TEXT_1 = new TranslatableComponent("twilightforest.misc.wip1").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
	private static final MutableComponent NYI_TEXT = new TranslatableComponent("twilightforest.misc.nyi").setStyle(Style.EMPTY.withColor(ChatFormatting.RED));

	public static void tooltipEvent(ItemStack item, TooltipFlag context, List<Component> lines) {
		/*
			There's some kinda crash here where the "Tag % used before it was bound" crash happens from
			StaticTagHelper$Wrapper.resolve() because the tag wrapped is null. I assume this crash happens because
			somehow the game attempts to load a tooltip for an item in the main menu or something upon
			resourcepack reload when the player has not loaded into a save. See Issue #1270 for crashlog
		*/

		//TODO do we need this anymore? If so, ho do we re-add it?
//		boolean wip = (ItemTagGenerator.WIP instanceof StaticTagHelper.Wrapper<Item> wrappedWIP) && wrappedWIP.tag != null && item.is(wrappedWIP);
		// WIP takes precedence over NYI
//		boolean nyi = !wip && (ItemTagGenerator.NYI instanceof StaticTagHelper.Wrapper<Item> wrappedNYI) && wrappedNYI.tag != null && item.is(wrappedNYI);

//		if (!wip && !nyi)
//			return;

		//if (item.getDisplayName() instanceof MutableComponent displayName)
		//	displayName/*.append(wip ? " [WIP]" : " [NYI]")*/.setStyle(displayName.getStyle().withColor(ChatFormatting.DARK_GRAY));

		if(!item.is(ItemTagGenerator.WIP) && !item.is(ItemTagGenerator.NYI)) return;

		if (item.is(ItemTagGenerator.WIP)) {
			lines.add(WIP_TEXT_0);
			lines.add(WIP_TEXT_1);
		} else {
			lines.add(NYI_TEXT);
		}
	}

	public static int time = 0;
	private static int rotationTickerI = 0;
	private static int sineTickerI = 0;
	public static float rotationTicker = 0;
	public static float sineTicker = 0;
	public static final float PI = (float) Math.PI;
	private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	public static double FOVUpdate(GameRenderer renderer, Camera camera, double partialTick, double fov) {
		if (camera.getEntity() instanceof LivingEntity living && living.isUsingItem()) {
			Item useItem = living.getUseItem().getItem();
			if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
				float f = (living.getTicksUsingItem() + (float)partialTick) / 20F;
				f = f > 1.0F ? 1.0F : f * f;
				return fov * (1.0F - f * 0.15F);
			}
		}
		return fov;
	}

	public static void unrenderHeadWithTrophies(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialRenderTick, PoseStack matrixStack, MultiBufferSource buffers, int light) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
		boolean visible = !(stack.getItem() instanceof TrophyItem) && !(stack.getItem() instanceof SkullCandleItem) && !areCuriosEquipped(entity);

		if (visible) {
			// Default state is visible, additional visibility setting causes conflict
			return;
		} else {
			if (renderer.getModel() instanceof HeadedModel headedModel) {
				headedModel.getHead().visible = false;
				if (renderer.getModel() instanceof HumanoidModel<?> humanoidModel) {
					humanoidModel.hat.visible = false;
				}
			}
		}
	}

	private static boolean partShown(Entity entity) {
		return !(entity instanceof AbstractClientPlayer player) || player.isModelPartShown(PlayerModelPart.HAT);
	}

	private static boolean areCuriosEquipped(LivingEntity entity) {
		if (FabricLoader.getInstance().isModLoaded(TFCompat.TRINKETS_ID)) {
			return TrinketsCompat.isTrophyCurioEquipped(entity) || TrinketsCompat.isSkullCurioEquipped(entity);
		}
		return false;
	}
}
