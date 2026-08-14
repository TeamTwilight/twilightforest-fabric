package twilightforest;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.config.ConfigSetup;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;
import twilightforest.network.*;
import twilightforest.util.Enforcement;
import twilightforest.util.Restriction;
import twilightforest.util.TFRemapper;
import twilightforest.util.woods.WoodPalette;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.chunkblanketing.ChunkBlanketType;
import twilightforest.world.components.layer.BiomeDensitySource;
import twilightforest.world.components.structures.StructureSpeleothemConfig;
import twilightforest.world.components.structures.markerhandler.TemplateMarkerHandlerType;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Locale;

public final class TFMain implements ModInitializer {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		Reflection.initialize(ConfigSetup.class);

		TFKeyBinds.init();
		TFItems.init();
		TFStats.init();
		TFLoot.init();
		TFBlocks.init();
		TFPOITypes.init();
		TFSounds.init();
		TFGameRules.init();
		TFFeatures.init();
		TFCreativeTabs.init();
		ItemDisplays.init();
		TFMenuTypes.init();
		TFRecipes.init();
		TFEntities.init();
		TFAttributes.init();
		TFAdvancements.init();
		TFMobEffects.init();
		//TFItemSubPredicates.TYPES.register(bus); TODO: check comment
		Enforcements.init();
		TFCaveCarvers.init();
		TFDataComponents.init();
		TFMapDecorations.init();
		TFParticleType.init();
		TravellersModifierTypes.init();
		TFBlockEntities.init();
		//TFLootModifiers.LOOT_MODIFIERS.register(bus); TODO: [Fabric] check comment
		TFConsumeEffects.init();
		TFStructureTypes.init();
		BiomeLayerTypes.init();
		TFDataAttachments.init();
		TFDataSerializers.init();
		TFFeatureModifiers.init();
		TFEnchantmentEffects.init();
		TFDensityFunctions.init();
		TFStructureProcessors.init();
		TFStructurePieceTypes.init();
		ChunkBlanketProcessors.init();
		TFStructurePlacementTypes.init();
		TemplateMarkerHandlers.init();

		TFRemapper.addRegistryAliases();

		registerPackets();
		registerCustomRegistries();
		registerDynamicRegistries();
		registerBiomeSource();
		registerConfig();
	}

	public static void registerPackets() {
		PayloadTypeRegistry.clientboundPlay().register(AreaProtectionPacket.TYPE, AreaProtectionPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(CreateMovingCicadaSoundPacket.TYPE, CreateMovingCicadaSoundPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(EnforceProgressionStatusPacket.TYPE, EnforceProgressionStatusPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MagicMapPacket.TYPE, MagicMapPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MazeMapPacket.TYPE, MazeMapPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MissingAdvancementToastPacket.TYPE, MissingAdvancementToastPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(MovePlayerPacket.TYPE, MovePlayerPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SpawnCharmPacket.TYPE, SpawnCharmPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SpawnFallenLeafFromPacket.TYPE, SpawnFallenLeafFromPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(StructureProtectionPacket.TYPE, StructureProtectionPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncUncraftingTableConfigPacket.TYPE, SyncUncraftingTableConfigPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateTFMultipartPacket.TYPE, UpdateTFMultipartPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateThrownPacket.TYPE, UpdateThrownPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(LifedrainParticlePacket.TYPE, LifedrainParticlePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateDeathTimePacket.TYPE, UpdateDeathTimePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(TFBossBarPacket.AddTFBossBarPacket.TYPE, TFBossBarPacket.AddTFBossBarPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(TFBossBarPacket.UpdateTFBossBarStylePacket.TYPE, TFBossBarPacket.UpdateTFBossBarStylePacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SetMasonJarItemPacket.TYPE, SetMasonJarItemPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SyncQuestsPacket.TYPE, SyncQuestsPacket.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(TravellersWingsStatePacket.TYPE, TravellersWingsStatePacket.STREAM_CODEC);

		PayloadTypeRegistry.serverboundPlay().register(GogglesZoomPacket.TYPE, GogglesZoomPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(GradualGlidePacket.TYPE, GradualGlidePacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SwapHotbarPacket.TYPE, SwapHotbarPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(PerformSidestepPacket.TYPE, PerformSidestepPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(CycleMapSlotPacket.TYPE, CycleMapSlotPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(UncraftingGuiPacket.TYPE, UncraftingGuiPacket.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(WipeOreMeterPacket.TYPE, WipeOreMeterPacket.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(GogglesZoomPacket.TYPE, GogglesZoomPacket::handleServer);
		ServerPlayNetworking.registerGlobalReceiver(GradualGlidePacket.TYPE, GradualGlidePacket::handleServer);
		ServerPlayNetworking.registerGlobalReceiver(PerformDoubleJumpPacket.TYPE, PerformDoubleJumpPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SwapHotbarPacket.TYPE, SwapHotbarPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(PerformSidestepPacket.TYPE, PerformSidestepPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(CycleMapSlotPacket.TYPE, CycleMapSlotPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(UncraftingGuiPacket.TYPE, UncraftingGuiPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(WipeOreMeterPacket.TYPE, WipeOreMeterPacket::handle);
	}

	public static void registerCustomRegistries() {
		FabricRegistryBuilder.create(TFRegistries.Keys.ENFORCEMENT).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.CHUNK_BLANKET_TYPE).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_TYPE).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.ITEM_DISPLAY_TYPE).buildAndRegister();
		FabricRegistryBuilder.create(TFRegistries.Keys.TRAVELLERS_MODIFIER_TYPE).buildAndRegister();
	}

	public static void registerDynamicRegistries() {
		DynamicRegistries.registerSynced(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.BIOME_STACK, BiomeLayerStack.DISPATCH_CODEC);
		DynamicRegistries.register(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.RESTRICTIONS, Restriction.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.MAGIC_PAINTINGS, MagicPaintingVariant.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.STRUCTURE_SPELEOTHEM_SETTINGS, StructureSpeleothemConfig.CODEC);
		DynamicRegistries.register(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, ChunkBlanketProcessors.DISPATCH_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, TemplateMarkerHandlers.DISPATCH_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TemplateMarkerHandlerList.CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.DWARF_RABBIT_VARIANT, DwarfRabbitVariant.DIRECT_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TINY_BIRD_VARIANT, TinyBirdVariant.DIRECT_CODEC);
		DynamicRegistries.registerSynced(TFRegistries.Keys.TRAVELLERS_MODIFIERS, TravellersModifier.CODEC);
	}

	public static void registerBiomeSource() {
		Registry.register(
			BuiltInRegistries.BIOME_SOURCE,
			TFMain.prefix("twilight_biomes"),
			TFBiomeProvider.TF_CODEC
		);
	}

	public static void registerConfig() {
		ConfigSetup.loadConfigs();
		ConfigSetup.reloadConfigs();
		ConfigSetup.syncUncraftingConfig();
	}

	public static Identifier prefix(String name) {
		return Identifier.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static Identifier getModelTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static Identifier getGuiTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static Identifier getEnvTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}
}