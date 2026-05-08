package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.DetectedVersion;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.QuestGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.custom.structuredefinitions.CampStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.FinalCastleStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.LichTowerStructureDefinitionGenerator;
import twilightforest.data.custom.structuredefinitions.NagaCourtyardStructureDefinitionGenerator;
import twilightforest.data.tags.compat.ModdedBlockTagGenerator;
import twilightforest.data.tags.compat.ModdedEntityTagGenerator;
import twilightforest.data.tags.compat.ModdedItemTagGenerator;

import java.util.Optional;

public final class DataGenerators {
	private DataGenerators() {
	}

	public static void addToPack(FabricDataGenerator.Pack pack) {
		pack.addProvider((FabricDataGenerator.Pack.Factory<BlockstateGenerator>) BlockstateGenerator::new);
		pack.addProvider((FabricDataGenerator.Pack.Factory<ItemModelGenerator>) ItemModelGenerator::new);
		pack.addProvider((FabricDataGenerator.Pack.Factory<ParticleGenerator>) output -> new ParticleGenerator(output, null));
		pack.addProvider((FabricDataGenerator.Pack.Factory<SoundGenerator>) output -> new SoundGenerator(output, null));

		pack.addProvider(RegistryDataGenerator::new);
		pack.addProvider(TFAdvancementProvider::new);
		pack.addProvider(LootGenerator::new);
		pack.addProvider(DataMapGenerator::new);
		pack.addProvider(CraftingGenerator::new);
		pack.addProvider(LootModifierGenerator::new);

		pack.addProvider((output, registries) -> new StalactiteGenerator(output));
		pack.addProvider((output, registries) -> new TFStructureUpdater("structures", output, null));
		pack.addProvider((output, registries) -> new CampStructureDefinitionGenerator(output, registries, null));
		pack.addProvider((output, registries) -> new FinalCastleStructureDefinitionGenerator(output, registries, null));
		pack.addProvider((output, registries) -> new LichTowerStructureDefinitionGenerator(output, registries, null));
		pack.addProvider((output, registries) -> new NagaCourtyardStructureDefinitionGenerator(output, registries, null));
		pack.addProvider((output, registries) -> new AtlasGenerator(output, registries, null));
		pack.addProvider(LangGenerator::new);
		pack.addProvider((output, registries) -> new QuestGenerator(output));

		pack.addProvider((FabricDataGenerator.Pack.Factory<PackMetadataGenerator>) DataGenerators::packMetadata);

		ModdedBlockTagGenerator moddedBlockTags = pack.addProvider((output, registries) -> new ModdedBlockTagGenerator(output, registries, null));
		pack.addProvider((output, registries) -> new ModdedItemTagGenerator(output, registries, moddedBlockTags.contentsGetter(), null));
		pack.addProvider((output, registries) -> new ModdedEntityTagGenerator(output, registries, null));
	}

	public static void addToRegistryBuilder(RegistrySetBuilder registryBuilder) {
		RegistryDataGenerator.addToBuilder(registryBuilder);
	}

	private static PackMetadataGenerator packMetadata(PackOutput output) {
		return new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
			Component.literal("Resources for Twilight Forest"),
			DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
			Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE))));
	}
}
