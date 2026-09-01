package twilightforest.item.mapdata;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFBiomes;

import java.util.HashMap;
import java.util.Map;

/*
* Most of the content in this class is adapted from implementations in ServerLevel and ClientLevel.
*/
public final class MapDataManager {
	public static final Map<ResourceKey<Biome>, Pair<MapColor, Integer>> MAGIC_MAP_BIOME_COLOR = Map.ofEntries(
		Map.entry(TFBiomes.FOREST, Pair.of(MapColor.PLANT, 1)),
		Map.entry(TFBiomes.DENSE_FOREST, Pair.of(MapColor.PLANT, 0)),
		Map.entry(TFBiomes.LAKE, Pair.of(MapColor.WATER, 3)),
		Map.entry(TFBiomes.STREAM, Pair.of(MapColor.WATER, 1)),
		Map.entry(TFBiomes.SWAMP, Pair.of(MapColor.DIAMOND, 3)),
		Map.entry(TFBiomes.FIRE_SWAMP, Pair.of(MapColor.NETHER, 1)),
		Map.entry(TFBiomes.CLEARING, Pair.of(MapColor.GRASS, 2)),
		Map.entry(TFBiomes.OAK_SAVANNAH, Pair.of(MapColor.GRASS, 0)),
		Map.entry(TFBiomes.HIGHLANDS, Pair.of(MapColor.DIRT, 0)),
		Map.entry(TFBiomes.THORNLANDS, Pair.of(MapColor.WOOD, 3)),
		Map.entry(TFBiomes.FINAL_PLATEAU, Pair.of(MapColor.COLOR_LIGHT_GRAY, 2)),
		Map.entry(TFBiomes.FIREFLY_FOREST, Pair.of(MapColor.EMERALD, 1)),
		Map.entry(TFBiomes.DARK_FOREST, Pair.of(MapColor.COLOR_GREEN, 3)),
		Map.entry(TFBiomes.DARK_FOREST_CENTER, Pair.of(MapColor.COLOR_ORANGE, 3)),
		Map.entry(TFBiomes.SNOWY_FOREST, Pair.of(MapColor.SNOW, 1)),
		Map.entry(TFBiomes.GLACIER, Pair.of(MapColor.ICE, 1)),
		Map.entry(TFBiomes.MUSHROOM_FOREST, Pair.of(MapColor.COLOR_ORANGE, 0)),
		Map.entry(TFBiomes.DENSE_MUSHROOM_FOREST, Pair.of(MapColor.COLOR_PINK, 0)),
		Map.entry(TFBiomes.ENCHANTED_FOREST, Pair.of(MapColor.COLOR_CYAN, 2)),
		Map.entry(TFBiomes.SPOOKY_FOREST, Pair.of(MapColor.COLOR_PURPLE, 0))
	);

	public static final Map<Either<TagKey<Block>, Block>, MapColor> ORE_MAP_ORE_COLOR = Map.ofEntries(
		Map.entry(Either.left(BlockTags.COPPER_ORES), MapColor.COLOR_ORANGE),
		Map.entry(Either.left(BlockTags.COAL_ORES), MapColor.COLOR_BLACK),
		Map.entry(Either.left(BlockTags.IRON_ORES),MapColor.RAW_IRON),
		Map.entry(Either.left(BlockTags.LAPIS_ORES), MapColor.LAPIS),
		Map.entry(Either.left(BlockTags.GOLD_ORES), MapColor.GOLD),
		Map.entry(Either.left(BlockTags.REDSTONE_ORES), MapColor.COLOR_RED),
		Map.entry(Either.left(BlockTags.DIAMOND_ORES), MapColor.DIAMOND),
		Map.entry(Either.left(BlockTags.EMERALD_ORES), MapColor.EMERALD),
		Map.entry(Either.right(Blocks.ANCIENT_DEBRIS), MapColor.TERRACOTTA_BROWN)
	);

	private static final Map<MapId, TFMagicMapData> MAGIC_MAP_CLIENT_DATA = new HashMap<>();
	private static final Map<MapId, TFMazeMapData> MAZE_MAP_CLIENT_DATA = new HashMap<>();

	public static @Nullable TFMagicMapData getServerMagicMapData(ServerLevel serverLevel, MapId id) {
		return serverLevel.getServer().getDataStorage().get(TFMagicMapData.magicMapType(id));
	}

	public static @Nullable TFMazeMapData getServerMazeMapData(ServerLevel serverLevel, MapId id) {
		return serverLevel.getServer().getDataStorage().get(TFMazeMapData.mazeMapType(id));
	}

	public static @Nullable TFMagicMapData getClientMagicMapData(MapId id) {
		return MAGIC_MAP_CLIENT_DATA.get(id);
	}

	public static @Nullable TFMazeMapData getClientMazeMapData(MapId id) {
		return MAZE_MAP_CLIENT_DATA.get(id);
	}

	public static void saveServerMagicMapData(ServerLevel serverLevel, MapId id, TFMagicMapData data) {
		serverLevel.getServer().getDataStorage().set(TFMagicMapData.magicMapType(id), data);
	}

	public static void saveServerMazeMapData(ServerLevel serverLevel, MapId id, TFMazeMapData data) {
		serverLevel.getServer().getDataStorage().set(TFMazeMapData.mazeMapType(id), data);
	}

	public static void saveClientMagicMapData(MapId id, TFMagicMapData data) {
		MAGIC_MAP_CLIENT_DATA.put(id, data);
	}

	public static void saveClientMazeMapData(MapId id, TFMazeMapData data) {
		MAZE_MAP_CLIENT_DATA.put(id, data);
	}

	public static void init() {
		ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> {
			MAGIC_MAP_CLIENT_DATA.clear();
			MAZE_MAP_CLIENT_DATA.clear();
		});
	}
}