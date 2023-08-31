package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.*;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.init.custom.MagicPaintingVariants;
import twilightforest.init.custom.Restrictions;
import twilightforest.init.custom.WoodPalettes;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
			.add(Registries.STRUCTURE, TFStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
			.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap)
			.add(Registries.NOISE_SETTINGS, TFDimensionSettings::bootstrapNoise)
			.add(BiomeLayerStack.BIOME_STACK_KEY, BiomeLayerStack::bootstrap)
			.add(Registries.DIMENSION_TYPE, TFDimensionSettings::bootstrapType)
			.add(Registries.LEVEL_STEM, TFDimensionSettings::bootstrapStem)
			.add(Registries.BIOME, TFBiomes::bootstrap)
			.add(WoodPalettes.WOOD_PALETTE_TYPE_KEY, WoodPalettes::bootstrap)
			.add(Registries.DAMAGE_TYPE, TFDamageTypes::bootstrap)
			.add(Registries.TRIM_MATERIAL, TFTrimMaterials::bootstrap)
			.add(Restrictions.RESTRICTION_KEY, Restrictions::bootstrap)
			.add(MagicPaintingVariants.REGISTRY_KEY, MagicPaintingVariants::bootstrap);

	public RegistryDataGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of("minecraft", TwilightForestMod.ID));
	}
}
