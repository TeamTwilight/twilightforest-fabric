package twilightforest.data.tags;

import me.alphamode.forgetags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.TwilightForestMod;
import twilightforest.init.BiomeKeys;

public class BiomeTagGenerator extends FabricTagProvider.DynamicRegistryTagProvider<Biome> {

	public static final TagKey<Biome> IS_TWILIGHT = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("in_twilight_forest"));

	public static final TagKey<Biome> VALID_QUEST_GROVE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_quest_grove_biomes"));

	public static final TagKey<Biome> VALID_HEDGE_MAZE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_hedge_maze_biomes"));
	public static final TagKey<Biome> VALID_HOLLOW_HILL_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_hollow_hill_biomes"));
	public static final TagKey<Biome> VALID_MUSHROOM_TOWER_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_mushroom_tower_biomes"));

	public static final TagKey<Biome> VALID_NAGA_COURTYARD_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_naga_courtyard_biomes"));
	public static final TagKey<Biome> VALID_LICH_TOWER_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_lich_tower_biomes"));
	public static final TagKey<Biome> VALID_LABYRINTH_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_labyrinth_biomes"));
	public static final TagKey<Biome> VALID_HYDRA_LAIR_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_hydra_lair_biomes"));
	public static final TagKey<Biome> VALID_KNIGHT_STRONGHOLD_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_knight_stronghold_biomes"));
	public static final TagKey<Biome> VALID_DARK_TOWER_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_dark_tower_biomes"));
	public static final TagKey<Biome> VALID_YETI_CAVE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_yeti_cave_biomes"));
	public static final TagKey<Biome> VALID_AURORA_PALACE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_aurora_palace_biomes"));
	public static final TagKey<Biome> VALID_TROLL_CAVE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_troll_cave_biomes"));
	public static final TagKey<Biome> VALID_FINAL_CASTLE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, TwilightForestMod.prefix("valid_final_castle_biomes"));

	public BiomeTagGenerator(FabricDataGenerator generator) {
		super(generator, Registry.BIOME_REGISTRY);
	}

	@Override
	protected void generateTags() {

		tag(IS_TWILIGHT).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST,
				BiomeKeys.ENCHANTED_FOREST, BiomeKeys.DENSE_MUSHROOM_FOREST,
				BiomeKeys.LAKE, BiomeKeys.STREAM, BiomeKeys.UNDERGROUND,
				BiomeKeys.SWAMP, BiomeKeys.FIRE_SWAMP,
				BiomeKeys.DARK_FOREST, BiomeKeys.DARK_FOREST_CENTER,
				BiomeKeys.SNOWY_FOREST, BiomeKeys.GLACIER,
				BiomeKeys.HIGHLANDS, BiomeKeys.THORNLANDS, BiomeKeys.FINAL_PLATEAU
		);

		tag(VALID_QUEST_GROVE_BIOMES).add(BiomeKeys.ENCHANTED_FOREST);
		tag(VALID_MUSHROOM_TOWER_BIOMES).add(BiomeKeys.DENSE_MUSHROOM_FOREST);

		tag(VALID_HEDGE_MAZE_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_HOLLOW_HILL_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_NAGA_COURTYARD_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_LICH_TOWER_BIOMES).add(
				BiomeKeys.CLEARING, BiomeKeys.DENSE_FOREST,
				BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.FOREST, BiomeKeys.MUSHROOM_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.SPOOKY_FOREST
		);
		tag(VALID_LABYRINTH_BIOMES).add(BiomeKeys.SWAMP);
		tag(VALID_HYDRA_LAIR_BIOMES).add(BiomeKeys.FIRE_SWAMP);
		tag(VALID_KNIGHT_STRONGHOLD_BIOMES).add(BiomeKeys.DARK_FOREST);
		tag(VALID_DARK_TOWER_BIOMES).add(BiomeKeys.DARK_FOREST_CENTER);
		tag(VALID_YETI_CAVE_BIOMES).add(BiomeKeys.SNOWY_FOREST);
		tag(VALID_AURORA_PALACE_BIOMES).add(BiomeKeys.GLACIER);
		tag(VALID_TROLL_CAVE_BIOMES).add(BiomeKeys.HIGHLANDS);
		tag(VALID_FINAL_CASTLE_BIOMES).add(BiomeKeys.FINAL_PLATEAU);

		//vanilla biome categories
		tag(BiomeTags.IS_FOREST).add(
				BiomeKeys.FOREST, BiomeKeys.DENSE_FOREST, BiomeKeys.FIREFLY_FOREST,
				BiomeKeys.OAK_SAVANNAH, BiomeKeys.MUSHROOM_FOREST, BiomeKeys.DENSE_MUSHROOM_FOREST,
				BiomeKeys.DARK_FOREST, BiomeKeys.DARK_FOREST_CENTER,
				BiomeKeys.SNOWY_FOREST, BiomeKeys.HIGHLANDS
		);
		tag(BiomeTags.IS_MOUNTAIN).add(BiomeKeys.HIGHLANDS);
		tag(BiomeTags.IS_HILL).add(BiomeKeys.THORNLANDS);

		//forge biome categories
		tag(Tags.Biomes.IS_DENSE).add(BiomeKeys.DENSE_FOREST, BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.DARK_FOREST, BiomeKeys.DARK_FOREST_CENTER, BiomeKeys.SNOWY_FOREST, BiomeKeys.THORNLANDS);
		tag(Tags.Biomes.IS_SPARSE).add(BiomeKeys.CLEARING, BiomeKeys.OAK_SAVANNAH, BiomeKeys.GLACIER, BiomeKeys.FINAL_PLATEAU);
		tag(Tags.Biomes.IS_PLAINS).add(BiomeKeys.CLEARING);
		tag(Tags.Biomes.IS_MUSHROOM).add(BiomeKeys.MUSHROOM_FOREST, BiomeKeys.DENSE_MUSHROOM_FOREST);
		tag(Tags.Biomes.IS_RARE).add(BiomeKeys.ENCHANTED_FOREST, BiomeKeys.SPOOKY_FOREST, BiomeKeys.CLEARING, BiomeKeys.DENSE_MUSHROOM_FOREST, BiomeKeys.LAKE);
		tag(Tags.Biomes.IS_WATER).add(BiomeKeys.LAKE, BiomeKeys.STREAM);
		tag(Tags.Biomes.IS_MAGICAL).add(BiomeKeys.ENCHANTED_FOREST, BiomeKeys.DARK_FOREST_CENTER);
		tag(Tags.Biomes.IS_SPOOKY).add(BiomeKeys.SPOOKY_FOREST, BiomeKeys.DARK_FOREST, BiomeKeys.DARK_FOREST_CENTER);
		tag(Tags.Biomes.IS_DEAD).add(BiomeKeys.SPOOKY_FOREST, BiomeKeys.THORNLANDS, BiomeKeys.FINAL_PLATEAU);
		tag(Tags.Biomes.IS_SWAMP).add(BiomeKeys.SWAMP, BiomeKeys.FIRE_SWAMP);
		tag(Tags.Biomes.IS_SNOWY).add(BiomeKeys.SNOWY_FOREST);
		tag(Tags.Biomes.IS_CONIFEROUS).add(BiomeKeys.SNOWY_FOREST, BiomeKeys.HIGHLANDS);
		tag(Tags.Biomes.IS_COLD).add(BiomeKeys.SNOWY_FOREST, BiomeKeys.GLACIER);
		tag(Tags.Biomes.IS_WASTELAND).add(BiomeKeys.GLACIER, BiomeKeys.THORNLANDS, BiomeKeys.FINAL_PLATEAU);
		tag(Tags.Biomes.IS_DRY).add(BiomeKeys.THORNLANDS, BiomeKeys.FINAL_PLATEAU);
		tag(Tags.Biomes.IS_PLATEAU).add(BiomeKeys.FINAL_PLATEAU);
		tag(Tags.Biomes.IS_UNDERGROUND).add(BiomeKeys.UNDERGROUND);

		//other vanilla tags
		tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).addTag(IS_TWILIGHT);
		tag(BiomeTags.WITHOUT_PATROL_SPAWNS).addTag(IS_TWILIGHT);
		tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).addTag(IS_TWILIGHT);

		//even though we won't spawn vanilla frogs, we'll still add support for the variants
		tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS).add(BiomeKeys.SNOWY_FOREST, BiomeKeys.GLACIER);
		tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS).add(BiomeKeys.OAK_SAVANNAH, BiomeKeys.FIRE_SWAMP);

		tag(BiomeTags.HAS_CLOSER_WATER_FOG).add(BiomeKeys.SPOOKY_FOREST, BiomeKeys.SWAMP, BiomeKeys.FIRE_SWAMP);
	}

	@Override
	public String getName() {
		return "Twilight Forest Biome Tags";
	}
}