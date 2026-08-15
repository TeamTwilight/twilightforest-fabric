package twilightforest.util;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;
import twilightforest.TFMain;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// For Datagen only! Avoid referencing this class too early, or the DeferredHolders will return null!
public class TFBlockFamilies {

	private static final Map<Block, BlockFamily> MAP = Maps.newHashMap();

	public static final BlockFamily TWILIGHT_OAK = familyBuilder(TFBlocks.TWILIGHT_OAK_PLANKS)
		.stairs(TFBlocks.TWILIGHT_OAK_STAIRS)
		.slab(TFBlocks.TWILIGHT_OAK_SLAB)
		.button(TFBlocks.TWILIGHT_OAK_BUTTON)
		.fence(TFBlocks.TWILIGHT_OAK_FENCE)
		.fenceGate(TFBlocks.TWILIGHT_OAK_GATE)
		.pressurePlate(TFBlocks.TWILIGHT_OAK_PLATE)
		.door(TFBlocks.TWILIGHT_OAK_DOOR)
		.trapdoor(TFBlocks.TWILIGHT_OAK_TRAPDOOR)
		.sign(TFBlocks.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily CANOPY = familyBuilder(TFBlocks.CANOPY_PLANKS)
		.stairs(TFBlocks.CANOPY_STAIRS)
		.slab(TFBlocks.CANOPY_SLAB)
		.button(TFBlocks.CANOPY_BUTTON)
		.fence(TFBlocks.CANOPY_FENCE)
		.fenceGate(TFBlocks.CANOPY_GATE)
		.pressurePlate(TFBlocks.CANOPY_PLATE)
		.door(TFBlocks.CANOPY_DOOR)
		.trapdoor(TFBlocks.CANOPY_TRAPDOOR)
		.sign(TFBlocks.CANOPY_SIGN, TFBlocks.CANOPY_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily MANGROVE = familyBuilder(TFBlocks.MANGROVE_PLANKS)
		.stairs(TFBlocks.MANGROVE_STAIRS)
		.slab(TFBlocks.MANGROVE_SLAB)
		.button(TFBlocks.MANGROVE_BUTTON)
		.fence(TFBlocks.MANGROVE_FENCE)
		.fenceGate(TFBlocks.MANGROVE_GATE)
		.pressurePlate(TFBlocks.MANGROVE_PLATE)
		.door(TFBlocks.MANGROVE_DOOR)
		.trapdoor(TFBlocks.MANGROVE_TRAPDOOR)
		.sign(TFBlocks.MANGROVE_SIGN, TFBlocks.MANGROVE_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily DARKWOOD = familyBuilder(TFBlocks.DARK_PLANKS)
		.stairs(TFBlocks.DARK_STAIRS)
		.slab(TFBlocks.DARK_SLAB)
		.button(TFBlocks.DARK_BUTTON)
		.fence(TFBlocks.DARK_FENCE)
		.fenceGate(TFBlocks.DARK_GATE)
		.pressurePlate(TFBlocks.DARK_PLATE)
		.door(TFBlocks.DARK_DOOR)
		.trapdoor(TFBlocks.DARK_TRAPDOOR)
		.sign(TFBlocks.DARK_SIGN, TFBlocks.DARK_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily TIMEWOOD = familyBuilder(TFBlocks.TIME_PLANKS)
		.stairs(TFBlocks.TIME_STAIRS)
		.slab(TFBlocks.TIME_SLAB)
		.button(TFBlocks.TIME_BUTTON)
		.fence(TFBlocks.TIME_FENCE)
		.fenceGate(TFBlocks.TIME_GATE)
		.pressurePlate(TFBlocks.TIME_PLATE)
		.door(TFBlocks.TIME_DOOR)
		.trapdoor(TFBlocks.TIME_TRAPDOOR)
		.sign(TFBlocks.TIME_SIGN, TFBlocks.TIME_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily TRANSWOOD = familyBuilder(TFBlocks.TRANSFORMATION_PLANKS)
		.stairs(TFBlocks.TRANSFORMATION_STAIRS)
		.slab(TFBlocks.TRANSFORMATION_SLAB)
		.button(TFBlocks.TRANSFORMATION_BUTTON)
		.fence(TFBlocks.TRANSFORMATION_FENCE)
		.fenceGate(TFBlocks.TRANSFORMATION_GATE)
		.pressurePlate(TFBlocks.TRANSFORMATION_PLATE)
		.door(TFBlocks.TRANSFORMATION_DOOR)
		.trapdoor(TFBlocks.TRANSFORMATION_TRAPDOOR)
		.sign(TFBlocks.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily MINEWOOD = familyBuilder(TFBlocks.MINING_PLANKS)
		.stairs(TFBlocks.MINING_STAIRS)
		.slab(TFBlocks.MINING_SLAB)
		.button(TFBlocks.MINING_BUTTON)
		.fence(TFBlocks.MINING_FENCE)
		.fenceGate(TFBlocks.MINING_GATE)
		.pressurePlate(TFBlocks.MINING_PLATE)
		.door(TFBlocks.MINING_DOOR)
		.trapdoor(TFBlocks.MINING_TRAPDOOR)
		.sign(TFBlocks.MINING_SIGN, TFBlocks.MINING_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static final BlockFamily SORTWOOD = familyBuilder(TFBlocks.SORTING_PLANKS)
		.stairs(TFBlocks.SORTING_STAIRS)
		.slab(TFBlocks.SORTING_SLAB)
		.button(TFBlocks.SORTING_BUTTON)
		.fence(TFBlocks.SORTING_FENCE)
		.fenceGate(TFBlocks.SORTING_GATE)
		.pressurePlate(TFBlocks.SORTING_PLATE)
		.door(TFBlocks.SORTING_DOOR)
		.trapdoor(TFBlocks.SORTING_TRAPDOOR)
		.sign(TFBlocks.SORTING_SIGN, TFBlocks.SORTING_WALL_SIGN)
		.recipeGroupPrefix("wooden")
		.recipeUnlockedBy("has_planks")
		.getFamily();

	public static void verifyFamilyShapes(BlockFamily family, BlockFamily.Variant... required) {
		List<BlockFamily.Variant> missing = findMissingFamilyShapes(family, required);

		if (!missing.isEmpty())
			TFMain.LOGGER.warn("BlockFamily " + family + " for " + family.getBaseBlock() + " is missing variants for " + missing);
	}

	public static List<BlockFamily.Variant> findMissingFamilyShapes(BlockFamily family, BlockFamily.Variant... required) {
		ArrayList<BlockFamily.Variant> available = new ArrayList<>(Arrays.asList(required));
		available.removeAll(family.getVariants().keySet());
		return available;
	}

	private static BlockFamily.Builder familyBuilder(Block baseBlock) {
		BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
		BlockFamily blockfamily = MAP.put(baseBlock, builder.getFamily());
		if (blockfamily != null) {
			throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getKey(baseBlock));
		} else {
			return builder;
		}
	}

	public static Stream<BlockFamily> getAllFamilies() {
		return MAP.values().stream();
	}
}
