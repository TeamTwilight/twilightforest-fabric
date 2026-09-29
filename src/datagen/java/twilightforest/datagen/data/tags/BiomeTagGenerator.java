package twilightforest.datagen.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import twilightforest.init.TFBiomes;
import twilightforest.tags.TFBiomeTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends BiomeTagsProvider {

	public BiomeTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		this.tag(TFBiomeTags.IS_TWILIGHT).addAll(List.of(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST,
			TFBiomes.ENCHANTED_FOREST, TFBiomes.DENSE_MUSHROOM_FOREST,
			TFBiomes.LAKE, TFBiomes.STREAM, TFBiomes.UNDERGROUND,
			TFBiomes.SWAMP, TFBiomes.FIRE_SWAMP,
			TFBiomes.DARK_FOREST, TFBiomes.DARK_FOREST_CENTER,
			TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER,
			TFBiomes.HIGHLANDS, TFBiomes.THORNLANDS, TFBiomes.FINAL_PLATEAU
		));

		this.tag(TFBiomeTags.VALID_QUEST_GROVE_BIOMES).add(TFBiomes.ENCHANTED_FOREST);
		this.tag(TFBiomeTags.VALID_MUSHROOM_TOWER_BIOMES).add(TFBiomes.DENSE_MUSHROOM_FOREST);

		this.tag(TFBiomeTags.VALID_CAMP_BIOMES).addAll(List.of(
			TFBiomes.OAK_SAVANNAH, TFBiomes.CLEARING, TFBiomes.MUSHROOM_FOREST, TFBiomes.FOREST, TFBiomes.FIREFLY_FOREST
		));

		this.tag(TFBiomeTags.VALID_HOLLOW_TREE_BIOMES).addAll(List.of(
			TFBiomes.DENSE_FOREST, TFBiomes.FIRE_SWAMP,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.ENCHANTED_FOREST
		));
		this.tag(TFBiomeTags.VALID_HEDGE_MAZE_BIOMES).addAll(List.of(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		));
		this.tag(TFBiomeTags.VALID_HOLLOW_HILL_BIOMES).addAll(List.of(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		));
		this.tag(TFBiomeTags.VALID_NAGA_COURTYARD_BIOMES).addAll(List.of(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		));
		this.tag(TFBiomeTags.VALID_LICH_TOWER_BIOMES).addAll(List.of(
			TFBiomes.CLEARING, TFBiomes.DENSE_FOREST,
			TFBiomes.DENSE_MUSHROOM_FOREST, TFBiomes.FIREFLY_FOREST,
			TFBiomes.FOREST, TFBiomes.MUSHROOM_FOREST,
			TFBiomes.OAK_SAVANNAH, TFBiomes.SPOOKY_FOREST
		));
		this.tag(TFBiomeTags.VALID_LABYRINTH_BIOMES).add(TFBiomes.SWAMP);
		this.tag(TFBiomeTags.VALID_HYDRA_LAIR_BIOMES).add(TFBiomes.FIRE_SWAMP);
		this.tag(TFBiomeTags.VALID_KNIGHT_STRONGHOLD_BIOMES).add(TFBiomes.DARK_FOREST);
		this.tag(TFBiomeTags.VALID_DARK_TOWER_BIOMES).add(TFBiomes.DARK_FOREST_CENTER);
		this.tag(TFBiomeTags.VALID_YETI_CAVE_BIOMES).add(TFBiomes.SNOWY_FOREST);
		this.tag(TFBiomeTags.VALID_AURORA_PALACE_BIOMES).add(TFBiomes.GLACIER);
		this.tag(TFBiomeTags.VALID_TROLL_CAVE_BIOMES).add(TFBiomes.HIGHLANDS);
		this.tag(TFBiomeTags.VALID_GIANT_HOUSE_BIOMES).add(TFBiomes.HIGHLANDS);
		this.tag(TFBiomeTags.VALID_FINAL_CASTLE_BIOMES).add(TFBiomes.FINAL_PLATEAU);

		//other vanilla tags
		this.tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addTag(TFBiomeTags.IS_TWILIGHT);
		this.tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addTag(TFBiomeTags.IS_TWILIGHT);
		this.tag(BiomeTags.WATER_ON_MAP_OUTLINES).addAll(List.of(TFBiomes.STREAM, TFBiomes.LAKE));

		//even though we won't spawn vanilla frogs, we'll still add support for the variants
		this.tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).addAll(List.of(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER));
		this.tag(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS).addAll(List.of(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER));
		this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).addAll(List.of(TFBiomes.OAK_SAVANNAH, TFBiomes.FIRE_SWAMP));
		this.tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS).addAll(List.of(TFBiomes.OAK_SAVANNAH, TFBiomes.FIRE_SWAMP));

		this.tag(BiomeTags.SPAWNS_SNOW_FOXES).addAll(List.of(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER));
		this.tag(BiomeTags.SPAWNS_WHITE_RABBITS).addAll(List.of(TFBiomes.SNOWY_FOREST, TFBiomes.GLACIER));
	}

	@Override
	public String getName() {
		return "Twilight Forest Biome Tags";
	}
}