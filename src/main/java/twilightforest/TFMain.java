package twilightforest;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.config.ConfigSetup;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.util.TFRemapper;

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