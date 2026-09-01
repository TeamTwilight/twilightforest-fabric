package twilightforest.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.ParrotImitation;
import twilightforest.tags.TFItemTags;
import twilightforest.init.*;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
	public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void gather(HolderLookup.Provider provider) {
		var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
		compostables.add(TFBlocks.FALLEN_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.1F), false);
		compostables.add(TFBlocks.CANOPY_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.CLOVER_PATCH.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.DARK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.FIDDLEHEAD.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.HEDGE.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MANGROVE_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MAYAPPLE.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MINING_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TWILIGHT_OAK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.RAINBOW_OAK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.ROOT_STRAND.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.SORTING_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TIME_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TRANSFORMATION_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TWILIGHT_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.CANOPY_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MANGROVE_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.DARKWOOD_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.RAINBOW_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFItems.TORCHBERRIES, new Compostable(0.3F), false);
		compostables.add(TFBlocks.BEANSTALK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.MOSS_PATCH.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.ROOT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TROLLVIDR.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.HOLLOW_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TIME_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TRANSFORMATION_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.MINING_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.SORTING_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TORCHBERRY_PLANT.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFItems.LIVEROOT, new Compostable(0.5F), false);
		compostables.add(TFBlocks.HUGE_MUSHGLOOM_STEM.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_WATER_LILY.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.LIVEROOT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.UBEROUS_SOIL.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_STALK.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.UNRIPE_TROLLBER.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.TROLLBER.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFItems.MAZE_WAFER, new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_LILY_PAD.asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
		compostables.add(TFBlocks.HUGE_MUSHGLOOM.asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
		compostables.add(TFItems.EXPERIMENT_115, new Compostable(0.85F), false);
		compostables.add(TFItems.MAGIC_BEANS, new Compostable(0.85F), false);

		var fuels = this.builder(NeoForgeDataMaps.FURNACE_FUELS);
		fuels.add(TFItemTags.BANISTERS, new FurnaceFuel(300), false);

		var parrot = this.builder(NeoForgeDataMaps.PARROT_IMITATIONS);
		parrot.add(TFEntities.ALPHA_YETI, new ParrotImitation(TFSounds.ALPHA_YETI_PARROT.get()), false);
		parrot.add(TFEntities.BLOCKCHAIN_GOBLIN, new ParrotImitation(TFSounds.REDCAP_PARROT.get()), false);
		parrot.add(TFEntities.CARMINITE_BROODLING, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.CARMINITE_GOLEM, new ParrotImitation(TFSounds.CARMINITE_GOLEM_PARROT.get()), false);
		parrot.add(TFEntities.FIRE_BEETLE, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.CARMINITE_GHASTLING, new ParrotImitation(SoundEvents.PARROT_IMITATE_GHAST), false);
		parrot.add(TFEntities.CARMINITE_GHASTGUARD, new ParrotImitation(SoundEvents.PARROT_IMITATE_GHAST), false);
		parrot.add(TFEntities.HEDGE_SPIDER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.HELMET_CRAB, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.HOSTILE_WOLF, new ParrotImitation(TFSounds.HOSTILE_WOLF_PARROT.get()), false);
		parrot.add(TFEntities.HYDRA, new ParrotImitation(TFSounds.HYDRA_PARROT.get()), false);
		parrot.add(TFEntities.STABLE_ICE_CORE, new ParrotImitation(TFSounds.ICE_CORE_PARROT.get()), false);
		parrot.add(TFEntities.KING_SPIDER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.KOBOLD, new ParrotImitation(TFSounds.KOBOLD_PARROT.get()), false);
		parrot.add(TFEntities.LICH, new ParrotImitation(SoundEvents.PARROT_IMITATE_BLAZE), false);
		parrot.add(TFEntities.MAZE_SLIME, new ParrotImitation(SoundEvents.PARROT_IMITATE_SLIME), false);
		parrot.add(TFEntities.LICH_MINION, new ParrotImitation(SoundEvents.PARROT_IMITATE_ZOMBIE), false);
		parrot.add(TFEntities.MINOSHROOM, new ParrotImitation(TFSounds.MINOTAUR_PARROT.get()), false);
		parrot.add(TFEntities.MINOTAUR, new ParrotImitation(TFSounds.MINOTAUR_PARROT.get()), false);
		parrot.add(TFEntities.MIST_WOLF, new ParrotImitation(TFSounds.HOSTILE_WOLF_PARROT.get()), false);
		parrot.add(TFEntities.MOSQUITO_SWARM, new ParrotImitation(TFSounds.MOSQUITO_PARROT.get()), false);
		parrot.add(TFEntities.NAGA, new ParrotImitation(TFSounds.NAGA_PARROT.get()), false);
		parrot.add(TFEntities.KNIGHT_PHANTOM, new ParrotImitation(TFSounds.WRAITH_PARROT.get()), false);
		parrot.add(TFEntities.PINCH_BEETLE, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.REDCAP, new ParrotImitation(TFSounds.REDCAP_PARROT.get()), false);
		parrot.add(TFEntities.REDCAP_SAPPER, new ParrotImitation(TFSounds.REDCAP_PARROT.get()), false);
		parrot.add(TFEntities.SKELETON_DRUID, new ParrotImitation(SoundEvents.PARROT_IMITATE_SKELETON), false);
		parrot.add(TFEntities.SLIME_BEETLE, new ParrotImitation(SoundEvents.PARROT_IMITATE_SLIME), false);
		parrot.add(TFEntities.SNOW_GUARDIAN, new ParrotImitation(TFSounds.ICE_CORE_PARROT.get()), false);
		parrot.add(TFEntities.SNOW_QUEEN, new ParrotImitation(TFSounds.ICE_CORE_PARROT.get()), false);
		parrot.add(TFEntities.SWARM_SPIDER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SPIDER), false);
		parrot.add(TFEntities.TOWERWOOD_BORER, new ParrotImitation(SoundEvents.PARROT_IMITATE_SILVERFISH), false);
		parrot.add(TFEntities.DEATH_TOME, new ParrotImitation(TFSounds.DEATH_TOME_PARROT.get()), false);
		parrot.add(TFEntities.UR_GHAST, new ParrotImitation(SoundEvents.PARROT_IMITATE_GHAST), false);
		parrot.add(TFEntities.WINTER_WOLF, new ParrotImitation(TFSounds.HOSTILE_WOLF_PARROT.get()), false);
		parrot.add(TFEntities.WRAITH, new ParrotImitation(TFSounds.WRAITH_PARROT.get()), false);
		parrot.add(TFEntities.YETI, new ParrotImitation(TFSounds.ALPHA_YETI_PARROT.get()), false);
	}
}
