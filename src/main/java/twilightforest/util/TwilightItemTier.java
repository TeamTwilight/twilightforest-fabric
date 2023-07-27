package twilightforest.util;

import io.github.fabricators_of_create.porting_lib.util.LazyTier;
import io.github.fabricators_of_create.porting_lib.util.TierSortingRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.List;

public class TwilightItemTier {
	// harvestLevel, maxUses, efficiency, damage, enchantability

	public static final Tier IRONWOOD = TierSortingRegistry.registerTier(
			new LazyTier(2, 512, 6.5F, 2, 25, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_ironwood_tool")), () -> Ingredient.of(TFItems.IRONWOOD_INGOT.get())),
			TwilightForestMod.prefix("ironwood"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

	public static final Tier FIERY = TierSortingRegistry.registerTier(
			new LazyTier(4, 1024, 9F, 4, 10, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_fiery_tool")), () -> Ingredient.of(TFItems.FIERY_INGOT.get())),
			TwilightForestMod.prefix("fiery"), List.of(Tiers.NETHERITE), List.of());

	public static final Tier STEELEAF = TierSortingRegistry.registerTier(
			new LazyTier(3, 131, 8.0F, 3, 9, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_steeleaf_tool")), () -> Ingredient.of(TFItems.STEELEAF_INGOT.get())),
			TwilightForestMod.prefix("steeleaf"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));

	public static final Tier KNIGHTMETAL = TierSortingRegistry.registerTier(
			new LazyTier(3, 512, 8.0F, 3, 8, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_knightmetal_tool")), () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get())),
			TwilightForestMod.prefix("knightmetal"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));

	public static final Tier GIANT = TierSortingRegistry.registerTier(
			new LazyTier(1, 1024, 4.0F, 1.0F, 5, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_giant_tool")), () -> Ingredient.of(TFBlocks.GIANT_COBBLESTONE.get())),
			TwilightForestMod.prefix("giant"), List.of(Tiers.STONE), List.of(Tiers.IRON));

	public static final Tier ICE = TierSortingRegistry.registerTier(
			new LazyTier(0, 32, 1.0F, 3.5F, 5, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_ice_tool")), () -> Ingredient.of(Blocks.PACKED_ICE)),
			TwilightForestMod.prefix("ice"), List.of(Tiers.WOOD), List.of());

	public static final Tier GLASS = TierSortingRegistry.registerTier(
			new LazyTier(0, 1, 1.0F, 36.0F, 30, TagKey.create(Registries.BLOCK, TwilightForestMod.prefix("needs_glass_tool")), () -> Ingredient.EMPTY),
			TwilightForestMod.prefix("glass"), List.of(Tiers.WOOD), List.of());

}
