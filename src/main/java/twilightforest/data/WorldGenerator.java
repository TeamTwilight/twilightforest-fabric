package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import twilightforest.TwilightForestMod;
import twilightforest.init.*;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class WorldGenerator extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, TFConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, TFPlacedFeatures::bootstrap)
			.add(Registries.STRUCTURE, TFStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, TFStructureSets::bootstrap)
			.add(Registries.CONFIGURED_CARVER, TFCaveCarvers::bootstrap)
			.add(Registries.NOISE_SETTINGS, TFDimensionSettings::bootstrapNoise)
			.add(Registries.DIMENSION_TYPE, TFDimensionSettings::bootstrapType)
			.add(Registries.LEVEL_STEM, TFDimensionSettings::bootstrapStem)
			.add(Registries.BIOME, TFBiomes::bootstrap);

	public WorldGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Collections.singleton(TwilightForestMod.ID));
	}
}
