package twilightforest.world.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.courtyard.NagaCourtyardPieces;
import twilightforest.world.components.structures.darktower.DarkTowerPieces;
import twilightforest.world.components.structures.finalcastle.FinalCastlePieces;
import twilightforest.world.components.structures.icetower.IceTowerPieces;
import twilightforest.world.components.structures.lichtower.LichTowerPieces;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerRevampPieces;
import twilightforest.world.components.structures.minotaurmaze.MinotaurMazePieces;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerPieces;
import twilightforest.world.components.structures.start.LegacyStructureFeature;
import twilightforest.world.components.structures.stronghold.StrongholdPieces;
import twilightforest.world.components.structures.trollcave.TrollCavePieces;

@SuppressWarnings("deprecation")
public class TFStructures {

	public static final StructureFeature<NoneFeatureConfiguration> HEDGE_MAZE = new LegacyStructureFeature(TFFeature.HEDGE_MAZE);
	public static final StructureFeature<NoneFeatureConfiguration> QUEST_GROVE = new LegacyStructureFeature(TFFeature.QUEST_GROVE);
	public static final StructureFeature<NoneFeatureConfiguration> MUSHROOM_TOWER = new LegacyStructureFeature(TFFeature.MUSHROOM_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_SMALL = new LegacyStructureFeature(TFFeature.SMALL_HILL);
	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_MEDIUM = new LegacyStructureFeature(TFFeature.MEDIUM_HILL);
	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_LARGE = new LegacyStructureFeature(TFFeature.LARGE_HILL);
	public static final StructureFeature<NoneFeatureConfiguration> NAGA_COURTYARD = new LegacyStructureFeature(TFFeature.NAGA_COURTYARD);
	public static final StructureFeature<NoneFeatureConfiguration> LICH_TOWER = new LegacyStructureFeature(TFFeature.LICH_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> LABYRINTH = new LegacyStructureFeature(TFFeature.LABYRINTH);
	public static final StructureFeature<NoneFeatureConfiguration> HYDRA_LAIR = new LegacyStructureFeature(TFFeature.HYDRA_LAIR);
	public static final StructureFeature<NoneFeatureConfiguration> KNIGHT_STRONGHOLD = new LegacyStructureFeature(TFFeature.KNIGHT_STRONGHOLD);
	public static final StructureFeature<NoneFeatureConfiguration> DARK_TOWER = new LegacyStructureFeature(TFFeature.DARK_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> YETI_CAVE = new LegacyStructureFeature(TFFeature.YETI_CAVE);
	public static final StructureFeature<NoneFeatureConfiguration> AURORA_PALACE = new LegacyStructureFeature(TFFeature.ICE_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> TROLL_CAVE = new LegacyStructureFeature(TFFeature.TROLL_CAVE);
	public static final StructureFeature<NoneFeatureConfiguration> FINAL_CASTLE = new LegacyStructureFeature(TFFeature.FINAL_CASTLE);

	public static void register() {
		TFFeature.init();
		new MushroomTowerPieces();
		new NagaCourtyardPieces();
		new LichTowerPieces();
		new LichTowerRevampPieces();
		new MinotaurMazePieces();
		new StrongholdPieces();
		new DarkTowerPieces();
		new IceTowerPieces();
		new TrollCavePieces();
		new FinalCastlePieces();

		register(HEDGE_MAZE, "hedge_maze");
		register(QUEST_GROVE, "quest_grove");
		register(MUSHROOM_TOWER, "mushroom_tower");
		register(HOLLOW_HILL_SMALL, "hollow_hill_small");
		register(HOLLOW_HILL_MEDIUM, "hollow_hill_medium");
		register(HOLLOW_HILL_LARGE, "hollow_hill_large");
		register(NAGA_COURTYARD, "courtyard");
		register(LICH_TOWER, "lich_tower");
		register(LABYRINTH, "labyrinth");
		register(HYDRA_LAIR, "hydra_lair");
		register(KNIGHT_STRONGHOLD, "knight_stronghold");
		register(DARK_TOWER, "dark_tower");
		register(YETI_CAVE, "yeti_cave");
		register(AURORA_PALACE, "aurora_palace");
		register(TROLL_CAVE, "troll_cave");
		register(FINAL_CASTLE, "final_castle");

	}

	private static void register(StructureFeature<?> structure, String name) {
		Registry.register(Registry.STRUCTURE_FEATURE, TwilightForestMod.prefix(name), structure);
	}
}
