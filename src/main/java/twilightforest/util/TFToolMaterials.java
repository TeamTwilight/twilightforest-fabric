package twilightforest.util;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import io.github.fabricators_of_create.porting_lib.common.util.SimpleTier;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class TFToolMaterials {
	public static final Tier IRONWOOD = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_IRONWOOD_TOOL, 512, 6.5F, 2, 25, BlockTagGenerator.INCORRECT_FOR_IRONWOOD_TOOL, () -> Ingredient.of(ItemTagGenerator.REPAIRS_IRONWOOD_TOOLS));
	public static final Tier FIERY = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_FIERY_TOOL, 1024, 9F, 4, 10, BlockTagGenerator.INCORRECT_FOR_FIERY_TOOL, () -> Ingredient.of(ItemTagGenerator.REPAIRS_FIERY_TOOLS));
	public static final Tier STEELEAF = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_STEELEAF_TOOL, 131, 8.0F, 3, 9, BlockTagGenerator.INCORRECT_FOR_STEELEAF_TOOL, () -> Ingredient.of(ItemTagGenerator.REPAIRS_STEELEAF_TOOLS));
	public static final Tier KNIGHTMETAL = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_KNIGHTMETAL_TOOL, 512, 8.0F, 3, 8, BlockTagGenerator.INCORRECT_FOR_KNIGHTMETAL_TOOL, () -> Ingredient.of(ItemTagGenerator.REPAIRS_KNIGHTMETAL_TOOLS));
	public static final Tier GIANT = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_GIANT_TOOL, 1024, 4.0F, 1.0F, 5, BlockTagGenerator.INCORRECT_FOR_GIANT_TOOL, () -> Ingredient.of(ItemTagGenerator.REPAIRS_GIANT_TOOLS));
	public static final Tier ICE = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_ICE_TOOL, 32, 1.0F, 3.5F, 5, BlockTagGenerator.INCORRECT_FOR_ICE_TOOL, () -> Ingredient.of(ItemTagGenerator.REPAIRS_ICE_TOOLS));
	public static final Tier GLASS = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_GLASS_TOOL, 1, 1.0F, 36.0F, 30, BlockTagGenerator.INCORRECT_FOR_GLASS_TOOL, () -> Ingredient.EMPTY);
}
