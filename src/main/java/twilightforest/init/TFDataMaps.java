package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

import java.util.IdentityHashMap;
import java.util.Map;

public final class TFDataMaps {
	private static final Map<Block, CrumbledBlock> CRUMBLE_HORN = new IdentityHashMap<>();
	private static final Map<EntityType<?>, EntityTransformation> TRANSFORMATION_POWDER = new IdentityHashMap<>();
	private static final Map<EntityType<?>, EntityTransformation> OMINOUS_FIRE = new IdentityHashMap<>();

	private static final Map<ResourceKey<Biome>, MagicMapBiomeColor> MAGIC_MAP_BIOME_COLORS = Map.ofEntries(
		Map.entry(TFBiomes.FOREST, new MagicMapBiomeColor(MapColor.PLANT, 1)),
		Map.entry(TFBiomes.DENSE_FOREST, new MagicMapBiomeColor(MapColor.PLANT, 0)),
		Map.entry(TFBiomes.LAKE, new MagicMapBiomeColor(MapColor.WATER, 3)),
		Map.entry(TFBiomes.STREAM, new MagicMapBiomeColor(MapColor.WATER, 1)),
		Map.entry(TFBiomes.SWAMP, new MagicMapBiomeColor(MapColor.DIAMOND, 3)),
		Map.entry(TFBiomes.FIRE_SWAMP, new MagicMapBiomeColor(MapColor.NETHER, 1)),
		Map.entry(TFBiomes.CLEARING, new MagicMapBiomeColor(MapColor.GRASS, 2)),
		Map.entry(TFBiomes.OAK_SAVANNAH, new MagicMapBiomeColor(MapColor.GRASS, 0)),
		Map.entry(TFBiomes.HIGHLANDS, new MagicMapBiomeColor(MapColor.DIRT, 0)),
		Map.entry(TFBiomes.THORNLANDS, new MagicMapBiomeColor(MapColor.WOOD, 3)),
		Map.entry(TFBiomes.FINAL_PLATEAU, new MagicMapBiomeColor(MapColor.COLOR_LIGHT_GRAY, 2)),
		Map.entry(TFBiomes.FIREFLY_FOREST, new MagicMapBiomeColor(MapColor.EMERALD, 1)),
		Map.entry(TFBiomes.DARK_FOREST, new MagicMapBiomeColor(MapColor.COLOR_GREEN, 3)),
		Map.entry(TFBiomes.DARK_FOREST_CENTER, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 3)),
		Map.entry(TFBiomes.SNOWY_FOREST, new MagicMapBiomeColor(MapColor.SNOW, 1)),
		Map.entry(TFBiomes.GLACIER, new MagicMapBiomeColor(MapColor.ICE, 1)),
		Map.entry(TFBiomes.MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 0)),
		Map.entry(TFBiomes.DENSE_MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PINK, 0)),
		Map.entry(TFBiomes.ENCHANTED_FOREST, new MagicMapBiomeColor(MapColor.COLOR_CYAN, 2)),
		Map.entry(TFBiomes.SPOOKY_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PURPLE, 0))
	);

	static {
		crumble(Blocks.ANDESITE, Blocks.AIR, 0.05F);
		crumble(Blocks.CLAY, Blocks.AIR, 0.05F);
		crumble(Blocks.COARSE_DIRT, Blocks.DIRT, 0.2F);
		crumble(Blocks.COBBLESTONE, Blocks.GRAVEL, 0.2F);
		crumble(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE, 0.2F);
		crumble(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS, 0.2F);
		crumble(Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES, 0.2F);
		crumble(Blocks.DIORITE, Blocks.AIR, 0.05F);
		crumble(Blocks.DIRT, Blocks.AIR, 0.05F);
		crumble(Blocks.EXPOSED_CHISELED_COPPER, Blocks.CHISELED_COPPER, 0.2F);
		crumble(Blocks.EXPOSED_COPPER, Blocks.COPPER_BLOCK, 0.2F);
		crumble(Blocks.EXPOSED_COPPER_BULB, Blocks.COPPER_BULB, 0.2F);
		crumble(Blocks.EXPOSED_COPPER_DOOR, Blocks.COPPER_DOOR, 0.2F);
		crumble(Blocks.EXPOSED_COPPER_GRATE, Blocks.COPPER_GRATE, 0.2F);
		crumble(Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.COPPER_TRAPDOOR, 0.2F);
		crumble(Blocks.EXPOSED_CUT_COPPER, Blocks.CUT_COPPER, 0.2F);
		crumble(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.CUT_COPPER_SLAB, 0.2F);
		crumble(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.CUT_COPPER_STAIRS, 0.2F);
		crumble(Blocks.GRANITE, Blocks.AIR, 0.05F);
		crumble(Blocks.GRASS_BLOCK, Blocks.DIRT, 0.2F);
		crumble(Blocks.GRAVEL, Blocks.AIR, 0.05F);
		crumble(Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, 0.2F);
		crumble(Blocks.MYCELIUM, Blocks.DIRT, 0.2F);
		crumble(Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS, 0.2F);
		crumble(Blocks.OXIDIZED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER, 0.2F);
		crumble(Blocks.OXIDIZED_COPPER, Blocks.WEATHERED_COPPER, 0.2F);
		crumble(Blocks.OXIDIZED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB, 0.2F);
		crumble(Blocks.OXIDIZED_COPPER_DOOR, Blocks.WEATHERED_COPPER_DOOR, 0.2F);
		crumble(Blocks.OXIDIZED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE, 0.2F);
		crumble(Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, 0.2F);
		crumble(Blocks.OXIDIZED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, 0.2F);
		crumble(Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, 0.2F);
		crumble(Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, 0.2F);
		crumble(Blocks.PODZOL, Blocks.DIRT, 0.2F);
		crumble(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.2F);
		crumble(Blocks.RED_SAND, Blocks.AIR, 0.05F);
		crumble(Blocks.RED_SANDSTONE, Blocks.RED_SAND, 0.2F);
		crumble(Blocks.ROOTED_DIRT, Blocks.DIRT, 0.2F);
		crumble(Blocks.SAND, Blocks.AIR, 0.05F);
		crumble(Blocks.SANDSTONE, Blocks.SAND, 0.2F);
		crumble(Blocks.STONE, Blocks.COBBLESTONE, 0.2F);
		crumble(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, 0.2F);
		crumble(Blocks.WEATHERED_CHISELED_COPPER, Blocks.EXPOSED_CHISELED_COPPER, 0.2F);
		crumble(Blocks.WEATHERED_COPPER, Blocks.EXPOSED_COPPER, 0.2F);
		crumble(Blocks.WEATHERED_COPPER_BULB, Blocks.EXPOSED_COPPER_BULB, 0.2F);
		crumble(Blocks.WEATHERED_COPPER_DOOR, Blocks.EXPOSED_COPPER_DOOR, 0.2F);
		crumble(Blocks.WEATHERED_COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE, 0.2F);
		crumble(Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, 0.2F);
		crumble(Blocks.WEATHERED_CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, 0.2F);
		crumble(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, 0.2F);
		crumble(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, 0.2F);

		crumble(TFBlocks.CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK, 0.2F);
		crumble(TFBlocks.CASTLE_BRICK_STAIRS, TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, 0.2F);
		crumble(TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK, 0.2F);
		crumble(TFBlocks.CRACKED_DEADROCK, TFBlocks.WEATHERED_DEADROCK, 0.2F);
		crumble(TFBlocks.DEADROCK, TFBlocks.CRACKED_DEADROCK, 0.2F);
		crumble(TFBlocks.ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE, 0.2F);
		crumble(TFBlocks.MAZESTONE_BRICK, TFBlocks.CRACKED_MAZESTONE, 0.2F);
		crumble(TFBlocks.NAGASTONE_PILLAR, TFBlocks.CRACKED_NAGASTONE_PILLAR, 0.2F);
		crumble(TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, 0.2F);
		crumble(TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, 0.2F);
		crumble(TFBlocks.TOWERWOOD, TFBlocks.CRACKED_TOWERWOOD, 0.2F);
		crumble(TFBlocks.UNDERBRICK, TFBlocks.CRACKED_UNDERBRICK, 0.2F);

		transform(EntityType.BAT, TFEntities.RAVEN.get());
		transform(EntityType.CAVE_SPIDER, TFEntities.SWARM_SPIDER.get());
		transform(EntityType.CHICKEN, TFEntities.PENGUIN.get());
		transform(EntityType.COW, TFEntities.DEER.get());
		transform(EntityType.GHAST, TFEntities.CARMINITE_GHASTGUARD.get());
		transform(EntityType.PARROT, TFEntities.TINY_BIRD.get());
		transform(EntityType.PIG, TFEntities.BOAR.get());
		transform(EntityType.RABBIT, TFEntities.DWARF_RABBIT.get());
		transform(EntityType.SHEEP, TFEntities.BIGHORN_SHEEP.get());
		transform(EntityType.SILVERFISH, TFEntities.TOWERWOOD_BORER.get());
		transform(EntityType.SLIME, TFEntities.MAZE_SLIME.get());
		transform(EntityType.SPIDER, TFEntities.HEDGE_SPIDER.get());
		transform(EntityType.VEX, TFEntities.WRAITH.get());
		transform(EntityType.WITCH, TFEntities.SKELETON_DRUID.get());
		transform(EntityType.WOLF, TFEntities.HOSTILE_WOLF.get());
		transform(EntityType.ZOMBIFIED_PIGLIN, TFEntities.MINOTAUR.get());
		transform(TFEntities.BIGHORN_SHEEP.get(), EntityType.SHEEP);
		transform(TFEntities.BOAR.get(), EntityType.PIG);
		transform(TFEntities.CARMINITE_GHASTGUARD.get(), EntityType.GHAST);
		transform(TFEntities.DEER.get(), EntityType.COW);
		transform(TFEntities.DWARF_RABBIT.get(), EntityType.RABBIT);
		transform(TFEntities.HEDGE_SPIDER.get(), EntityType.SPIDER);
		transform(TFEntities.HOSTILE_WOLF.get(), EntityType.WOLF);
		transform(TFEntities.MAZE_SLIME.get(), EntityType.SLIME);
		transform(TFEntities.MINOTAUR.get(), EntityType.ZOMBIFIED_PIGLIN);
		transform(TFEntities.PENGUIN.get(), EntityType.CHICKEN);
		transform(TFEntities.RAVEN.get(), EntityType.BAT);
		transform(TFEntities.SKELETON_DRUID.get(), EntityType.WITCH);
		transform(TFEntities.SWARM_SPIDER.get(), EntityType.CAVE_SPIDER);
		transform(TFEntities.TINY_BIRD.get(), EntityType.PARROT);
		transform(TFEntities.TOWERWOOD_BORER.get(), EntityType.SILVERFISH);
		transform(TFEntities.WRAITH.get(), EntityType.VEX);

		ominous(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
		ominous(EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN);
		ominous(EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER);
	}

	private TFDataMaps() {
	}

	private static void crumble(Block input, Block output, float chance) {
		CRUMBLE_HORN.put(input, new CrumbledBlock(output, chance));
	}

	private static void crumble(TFRegistryObject<Block> input, TFRegistryObject<Block> output, float chance) {
		crumble(input.get(), output.get(), chance);
	}

	private static void transform(EntityType<?> input, EntityType<?> output) {
		TRANSFORMATION_POWDER.put(input, new EntityTransformation(output));
	}

	private static void ominous(EntityType<?> input, EntityType<?> output) {
		OMINOUS_FIRE.put(input, new EntityTransformation(output));
	}

	@Nullable
	public static MagicMapBiomeColor getMagicMapBiomeColor(Holder<Biome> biome) {
		return biome.unwrapKey().map(MAGIC_MAP_BIOME_COLORS::get).orElse(null);
	}

	public static Map<Block, CrumbledBlock> crumbleHornEntries() {
		return Map.copyOf(CRUMBLE_HORN);
	}

	public static Map<EntityType<?>, EntityTransformation> transformationPowderEntries() {
		return Map.copyOf(TRANSFORMATION_POWDER);
	}

	public static Map<EntityType<?>, EntityTransformation> ominousFireEntries() {
		return Map.copyOf(OMINOUS_FIRE);
	}

	public static Map<ResourceKey<Biome>, MagicMapBiomeColor> magicMapBiomeColorEntries() {
		return MAGIC_MAP_BIOME_COLORS;
	}

	@Nullable
	public static CrumbledBlock getCrumbleHorn(BlockState state) {
		return CRUMBLE_HORN.get(state.getBlock());
	}

	@Nullable
	public static BlockState getCrumbleHornResult(BlockState state, RandomSource random) {
		CrumbledBlock crumbled = getCrumbleHorn(state);
		if (crumbled == null || random.nextFloat() >= crumbled.chanceToCrumble()) {
			return null;
		}
		return crumbled.result() == Blocks.AIR ? Blocks.AIR.defaultBlockState() : crumbled.result().withPropertiesOf(state);
	}

	@Nullable
	public static EntityTransformation getTransformationPowder(EntityType<?> type) {
		return TRANSFORMATION_POWDER.get(type);
	}

	@Nullable
	public static EntityType<?> getTransformationPowderResult(EntityType<?> type) {
		EntityTransformation transformation = getTransformationPowder(type);
		return transformation != null ? transformation.result() : null;
	}

	@Nullable
	public static EntityTransformation getOminousFire(EntityType<?> type) {
		return OMINOUS_FIRE.get(type);
	}

	@Nullable
	public static OreMapOreColor getOreMapOreColor(BlockState state) {
		if (state.is(BlockTags.COAL_ORES)) return new OreMapOreColor(MapColor.TERRACOTTA_BLACK);
		if (state.is(BlockTags.COPPER_ORES)) return new OreMapOreColor(MapColor.COLOR_ORANGE);
		if (state.is(BlockTags.DIAMOND_ORES)) return new OreMapOreColor(MapColor.DIAMOND);
		if (state.is(BlockTags.EMERALD_ORES)) return new OreMapOreColor(MapColor.EMERALD);
		if (state.is(BlockTags.GOLD_ORES)) return new OreMapOreColor(MapColor.GOLD);
		if (state.is(BlockTags.IRON_ORES)) return new OreMapOreColor(MapColor.RAW_IRON);
		if (state.is(BlockTags.LAPIS_ORES)) return new OreMapOreColor(MapColor.LAPIS);
		if (state.is(BlockTags.REDSTONE_ORES)) return new OreMapOreColor(MapColor.TERRACOTTA_RED);
		if (state.is(Blocks.ANCIENT_DEBRIS)) return new OreMapOreColor(MapColor.TERRACOTTA_BROWN);
		return null;
	}
}
