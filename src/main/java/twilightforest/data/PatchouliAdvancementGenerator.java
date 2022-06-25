package twilightforest.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFStructures;
import twilightforest.init.BiomeKeys;

import java.util.function.Consumer;

public class PatchouliAdvancementGenerator extends FabricAdvancementProvider {
	public PatchouliAdvancementGenerator(FabricDataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	public void generateAdvancement(Consumer<Advancement> consumer) {
		Advancement root = Advancement.Builder.advancement()
				.addCriterion("hidden", new ImpossibleTrigger.TriggerInstance())
				.save(consumer, "twilightforest:alt/root");

		//biomes
		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_forest", this.advancementTrigger("alt/biomes/twilight_forest"))
				.addCriterion("has_firefly", this.advancementTrigger("alt/biomes/firefly_forest"))
				.addCriterion("has_clearing", this.advancementTrigger("alt/biomes/twilight_clearing"))
				.addCriterion("has_savannah", this.advancementTrigger("alt/biomes/oak_savannah"))
				.addCriterion("has_dense", this.advancementTrigger("alt/biomes/dense_twilight_forest"))
				.addCriterion("has_mush", this.advancementTrigger("alt/biomes/mushroom_forest"))
				.addCriterion("has_dense_mush", this.advancementTrigger("alt/biomes/dense_mushroom_forest"))
				.addCriterion("has_lake", this.advancementTrigger("alt/biomes/twilight_lake"))
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
		entityAdvancement(TFEntities.ADHERENT.get(), consumer, root);
		entityAdvancement(TFEntities.ARMORED_GIANT.get(), consumer, root);
		entityAdvancement(TFEntities.BIGHORN_SHEEP.get(), consumer, root);
		entityAdvancement(TFEntities.BLOCKCHAIN_GOBLIN.get(), consumer, root);
		entityAdvancement(TFEntities.DWARF_RABBIT.get(), consumer, root);
		entityAdvancement(TFEntities.DEATH_TOME.get(), consumer, root);
		entityAdvancement(TFEntities.DEER.get(), consumer, root);
		entityAdvancement(TFEntities.FIRE_BEETLE.get(), consumer, root);
		entityAdvancement(TFEntities.GIANT_MINER.get(), consumer, root);
		entityAdvancement(TFEntities.HARBINGER_CUBE.get(), consumer, root);
		entityAdvancement(TFEntities.HEDGE_SPIDER.get(), consumer, root);
		entityAdvancement(TFEntities.HELMET_CRAB.get(), consumer, root);
		entityAdvancement(TFEntities.HOSTILE_WOLF.get(), consumer, root);
		entityAdvancement(TFEntities.HYDRA.get(), consumer, root);
		entityAdvancement(TFEntities.ICE_CRYSTAL.get(), consumer, root);
		entityAdvancement(TFEntities.KING_SPIDER.get(), consumer, root);
		entityAdvancement(TFEntities.KNIGHT_PHANTOM.get(), consumer, root);
		entityAdvancement(TFEntities.KOBOLD.get(), consumer, root);
		entityAdvancement(TFEntities.LICH.get(), consumer, root);
		entityAdvancement(TFEntities.LICH_MINION.get(), consumer, root);
		entityAdvancement(TFEntities.MAZE_SLIME.get(), consumer, root);
		entityAdvancement(TFEntities.CARMINITE_GHASTLING.get(), consumer, root);
		entityAdvancement(TFEntities.MINOSHROOM.get(), consumer, root);
		entityAdvancement(TFEntities.MINOTAUR.get(), consumer, root);
		entityAdvancement(TFEntities.MIST_WOLF.get(), consumer, root);
		entityAdvancement(TFEntities.MOSQUITO_SWARM.get(), consumer, root);
		entityAdvancement(TFEntities.NAGA.get(), consumer, root);
		entityAdvancement(TFEntities.PENGUIN.get(), consumer, root);
		entityAdvancement(TFEntities.PINCH_BEETLE.get(), consumer, root);
		entityAdvancement(TFEntities.RAVEN.get(), consumer, root);
		entityAdvancement(TFEntities.REDCAP.get(), consumer, root);
		entityAdvancement(TFEntities.REDCAP_SAPPER.get(), consumer, root);
		entityAdvancement(TFEntities.SKELETON_DRUID.get(), consumer, root);
		entityAdvancement(TFEntities.SLIME_BEETLE.get(), consumer, root);
		entityAdvancement(TFEntities.SNOW_GUARDIAN.get(), consumer, root);
		entityAdvancement(TFEntities.SNOW_QUEEN.get(), consumer, root);
		entityAdvancement(TFEntities.SQUIRREL.get(), consumer, root);
		entityAdvancement(TFEntities.STABLE_ICE_CORE.get(), consumer, root);
		entityAdvancement(TFEntities.SWARM_SPIDER.get(), consumer, root);
		entityAdvancement(TFEntities.TINY_BIRD.get(), consumer, root);
		entityAdvancement(TFEntities.CARMINITE_BROODLING.get(), consumer, root);
		entityAdvancement(TFEntities.CARMINITE_GHASTGUARD.get(), consumer, root);
		entityAdvancement(TFEntities.CARMINITE_GOLEM.get(), consumer, root);
		entityAdvancement(TFEntities.TOWERWOOD_BORER.get(), consumer, root);
		entityAdvancement(TFEntities.TROLL.get(), consumer, root);
		entityAdvancement(TFEntities.UNSTABLE_ICE_CORE.get(), consumer, root);
		entityAdvancement(TFEntities.UR_GHAST.get(), consumer, root);
		entityAdvancement(TFEntities.BOAR.get(), consumer, root);
		entityAdvancement(TFEntities.WINTER_WOLF.get(), consumer, root);
		entityAdvancement(TFEntities.WRAITH.get(), consumer, root);
		entityAdvancement(TFEntities.YETI.get(), consumer, root);
		entityAdvancement(TFEntities.ALPHA_YETI.get(), consumer, root);

		Advancement.Builder.advancement().parent(root)
				.addCriterion("hurt_by_indirect", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT.get()).build())))
				.addCriterion("slain_by", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT.get())))
				.addCriterion("hurt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT.get()).build()))
				.addCriterion("slay", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT.get())))
				.addCriterion("summon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT.get())))
				.addCriterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(TFEntities.LOWER_GOBLIN_KNIGHT.get()).build()))
				.addCriterion("hurt_by_indirect_alt", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT.get()).build())))
				.addCriterion("slain_by_alt", KilledTrigger.TriggerInstance.entityKilledPlayer(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT.get())))
				.addCriterion("hurt_alt", PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT.get()).build()))
				.addCriterion("slay_alt", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT.get())))
				.addCriterion("summon_alt", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT.get())))
				.addCriterion("tame_alt", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(TFEntities.UPPER_GOBLIN_KNIGHT.get()).build()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/goblin_knight");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_bighorn", this.advancementTrigger("alt/entities/bighorn_sheep"))
				.addCriterion("has_bunny", this.advancementTrigger("alt/entities/bunny"))
				.addCriterion("has_deer", this.advancementTrigger("alt/entities/deer"))
				.addCriterion("has_raven", this.advancementTrigger("alt/entities/raven"))
				.addCriterion("has_squirrel", this.advancementTrigger("alt/entities/squirrel"))
				.addCriterion("has_boar", this.advancementTrigger("alt/entities/wild_boar"))
				.addCriterion("has_penguin", this.advancementTrigger("alt/entities/penguin"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/animals");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_hedge", this.advancementTrigger("alt/entities/hedge_spider"))
				.addCriterion("has_swarm", this.advancementTrigger("alt/entities/swarm_spider"))
				.addCriterion("has_slime", this.advancementTrigger("alt/entities/slime_beetle"))
				.addCriterion("has_fire", this.advancementTrigger("alt/entities/fire_beetle"))
				.addCriterion("has_pinch", this.advancementTrigger("alt/entities/pinch_beetle"))
				.addCriterion("has_mosquito", this.advancementTrigger("alt/entities/mosquito_swarm"))
				.addCriterion("has_king", this.advancementTrigger("alt/entities/king_spider"))
				.addCriterion("has_termite", this.advancementTrigger("alt/entities/tower_termite"))
				.addCriterion("has_broodling", this.advancementTrigger("alt/entities/tower_broodling"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/arthopods");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ghastling", this.advancementTrigger("alt/entities/mini_ghast"))
				.addCriterion("has_ghastguard", this.advancementTrigger("alt/entities/tower_ghast"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/ghasts");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_armored", this.advancementTrigger("alt/entities/armored_giant"))
				.addCriterion("has_miner", this.advancementTrigger("alt/entities/giant_miner"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/giants");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_redcap", this.advancementTrigger("alt/entities/redcap"))
				.addCriterion("has_sapper", this.advancementTrigger("alt/entities/redcap_sapper"))
				.addCriterion("has_blockchain", this.advancementTrigger("alt/entities/blockchain_goblin"))
				.addCriterion("has_knight", this.advancementTrigger("alt/entities/goblin_knight"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/goblins");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_stable", this.advancementTrigger("alt/entities/stable_ice_core"))
				.addCriterion("has_unstable", this.advancementTrigger("alt/entities/unstable_ice_core"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/ice_cores");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_druid", this.advancementTrigger("alt/entities/skeleton_druid"))
				.addCriterion("has_wraith", this.advancementTrigger("alt/entities/wraith"))
				.addCriterion("has_guardian", this.advancementTrigger("alt/entities/snow_guardian"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/entities/undead");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_hostile", this.advancementTrigger("alt/entities/hostile_wolf"))
				.addCriterion("has_mist", this.advancementTrigger("alt/entities/mist_wolf"))
				.addCriterion("has_winter", this.advancementTrigger("alt/entities/winter_wolf"))
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

		// 		Advancement.Builder.advancement().parent(root)
		//				.addCriterion("found_structure", PlayerTrigger.TriggerInstance.located(LocationPredicate.inStructure(structure)))
		//				.save(consumer, "twilightforest:alt/major_landmarks/" + structure.location().getPath());

		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setStructure(TFStructures.cleanKey(TFStructures.TROLL_CAVE)).setY(MinMaxBounds.Doubles.atLeast(150)).build()))
				.save(consumer, "twilightforest:alt/major_landmarks/giant_cloud");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure",PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setStructure(TFStructures.cleanKey(TFStructures.TROLL_CAVE)).setY(MinMaxBounds.Doubles.atMost(50)).build()))
				.save(consumer, "twilightforest:alt/major_landmarks/troll_cave");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_smol", this.advancementTrigger("alt/major_landmarks/hollow_hill_small"))
				.addCriterion("has_med", this.advancementTrigger("alt/major_landmarks/hollow_hill_medium"))
				.addCriterion("has_large", this.advancementTrigger("alt/major_landmarks/hollow_hill_large"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/major_landmarks/hollow_hills");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_FUR.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARCTIC_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/arctic_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ironwood", this.advancementTrigger("alt/treasures/ironwood_armor_set"))
				.addCriterion("has_naga", this.advancementTrigger("alt/treasures/naga_armor_set"))
				.addCriterion("has_steeleaf", this.advancementTrigger("alt/treasures/steeleaf_armor_set"))
				.addCriterion("has_fiery", this.advancementTrigger("alt/treasures/fiery_armor_set"))
				.addCriterion("has_knightmetal", this.advancementTrigger("alt/treasures/knightmetal_armor_set"))
				.addCriterion("has_arctic", this.advancementTrigger("alt/treasures/arctic_armor_set"))
				.addCriterion("has_yeti", this.advancementTrigger("alt/treasures/yeti_armor_set"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/armors");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_triple", this.advancementTrigger("alt/treasures/triple_bow"))
				.addCriterion("has_ice", this.advancementTrigger("alt/treasures/ice_bow"))
				.addCriterion("has_seeker", this.advancementTrigger("alt/treasures/seeker_bow"))
				.addCriterion("has_ender", this.advancementTrigger("alt/treasures/ender_bow"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/bows");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("carminite", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CARMINITE.get()))
				.addCriterion("carminite_block", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.CARMINITE_BLOCK.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/carminite");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_charm1", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_KEEPING_1.get()))
				.addCriterion("has_charm2", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_KEEPING_2.get()))
				.addCriterion("has_charm3", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_KEEPING_3.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charm_of_keeping");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_charm1", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_LIFE_1.get()))
				.addCriterion("has_charm2", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CHARM_OF_LIFE_2.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charm_of_life");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_keeping", this.advancementTrigger("alt/treasures/charm_of_keeping"))
				.addCriterion("has_life", this.advancementTrigger("alt/treasures/charm_of_life"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/charms");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.CRUMBLE_HORN.get()))
				.addCriterion("used", this.advancementTrigger("alt/treasures/crumble_horn_used"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/crumble_horn");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("used", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location(), ItemPredicate.Builder.item().of(TFItems.CRUMBLE_HORN.get())))
				.save(consumer, "twilightforest:alt/treasures/crumble_horn_used");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_jet", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.FIRE_JET.get()))
				.addCriterion("has_towerwood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_TOWERWOOD.get()))
				.addCriterion("has_encased_ver", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_FIRE_JET.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/encased_fire_jet");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_smoker", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SMOKER.get()))
				.addCriterion("has_towerwood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_TOWERWOOD.get()))
				.addCriterion("has_encased_ver", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.ENCASED_SMOKER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/encased_smoker");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ENDER_BOW.get()))
				.save(consumer, "twilightforest:alt/treasures/ender_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_blood_tears");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_blood", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_BLOOD.get()))
				.addCriterion("has_tears", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_TEARS.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FIERY_PICKAXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fiery_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.FIRE_JET.get()))
				.addCriterion("in_swamp", this.advancementTrigger("alt/biomes/fire_swamp"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/fire_jet");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_cobble", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_COBBLESTONE.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_LOG.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_LEAVES.get()))
				.addCriterion("has_obby", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_OBSIDIAN.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/giant_blocks");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_cobble", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_COBBLESTONE.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.GIANT_LOG.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GIANT_PICKAXE.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GIANT_SWORD.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/giant_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GLASS_SWORD.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/glass_sword");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ICE_BOMB.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_bomb");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ICE_BOW.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ICE_SWORD.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ice_sword");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_IRONWOOD.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ironwood_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_IRONWOOD.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_PICKAXE.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_AXE.get()))
				.addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_SHOVEL.get()))
				.addCriterion("has_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.IRONWOOD_HOE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ironwood_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD.get()))
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD_CLUSTER.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/knightmetal_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_shard", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD.get()))
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ARMOR_SHARD_CLUSTER.get()))
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_PICKAXE.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.KNIGHTMETAL_AXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/knightmetal_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.LIFEDRAIN_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/lifedrain_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_BEANS.get()))
				.addCriterion("used", this.advancementTrigger("alt/treaures/magic_beanstalk"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_beans");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", ItemInteractWithBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(TFBlocks.UBEROUS_SOIL.get()).build()), ItemPredicate.Builder.item().of(TFItems.MAGIC_BEANS.get())))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_beanstalk");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP_FOCUS.get()))
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAGIC_MAP.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAGIC_MAP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_time", this.advancementTrigger("alt/treasures/tree_of_time"))
				.addCriterion("has_trans", this.advancementTrigger("alt/treasures/tree_of_transformation"))
				.addCriterion("has_mine", this.advancementTrigger("alt/treasures/tree_of_mining"))
				.addCriterion("has_sort", this.advancementTrigger("alt/treasures/tree_of_sorting"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/magic_trees");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_magic", this.advancementTrigger("alt/treasures/magic_map"))
				.addCriterion("has_maze", this.advancementTrigger("alt/treasures/maze_map"))
				.addCriterion("has_ore", this.advancementTrigger("alt/treasures/ore_map"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/maps");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_focus", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZE_MAP_FOCUS.get()))
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZE_MAP.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_MAZE_MAP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/maze_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZEBREAKER_PICKAXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/mazebreaker_pickaxe");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.DIAMOND_MINOTAUR_AXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axe_diamond");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.GOLDEN_MINOTAUR_AXE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axe_gold");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_diamond", this.advancementTrigger("alt/treasures/minotaur_axe_diamond"))
				.addCriterion("has_gold", this.advancementTrigger("alt/treasures/minotaur_axe_gold"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/minotaur_axes");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MOON_DIAL.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/moon_dial");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_scale", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_SCALE.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.NAGA_LEGGINGS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/naga_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_empty", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ORE_MAP.get()))
				.addCriterion("has_filled", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FILLED_ORE_MAP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/ore_map");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_helm", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.PHANTOM_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.PHANTOM_CHESTPLATE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/phantom_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_twilight", this.advancementTrigger("alt/treasures/twilight_scepter"))
				.addCriterion("has_lifedrain", this.advancementTrigger("alt/treasures/lifedrain_scepter"))
				.addCriterion("has_zombie", this.advancementTrigger("alt/treasures/zombie_scepter"))
				.addCriterion("has_shield", this.advancementTrigger("alt/treasures/shield_scepter"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/scepters");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.SEEKER_BOW.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/seeker_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.FORTIFICATION_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/shield_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SMOKER.get()))
				.addCriterion("in_swamp", this.advancementTrigger("alt/biomes/fire_swamp"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/smoking_block");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_INGOT.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/steeleaf_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_INGOT.get()))
				.addCriterion("has_sword", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_SWORD.get()))
				.addCriterion("has_pick", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_PICKAXE.get()))
				.addCriterion("has_axe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_AXE.get()))
				.addCriterion("has_shovel", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_SHOVEL.get()))
				.addCriterion("has_hoe", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.STEELEAF_HOE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/steeleaf_tool_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ironwood", this.advancementTrigger("alt/treasures/ironwood_tool_set"))
				.addCriterion("has_steeleaf", this.advancementTrigger("alt/treasures/steeleaf_tool_set"))
				.addCriterion("has_fiery", this.advancementTrigger("alt/treasures/fiery_tool_set"))
				.addCriterion("has_knightmetal", this.advancementTrigger("alt/treasures/knightmetal_tool_set"))
				.addCriterion("has_ice", this.advancementTrigger("alt/treasures/ice_sword"))
				.addCriterion("has_glass", this.advancementTrigger("alt/treasures/glass_sword"))
				.addCriterion("has_giant", this.advancementTrigger("alt/treasures/giant_tool_set"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tools");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_LOG.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_WOOD.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TIME_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_time");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SORTING_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SORTING_LOG.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.SORTING_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_sorting");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_LOG.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_WOOD.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.MINING_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_mining");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_sapling", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_SAPLING.get()))
				.addCriterion("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_LOG.get()))
				.addCriterion("has_wood", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_WOOD.get()))
				.addCriterion("has_leaves", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TRANSFORMATION_LEAVES.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/tree_of_transformation");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TRIPLE_BOW.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/triple_bow");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("pedestal", this.advancementTrigger("progress_trophy_pedestal"))
				.addCriterion("naga", this.advancementTrigger("progress_naga"))
				.addCriterion("lich", this.advancementTrigger("progress_lich"))
				.addCriterion("minoshroom", this.advancementTrigger("progress_labyrinth"))
				.addCriterion("hydra", this.advancementTrigger("progress_hydra"))
				.addCriterion("knights", this.advancementTrigger("progress_knights"))
				.addCriterion("ghast", this.advancementTrigger("progress_ur_ghast"))
				.addCriterion("yeti", this.advancementTrigger("progress_yeti"))
				.addCriterion("queen", this.advancementTrigger("progress_glacier"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/trophies");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.TROPHY_PEDESTAL.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/trophy_pedestal");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.TWILIGHT_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/twilight_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFBlocks.UNCRAFTING_TABLE.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/uncrafting_table");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ALPHA_YETI_FUR.get()))
				.addCriterion("has_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_HELMET.get()))
				.addCriterion("has_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_CHESTPLATE.get()))
				.addCriterion("has_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_LEGGINGS.get()))
				.addCriterion("has_boots", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.YETI_BOOTS.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/yeti_armor_set");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.ZOMBIE_SCEPTER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/zombie_scepter");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("hydra_chop", this.advancementTrigger("alt/treasures/foods/hydra_chop"))
				.addCriterion("maze_wafer", this.advancementTrigger("alt/treasures/foods/maze_wafer"))
				.addCriterion("meef", this.advancementTrigger("alt/treasures/foods/meef"))
				.addCriterion("venison", this.advancementTrigger("alt/treasures/foods/venison"))
				.addCriterion("e115", this.advancementTrigger("alt/treasures/foods/experiment_115"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/any");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.EXPERIMENT_115.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/experiment_115");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.HYDRA_CHOP.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/hydra_chop");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.MAZE_WAFER.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/maze_wafer");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_MEEF.get()))
				.addCriterion("has_cooked", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.COOKED_MEEF.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/meef");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("has_raw", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.RAW_VENISON.get()))
				.addCriterion("has_cooked", InventoryChangeTrigger.TriggerInstance.hasItems(TFItems.COOKED_VENISON.get()))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/treasures/foods/venison");
	}

	private void makeBiomeAdvancement(String name, ResourceKey<Biome> key, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(key)))
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
				.save(consumer, "twilightforest:alt/entities/" + Registry.ENTITY_TYPE.getKey(entity).getPath());
	}

	private void landmarkAdvancement(RegistryObject<? extends Structure> structure, Consumer<Advancement> consumer, Advancement root) {
		this.landmarkAdvancement(structure.getId().getPath(), LocationPredicate.inStructure(TFStructures.cleanKey(structure)), consumer, root);
	}

	private void landmarkAdvancement(String name, LocationPredicate locationPredicate, Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("found_structure", PlayerTrigger.TriggerInstance.located(locationPredicate))
				.save(consumer, "twilightforest:alt/major_landmarks/" + name);
	}

	public static LocationPredicate inStructure(ResourceKey<Structure> p_220590_) {
		return new LocationPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, (ResourceKey<Biome>)null, p_220590_, (ResourceKey<Level>)null, (Boolean)null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
	}

	private void minorKeyBiomes(Consumer<Advancement> consumer, Advancement root) {
		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.DARK_FOREST)))
				.addCriterion("has_other", this.advancementTrigger("alt/biomes/dark_forest_center"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/dark_forest");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.SWAMP)))
				.addCriterion("has_other", this.advancementTrigger("alt/biomes/fire_swamp"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/swamp");

		Advancement.Builder.advancement().parent(root)
				.addCriterion("in_biome", PlayerTrigger.TriggerInstance.located(LocationPredicate.inBiome(BiomeKeys.SNOWY_FOREST)))
				.addCriterion("has_other", this.advancementTrigger("alt/biomes/twilight_glacier"))
				.requirements(RequirementsStrategy.OR)
				.save(consumer, "twilightforest:alt/biomes/snowy_forest");
	}

	private PlayerTrigger.TriggerInstance advancementTrigger(String name) {
		return new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.getId(), EntityPredicate.Composite.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(PlayerPredicate.Builder.player().checkAdvancementDone(TwilightForestMod.prefix(name), true).build())).build()));
	}

	@Override
	public String getName() {
		return "Twilight Forest Patchouli Advancements";
	}
}
