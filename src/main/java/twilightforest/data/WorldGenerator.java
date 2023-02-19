package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.init.*;
import twilightforest.init.custom.WoodPalettes;

import java.util.Set;
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
			.add(Registries.BIOME, TFBiomes::bootstrap)
			.add(WoodPalettes.WOOD_PALETTE_TYPE_KEY, WoodPalettes::bootstrap);

	// Use addProviders() instead
	private WorldGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of("minecraft", TwilightForestMod.ID));
	}

	public static void addProviders(FabricDataGenerator.Pack generator, @Nullable ExistingFileHelper helper) {
		generator.addProvider(WorldGenerator::new);
		// This is needed here because Minecraft Forge doesn't properly support tagging custom registries, without problems.
		// If you think this looks fixable, please ensure the fixes are tested in runData & runClient as these current issues exist entirely within Forge's internals.
		generator.addProvider((output, provider) -> new CustomTagGenerator.WoodPaletteTagGenerator(output, provider.thenApply(r -> append(r, BUILDER)), helper));
	}

	private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
		return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
	}
}
