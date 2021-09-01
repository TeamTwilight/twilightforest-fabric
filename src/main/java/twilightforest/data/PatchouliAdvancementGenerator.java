package twilightforest.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.ArmorInventoryChangedTrigger;
import twilightforest.advancements.HasAdvancementTrigger;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.item.TFItems;
import twilightforest.world.registration.TFStructures;
import twilightforest.world.registration.biomes.BiomeKeys;

import java.util.Locale;
import java.util.function.Consumer;

public class PatchouliAdvancementGenerator implements Consumer<Consumer<Advancement>> {
	@Override
	public void accept(Consumer<Advancement> consumer) {
		Advancement root = Advancement.Builder.advancement()
				.addCriterion("hidden", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, "twilightforest:alt/root");

		//biomes
		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_forest", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_forest")))
				.addCriterion("has_firefly", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/firefly_forest")))
				.addCriterion("has_clearing", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_clearing")))
				.addCriterion("has_savannah", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/oak_savannah")))
				.addCriterion("has_dense", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/dense_twilight_forest")))
				.addCriterion("has_mush", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/mushroom_forest")))
				.addCriterion("has_dense_mush", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/dense_mushroom_forest")))
				.addCriterion("has_lake", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_lake")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/general_biomes");

		minorKeyBiomes(consumer, root);
		makeBiomeAdvancement("dark_forest_center", BiomeKeys.DARK_FOREST_CENTER, consumer, root);
		makeBiomeAdvancement("dense_mushroom_forest", BiomeKeys.DENSE_MUSHROOM_FOREST, consumer, root);
		makeBiomeAdvancement("dense_twilight_forest", BiomeKeys.DENSE_FOREST, consumer, root);
		makeBiomeAdvancement("enchanted_forest", BiomeKeys.ENCHANTED_FOREST, consumer, root);
		makeBiomeAdvancement("final_plateau", BiomeKeys.FINAL_PLATEAU, consumer, root);
		makeBiomeAdvancement("fire_swamp", BiomeKeys.FIRE_SWAMP, consumer, root);
		makeBiomeAdvancement("firefly_forest", BiomeKeys.FIREFLY_FOREST, consumer, root);
		makeBiomeAdvancement("mushroom_forest", BiomeKeys.MUSHROOM_FOREST, consumer, root);
		makeBiomeAdvancement("oak_savannah", BiomeKeys.OAK_SAVANNAH, consumer, root);
		makeBiomeAdvancement("spooky_forest", BiomeKeys.SPOOKY_FOREST, consumer, root);
		makeBiomeAdvancement("thornlands", BiomeKeys.THORNLANDS, consumer, root);
		makeBiomeAdvancement("twilight_clearing", BiomeKeys.CLEARING, consumer, root);
		makeBiomeAdvancement("twilight_forest", BiomeKeys.FOREST, consumer, root);
		makeBiomeAdvancement("twilight_glacier", BiomeKeys.GLACIER, consumer, root);
		makeBiomeAdvancement("twilight_highlands", BiomeKeys.HIGHLANDS, consumer, root);
		makeBiomeAdvancement("twilight_lake", BiomeKeys.LAKE, consumer, root);

		//entities
		entityAdvancement(TFEntities.adherent, consumer, root);
		entityAdvancement(TFEntities.armored_giant, consumer, root);
		entityAdvancement(TFEntities.bighorn_sheep, consumer, root);
		entityAdvancement(TFEntities.blockchain_goblin, consumer, root);
		entityAdvancement(TFEntities.bunny, consumer, root);
		entityAdvancement(TFEntities.death_tome, consumer, root);
		entityAdvancement(TFEntities.deer, consumer, root);
		entityAdvancement(TFEntities.fire_beetle, consumer, root);
		entityAdvancement(TFEntities.giant_miner, consumer, root);
		entityAdvancement(TFEntities.harbinger_cube, consumer, root);
		entityAdvancement(TFEntities.hedge_spider, consumer, root);
		entityAdvancement(TFEntities.helmet_crab, consumer, root);
		entityAdvancement(TFEntities.hostile_wolf, consumer, root);
		entityAdvancement(TFEntities.hydra, consumer, root);
		entityAdvancement(TFEntities.ice_crystal, consumer, root);
		entityAdvancement(TFEntities.king_spider, consumer, root);
		entityAdvancement(TFEntities.knight_phantom, consumer, root);
		entityAdvancement(TFEntities.kobold, consumer, root);
		entityAdvancement(TFEntities.lich, consumer, root);
		entityAdvancement(TFEntities.lich_minion, consumer, root);
		entityAdvancement(TFEntities.maze_slime, consumer, root);
		entityAdvancement(TFEntities.mini_ghast, consumer, root);
		entityAdvancement(TFEntities.minoshroom, consumer, root);
		entityAdvancement(TFEntities.minotaur, consumer, root);
		entityAdvancement(TFEntities.mist_wolf, consumer, root);
		entityAdvancement(TFEntities.mosquito_swarm, consumer, root);
		entityAdvancement(TFEntities.naga, consumer, root);
		entityAdvancement(TFEntities.penguin, consumer, root);
		entityAdvancement(TFEntities.pinch_beetle, consumer, root);
		entityAdvancement(TFEntities.raven, consumer, root);
		entityAdvancement(TFEntities.redcap, consumer, root);
		entityAdvancement(TFEntities.redcap_sapper, consumer, root);
		entityAdvancement(TFEntities.skeleton_druid, consumer, root);
		entityAdvancement(TFEntities.slime_beetle, consumer, root);
		entityAdvancement(TFEntities.snow_guardian, consumer, root);
		entityAdvancement(TFEntities.snow_queen, consumer, root);
		entityAdvancement(TFEntities.squirrel, consumer, root);
		entityAdvancement(TFEntities.stable_ice_core, consumer, root);
		entityAdvancement(TFEntities.swarm_spider, consumer, root);
		entityAdvancement(TFEntities.tiny_bird, consumer, root);
		entityAdvancement(TFEntities.tower_broodling, consumer, root);
		entityAdvancement(TFEntities.tower_ghast, consumer, root);
		entityAdvancement(TFEntities.tower_golem, consumer, root);
		entityAdvancement(TFEntities.tower_termite, consumer, root);
		entityAdvancement(TFEntities.troll, consumer, root);
		entityAdvancement(TFEntities.unstable_ice_core, consumer, root);
		entityAdvancement(TFEntities.ur_ghast, consumer, root);
		entityAdvancement(TFEntities.wild_boar, consumer, root);
		entityAdvancement(TFEntities.winter_wolf, consumer, root);
		entityAdvancement(TFEntities.wraith, consumer, root);
		entityAdvancement(TFEntities.yeti, consumer, root);
		entityAdvancement(TFEntities.yeti_alpha, consumer, root);

		Advancement.Builder.advancement().parent(root)
				.addCriterion("hurt_by_indirect", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_lower).build())))
				.addCriterion("slain_by", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_lower)))
				.addCriterion("hurt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_lower).build()))
				.addCriterion("slay", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_lower)))
				.addCriterion("summon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_lower)))
				.addCriterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_lower).build()))
				.addCriterion("hurt_by_indirect_alt", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_upper).build())))
				.addCriterion("slain_by_alt", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_upper)))
				.addCriterion("hurt_alt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_upper).build()))
				.addCriterion("slay_alt", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_upper)))
				.addCriterion("summon_alt", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_upper)))
				.addCriterion("tame_alt", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(TFEntities.goblin_knight_upper).build()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/goblin_knight");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_bighorn", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/bighorn_sheep")))
				.addCriterion("has_bunny", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/bunny")))
				.addCriterion("has_deer", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/deer")))
				.addCriterion("has_raven", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/raven")))
				.addCriterion("has_squirrel", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/squirrel")))
				.addCriterion("has_boar", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/wild_boar")))
				.addCriterion("has_penguin", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/penguin")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/animals");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_hedge", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/hedge_spider")))
				.addCriterion("has_swarm", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/swarm_spider")))
				.addCriterion("has_slime", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/slime_beetle")))
				.addCriterion("has_fire", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/fire_beetle")))
				.addCriterion("has_pinch", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/pinch_beetle")))
				.addCriterion("has_mosquito", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/mosquito_swarm")))
				.addCriterion("has_king", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/king_spider")))
				.addCriterion("has_termite", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/tower_termite")))
				.addCriterion("has_broodling", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/tower_broodling")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/arthopods");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ghastling", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/mini_ghast")))
				.addCriterion("has_ghastguard", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/tower_ghast")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/ghasts");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_armored", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/armored_giant")))
				.addCriterion("has_miner", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/giant_miner")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/giants");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_redcap", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/redcap")))
				.addCriterion("has_sapper", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/redcap_sapper")))
				.addCriterion("has_blockchain", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/blockchain_goblin")))
				.addCriterion("has_knight", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/goblin_knight")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/goblins");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_stable", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/stable_ice_core")))
				.addCriterion("has_unstable", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/unstable_ice_core")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/ice_cores");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_druid", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/skeleton_druid")))
				.addCriterion("has_wraith", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/wraith")))
				.addCriterion("has_guardian", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/snow_gurdian")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/undead");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_hostile", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/hostile_wolf")))
				.addCriterion("has_mist", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/mist_wolf")))
				.addCriterion("has_winter", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/entities/winter_wolf")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/wolves");

		//landmarks
		landmarkAdvancement(TFStructures.DARK_TOWER, consumer, root);
		landmarkAdvancement(TFStructures.FINAL_CASTLE, consumer, root);
		landmarkAdvancement(TFStructures.HEDGE_MAZE, consumer, root);
		landmarkAdvancement(TFStructures.HYDRA_LAIR, consumer, root);
		landmarkAdvancement(TFStructures.AURORA_PALACE, consumer, root);
		landmarkAdvancement(TFStructures.KNIGHT_STRONGHOLD, consumer, root);
		landmarkAdvancement(TFStructures.LABYRINTH, consumer, root);
		landmarkAdvancement(TFStructures.HOLLOW_HILL_LARGE, consumer, root);
		landmarkAdvancement(TFStructures.LICH_TOWER, consumer, root);
		landmarkAdvancement(TFStructures.HOLLOW_HILL_MEDIUM, consumer, root);
		landmarkAdvancement(TFStructures.MUSHROOM_TOWER, consumer, root);
		landmarkAdvancement(TFStructures.NAGA_COURTYARD, consumer, root);
		landmarkAdvancement(TFStructures.QUEST_GROVE, consumer, root);
		landmarkAdvancement(TFStructures.HOLLOW_HILL_SMALL, consumer, root);
		landmarkAdvancement(TFStructures.YETI_CAVE, consumer, root);

		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", LocationTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setFeature(TFStructures.TROLL_CAVE).setY(MinMaxBounds.Doubles.atLeast(150)).build()))
				.save(consumer, "twilightforest:alt/major_landmarks/giant_cloud");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", LocationTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setFeature(TFStructures.TROLL_CAVE).setY(MinMaxBounds.Doubles.atMost(50)).build()))
				.save(consumer, "twilightforest:alt/major_landmarks/troll_cave");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_smol", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/major_landmarks/hollow_hill_small")))
				.addCriterion("has_med", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/major_landmarks/hollow_hill_medium")))
				.addCriterion("has_large", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/major_landmarks/hollow_hill_large")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/major_landmarks/hollow_hills");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.arctic_fur.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.arctic_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.arctic_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.arctic_leggings.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.arctic_boots.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/arctic_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ironwood", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ironwood_armor_set")))
				.addCriterion("has_naga", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/naga_armor_set")))
				.addCriterion("has_steeleaf", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/steeleaf_armor_set")))
				.addCriterion("has_fiery", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/fiery_armor_set")))
				.addCriterion("has_knightmetal", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/knightmetal_armor_set")))
				.addCriterion("has_arctic", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/arctic_armor_set")))
				.addCriterion("has_yeti", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/yeti_armor_set")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/armors");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_triple", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/triple_bow")))
				.addCriterion("has_ice", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ice_bow")))
				.addCriterion("has_seeker", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/seeker_bow")))
				.addCriterion("has_ender", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ender_bow")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/bows");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("carminite", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.carminite.get()))
				.addCriterion("carminite_block", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.carminite_block.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/carminite");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_charm1", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.charm_of_keeping_1.get()))
				.addCriterion("has_charm2", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.charm_of_keeping_2.get()))
				.addCriterion("has_charm3", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.charm_of_keeping_3.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charm_of_keeping");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_charm1", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.charm_of_life_1.get()))
				.addCriterion("has_charm2", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.charm_of_life_2.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charm_of_life");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_keeping", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/charm_of_keeping")))
				.addCriterion("has_life", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/charm_of_life")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charms");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.crumble_horn.get()))
				.addCriterion("used", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/crumble_horn_used")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/crumble_horn");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("used", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location(), ItemPredicate.Builder.item().of(TFItems.crumble_horn.get())))
				.save(consumer, "twilightforest:alt/treasures/crumble_horn_used");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_jet", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.fire_jet.get()))
				.addCriterion("has_towerwood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.tower_wood_encased.get()))
				.addCriterion("has_encased_ver", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.encased_fire_jet.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/encased_fire_jet");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_smoker", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.smoker.get()))
				.addCriterion("has_towerwood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.tower_wood_encased.get()))
				.addCriterion("has_encased_ver", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.encased_smoker.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/encased_smoker");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ender_bow.get()))
				.save(consumer, "twilightforest:alt/treasures/ender_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_blood.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_tears.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_ingot.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_leggings.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_boots.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_blood.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_tears.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_blood_tears");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_blood.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_tears.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_ingot.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_sword.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.fiery_pickaxe.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.fire_jet.get()))
				.addCriterion("in_swamp", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/fire_swamp")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fire_jet");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_cobble", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.giant_cobblestone.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.giant_log.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.giant_leaves.get()))
				.addCriterion("has_obby", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.giant_obsidian.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/giant_blocks");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_cobble", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.giant_cobblestone.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.giant_log.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.giant_pickaxe.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.giant_sword.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/giant_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.glass_sword.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/glass_sword");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ice_bomb.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_bomb");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ice_bow.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ice_sword.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_sword");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_raw.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_ingot.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_leggings.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_boots.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ironwood_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_raw.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_ingot.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_sword.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_pickaxe.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_axe.get()))
				.addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_shovel.get()))
				.addCriterion("has_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ironwood_hoe.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ironwood_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.armor_shard.get()))
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.armor_shard_cluster.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_ingot.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_leggings.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_boots.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/knightmetal_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.armor_shard.get()))
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.armor_shard_cluster.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_ingot.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_sword.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_pickaxe.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.knightmetal_axe.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/knightmetal_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.lifedrain_scepter.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/lifedrain_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.magic_beans.get()))
				.addCriterion("used", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treaures/magic_beanstalk")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_beans");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.uberous_soil.get()).build()), ItemPredicate.Builder.item().of(TFItems.magic_beans.get())))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_beanstalk");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.magic_map_focus.get()))
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.magic_map_empty.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.magic_map.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_time", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_time")))
				.addCriterion("has_trans", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_transformation")))
				.addCriterion("has_mine", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_mining")))
				.addCriterion("has_sort", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/tree_of_sorting")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_trees");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_magic", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/magic_map")))
				.addCriterion("has_maze", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/maze_map")))
				.addCriterion("has_ore", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ore_map")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/maps");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.maze_map_focus.get()))
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.maze_map_empty.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.maze_map.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/maze_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.mazebreaker_pickaxe.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/mazebreaker_pickaxe");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.minotaur_axe.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axe_diamond");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.minotaur_axe_gold.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axe_gold");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_diamond", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/minotaur_axe_diamond")))
				.addCriterion("has_gold", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/minotaur_axe_gold")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axes");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.moon_dial.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/moon_dial");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.naga_scale.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.naga_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.naga_leggings.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/naga_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ore_map_empty.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ore_map.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ore_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_helm", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.phantom_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.phantom_chestplate.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/phantom_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_twilight", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/twilight_scepter")))
				.addCriterion("has_lifedrain", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/lifedrain_scepter")))
				.addCriterion("has_zombie", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/zombie_scepter")))
				.addCriterion("has_shield", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/shield_scepter")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/scepters");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.seeker_bow.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/seeker_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.shield_scepter.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/shield_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.smoker.get()))
				.addCriterion("in_swamp", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/fire_swamp")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/smoking_block");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_ingot.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_leggings.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_boots.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/steeleaf_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_ingot.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_sword.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_pickaxe.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_axe.get()))
				.addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_shovel.get()))
				.addCriterion("has_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.steeleaf_hoe.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/steeleaf_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ironwood", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ironwood_tool_set")))
				.addCriterion("has_steeleaf", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/steeleaf_tool_set")))
				.addCriterion("has_fiery", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/fiery_tool_set")))
				.addCriterion("has_knightmetal", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/knightmetal_tool_set")))
				.addCriterion("has_ice", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/ice_sword")))
				.addCriterion("has_glass", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/glass_sword")))
				.addCriterion("has_giant", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/giant_tool_set")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tools");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.time_sapling.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.time_log.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.time_wood.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.time_leaves.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_time");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.sorting_sapling.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.sorting_log.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.sorting_leaves.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_sorting");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.mining_sapling.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.mining_log.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.mining_wood.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.mining_leaves.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_mining");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.transformation_sapling.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.transformation_log.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.transformation_wood.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.transformation_leaves.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_transformation");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.triple_bow.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/triple_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("pedestal", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_trophy_pedestal")))
				.addCriterion("naga", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_naga")))
				.addCriterion("lich", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_lich")))
				.addCriterion("minoshroom", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_labyrinth")))
				.addCriterion("hydra", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_hydra")))
				.addCriterion("knights", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_knights")))
				.addCriterion("ghast", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_ur_ghast")))
				.addCriterion("yeti", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_yeti")))
				.addCriterion("queen", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("progress_glacier")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/trophies");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.trophy_pedestal.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/trophy_pedestal");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.twilight_scepter.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/twilight_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.uncrafting_table.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/uncrafting_table");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.alpha_fur.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.yeti_helmet.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.yeti_chestplate.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.yeti_leggings.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.yeti_boots.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/yeti_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.zombie_scepter.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/zombie_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("hydra_chop", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/hydra_chop")))
				.addCriterion("maze_wafer", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/maze_wafer")))
				.addCriterion("meef", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/meef")))
				.addCriterion("venison", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/venison")))
				.addCriterion("e115", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/treasures/foods/experiment_115")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/any");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.experiment_115.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/experiment_115");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.hydra_chop.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/hydra_chop");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.maze_wafer.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/maze_wafer");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.raw_meef.get()))
				.addCriterion("has_cooked", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.cooked_meef.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/meef");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.raw_venison.get()))
				.addCriterion("has_cooked", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.cooked_venison.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/venison");
	}

	private void makeBiomeAdvancement(String name, ResourceKey<Biome> key, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(key)))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/" + name);
	}

	private void entityAdvancement(EntityType<?> entity, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("hurt_by_indirect", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(entity).build())))
				.addCriterion("slain_by", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(entity)))
				.addCriterion("hurt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(entity).build()))
				.addCriterion("slay", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity)))
				.addCriterion("summon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(entity)))
				.addCriterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(entity).build()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/" + entity.getRegistryName().getPath());
	}

	private void landmarkAdvancement(StructureFeature<?> structure, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(structure)))
				.save(consumer, "twilightforest:alt/major_landmarks/" + structure.getRegistryName().getPath());
	}

	private void minorKeyBiomes(Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.DARK_FOREST)))
				.addCriterion("has_other", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/dark_forest_center")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/dark_forest");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.SWAMP)))
				.addCriterion("has_other", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/fire_swamp")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/swamp");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", LocationTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.SNOWY_FOREST)))
				.addCriterion("has_other", new HasAdvancementTrigger.Instance(EntityPredicate.Composite.ANY, TwilightForestMod.prefix("alt/biomes/twilight_glacier")))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/snowy_forest");
	}
}
