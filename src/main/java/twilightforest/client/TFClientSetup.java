package twilightforest.client;

import io.github.fabricators_of_create.porting_lib.client.armor.ArmorRendererRegistry;
import io.github.fabricators_of_create.porting_lib.client_extensions.ClientExtensionsRegistry;
import io.github.fabricators_of_create.porting_lib.client_extensions.IClientBlockExtensions;
import io.github.fabricators_of_create.porting_lib.models.geometry.RegisterGeometryLoadersCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.packs.PackType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.event.*;
import twilightforest.client.model.SeparateTransformsModel;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.block.ConditionalMippedModel;
import twilightforest.client.model.block.Experiment115Model;
import twilightforest.client.model.block.PortingLibEmissiveModel;
import twilightforest.client.model.block.TorchberryPlantModel;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModelLoader;
import twilightforest.client.model.block.carpet.RoyalRagsModelLoader;
import twilightforest.client.model.block.connected.ConnectedTextureModelLoader;
import twilightforest.client.model.block.doors.CastleDoorModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.item.ForceFieldItemModel;
import twilightforest.client.model.block.giantblock.GiantBlockModelLoader;
import twilightforest.client.model.block.patch.PatchModelLoader;

import twilightforest.client.model.entity.*;
import twilightforest.client.model.item.TravellersGearItemModel;
import twilightforest.client.particle.*;
import twilightforest.client.renderer.armor.TFArmorRenderer;
import twilightforest.client.renderer.armor.TFSimpleArmorRenderer;
import twilightforest.client.renderer.block.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.renderer.map.ConqueredMapIconRenderer;
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer;
import twilightforest.client.renderer.tooltip.ItemDisplayTooltipComponent;
import twilightforest.client.renderer.tooltip.PotionFlaskTooltipComponent;
import twilightforest.client.renderer.tooltip.TravellersBeltTooltipComponent;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.events.RegistrationEvents;
import twilightforest.init.*;
import twilightforest.item.*;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.TravellersGogglesItem;
import twilightforest.util.woods.TFWoodTypes;

import java.util.function.Predicate;

public class TFClientSetup implements ClientModInitializer {

	private static boolean optifinePresent = false;

	@Override
	public void onInitializeClient() {
		initializeClientEvents();

		// Detect optifine
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}

		// Key bindings
		TFKeyBinds.KEY_MAPPINGS.forEach(KeyBindingHelper::registerKeyBinding);

		// Wood types - register via reflection since WoodType.register() is private in 1.21.1
		registerWoodType(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE);
		registerWoodType(TFWoodTypes.CANOPY_WOOD_TYPE);
		registerWoodType(TFWoodTypes.MANGROVE_WOOD_TYPE);
		registerWoodType(TFWoodTypes.DARK_WOOD_TYPE);
		registerWoodType(TFWoodTypes.TIME_WOOD_TYPE);
		registerWoodType(TFWoodTypes.TRANSFORMATION_WOOD_TYPE);
		registerWoodType(TFWoodTypes.MINING_WOOD_TYPE);
		registerWoodType(TFWoodTypes.SORTING_WOOD_TYPE);

		registerEntityRenderers();
		registerLayerDefinitions();
		registerParticleFactories();
		registerScreens();
		registerModelLoaders();
		registerModelEvents();
		registerColorHandlers();
		registerDimEffects();
		registerReloadListeners();
		registerClientExtensions();
		registerMapDecorators();
		registerTooltipComponents();
		registerRenderLayers();

		// Shaders
		TFShaders.registerShaders();

		// Register client-side network packet handlers
		RegistrationEvents.registerClientPacketHandlers();

		// Compat
		if (FabricLoader.getInstance().isModLoaded("cosmeticarmorreworked")) {
			// Will be handled via mixin/event
		}
	}

	private void initializeClientEvents() {
		ClientGameEvents.init();
		CloudEvents.init();
		FoliageColorHandler.init();
		LockedBiomeToastHandler.init();
		OverlayHandler.init();
		TravellersClientEvents.init();
	}

	private void registerModelLoaders() {
		RegisterGeometryLoadersCallback.EVENT.register(loaders -> {
			loaders.put(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("giant_block"), GiantBlockModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("connected_texture_block"), ConnectedTextureModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("castle_door"), CastleDoorModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("noise_varying"), NoiseVaryingModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("royal_rags"), RoyalRagsModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("travellers_gear"), TravellersGearItemModel.Loader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("force_field"), ForceFieldModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("force_field_item"), ForceFieldItemModel.Loader.INSTANCE);

			// FIXME: Try to get Porting Lib model working
			loaders.put(SeparateTransformsModel.ID, SeparateTransformsModel.Loader.INSTANCE);
		});
	}

	private void registerModelEvents() {
		ModelLoadingPlugin.register(pluginContext -> {

			// Register additional models
			pluginContext.addModels(
				ShieldLayer.SHIELD_MODEL,
				new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory").id(),
				new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory").id(),
				new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory").id()
			);

			for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
				ResourceLocation location = lid.resourceLocation();
				String name = location.getPath();
				if (lid.customPath() != null) name = lid.customPath();
				pluginContext.addModels(TwilightForestMod.prefix("block/lid/" + name));
			}

			// Use Fabric API's modifyModelAfterBake for model modifications and jar lid caching
			pluginContext.modifyModelAfterBake().register((model, context) -> {
				Predicate<ResourceLocation> mippedIDs = location -> location.getNamespace().equals(TwilightForestMod.ID) &&
					((location.getPath().contains("leaves") && !location.getPath().contains("dark")) ||
						location.getPath().contains("_bush") ||
						location.getPath().contains("_oreberry"));

				BakedModel result = model;

				if (mippedIDs.test(context.resourceId() != null ? context.resourceId() : context.topLevelId().id())) {
					result = new ConditionalMippedModel(result);
				}

				// Cache jar lids - match on resourceId which is the plain ResourceLocation for standalone models
				var resourceId = context.resourceId();
				if (resourceId != null) {
					JarRenderer.LID_LOCATION_LIST.get().forEach(lid -> {
						String name = lid.resourceLocation().getPath();
						if (lid.customPath() != null) name = lid.customPath();
						if (resourceId.equals(TwilightForestMod.prefix("block/lid/" + name))) {
							JarRenderer.LIDS.put(lid.lid(), model);
						}
					});
				}

				// The torchberry plant's berries layer must render emissively. Porting Lib's
				// porting_lib_data renderer is gone on 1.21.1, so wrap the baked model to apply
				// a full-brightness material to the glow texture quads at render time.
				boolean isTorchberryPlant = context.topLevelId() != null
					&& context.topLevelId().equals(new ModelResourceLocation(TwilightForestMod.prefix("torchberry_plant"), "has_torchberries=true"));
				if (isTorchberryPlant) {
					result = new TorchberryPlantModel(result, TwilightForestMod.prefix("block/torchberry_plant_glow"));
				}

				// Same for the experiment 115 cake's sprinkle layer while regenerating.
				// Porting Lib's porting_lib_data renderer is gone on 1.21.1, so wrap the
				// baked model to make the sprinkle texture quads full-brightness.
				boolean isExperiment115Regenerating = context.resourceId() != null
					&& context.resourceId().getPath().startsWith("block/experiment115")
					&& context.resourceId().getPath().endsWith("_regenerating");
				if (isExperiment115Regenerating) {
					result = new Experiment115Model(result, TwilightForestMod.prefix("block/experiment115/experiment115_sprinkle"));
				}

				// Tower device blocks (antibuilder, carminite reactor, vanishing block, etc.)
				// used Porting Lib's porting_lib_data (block_light/sky_light) to render their
				// glowing texture overlays at full brightness. Porting Lib's renderer module
				// was removed in 1.21.1, so wrap these baked models to make quads with
				// tower_device_level_* textures render emissively via the Fabric Renderer API.
				if (result == model) {
					// resourceId can be null for block-state models; fall back to topLevelId().id()
					var emissiveId = context.resourceId() != null ? context.resourceId() : context.topLevelId().id();
					if (needsEmissiveWrapper(emissiveId.getPath())) {
						boolean translucent = needsTranslucentBlend(emissiveId.getPath());
						result = new PortingLibEmissiveModel(result, translucent);
					}
				}

				return result;
			});
		});

		// Register item properties
		registerItemProperties();
	}

	private void registerItemProperties() {
		ItemProperties.register(TFItems.CUBE_OF_ANNIHILATION.get(), TwilightForestMod.prefix("thrown"), (stack, level, entity, idk) ->
			stack.get(TFDataComponents.THROWN_PROJECTILE.get()) != null ? 1 : 0);

		ItemProperties.register(TFItems.KNIGHTMETAL_SHIELD.get(), ResourceLocation.parse("blocking"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.MOON_DIAL.get(), ResourceLocation.parse("phase"), new ClampedItemPropertyFunction() {
			@Override
			public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entityBase, int idk) {
				boolean flag = entityBase != null;
				Entity entity = flag ? entityBase : stack.getFrame();
				if (level == null && entity != null) level = (ClientLevel) entity.level();
				return level == null ? 0.0F : (float) (level.dimensionType().natural() ? Mth.frac(level.getMoonPhase() / 8.0f) : this.wobble(level, Math.random()));
			}

			double rotation;
			double rota;
			long lastUpdateTick;

			private double wobble(Level level, double rotation) {
				if (level.getGameTime() != this.lastUpdateTick) {
					this.lastUpdateTick = level.getGameTime();
					double delta = rotation - this.rotation;
					delta = Mth.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
					this.rota += delta * 0.1D;
					this.rota *= 0.9D;
					this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0D);
				}
				return this.rotation;
			}
		});

		ItemProperties.register(TFItems.ORE_METER.get(), TwilightForestMod.prefix("active"), (stack, level, entity, idk) -> {
			if (OreMeterItem.isLoading(stack)) {
				int totalLoadTime = OreMeterItem.LOAD_TIME + OreMeterItem.getRange(stack) * 25;
				int progress = OreMeterItem.getLoadProgress(stack);
				return progress % 5 >= 2 + (int) (Math.random() * 2) && progress <= totalLoadTime - 15 ? 1 : 0;
			}
			return stack.has(TFDataComponents.ORE_DATA.get()) ? 1 : 0;
		});

		ItemProperties.register(TFItems.MOONWORM_QUEEN.get(), TwilightForestMod.prefix("alt"), (stack, level, entity, idk) -> {
			if (entity != null && entity.getUseItem() == stack) {
				int useTime = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
				if (useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
					return 1;
				}
			}
			return 0;
		});

		ItemProperties.register(TFItems.ENDER_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});
		ItemProperties.register(TFItems.ENDER_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ICE_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});
		ItemProperties.register(TFItems.ICE_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.SEEKER_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});
		ItemProperties.register(TFItems.SEEKER_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});
		ItemProperties.register(TFItems.TRIPLE_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ORE_MAGNET.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else {
				ItemStack itemstack = entity.getUseItem();
				return !itemstack.isEmpty() ? (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
			}
		});
		ItemProperties.register(TFItems.ORE_MAGNET.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFBlocks.RED_THREAD.get().asItem(), TwilightForestMod.prefix("size"), (stack, level, entity, idk) -> {
			if (stack.getCount() >= 32) return 1.0F;
			else if (stack.getCount() >= 16) return 0.5F;
			else if (stack.getCount() >= 4) return 0.25F;
			return 0.0F;
		});

		ItemProperties.register(TFItems.BLOCK_AND_CHAIN.get(), TwilightForestMod.prefix("thrown"), (stack, level, entity, idk) ->
			stack.get(TFDataComponents.THROWN_PROJECTILE.get()) != null ? 1 : 0);

		ItemProperties.register(TFItems.EXPERIMENT_115.get(), Experiment115Item.THINK, (stack, level, entity, idk) ->
			stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()) != null && stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()).equals("think") ? 1 : 0);
		ItemProperties.register(TFItems.EXPERIMENT_115.get(), Experiment115Item.FULL, (stack, level, entity, idk) ->
			stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()) != null && stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()).equals("full") ? 1 : 0);

		ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("breakage"), (stack, level, entity, i) ->
			stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY).breakage());
		ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, level, entity, i) ->
			stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY).doses());
		ItemProperties.register(TFItems.GREATER_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, level, entity, i) ->
			stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY).doses());

		ItemProperties.register(TFItems.CRUMBLE_HORN.get(), TwilightForestMod.prefix("tooting"), (stack, world, entity, i) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}

	private void registerEntityRenderers() {
		// Bosses
		EntityRendererRegistry.register(TFEntities.HYDRA.get(), m -> new HydraRenderer<>(m, new HydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		EntityRendererRegistry.register(TFEntities.LICH.get(), m -> new LichRenderer<>(m, new LichModel<>(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		EntityRendererRegistry.register(TFEntities.NAGA.get(), m -> new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		EntityRendererRegistry.register(TFEntities.MINOSHROOM.get(), m -> new MinoshroomRenderer<>(m, new MinoshroomModel<>(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.UR_GHAST.get(), m -> new UrGhastRenderer<>(m, new UrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24.0F));
		EntityRendererRegistry.register(TFEntities.ALPHA_YETI.get(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		EntityRendererRegistry.register(TFEntities.SNOW_QUEEN.get(), m -> new SnowQueenRenderer<>(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		EntityRendererRegistry.register(TFEntities.KNIGHT_PHANTOM.get(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// Passive
		EntityRendererRegistry.register(TFEntities.BOAR.get(), m -> new BoarRenderer<>(m, new BoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		EntityRendererRegistry.register(TFEntities.BIGHORN_SHEEP.get(), m -> new BighornRenderer(m, new BighornModel<>(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP)), 0.7F));
		EntityRendererRegistry.register(TFEntities.DEER.get(), m -> new TFGenericMobRenderer<>(m, new DeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		EntityRendererRegistry.register(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		EntityRendererRegistry.register(TFEntities.TINY_BIRD.get(), m -> new TinyBirdRenderer<>(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		EntityRendererRegistry.register(TFEntities.SQUIRREL.get(), m -> new TFGenericMobRenderer<>(m, new SquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		EntityRendererRegistry.register(TFEntities.DWARF_RABBIT.get(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		EntityRendererRegistry.register(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		EntityRendererRegistry.register(TFEntities.QUEST_RAM.get(), m -> new QuestRamRenderer<>(m, new QuestRamModel<>(m.bakeLayer(TFModelLayers.QUEST_RAM))));

		// Monsters
		EntityRendererRegistry.register(TFEntities.REDCAP.get(), m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		EntityRendererRegistry.register(TFEntities.REDCAP_SAPPER.get(), m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		EntityRendererRegistry.register(TFEntities.SKELETON_DRUID.get(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), 0.5F, "skeletondruid.png"));
		EntityRendererRegistry.register(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.WRAITH.get(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		EntityRendererRegistry.register(TFEntities.KOBOLD.get(), m -> new TFBipedRenderer<>(m, new KoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		EntityRendererRegistry.register(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		EntityRendererRegistry.register(TFEntities.DEATH_TOME.get(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		EntityRendererRegistry.register(TFEntities.MINOTAUR.get(), m -> new TFBipedRenderer<>(m, new MinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		EntityRendererRegistry.register(TFEntities.FIRE_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, new FireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		EntityRendererRegistry.register(TFEntities.SLIME_BEETLE.get(), m -> new SlimeBeetleRenderer<>(m, new SlimeBeetleModel<>(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), m.bakeLayer(TFModelLayers.SLIME_BEETLE_TAIL), 0.6F));
		EntityRendererRegistry.register(TFEntities.PINCH_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, new PinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTGUARD.get(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GOLEM.get(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		EntityRendererRegistry.register(TFEntities.TOWERWOOD_BORER.get(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		EntityRendererRegistry.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), m -> new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		EntityRendererRegistry.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), m -> new UpperGoblinKnightRenderer<>(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		EntityRendererRegistry.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), m -> new TFBipedRenderer<>(m, new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		EntityRendererRegistry.register(TFEntities.HELMET_CRAB.get(), HelmetCrabRenderer::new);
		EntityRendererRegistry.register(TFEntities.SWARM_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		EntityRendererRegistry.register(TFEntities.KING_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_BROODLING.get(), m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		EntityRendererRegistry.register(TFEntities.HEDGE_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		EntityRendererRegistry.register(TFEntities.MAZE_SLIME.get(), m -> new MazeSlimeRenderer(m, 0.625F));
		EntityRendererRegistry.register(TFEntities.YETI.get(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		EntityRendererRegistry.register(TFEntities.TROLL.get(), m -> new TFBipedRenderer<>(m, new TrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		EntityRendererRegistry.register(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		EntityRendererRegistry.register(TFEntities.SNOW_GUARDIAN.get(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		EntityRendererRegistry.register(TFEntities.STABLE_ICE_CORE.get(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.UNSTABLE_ICE_CORE.get(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.LICH_MINION.get(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.LOYAL_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.RISING_ZOMBIE.get(), RisingZombieRenderer::new);
		EntityRendererRegistry.register(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		EntityRendererRegistry.register(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		EntityRendererRegistry.register(TFEntities.HARBINGER_CUBE.get(), m -> new TFGenericMobRenderer<>(m, new HarbingerCubeModel<>(m.bakeLayer(TFModelLayers.HARBINGER_CUBE)), 1.0F, "apocalypse2.png"));

		// Special
		EntityRendererRegistry.register(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		EntityRendererRegistry.register(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		EntityRendererRegistry.register(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);

		// Projectiles
		EntityRendererRegistry.register(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRendererRegistry.register(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRendererRegistry.register(TFEntities.LICH_BOMB.get(), c -> new CustomProjectileTextureRenderer(c, ResourceLocation.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
		EntityRendererRegistry.register(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		EntityRendererRegistry.register(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		EntityRendererRegistry.register(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);

		// Block Entities
		BlockEntityRendererRegistry.register(TFBlockEntities.FIREFLY.get(), FireflyRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.CICADA.get(), CicadaRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.MOONWORM.get(), MoonwormRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.TROPHY.get(), TrophyRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityType.CHEST, TFChestRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityType.TRAPPED_CHEST, TFChestRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.SKULL_CHEST.get(), SkullChestRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.KEEPSAKE_CASKET.get(), KeepsakeCasketRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.SKULL_CANDLE.get(), SkullCandleRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.REACTOR_DEBRIS.get(), ReactorDebrisRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.RED_THREAD.get(), RedThreadRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.CANDELABRA.get(), CandelabraRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.JAR.get(), JarRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.MASON_JAR.get(), JarRenderer.MasonJarRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.OMINOUS_CANDLE.get(), OminousCandleRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.SINISTER_SPAWNER.get(), SinisterSpawnerRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.BRAZIER.get(), BrazierRenderer::new);
		BlockEntityRendererRegistry.register(TFBlockEntities.DRYING_RACK.get(), DryingRackRenderer::new);
	}

	private void registerLayerDefinitions() {
		// INNER_ARMOR_DEFORMATION/OUTER_ARMOR_DEFORMATION exposed via access widener
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_HELMET, () -> LayerDefinition.create(TravellersGearModels.addGogglePieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), false), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, () -> LayerDefinition.create(TravellersGearModels.addGlovePieces(new CubeDeformation(0.295F), true), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, () -> TravellersWingsModel.createLayer(0.25F));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TRAVELLERS_ARMOR_BOOTS, () -> LayerDefinition.create(TravellersGearModels.addBootPieces(new CubeDeformation(0.5F)), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		// Trophies
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_TROPHY, LichModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_TROPHY, NagaModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::checkForPackTrophyEdition);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);

		// Entities
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ADHERENT, AdherentModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP, BighornModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR, BoarModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY, BunnyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN, ChainModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.DEER, DeerModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIRE_BEETLE, FireBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HELMET_CRAB, HelmetCrabModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_HEAD, HydraHeadModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA, HydraModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_NECK, HydraNeckModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KOBOLD, KoboldModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH, LichModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM, MinoshroomModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOTAUR, MinotaurModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA, NagaModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_BODY, NagaModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN, PenguinModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM, QuestRamModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RAVEN, RavenModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP, RedcapModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN, SnowQueenModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SQUIRREL, SquirrelModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TINY_BIRD, TinyBirdModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TROLL, TrollModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST, UrGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.WRAITH, WraithModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI, YetiModel::create);

		// Block Entities
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET, () -> SkullChestRenderer.create(true));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SKULL_CHEST, () -> SkullChestRenderer.create(false));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BRAZIER, BrazierModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
	}

	private void registerParticleFactories() {
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		ParticleFactoryRegistry.getInstance().register(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.SimpleFactory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.EXTENDED_SNOW_WARNING.get(), SnowWarningParticle.ExtendedFactory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.PERFECT_DODGE.get(), PerfectDodgeParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.DOUBLE_JUMP.get(), DoubleJumpParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FIREFLY.get(), FireflyParticle.StationaryProvider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.WANDERING_FIREFLY.get(), FireflyParticle.WanderingProvider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), FireflyParticle.ParticleSpawnerProvider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.DIM_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SORTING_PARTICLE.get(), SortingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.TRANSFORMATION_PARTICLE.get(), TransformationParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.LOG_CORE_PARTICLE.get(), LogCoreParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.CLOUD_PUFF.get(), CloudPuffParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.DRYING_RACK.get(), DryingRackParticle.Provider::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.MAGIC_EFFECT.get(), MagicEffectParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.ANGRY_LICH.get(), AngryLichParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(TFParticleType.TWILIGHT_ORB.get(), sprite -> new CustomTextureParticle.Factory(sprite, true));
		ParticleFactoryRegistry.getInstance().register(TFParticleType.SHIELD_BREAK.get(), sprite -> new CustomTextureParticle.ShieldBreak(sprite));
	}

	private void registerScreens() {
		MenuScreens.register(TFMenuTypes.UNCRAFTING.get(), UncraftingScreen::new);
	}

	private void registerColorHandlers() {
		ColorHandler.registerBlockColors();
		ColorHandler.registerItemColors();
	}

	private void registerDimEffects() {
		DimensionRenderingRegistry.registerDimensionEffects(
			TFDimension.DIMENSION_RENDERER,
			new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false)
		);
	}

	private void registerReloadListeners() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(TextureGeneratorReloadListener.INSTANCE);
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new TFArmorRenderer.ResourceReloadListener());
	}

	private void registerClientExtensions() {
		// Cloud block hit/destroy effects
		IClientBlockExtensions cloudExtensions = new IClientBlockExtensions() {
			@Override
			public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
				if (level.random.nextBoolean() && target instanceof BlockHitResult hitResult) {
					BlockPos pos = hitResult.getBlockPos();
					BlockState blockstate = level.getBlockState(pos);
					if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
						Direction side = hitResult.getDirection();
						int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();
						AABB aabb = blockstate.getShape(level, pos).bounds();
						double x = (double) posX + level.random.nextDouble() * (aabb.maxX - aabb.minX - (double) 0.2F) + (double) 0.1F + aabb.minX;
						double y = (double) posY + level.random.nextDouble() * (aabb.maxY - aabb.minY - (double) 0.2F) + (double) 0.1F + aabb.minY;
						double z = (double) posZ + level.random.nextDouble() * (aabb.maxZ - aabb.minZ - (double) 0.2F) + (double) 0.1F + aabb.minZ;
						if (side == Direction.DOWN) y = (double) posY + aabb.minY - (double) 0.1F;
						if (side == Direction.UP) y = (double) posY + aabb.maxY + (double) 0.1F;
						if (side == Direction.NORTH) z = (double) posZ + aabb.minZ - (double) 0.1F;
						if (side == Direction.SOUTH) z = (double) posZ + aabb.maxZ + (double) 0.1F;
						if (side == Direction.WEST) x = (double) posX + aabb.minX - (double) 0.1F;
						if (side == Direction.EAST) x = (double) posX + aabb.maxX + (double) 0.1F;
						Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), x, y, z, (double) side.getStepX() * 0.01D, (double) side.getStepY() * 0.01D, (double) side.getStepZ() * 0.01D);
						if (particle == null) return true;
						manager.add(particle);
					}
				}
				return true;
			}

			@Override
			public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) {
				state.getShape(level, pos).forAllBoxes((boxX, boxY, boxZ, boxX1, boxY1, boxZ1) -> {
					double xSize = Math.min(1.0D, boxX1 - boxX);
					double ySize = Math.min(1.0D, boxY1 - boxY);
					double zSize = Math.min(1.0D, boxZ1 - boxZ);
					int xMax = Math.max(2, Mth.ceil(xSize / 0.25D));
					int yMax = Math.max(2, Mth.ceil(ySize / 0.25D));
					int zMax = Math.max(2, Mth.ceil(zSize / 0.25D));
					for (int xSlice = 0; xSlice < xMax; ++xSlice) {
						if (level.random.nextInt(3) == 1) continue;
						for (int ySlice = 0; ySlice < yMax; ++ySlice) {
							if (level.random.nextInt(3) == 1) continue;
							for (int zSlice = 0; zSlice < zMax; ++zSlice) {
								if (level.random.nextInt(3) == 1) continue;
								double speedX = ((double) xSlice + 0.5D) / (double) xMax;
								double speedY = ((double) ySlice + 0.5D) / (double) yMax;
								double speedZ = ((double) zSlice + 0.5D) / (double) zMax;
								double x = speedX * xSize + boxX;
								double y = speedY * ySize + boxY;
								double z = speedZ * zSize + boxZ;
								speedX = (speedX - 0.5D) * 0.05D;
								speedY = (speedY - 0.5D) * 0.05D;
								speedZ = (speedZ - 0.5D) * 0.05D;
								Particle particle = Minecraft.getInstance().particleEngine.createParticle(TFParticleType.CLOUD_PUFF.get(), (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, speedX, speedY, speedZ);
								if (particle == null) return;
								manager.add(particle);
							}
						}
					}
				});
				return true;
			}
		};

		ClientExtensionsRegistry.registerBlock(cloudExtensions, TFBlocks.WISPY_CLOUD.get(), TFBlocks.RAINY_CLOUD.get(), TFBlocks.SNOWY_CLOUD.get(), TFBlocks.FLUFFY_CLOUD.get());

		// Item extensions
		ClientExtensionsRegistry.registerItem(ISTER.CLIENT_ITEM_EXTENSION,
			TFBlocks.CICADA.asItem(), TFBlocks.FIREFLY.asItem(), TFBlocks.MOONWORM.asItem(), TFBlocks.SKULL_CHEST.asItem(), TFBlocks.KEEPSAKE_CASKET.asItem(), TFBlocks.CANDELABRA.asItem(), TFBlocks.BRAZIER.asItem(),
			TFItems.CICADA_JAR.get(), TFItems.FIREFLY_JAR.get(), TFItems.MASON_JAR.get(), TFItems.KNIGHTMETAL_SHIELD.get(), TFItems.MYSTIC_CROWN.value(),
			TFBlocks.TWILIGHT_OAK_CHEST.asItem(), TFBlocks.CANOPY_CHEST.asItem(), TFBlocks.MANGROVE_CHEST.asItem(), TFBlocks.DARK_CHEST.asItem(), TFBlocks.TIME_CHEST.asItem(), TFBlocks.TRANSFORMATION_CHEST.asItem(), TFBlocks.MINING_CHEST.asItem(), TFBlocks.SORTING_CHEST.asItem(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.asItem(), TFBlocks.CANOPY_TRAPPED_CHEST.asItem(), TFBlocks.MANGROVE_TRAPPED_CHEST.asItem(), TFBlocks.DARK_TRAPPED_CHEST.asItem(), TFBlocks.TIME_TRAPPED_CHEST.asItem(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.asItem(), TFBlocks.MINING_TRAPPED_CHEST.asItem(), TFBlocks.SORTING_TRAPPED_CHEST.asItem(),
			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(), TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(), TFItems.KNIGHT_PHANTOM_TROPHY.get(), TFItems.UR_GHAST_TROPHY.get(), TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get(), TFItems.QUEST_RAM_TROPHY.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), TFItems.ZOMBIE_SKULL_CANDLE.get()
		);

		// Armor render extensions
		ArmorRendererRegistry.register(
			new ArcticArmorItem.ArmorRender(),
			TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		ArmorRendererRegistry.register(
			new TFSimpleArmorRenderer(FieryArmorModel::new, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER),
			TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get());
		ArmorRendererRegistry.register(
			new TravellersArmorItem.ArmorRender(),
			TFItems.TRAVELLERS_GOGGLES.get(), TFItems.TRAVELLERS_VEST.get(), TFItems.TRAVELLERS_GLOVES.get(), TFItems.TRAVELLERS_WINGS.get(), TFItems.TRAVELLERS_BELT.get(), TFItems.TRAVELLERS_BOOTS.get());
		ArmorRendererRegistry.register(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER),
			TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get());
		ArmorRendererRegistry.register(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER),
			TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get());
		ArmorRendererRegistry.register(
			new TFSimpleArmorRenderer(YetiArmorModel::new, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER),
			TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get());
	}

	private void registerMapDecorators() {
		// Register custom map decoration renderers via PortingLib's MapDecorationRendererManager.
		// PortingLib's MapRendererMapInstanceMixin will call these renderers during map drawing.
		ConqueredMapIconRenderer conqueredRenderer = new ConqueredMapIconRenderer();
		MagicMapPlayerIconRenderer playerIconRenderer = new MagicMapPlayerIconRenderer();

		// Register the conquered X overlay for all TF map decoration types
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.HEDGE_MAZE.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.SMALL_HOLLOW_HILL.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.MEDIUM_HOLLOW_HILL.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.LARGE_HOLLOW_HILL.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.QUEST_GROVE.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.NAGA_COURTYARD.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.LICH_TOWER.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.LABYRINTH.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.HYDRA_LAIR.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.KNIGHT_STRONGHOLD.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.DARK_TOWER.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.YETI_LAIR.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.AURORA_PALACE.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.TROLL_CAVES.get(), conqueredRenderer);
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			TFMapDecorations.FINAL_CASTLE.get(), conqueredRenderer);

		// Register the player icon renderer for magic maps
		io.github.fabricators_of_create.porting_lib.gui.map.MapDecorationRendererManager.register(
			net.minecraft.world.level.saveddata.maps.MapDecorationTypes.PLAYER.value(), playerIconRenderer);
	}

	private void registerTooltipComponents() {
		TooltipComponentCallback.EVENT.register(data -> {
			if (data instanceof BrittleFlaskItem.Tooltip tooltip) {
				return new PotionFlaskTooltipComponent(tooltip);
			}
			if (data instanceof TravellersArmorBeltItem.Tooltip tooltip) {
				return new TravellersBeltTooltipComponent(tooltip);
			}
			if (data instanceof TravellersGogglesItem.Tooltip tooltip) {
				return new ItemDisplayTooltipComponent(tooltip);
			}
			return null;
		});
	}

	private void registerRenderLayers() {
		// Register cutout render layer for berry bushes and oreberries so textures render with transparency
		var cutout = net.minecraft.client.renderer.RenderType.cutout();
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.RASPBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUEBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLACKBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.MALOBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLIGHTBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.DUSKBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.SKYBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.STINGBERRY_BUSH.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.IRON_OREBERRY.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GOLD_OREBERRY.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.COPPER_OREBERRY.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ESSENCE_OREBERRY.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.TORCHBERRY_PLANT.get(), cutout);

		// Tower device blocks use cutout rendering because their glowing texture overlays
		// (tower_device_level_* textures) have transparent gaps that must be alpha-tested.
		// Without this, they render in the SOLID pass where emissive CUTOUT materials fail.
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ANTIBUILDER.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ANTIBUILT_BLOCK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BUILT_BLOCK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.CARMINITE_REACTOR.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.CARMINITE_BUILDER.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.REAPPEARING_BLOCK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VANISHING_BLOCK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.UNBREAKABLE_VANISHING_BLOCK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.LOCKED_VANISHING_BLOCK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GHAST_TRAP.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ENCASED_FIRE_JET.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ENCASED_SMOKER.get(), cutout);

		// Castle rune bricks use a composite model with a cutout runes overlay on a solid brick base.
		// Register them for cutout so the runes render with alpha testing on Fabric.
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), cutout);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), cutout);

		// Register translucent render layer for force field blocks so their BlockItems render with alpha blending
		var translucent = net.minecraft.client.renderer.RenderType.translucent();
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.PINK_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.BLUE_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.GREEN_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.VIOLET_FORCE_FIELD.get(), translucent);
		BlockRenderLayerMap.INSTANCE.putBlock(TFBlocks.ORANGE_FORCE_FIELD.get(), translucent);

		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof LivingEntityRenderer<?, ?> living) {
				registrationHelper.register(new ShieldLayer<>(living));
				registrationHelper.register(new IceLayer<>(living));
			}
		});
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}

	private static void registerWoodType(WoodType woodType) {
		try {
			java.lang.reflect.Method register = WoodType.class.getDeclaredMethod("register", WoodType.class);
			register.setAccessible(true);
			register.invoke(null, woodType);
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Failed to register wood type: {}", woodType.name(), e);
		}
	}

	/**
	 * Checks whether a block model path belongs to a model that uses
	 * {@code porting_lib_data} with {@code block_light}/{@code sky_light}.
	 * These models need the {@link PortingLibEmissiveModel} wrapper to render
	 * their glowing texture overlays at full brightness on Fabric 1.21.1.
	 * <p>
	 * The path may or may not have a {@code block/} prefix depending on
	 * whether it comes from {@code resourceId} or {@code topLevelId}.
	 */
	private static boolean needsEmissiveWrapper(String modelPath) {
		// Strip "block/" prefix if present
		if (modelPath.startsWith("block/")) {
			modelPath = modelPath.substring("block/".length());
		}

		return modelPath.startsWith("antibuilder")
			|| modelPath.startsWith("antibuilt_block")
			|| modelPath.startsWith("built_block")
			|| modelPath.startsWith("carminite_block")
			|| modelPath.startsWith("carminite_builder")
			|| modelPath.startsWith("carminite_reactor")
			|| modelPath.startsWith("encased_fire_jet")
			|| modelPath.startsWith("encased_smoker")
			|| modelPath.startsWith("ghast_trap")
			|| modelPath.startsWith("locked_vanishing_block")
			|| modelPath.startsWith("reappearing_block")
			|| modelPath.startsWith("vanishing_block")
			|| modelPath.startsWith("trophy_pedestal_active")
			|| modelPath.startsWith("uncrafting_table")
			|| modelPath.startsWith("mushgloom")
			|| modelPath.startsWith("trollber");
	}

	/**
	 * Checks whether a block model uses translucent rendering
	 * ({@code render_type: minecraft:translucent}) and therefore needs
	 * {@link BlendMode#TRANSLUCENT} instead of {@link BlendMode#CUTOUT}
	 * in its emissive wrapper material.
	 */
	private static boolean needsTranslucentBlend(String modelPath) {
		if (modelPath.startsWith("block/")) {
			modelPath = modelPath.substring("block/".length());
		}

		return modelPath.startsWith("uncrafting_table")
			|| modelPath.startsWith("trophy_pedestal_active");
	}
}