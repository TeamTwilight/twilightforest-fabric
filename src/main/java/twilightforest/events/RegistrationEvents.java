package twilightforest.events;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.ItemLike;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFRegistryObject;
import twilightforest.init.TFSounds;
import twilightforest.mixin.AxeItemAccessor;
import twilightforest.mixin.FlowerPotBlockInvoker;
import twilightforest.mixin.ParrotAccessor;

public final class RegistrationEvents {
	private static boolean bootstrapped;

	private RegistrationEvents() {
	}

	public static void bootstrap() {
		if (bootstrapped) return;
		bootstrapped = true;

		registerCauldronInteractions();
		registerCompostables();
		registerFuels();
		registerParrotImitations();
		registerAxeStrippables();
		registerFlowerPots();
		registerFlammables();
	}

	private static void registerCauldronInteractions() {
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_HELMET.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_CHESTPLATE.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_LEGGINGS.get(), CauldronInteraction.DYED_ITEM);
		CauldronInteraction.WATER.map().put(TFItems.ARCTIC_BOOTS.get(), CauldronInteraction.DYED_ITEM);
	}

	private static void registerCompostables() {
		compost(TFBlocks.FALLEN_LEAVES, 0.1F);
		compost(TFBlocks.CANOPY_LEAVES, 0.3F);
		compost(TFBlocks.CLOVER_PATCH, 0.3F);
		compost(TFBlocks.DARK_LEAVES, 0.3F);
		compost(TFBlocks.FIDDLEHEAD, 0.3F);
		compost(TFBlocks.HEDGE, 0.3F);
		compost(TFBlocks.MANGROVE_LEAVES, 0.3F);
		compost(TFBlocks.MAYAPPLE, 0.3F);
		compost(TFBlocks.MINING_LEAVES, 0.3F);
		compost(TFBlocks.TWILIGHT_OAK_LEAVES, 0.3F);
		compost(TFBlocks.RAINBOW_OAK_LEAVES, 0.3F);
		compost(TFBlocks.ROOT_STRAND, 0.3F);
		compost(TFBlocks.SORTING_LEAVES, 0.3F);
		compost(TFBlocks.THORN_LEAVES, 0.3F);
		compost(TFBlocks.TIME_LEAVES, 0.3F);
		compost(TFBlocks.TRANSFORMATION_LEAVES, 0.3F);
		compost(TFBlocks.TWILIGHT_OAK_SAPLING, 0.3F);
		compost(TFBlocks.CANOPY_SAPLING, 0.3F);
		compost(TFBlocks.MANGROVE_SAPLING, 0.3F);
		compost(TFBlocks.DARKWOOD_SAPLING, 0.3F);
		compost(TFBlocks.RAINBOW_OAK_SAPLING, 0.3F);
		compost(TFItems.TORCHBERRIES, 0.3F);
		compost(TFBlocks.BEANSTALK_LEAVES, 0.5F);
		compost(TFBlocks.MOSS_PATCH, 0.5F);
		compost(TFBlocks.ROOT_BLOCK, 0.5F);
		compost(TFBlocks.THORN_ROSE, 0.5F);
		compost(TFBlocks.TROLLVIDR, 0.5F);
		compost(TFBlocks.HOLLOW_OAK_SAPLING, 0.5F);
		compost(TFBlocks.TIME_SAPLING, 0.5F);
		compost(TFBlocks.TRANSFORMATION_SAPLING, 0.5F);
		compost(TFBlocks.MINING_SAPLING, 0.5F);
		compost(TFBlocks.SORTING_SAPLING, 0.5F);
		compost(TFBlocks.TORCHBERRY_PLANT, 0.5F);
		compost(TFItems.LIVEROOT, 0.5F);
		compost(TFBlocks.HUGE_MUSHGLOOM_STEM, 0.65F);
		compost(TFBlocks.HUGE_WATER_LILY, 0.65F);
		compost(TFBlocks.LIVEROOT_BLOCK, 0.65F);
		compost(TFBlocks.MUSHGLOOM, 0.65F);
		compost(TFBlocks.UBEROUS_SOIL, 0.65F);
		compost(TFBlocks.HUGE_STALK, 0.65F);
		compost(TFBlocks.UNRIPE_TROLLBER, 0.65F);
		compost(TFBlocks.TROLLBER, 0.65F);
		compost(TFItems.MAZE_WAFER, 0.65F);
		compost(TFBlocks.HUGE_LILY_PAD, 0.85F);
		compost(TFBlocks.HUGE_MUSHGLOOM, 0.85F);
		compost(TFItems.EXPERIMENT_115, 0.85F);
		compost(TFItems.MAGIC_BEANS, 0.85F);
		compost(TFBlocks.RASPBERRY_BUSH, 0.5F);
		compost(TFBlocks.BLUEBERRY_BUSH, 0.5F);
		compost(TFBlocks.BLACKBERRY_BUSH, 0.5F);
		compost(TFBlocks.MALOBERRY_BUSH, 0.5F);
		compost(TFBlocks.BLIGHTBERRY_BUSH, 0.5F);
		compost(TFBlocks.DUSKBERRY_BUSH, 0.5F);
		compost(TFBlocks.SKYBERRY_BUSH, 0.5F);
		compost(TFBlocks.STINGBERRY_BUSH, 0.5F);
		compost(TFItems.RASPBERRY, 0.3F);
		compost(TFItems.BLUEBERRY, 0.3F);
		compost(TFItems.BLACKBERRY, 0.3F);
		compost(TFItems.MALOBERRY, 0.3F);
		compost(TFItems.BLIGHTBERRY, 0.3F);
		compost(TFItems.DUSKBERRY, 0.3F);
		compost(TFItems.SKYBERRY, 0.3F);
		compost(TFItems.STINGBERRY, 0.3F);
	}

	private static void registerFuels() {
		FuelRegistry.INSTANCE.add(ItemTagGenerator.BANISTERS, 300);
	}

	private static void registerParrotImitations() {
		imitate(TFEntities.ALPHA_YETI, TFSounds.ALPHA_YETI_PARROT);
		imitate(TFEntities.BLOCKCHAIN_GOBLIN, TFSounds.REDCAP_PARROT);
		imitate(TFEntities.CARMINITE_BROODLING, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.CARMINITE_GOLEM, TFSounds.CARMINITE_GOLEM_PARROT);
		imitate(TFEntities.FIRE_BEETLE, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.CARMINITE_GHASTLING, SoundEvents.PARROT_IMITATE_GHAST);
		imitate(TFEntities.CARMINITE_GHASTGUARD, SoundEvents.PARROT_IMITATE_GHAST);
		imitate(TFEntities.HEDGE_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.HELMET_CRAB, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.HOSTILE_WOLF, TFSounds.HOSTILE_WOLF_PARROT);
		imitate(TFEntities.HYDRA, TFSounds.HYDRA_PARROT);
		imitate(TFEntities.STABLE_ICE_CORE, TFSounds.ICE_CORE_PARROT);
		imitate(TFEntities.KING_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.KOBOLD, TFSounds.KOBOLD_PARROT);
		imitate(TFEntities.LICH, SoundEvents.PARROT_IMITATE_BLAZE);
		imitate(TFEntities.MAZE_SLIME, SoundEvents.PARROT_IMITATE_SLIME);
		imitate(TFEntities.LICH_MINION, SoundEvents.PARROT_IMITATE_ZOMBIE);
		imitate(TFEntities.MINOSHROOM, TFSounds.MINOTAUR_PARROT);
		imitate(TFEntities.MINOTAUR, TFSounds.MINOTAUR_PARROT);
		imitate(TFEntities.MIST_WOLF, TFSounds.HOSTILE_WOLF_PARROT);
		imitate(TFEntities.MOSQUITO_SWARM, TFSounds.MOSQUITO_PARROT);
		imitate(TFEntities.NAGA, TFSounds.NAGA_PARROT);
		imitate(TFEntities.KNIGHT_PHANTOM, TFSounds.WRAITH_PARROT);
		imitate(TFEntities.PINCH_BEETLE, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.REDCAP, TFSounds.REDCAP_PARROT);
		imitate(TFEntities.REDCAP_SAPPER, TFSounds.REDCAP_PARROT);
		imitate(TFEntities.SKELETON_DRUID, SoundEvents.PARROT_IMITATE_SKELETON);
		imitate(TFEntities.SLIME_BEETLE, SoundEvents.PARROT_IMITATE_SLIME);
		imitate(TFEntities.SNOW_GUARDIAN, TFSounds.ICE_CORE_PARROT);
		imitate(TFEntities.SNOW_QUEEN, TFSounds.ICE_CORE_PARROT);
		imitate(TFEntities.SWARM_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
		imitate(TFEntities.TOWERWOOD_BORER, SoundEvents.PARROT_IMITATE_SILVERFISH);
		imitate(TFEntities.DEATH_TOME, TFSounds.DEATH_TOME_PARROT);
		imitate(TFEntities.UR_GHAST, SoundEvents.PARROT_IMITATE_GHAST);
		imitate(TFEntities.WINTER_WOLF, TFSounds.HOSTILE_WOLF_PARROT);
		imitate(TFEntities.WRAITH, TFSounds.WRAITH_PARROT);
		imitate(TFEntities.YETI, TFSounds.ALPHA_YETI_PARROT);
	}

	private static void registerAxeStrippables() {
		putStrippable(TFBlocks.TWILIGHT_OAK_LOG, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);
		putStrippable(TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD);
		putStrippable(TFBlocks.CANOPY_LOG, TFBlocks.STRIPPED_CANOPY_LOG);
		putStrippable(TFBlocks.CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_WOOD);
		putStrippable(TFBlocks.MANGROVE_LOG, TFBlocks.STRIPPED_MANGROVE_LOG);
		putStrippable(TFBlocks.MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_WOOD);
		putStrippable(TFBlocks.DARK_LOG, TFBlocks.STRIPPED_DARK_LOG);
		putStrippable(TFBlocks.DARK_WOOD, TFBlocks.STRIPPED_DARK_WOOD);
		putStrippable(TFBlocks.TIME_LOG, TFBlocks.STRIPPED_TIME_LOG);
		putStrippable(TFBlocks.TIME_WOOD, TFBlocks.STRIPPED_TIME_WOOD);
		putStrippable(TFBlocks.TRANSFORMATION_LOG, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		putStrippable(TFBlocks.TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_WOOD);
		putStrippable(TFBlocks.MINING_LOG, TFBlocks.STRIPPED_MINING_LOG);
		putStrippable(TFBlocks.MINING_WOOD, TFBlocks.STRIPPED_MINING_WOOD);
		putStrippable(TFBlocks.SORTING_LOG, TFBlocks.STRIPPED_SORTING_LOG);
		putStrippable(TFBlocks.SORTING_WOOD, TFBlocks.STRIPPED_SORTING_WOOD);
	}

	private static void registerFlowerPots() {
		FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
		addPlant(pot, TFBlocks.TWILIGHT_OAK_SAPLING, TFBlocks.POTTED_TWILIGHT_OAK_SAPLING);
		addPlant(pot, TFBlocks.CANOPY_SAPLING, TFBlocks.POTTED_CANOPY_SAPLING);
		addPlant(pot, TFBlocks.MANGROVE_SAPLING, TFBlocks.POTTED_MANGROVE_SAPLING);
		addPlant(pot, TFBlocks.DARKWOOD_SAPLING, TFBlocks.POTTED_DARKWOOD_SAPLING);
		addPlant(pot, TFBlocks.HOLLOW_OAK_SAPLING, TFBlocks.POTTED_HOLLOW_OAK_SAPLING);
		addPlant(pot, TFBlocks.RAINBOW_OAK_SAPLING, TFBlocks.POTTED_RAINBOW_OAK_SAPLING);
		addPlant(pot, TFBlocks.TIME_SAPLING, TFBlocks.POTTED_TIME_SAPLING);
		addPlant(pot, TFBlocks.TRANSFORMATION_SAPLING, TFBlocks.POTTED_TRANSFORMATION_SAPLING);
		addPlant(pot, TFBlocks.MINING_SAPLING, TFBlocks.POTTED_MINING_SAPLING);
		addPlant(pot, TFBlocks.SORTING_SAPLING, TFBlocks.POTTED_SORTING_SAPLING);
		addPlant(pot, TFBlocks.MAYAPPLE, TFBlocks.POTTED_MAYAPPLE);
		addPlant(pot, TFBlocks.FIDDLEHEAD, TFBlocks.POTTED_FIDDLEHEAD);
		addPlant(pot, TFBlocks.MUSHGLOOM, TFBlocks.POTTED_MUSHGLOOM);
		addPlant(pot, TFBlocks.BROWN_THORNS, TFBlocks.POTTED_THORN);
		addPlant(pot, TFBlocks.GREEN_THORNS, TFBlocks.POTTED_GREEN_THORN);
		addPlant(pot, TFBlocks.BURNT_THORNS, TFBlocks.POTTED_DEAD_THORN);
	}

	private static void registerFlammables() {
		FireBlock fire = (FireBlock) Blocks.FIRE;
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_LOG, 5, 5);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.TWILIGHT_OAK_FENCE_GATE, 5, 20);

		setFlammable(fire, TFBlocks.CANOPY_LOG, 5, 5);
		setFlammable(fire, TFBlocks.CANOPY_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_CANOPY_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_CANOPY_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.CANOPY_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.CANOPY_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.CANOPY_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.CANOPY_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.CANOPY_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.CANOPY_FENCE_GATE, 5, 20);
		setFlammable(fire, TFBlocks.CANOPY_BOOKSHELF, 5, 20);

		setFlammable(fire, TFBlocks.MANGROVE_LOG, 5, 5);
		setFlammable(fire, TFBlocks.MANGROVE_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_MANGROVE_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_MANGROVE_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.MANGROVE_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.MANGROVE_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.MANGROVE_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.MANGROVE_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.MANGROVE_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.MANGROVE_FENCE_GATE, 5, 20);
		setFlammable(fire, TFBlocks.MANGROVE_ROOT, 5, 20);

		setFlammable(fire, TFBlocks.DARK_LOG, 5, 5);
		setFlammable(fire, TFBlocks.DARK_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_DARK_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_DARK_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_DARK_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.DARK_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.DARK_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.DARK_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.DARK_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.DARK_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.DARK_FENCE_GATE, 5, 20);

		setFlammable(fire, TFBlocks.TIME_LOG, 5, 5);
		setFlammable(fire, TFBlocks.TIME_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_TIME_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_TIME_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TIME_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.TIME_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.TIME_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.TIME_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.TIME_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.TIME_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.TIME_FENCE_GATE, 5, 20);

		setFlammable(fire, TFBlocks.TRANSFORMATION_LOG, 5, 5);
		setFlammable(fire, TFBlocks.TRANSFORMATION_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_TRANSFORMATION_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_TRANSFORMATION_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.TRANSFORMATION_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.TRANSFORMATION_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.TRANSFORMATION_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.TRANSFORMATION_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.TRANSFORMATION_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.TRANSFORMATION_FENCE_GATE, 5, 20);

		setFlammable(fire, TFBlocks.MINING_LOG, 5, 5);
		setFlammable(fire, TFBlocks.MINING_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_MINING_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_MINING_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_MINING_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.MINING_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.MINING_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.MINING_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.MINING_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.MINING_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.MINING_FENCE_GATE, 5, 20);

		setFlammable(fire, TFBlocks.SORTING_LOG, 5, 5);
		setFlammable(fire, TFBlocks.SORTING_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_SORTING_LOG, 5, 5);
		setFlammable(fire, TFBlocks.STRIPPED_SORTING_WOOD, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_SORTING_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.SORTING_BANISTER, 5, 5);
		setFlammable(fire, TFBlocks.SORTING_PLANKS, 5, 20);
		setFlammable(fire, TFBlocks.SORTING_SLAB, 5, 20);
		setFlammable(fire, TFBlocks.SORTING_STAIRS, 5, 20);
		setFlammable(fire, TFBlocks.SORTING_FENCE, 5, 20);
		setFlammable(fire, TFBlocks.SORTING_FENCE_GATE, 5, 20);

		setFlammable(fire, TFBlocks.RASPBERRY_BUSH, 4, 25);
		setFlammable(fire, TFBlocks.BLUEBERRY_BUSH, 4, 25);
		setFlammable(fire, TFBlocks.BLACKBERRY_BUSH, 4, 25);
		setFlammable(fire, TFBlocks.MALOBERRY_BUSH, 4, 25);
		setFlammable(fire, TFBlocks.CLOVER_PATCH, 60, 100);
		setFlammable(fire, TFBlocks.FALLEN_LEAVES, 60, 100);
		setFlammable(fire, TFBlocks.FIDDLEHEAD, 60, 100);
		setFlammable(fire, TFBlocks.MAYAPPLE, 60, 100);
		setFlammable(fire, TFBlocks.MOSS_PATCH, 60, 100);
		setFlammable(fire, TFBlocks.ROOT_STRAND, 60, 100);
		setFlammable(fire, TFBlocks.TORCHBERRY_PLANT, 60, 100);
		setFlammable(fire, TFBlocks.ROOT_BLOCK, 5, 20);
		setFlammable(fire, TFBlocks.ARCTIC_FUR_BLOCK, 20, 20);
		setFlammable(fire, TFBlocks.LIVEROOT_BLOCK, 5, 20);
		setFlammable(fire, TFBlocks.CHISELED_CANOPY_BOOKSHELF, 30, 20);
		setFlammable(fire, TFBlocks.HUGE_STALK, 5, 5);

		setFlammable(fire, TFBlocks.TOWERWOOD, 0, 1);
		setFlammable(fire, TFBlocks.CRACKED_TOWERWOOD, 0, 1);
		setFlammable(fire, TFBlocks.MOSSY_TOWERWOOD, 0, 1);
		setFlammable(fire, TFBlocks.ENCASED_TOWERWOOD, 0, 1);
		setFlammable(fire, TFBlocks.INFESTED_TOWERWOOD, 0, 1);

		setFlammable(fire, TFBlocks.TWILIGHT_OAK_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.CANOPY_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.MANGROVE_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.DARK_LEAVES, 0, 1);
		setFlammable(fire, TFBlocks.HARDENED_DARK_LEAVES, 0, 1);
		setFlammable(fire, TFBlocks.TIME_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.TRANSFORMATION_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.MINING_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.SORTING_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.BEANSTALK_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.THORN_LEAVES, 30, 60);
		setFlammable(fire, TFBlocks.RAINBOW_OAK_LEAVES, 30, 60);

		setFlammable(fire, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_OAK_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.OAK_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.SPRUCE_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.BIRCH_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.JUNGLE_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.ACACIA_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.DARK_OAK_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.CRIMSON_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_WARPED_STEM_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.WARPED_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.VANGROVE_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL, 5, 5);
		setFlammable(fire, TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE, 5, 5);
		setFlammable(fire, TFBlocks.CHERRY_BANISTER, 5, 20);
		setFlammable(fire, TFBlocks.BAMBOO_BANISTER, 5, 20);
	}

	private static void putStrippable(TFRegistryObject<Block> input, TFRegistryObject<Block> output) {
		AxeItemAccessor.codex_twilight$getStrippables().put(input.get(), output.get());
	}

	private static void addPlant(FlowerPotBlock pot, TFRegistryObject<Block> plant, TFRegistryObject<Block> potted) {
		FlowerPotBlockInvoker.codex_twilight$getPottedByContent().put(plant.get(), potted.get());
	}

	private static void setFlammable(FireBlock fire, TFRegistryObject<Block> block, int encouragement, int flammability) {
		fire.setFlammable(block.get(), encouragement, flammability);
	}

	private static void compost(TFRegistryObject<? extends ItemLike> item, float chance) {
		CompostingChanceRegistry.INSTANCE.add(item.get(), chance);
	}

	private static void imitate(TFRegistryObject<? extends EntityType<?>> entityType, SoundEvent sound) {
		ParrotAccessor.codex_twilight$getMobSoundMap().put(entityType.get(), sound);
	}
}
