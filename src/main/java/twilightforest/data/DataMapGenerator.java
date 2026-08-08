package twilightforest.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import io.github.fabricators_of_create.porting_lib.data.DataMapProvider;

import twilightforest.init.*;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
	public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void gather(HolderLookup.Provider provider) {
	var transformation = this.builder(TFDataMaps.TRANSFORMATION_POWDER);
		this.add2WayTransform(transformation, TFEntities.MINOTAUR, EntityType.ZOMBIFIED_PIGLIN);
		this.add2WayTransform(transformation, TFEntities.DEER, EntityType.COW);
		this.add2WayTransform(transformation, TFEntities.BOAR, EntityType.PIG);
		this.add2WayTransform(transformation, TFEntities.BIGHORN_SHEEP, EntityType.SHEEP);
		this.add2WayTransform(transformation, TFEntities.DWARF_RABBIT, EntityType.RABBIT);
		this.add2WayTransform(transformation, TFEntities.TINY_BIRD, EntityType.PARROT);
		this.add2WayTransform(transformation, TFEntities.RAVEN, EntityType.BAT);
		this.add2WayTransform(transformation, TFEntities.HOSTILE_WOLF, EntityType.WOLF);
		this.add2WayTransform(transformation, TFEntities.PENGUIN, EntityType.CHICKEN);
		this.add2WayTransform(transformation, TFEntities.HEDGE_SPIDER, EntityType.SPIDER);
		this.add2WayTransform(transformation, TFEntities.SWARM_SPIDER, EntityType.CAVE_SPIDER);
		this.add2WayTransform(transformation, TFEntities.WRAITH, EntityType.VEX);
		this.add2WayTransform(transformation, TFEntities.SKELETON_DRUID, EntityType.WITCH);
		this.add2WayTransform(transformation, TFEntities.CARMINITE_GHASTGUARD, EntityType.GHAST);
		this.add2WayTransform(transformation, TFEntities.TOWERWOOD_BORER, EntityType.SILVERFISH);
		this.add2WayTransform(transformation, TFEntities.MAZE_SLIME, EntityType.SLIME);

        Builder<EntityTransformation, EntityType<?>> zombieBuilder = this.builder(TFDataMaps.OMINOUS_FIRE);
		this.add1WayTransform(zombieBuilder, EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER);
		this.add1WayTransform(zombieBuilder, EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN);
		this.add1WayTransform(zombieBuilder, EntityType.HORSE, EntityType.ZOMBIE_HORSE);

		var crumble = this.builder(TFDataMaps.CRUMBLE_HORN);
		crumble.add(Blocks.STONE_BRICKS.builtInRegistryHolder(), new CrumbledBlock(Blocks.CRACKED_STONE_BRICKS, 0.2F), false);
		crumble.add(Blocks.INFESTED_STONE_BRICKS.builtInRegistryHolder(), new CrumbledBlock(Blocks.INFESTED_CRACKED_STONE_BRICKS, 0.2F), false);
		crumble.add(Blocks.POLISHED_BLACKSTONE_BRICKS.builtInRegistryHolder(), new CrumbledBlock(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.2F), false);
		crumble.add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.builtInRegistryHolder(), new CrumbledBlock(Blocks.BLACKSTONE, 0.2F), false);
		crumble.add(Blocks.NETHER_BRICKS.builtInRegistryHolder(), new CrumbledBlock(Blocks.CRACKED_NETHER_BRICKS, 0.2F), false);
		crumble.add(Blocks.DEEPSLATE_BRICKS.builtInRegistryHolder(), new CrumbledBlock(Blocks.CRACKED_DEEPSLATE_BRICKS, 0.2F), false);
		crumble.add(Blocks.DEEPSLATE_TILES.builtInRegistryHolder(), new CrumbledBlock(Blocks.CRACKED_DEEPSLATE_TILES, 0.2F), false);
		crumble.add(TFBlocks.MAZESTONE_BRICK, new CrumbledBlock(TFBlocks.CRACKED_MAZESTONE.get(), 0.2F), false);
		crumble.add(TFBlocks.UNDERBRICK, new CrumbledBlock(TFBlocks.CRACKED_UNDERBRICK.get(), 0.2F), false);
		crumble.add(TFBlocks.DEADROCK, new CrumbledBlock(TFBlocks.CRACKED_DEADROCK.get(), 0.2F), false);
		crumble.add(TFBlocks.CRACKED_DEADROCK, new CrumbledBlock(TFBlocks.WEATHERED_DEADROCK.get(), 0.2F), false);
		crumble.add(TFBlocks.TOWERWOOD, new CrumbledBlock(TFBlocks.CRACKED_TOWERWOOD.get(), 0.2F), false);
		crumble.add(TFBlocks.CASTLE_BRICK, new CrumbledBlock(TFBlocks.CRACKED_CASTLE_BRICK.get(), 0.2F), false);
		crumble.add(TFBlocks.CRACKED_CASTLE_BRICK, new CrumbledBlock(TFBlocks.WORN_CASTLE_BRICK.get(), 0.2F), false);
		crumble.add(TFBlocks.NAGASTONE_PILLAR, new CrumbledBlock(TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), 0.2F), false);
		crumble.add(TFBlocks.ETCHED_NAGASTONE, new CrumbledBlock(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), 0.2F), false);
		crumble.add(TFBlocks.CASTLE_BRICK_STAIRS, new CrumbledBlock(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), 0.2F), false);
		crumble.add(TFBlocks.NAGASTONE_STAIRS_LEFT, new CrumbledBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), 0.2F), false);
		crumble.add(TFBlocks.NAGASTONE_STAIRS_RIGHT, new CrumbledBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), 0.2F), false);
		crumble.add(Blocks.STONE.builtInRegistryHolder(), new CrumbledBlock(Blocks.COBBLESTONE, 0.2F), false);
		crumble.add(Blocks.COBBLESTONE.builtInRegistryHolder(), new CrumbledBlock(Blocks.GRAVEL, 0.2F), false);
		crumble.add(Blocks.SANDSTONE.builtInRegistryHolder(), new CrumbledBlock(Blocks.SAND, 0.2F), false);
		crumble.add(Blocks.RED_SANDSTONE.builtInRegistryHolder(), new CrumbledBlock(Blocks.RED_SAND, 0.2F), false);
		crumble.add(Blocks.GRASS_BLOCK.builtInRegistryHolder(), new CrumbledBlock(Blocks.DIRT, 0.2F), false);
		crumble.add(Blocks.PODZOL.builtInRegistryHolder(), new CrumbledBlock(Blocks.DIRT, 0.2F), false);
		crumble.add(Blocks.MYCELIUM.builtInRegistryHolder(), new CrumbledBlock(Blocks.DIRT, 0.2F), false);
		crumble.add(Blocks.COARSE_DIRT.builtInRegistryHolder(), new CrumbledBlock(Blocks.DIRT, 0.2F), false);
		crumble.add(Blocks.ROOTED_DIRT.builtInRegistryHolder(), new CrumbledBlock(Blocks.DIRT, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_COPPER, 0.2F), false);
		crumble.add(Blocks.WEATHERED_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_COPPER, 0.2F), false);
		crumble.add(Blocks.EXPOSED_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.COPPER_BLOCK, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_CUT_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_CUT_COPPER, 0.2F), false);
		crumble.add(Blocks.WEATHERED_CUT_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_CUT_COPPER, 0.2F), false);
		crumble.add(Blocks.EXPOSED_CUT_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.CUT_COPPER, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_CUT_COPPER_STAIRS.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_CUT_COPPER_STAIRS, 0.2F), false);
		crumble.add(Blocks.WEATHERED_CUT_COPPER_STAIRS.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_CUT_COPPER_STAIRS, 0.2F), false);
		crumble.add(Blocks.EXPOSED_CUT_COPPER_STAIRS.builtInRegistryHolder(), new CrumbledBlock(Blocks.CUT_COPPER_STAIRS, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_CUT_COPPER_SLAB.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_CUT_COPPER_SLAB, 0.2F), false);
		crumble.add(Blocks.WEATHERED_CUT_COPPER_SLAB.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_CUT_COPPER_SLAB, 0.2F), false);
		crumble.add(Blocks.EXPOSED_CUT_COPPER_SLAB.builtInRegistryHolder(), new CrumbledBlock(Blocks.CUT_COPPER_SLAB, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_CHISELED_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_CHISELED_COPPER, 0.2F), false);
		crumble.add(Blocks.WEATHERED_CHISELED_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_CHISELED_COPPER, 0.2F), false);
		crumble.add(Blocks.EXPOSED_CHISELED_COPPER.builtInRegistryHolder(), new CrumbledBlock(Blocks.CHISELED_COPPER, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_COPPER_GRATE.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_COPPER_GRATE, 0.2F), false);
		crumble.add(Blocks.WEATHERED_COPPER_GRATE.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_COPPER_GRATE, 0.2F), false);
		crumble.add(Blocks.EXPOSED_COPPER_GRATE.builtInRegistryHolder(), new CrumbledBlock(Blocks.COPPER_GRATE, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_COPPER_BULB.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_COPPER_BULB, 0.2F), false);
		crumble.add(Blocks.WEATHERED_COPPER_BULB.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_COPPER_BULB, 0.2F), false);
		crumble.add(Blocks.EXPOSED_COPPER_BULB.builtInRegistryHolder(), new CrumbledBlock(Blocks.COPPER_BULB, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_COPPER_TRAPDOOR.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_COPPER_TRAPDOOR, 0.2F), false);
		crumble.add(Blocks.WEATHERED_COPPER_TRAPDOOR.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_COPPER_TRAPDOOR, 0.2F), false);
		crumble.add(Blocks.EXPOSED_COPPER_TRAPDOOR.builtInRegistryHolder(), new CrumbledBlock(Blocks.COPPER_TRAPDOOR, 0.2F), false);
		crumble.add(Blocks.OXIDIZED_COPPER_DOOR.builtInRegistryHolder(), new CrumbledBlock(Blocks.WEATHERED_COPPER_DOOR, 0.2F), false);
		crumble.add(Blocks.WEATHERED_COPPER_DOOR.builtInRegistryHolder(), new CrumbledBlock(Blocks.EXPOSED_COPPER_DOOR, 0.2F), false);
		crumble.add(Blocks.EXPOSED_COPPER_DOOR.builtInRegistryHolder(), new CrumbledBlock(Blocks.COPPER_DOOR, 0.2F), false);
		crumble.add(Blocks.GRAVEL.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.DIRT.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.SAND.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.RED_SAND.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.CLAY.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.ANDESITE.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.DIORITE.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);
		crumble.add(Blocks.GRANITE.builtInRegistryHolder(), new CrumbledBlock(Blocks.AIR, 0.05F), false);

		var magicMap = this.builder(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		magicMap.add(TFBiomes.FOREST, new MagicMapBiomeColor(MapColor.PLANT, 1), false);
		magicMap.add(TFBiomes.DENSE_FOREST, new MagicMapBiomeColor(MapColor.PLANT, 0), false);
		magicMap.add(TFBiomes.LAKE, new MagicMapBiomeColor(MapColor.WATER, 3), false);
		magicMap.add(TFBiomes.STREAM, new MagicMapBiomeColor(MapColor.WATER, 1), false);
		magicMap.add(TFBiomes.SWAMP, new MagicMapBiomeColor(MapColor.DIAMOND, 3), false);
		magicMap.add(TFBiomes.FIRE_SWAMP, new MagicMapBiomeColor(MapColor.NETHER, 1), false);
		magicMap.add(TFBiomes.CLEARING, new MagicMapBiomeColor(MapColor.GRASS, 2), false);
		magicMap.add(TFBiomes.OAK_SAVANNAH, new MagicMapBiomeColor(MapColor.GRASS, 0), false);
		magicMap.add(TFBiomes.HIGHLANDS, new MagicMapBiomeColor(MapColor.DIRT, 0), false);
		magicMap.add(TFBiomes.THORNLANDS, new MagicMapBiomeColor(MapColor.WOOD, 3), false);
		magicMap.add(TFBiomes.FINAL_PLATEAU, new MagicMapBiomeColor(MapColor.COLOR_LIGHT_GRAY, 2), false);
		magicMap.add(TFBiomes.FIREFLY_FOREST, new MagicMapBiomeColor(MapColor.EMERALD, 1), false);
		magicMap.add(TFBiomes.DARK_FOREST, new MagicMapBiomeColor(MapColor.COLOR_GREEN, 3), false);
		magicMap.add(TFBiomes.DARK_FOREST_CENTER, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 3), false);
		magicMap.add(TFBiomes.SNOWY_FOREST, new MagicMapBiomeColor(MapColor.SNOW, 1), false);
		magicMap.add(TFBiomes.GLACIER, new MagicMapBiomeColor(MapColor.ICE, 1), false);
		magicMap.add(TFBiomes.MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 0), false);
		magicMap.add(TFBiomes.DENSE_MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PINK, 0), false);
		magicMap.add(TFBiomes.ENCHANTED_FOREST, new MagicMapBiomeColor(MapColor.COLOR_CYAN, 2), false);
		magicMap.add(TFBiomes.SPOOKY_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PURPLE, 0), false);

		var oreMap = this.builder(TFDataMaps.ORE_MAP_ORE_COLOR);
		oreMap.add(BlockTags.COPPER_ORES, new OreMapOreColor(MapColor.COLOR_ORANGE), false);
		oreMap.add(BlockTags.COAL_ORES, new OreMapOreColor(MapColor.COLOR_BLACK), false);
		oreMap.add(BlockTags.IRON_ORES, new OreMapOreColor(MapColor.RAW_IRON), false);
		oreMap.add(BlockTags.LAPIS_ORES, new OreMapOreColor(MapColor.LAPIS), false);
		oreMap.add(BlockTags.GOLD_ORES, new OreMapOreColor(MapColor.GOLD), false);
		oreMap.add(BlockTags.REDSTONE_ORES, new OreMapOreColor(MapColor.COLOR_RED), false);
		oreMap.add(BlockTags.DIAMOND_ORES, new OreMapOreColor(MapColor.DIAMOND), false);
		oreMap.add(BlockTags.EMERALD_ORES, new OreMapOreColor(MapColor.EMERALD), false);
		oreMap.add(Blocks.ANCIENT_DEBRIS.builtInRegistryHolder(), new OreMapOreColor(MapColor.TERRACOTTA_BROWN), false);
	}

	private void add1WayTransform(Builder<EntityTransformation, EntityType<?>> builder, EntityType<?> from, EntityType<?> to) {
		builder.add(BuiltInRegistries.ENTITY_TYPE.getKey(from), new EntityTransformation(to), false);
	}

	private void add2WayTransform(Builder<EntityTransformation, EntityType<?>> builder, Holder<EntityType<?>> tfMob, EntityType<?> vanillaMob) {
		builder.add(tfMob, new EntityTransformation(vanillaMob), false);
		builder.add(BuiltInRegistries.ENTITY_TYPE.getKey(vanillaMob), new EntityTransformation(tfMob.value()), false);
	}
}
