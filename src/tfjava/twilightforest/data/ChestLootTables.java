package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;
import twilightforest.loot.conditions.UncraftingTableEnabledCondition;

import java.util.function.BiConsumer;

public record ChestLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> register) {
		HolderLookup.RegistryLookup<Enchantment> lookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		register.accept(TFLootTables.SUSPICIOUS_STEW,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).apply(SetStewEffectFunction.stewEffect()
						.withEffect(MobEffects.JUMP, UniformGenerator.between(7, 10))
						.withEffect(MobEffects.WEAKNESS, UniformGenerator.between(6, 8))
						.withEffect(MobEffects.BLINDNESS, UniformGenerator.between(5, 7))
						.withEffect(MobEffects.POISON, UniformGenerator.between(10, 20))
						.withEffect(MobEffects.SATURATION, UniformGenerator.between(7, 10))
						.withEffect(MobEffects.WITHER, UniformGenerator.between(6, 10))
					))));

		register.accept(TFLootTables.USELESS_LOOT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.POPPY).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.DANDELION).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.FEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.FLINT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.CACTUS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.SUGAR_CANE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.SAND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.FLOWER_POT))
					.add(LootItem.lootTableItem(Items.BONE_MEAL))));

		register.accept(TFLootTables.HUT_JUNK,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(9))
					// Nothing from USELESS_LOOT, too many "green/lively"-looking loot. Needs to be dead or close to actual junk
					.add(LootItem.lootTableItem(Items.COBWEB).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(3))
					.add(LootItem.lootTableItem(Items.BOWL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(3))
					.add(LootItem.lootTableItem(Items.GLASS_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))).setWeight(3))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.WATER)))
					.add(LootItem.lootTableItem(Blocks.COARSE_DIRT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
					.add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))))
					.add(LootItem.lootTableItem(Items.SAND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(3))
					.add(LootItem.lootTableItem(Items.FLINT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(3))
					.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
					.add(LootItem.lootTableItem(Items.BONE_MEAL))
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 8))).setWeight(3))
				).withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(NestedLootTable.lootTableReference(TFLootTables.SUSPICIOUS_STEW).setWeight(8))
					.add(LootItem.lootTableItem(Items.SUSPICIOUS_STEW).setWeight(2))
					.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(4))
					.add(LootItem.lootTableItem(Items.BELL))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
				));

		register.accept(TFLootTables.BASEMENT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.WATER)).setWeight(75))
					.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WHEAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(75))
					.add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(75))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(75))
					.add(LootItem.lootTableItem(Items.SWEET_BERRIES).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 7))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.BREAD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
					.add(LootItem.lootTableItem(TFItems.SHIKA_SENBEI.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.COOKED_BEEF).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
					.add(LootItem.lootTableItem(Items.BAKED_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
					.add(LootItem.lootTableItem(Items.COOKED_CHICKEN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 10))))
					.add(LootItem.lootTableItem(Items.COOKED_COD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MAP).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.CAKE).setWeight(25))
					.add(LootItem.lootTableItem(Items.OAK_BOAT).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.HOLLOW_OAK_SAPLING.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_HOME.get()).setWeight(15))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_RADIANCE.get()).setWeight(15))));

		register.accept(TFLootTables.FOUNDATION_BASEMENT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.WATER)).setWeight(75))
					.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(75))
					.add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.WHEAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.AWKWARD)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.MUNDANE)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.THICK)))
					.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MAP).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_THREAD.get()).setWeight(50))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.OAK_BOAT).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.HOLLOW_OAK_SAPLING.get()).setWeight(25))));

		register.accept(TFLootTables.WELL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(75))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
					.add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))).setWeight(75))
					.add(EmptyLootItem.emptyItem().setWeight(25)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.BUCKET))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.INK_SAC).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET))
					.add(LootItem.lootTableItem(Items.BOWL))
					.add(LootItem.lootTableItem(TFItems.RAW_IRONWOOD.get()))
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.SHEARS))
					.add(LootItem.lootTableItem(Items.SADDLE))
					.add(LootItem.lootTableItem(Items.DIAMOND))
					.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))));

		register.accept(TFLootTables.FANCY_WELL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15))))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 12))))
					.add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))))
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 20)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.BUCKET))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET))
					.add(LootItem.lootTableItem(TFItems.RAW_IRONWOOD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 7))))
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))))
					.add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					.add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 8)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 9))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_STEPS.get()).setWeight(50))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))).setWeight(25))
					.add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_2.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.TRANSFORMATION_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 17))).setWeight(25))));

		register.accept(TFLootTables.HEDGE_MAZE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Blocks.OAK_PLANKS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Blocks.BROWN_MUSHROOM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Blocks.RED_MUSHROOM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WHEAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.COBWEB).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(Items.SHEARS).setWeight(75))
					.add(LootItem.lootTableItem(Items.SADDLE).setWeight(75))
					.add(LootItem.lootTableItem(Items.BOW).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(25))
					.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(25))
					.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(25))
					.add(LootItem.lootTableItem(Items.DIAMOND_HOE).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_MOTION.get()).setWeight(15))));

		register.accept(TFLootTables.HEDGE_CLOTH,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(UniformGenerator.between(4, 6))
					.add(LootItem.lootTableItem(Items.WHITE_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.LIGHT_GRAY_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.GRAY_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.BLACK_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.RED_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.ORANGE_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.YELLOW_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.GREEN_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.LIME_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.BLUE_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.CYAN_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.LIGHT_BLUE_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.PURPLE_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.MAGENTA_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.PINK_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(Items.BROWN_CARPET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))))
				.withPool(LootPool.lootPool()
					.setRolls(UniformGenerator.between(2, 3))
					.add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 17))))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.EMPERORS_CLOTH.get()))));

		register.accept(TFLootTables.TREE_CACHE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WHEAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(75))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFBlocks.TWILIGHT_OAK_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.CANOPY_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.MANGROVE_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.DARKWOOD_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.PUMPKIN_PIE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(TFBlocks.HOLLOW_OAK_SAPLING.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.TIME_SAPLING.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.TRANSFORMATION_SAPLING.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.MINING_SAPLING.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.SORTING_SAPLING.get()).setWeight(25))));

		register.accept(TFLootTables.FALLEN_TRUNK_LOOT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.POISONOUS_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WHEAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.SHIKA_SENBEI.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WATER_BUCKET).setWeight(75))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFBlocks.TWILIGHT_OAK_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.CANOPY_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.MANGROVE_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.DARKWOOD_SAPLING.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.PUMPKIN_PIE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(TFBlocks.HOLLOW_OAK_SAPLING.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_SUPERSTITIOUS.get()).setWeight(25))));

		register.accept(TFLootTables.GRAVEYARD,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 32))))
					.add(LootItem.lootTableItem(Items.PUMPKIN_PIE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.MOON_DIAL.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.TRANSFORMATION_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFBlocks.UNCRAFTING_TABLE.get()).when(UncraftingTableEnabledCondition.uncraftingTableEnabled()))
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE))));

		register.accept(TFLootTables.CAMP_TENT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(TFItems.TANNIN.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))) // Guaranteed Tannin
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//one of the jerkys
					.add(LootItem.lootTableItem(TFItems.VENISON_JERKY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					.add(LootItem.lootTableItem(TFItems.MUTTON_JERKY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					.add(LootItem.lootTableItem(TFItems.PORK_JERKY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
				)
				.withPool(LootPool.lootPool()
					.setRolls(UniformGenerator.between(1, 2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(TFBlocks.BIRCH_DRYING_RACK.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
				)
				.withPool(LootPool.lootPool()
					//rare loot
					.add(LootItem.lootTableItem(Items.LEATHER_HELMET))
					.add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
					.add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS))
					.add(LootItem.lootTableItem(Items.LEATHER_BOOTS))
					.add(LootItem.lootTableItem(Items.SHEARS))
				)
		);

		register.accept(TFLootTables.CAMP_ARMOR_RACK,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.add(LootItem.lootTableItem(Items.LEATHER_HELMET))
					.add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
					.add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS))
					.add(LootItem.lootTableItem(Items.LEATHER_BOOTS))
					.add(LootItem.lootTableItem(Items.CHAINMAIL_HELMET))
					.add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE))
					.add(LootItem.lootTableItem(Items.CHAINMAIL_LEGGINGS))
					.add(LootItem.lootTableItem(Items.CHAINMAIL_BOOTS))
				)
		);

		register.accept(TFLootTables.CAMP_DRYING_RACK,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					//empty
					.add(EmptyLootItem.emptyItem().setWeight(20))
					//common
					.add(LootItem.lootTableItem(TFItems.CHICKEN_JERKY.get()).setWeight(8))
					.add(LootItem.lootTableItem(TFItems.MUTTON_JERKY.get()).setWeight(8))
					.add(LootItem.lootTableItem(TFItems.PORK_JERKY.get()).setWeight(8))
					.add(LootItem.lootTableItem(TFItems.VENISON_JERKY.get()).setWeight(8))
					//uncommon
					.add(LootItem.lootTableItem(TFItems.BEEF_JERKY.get()).setWeight(4))
					.add(LootItem.lootTableItem(TFItems.TANNED_LEATHER.get()).setWeight(4))
					.add(LootItem.lootTableItem(TFItems.RABBIT_JERKY.get()).setWeight(4))
					//rare
					.add(LootItem.lootTableItem(Items.LEATHER).setWeight(2))
					.add(NestedLootTable.inlineLootTable(LootTable.lootTable().withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.LEATHER_HELMET))
						.add(LootItem.lootTableItem(Items.LEATHER_CHESTPLATE))
						.add(LootItem.lootTableItem(Items.LEATHER_LEGGINGS))
						.add(LootItem.lootTableItem(Items.LEATHER_BOOTS))
					).build()).setWeight(2))
					//very rare
					.add(LootItem.lootTableItem(TFItems.GELATINOUS_SLIME_DROP.get()))
				)
		);

		register.accept(TFLootTables.CAMP_POT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					//common
					.add(LootItem.lootTableItem(TFItems.CHICKEN_JERKY.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					.add(LootItem.lootTableItem(TFItems.MUTTON_JERKY.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					.add(LootItem.lootTableItem(TFItems.PORK_JERKY.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					.add(LootItem.lootTableItem(TFItems.VENISON_JERKY.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
					//uncommon
					.add(LootItem.lootTableItem(TFItems.BEEF_JERKY.get()).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(TFItems.TANNED_LEATHER.get()).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					.add(LootItem.lootTableItem(TFItems.RABBIT_JERKY.get()).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))))
					//very rare
					.add(LootItem.lootTableItem(TFItems.GELATINOUS_SLIME_DROP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
				)
		);

		register.accept(TFLootTables.SMALL_HOLLOW_HILL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.WHEAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.BUCKET).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.BREAD))
					.add(LootItem.lootTableItem(TFItems.ORE_MAGNET.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.LIVEROOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
					.add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.ORE_METER.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.TRANSFORMATION_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_FINDINGS.get()).setWeight(25))));

		register.accept(TFLootTables.MEDIUM_HOLLOW_HILL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.LADDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.BUCKET).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.BAKED_POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFItems.ORE_MAGNET.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.MAGIC_MAP.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.TRANSFORMATION_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(TFBlocks.UNCRAFTING_TABLE.get()).when(UncraftingTableEnabledCondition.uncraftingTableEnabled()).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(25))
					.add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.PEACOCK_FEATHER_FAN.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.POCKET_WATCH.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_WAYFARER.get()).setWeight(25))));

		register.accept(TFLootTables.LARGE_HOLLOW_HILL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.COD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.PUMPKIN_PIE))
					.add(LootItem.lootTableItem(TFItems.ORE_MAGNET.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.TRANSFORMATION_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_PICKAXE.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(1)).withEnchantment(lookup.getOrThrow(Enchantments.FORTUNE), ConstantValue.exactly(1))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.MAGIC_MAP.get()).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MOONWORM_QUEEN.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFBlocks.IRONWOOD_BLOCK.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MUSIC_DISC_MAKER.get()).setWeight(20))));

		register.accept(TFLootTables.QUEST_GROVE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(LootItem.lootTableItem(Blocks.WHITE_WOOL))
					.add(LootItem.lootTableItem(Blocks.ORANGE_WOOL))
					.add(LootItem.lootTableItem(Blocks.MAGENTA_WOOL))
					.add(LootItem.lootTableItem(Blocks.LIGHT_BLUE_WOOL))
					.add(LootItem.lootTableItem(Blocks.YELLOW_WOOL))
					.add(LootItem.lootTableItem(Blocks.LIME_WOOL))
					.add(LootItem.lootTableItem(Blocks.PINK_WOOL))
					.add(LootItem.lootTableItem(Blocks.GRAY_WOOL))
					.add(LootItem.lootTableItem(Blocks.LIGHT_GRAY_WOOL))
					.add(LootItem.lootTableItem(Blocks.CYAN_WOOL))
					.add(LootItem.lootTableItem(Blocks.PURPLE_WOOL))
					.add(LootItem.lootTableItem(Blocks.BLUE_WOOL))
					.add(LootItem.lootTableItem(Blocks.BROWN_WOOL))
					.add(LootItem.lootTableItem(Blocks.GREEN_WOOL))
					.add(LootItem.lootTableItem(Blocks.RED_WOOL))
					.add(LootItem.lootTableItem(Blocks.BLACK_WOOL))));

		register.accept(TFLootTables.TOWER_LIBRARY,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(3))
					//common loot
					.add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(24, 56))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 12))))
					.add(LootItem.lootTableItem(Items.INK_SAC).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), UniformGenerator.between(5, 10))))
					.add(LootItem.lootTableItem(TFBlocks.CANOPY_BOOKSHELF.get().asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))
					.add(LootItem.lootTableItem(Blocks.BOOKSHELF).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))
					.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(Items.LADDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 10)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(EmptyLootItem.emptyItem().setWeight(3))
					.add(LootItem.lootTableItem(TFItems.BRITTLE_FLASK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(2))
					.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 6))).setWeight(2))
					.add(LootItem.lootTableItem(TFItems.CROWN_SPLINTER.get()))
					.add(LootItem.lootTableItem(Items.DIAMOND))
					.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(2))
					.add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), UniformGenerator.between(20, 30))).setWeight(2))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new SetEnchantmentsFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.RENEWAL), ConstantValue.exactly(1.0F))))));

		register.accept(TFLootTables.TOWER_ROOM,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.BONE).apply((SetItemCountFunction.setCount(UniformGenerator.between(4, 16)))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CLAY_BALL).apply((SetItemCountFunction.setCount(UniformGenerator.between(6, 12)))).setWeight(75))
					.add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))).setWeight(75))
					.add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(9, 24))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CANDLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))).setWeight(75))
					.add(NestedLootTable.lootTableReference(TFLootTables.SUSPICIOUS_STEW).setWeight(75))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))).setWeight(75))
					.add(LootItem.lootTableItem(Items.IRON_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 20))).setWeight(75))
					.add(LootItem.lootTableItem(Items.BREAD).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFItems.TRANSFORMATION_POWDER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(TFItems.MAGIC_MAP.get()))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))))
					.add(LootItem.lootTableItem(TFItems.EXANIMATE_ESSENCE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(TFItems.BRITTLE_FLASK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))))
					.add(LootItem.lootTableItem(Items.AMETHYST_SHARD).apply((SetItemCountFunction.setCount(UniformGenerator.between(4, 13)))))
					.add(LootItem.lootTableItem(Items.ENDER_PEARL).apply((SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.HEALING)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.REGENERATION)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRENGTH)))
					.add(LootItem.lootTableItem(Items.GOLDEN_SWORD).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries())))
					.add(LootItem.lootTableItem(Items.ENDER_PEARL).apply((SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), UniformGenerator.between(20, 30)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(EmptyLootItem.emptyItem().setWeight(75))
					.add(LootItem.lootTableItem(TFItems.LIVEROOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(75))
					.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CROWN_SPLINTER.get()).setWeight(75))
					.add(LootItem.lootTableItem(Items.OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GOLDEN_HELMET).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), UniformGenerator.between(10, 15))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()).setWeight(75))
					.add(LootItem.lootTableItem(Items.BOOK).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), UniformGenerator.between(20, 30))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GOLDEN_AXE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(20))).setWeight(75))
					//TODO XP Charm
					.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.MOONWORM_QUEEN.get()).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.PEACOCK_FEATHER_FAN.get()).setWeight(25))));

		register.accept(TFLootTables.TOWER_POTION,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 6))))
					.add(LootItem.lootTableItem(Items.GHAST_TEAR).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(Items.FERMENTED_SPIDER_EYE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.BLAZE_POWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9))))
					.add(LootItem.lootTableItem(Items.GLASS_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.MAGMA_CREAM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(Items.SUGAR).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 7))))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 20))))
					.add(LootItem.lootTableItem(Items.NETHER_WART).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 13)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					.add(LootItem.lootTableItem(Items.GOLDEN_CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 7))))
					.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
					.add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetPotionFunction.setPotion(Potions.HEALING)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRENGTH)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(Items.BLAZE_ROD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.SWIFTNESS)).setWeight(75))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.REGENERATION)).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.CROWN_SPLINTER.get()).setWeight(75))
					.add(LootItem.lootTableItem(Items.BREWING_STAND).setWeight(25))
					.add(LootItem.lootTableItem(TFItems.GREATER_FLASK.get()).setWeight(25)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.BRITTLE_FLASK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))))));

		register.accept(TFLootTables.JUST_BONES, LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(NestedLootTable.inlineLootTable(LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(49F, 50F)))))
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(39.0F))))).build()))
				.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(15.0F))).setWeight(50))
				.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(16.0F))).setWeight(50))
				.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(12.0F, 13.0F))).setWeight(50))
				.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(13.0F))).setWeight(50))));

		register.accept(TFLootTables.TOWER_GRAVE_UPPER, LootTable.lootTable()
			.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(TFLootTables.JUST_BONES)))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.SKELETON_SKULL)))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.2F)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.DIAMOND))
				.add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
				.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get())))
		);

		register.accept(TFLootTables.TOWER_GRAVE_LOWER, LootTable.lootTable()
			.withPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(TFLootTables.JUST_BONES)))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.LEATHER_BOOTS).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.2F)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.IRON_LEGGINGS).apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.1F, 0.2F)))))
		);

		register.accept(TFLootTables.TOWER_ENCHANTING, LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.LAPIS_LAZULI).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 15)))))
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.BOOK).apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 32))))));

		register.accept(TFLootTables.TOWER_JARS, LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.CHAIN))
				.add(LootItem.lootTableItem(Items.ENDER_PEARL))
				.add(LootItem.lootTableItem(Items.SPIDER_EYE))
				.add(LootItem.lootTableItem(Items.FERMENTED_SPIDER_EYE))
				.add(LootItem.lootTableItem(Items.BROWN_MUSHROOM))
				.add(LootItem.lootTableItem(Items.RED_MUSHROOM))
				.add(LootItem.lootTableItem(TFItems.RAVEN_FEATHER.get()))
				.add(LootItem.lootTableItem(TFItems.LIVEROOT.get()))
				.add(LootItem.lootTableItem(Items.BONE_MEAL))
				.add(LootItem.lootTableItem(Items.INK_SAC))
				.add(LootItem.lootTableItem(Items.PAPER))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH))
				.add(LootItem.lootTableItem(Items.ZOMBIE_HEAD))
				.add(LootItem.lootTableItem(Items.SKELETON_SKULL))
				.add(EmptyLootItem.emptyItem().setWeight(8))));

		register.accept(TFLootTables.TOWER_FOYER, LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3, 4))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(6))
				.add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(6))
				.add(LootItem.lootTableItem(Items.CHARCOAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(6))
				.add(LootItem.lootTableItem(TFItems.RAVEN_FEATHER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))).setWeight(6))
				.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(6))
				.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(6))
				.add(LootItem.lootTableItem(Items.BREAD).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3))).setWeight(3))
				.add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5))).setWeight(3))
				.add(NestedLootTable.lootTableReference(TFLootTables.SUSPICIOUS_STEW).setWeight(3))
				.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()))
				.add(LootItem.lootTableItem(Items.DIAMOND))
			));

		register.accept(TFLootTables.CASKET_LOOT, LootTable.lootTable()
			.withPool(LootPool.lootPool().setRolls(UniformGenerator.between(8, 16))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH))
				.add(LootItem.lootTableItem(Items.BONE).setWeight(3))));

		//all values in this loot table have been halved so I can fill both chests that appear in the dead ends
		register.accept(TFLootTables.LABYRINTH_DEAD_END,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75))
					.add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
					.add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.MILK_BUCKET))
					.add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
					.add(LootItem.lootTableItem(TFBlocks.RED_THREAD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 18))))
					.add(LootItem.lootTableItem(Items.BLAZE_ROD))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.FIRE_REACT))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))));

		register.accept(TFLootTables.LABYRINTH_ROOM,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET))
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_HELMET.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_LEGGINGS.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_BOOTS.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_PICKAXE.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_SWORD.get()))
					.add(LootItem.lootTableItem(TFBlocks.RED_THREAD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 15.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.MAZE_MAP_FOCUS.get()))
					.add(LootItem.lootTableItem(TFItems.MAZE_MAP.get()))
					.add(LootItem.lootTableItem(Items.TNT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.FIRE_REACT))))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)))));

		register.accept(TFLootTables.LABYRINTH_VAULT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))))
					.add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))))
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_HEALING)))
					.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(Potions.STRONG_SWIFTNESS))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.BOW).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.INFINITY), ConstantValue.exactly(1)).withEnchantment(lookup.getOrThrow(Enchantments.PUNCH), ConstantValue.exactly(2))))
					.add(LootItem.lootTableItem(Items.BOW).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.POWER), ConstantValue.exactly(3)).withEnchantment(lookup.getOrThrow(Enchantments.FLAME), ConstantValue.exactly(1))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.FIRE_REACT))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_SHOVEL.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(4)).withEnchantment(lookup.getOrThrow(Enchantments.UNBREAKING), ConstantValue.exactly(2))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_AXE.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(5))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_CHESTPLATE.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(3))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_BOOTS.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(2))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_LEGGINGS.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.FIRE_PROTECTION), ConstantValue.exactly(4))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_HELMET.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.RESPIRATION), ConstantValue.exactly(3)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(Items.EMERALD_BLOCK))
					.add(LootItem.lootTableItem(Items.ENDER_CHEST))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_PICKAXE.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(4)).withEnchantment(lookup.getOrThrow(Enchantments.SILK_TOUCH), ConstantValue.exactly(1))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_SWORD.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(4)).withEnchantment(lookup.getOrThrow(Enchantments.KNOCKBACK), ConstantValue.exactly(2))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_SWORD.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.BANE_OF_ARTHROPODS), ConstantValue.exactly(5)).withEnchantment(lookup.getOrThrow(Enchantments.FIRE_ASPECT), ConstantValue.exactly(2))))));

		//Same as the one above, but with a 100% chance to get a mazebreaker
		register.accept(TFLootTables.LABYRINTH_VAULT_JACKPOT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(NestedLootTable.lootTableReference(TFLootTables.LABYRINTH_VAULT)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//jackpot guaranteed mazebreaker
					.add(LootItem.lootTableItem(TFItems.MAZEBREAKER_PICKAXE.get()).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(4)).withEnchantment(lookup.getOrThrow(Enchantments.UNBREAKING), ConstantValue.exactly(3)).withEnchantment(lookup.getOrThrow(Enchantments.FORTUNE), ConstantValue.exactly(2))))));

		register.accept(TFLootTables.STRONGHOLD_CACHE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))))
					.add(LootItem.lootTableItem(Items.BLUE_WOOL))
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.BUCKET))
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()))
					.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(75))
					.add(LootItem.lootTableItem(Items.BOW).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(20))).setWeight(75))
					.add(LootItem.lootTableItem(Items.IRON_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(20))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_SWORD.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(15))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_SWORD.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(10))).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.BANE_OF_ARTHROPODS), ConstantValue.exactly(4))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(4))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.SMITE), ConstantValue.exactly(4))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.UNBREAKING), ConstantValue.exactly(2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.PROTECTION), ConstantValue.exactly(3))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.PROJECTILE_PROTECTION), ConstantValue.exactly(3))).setWeight(25))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.DESTRUCTION))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.FEATHER_FALLING), ConstantValue.exactly(3))).setWeight(25))));

		register.accept(TFLootTables.STRONGHOLD_ROOM,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_HELMET.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_LEGGINGS.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_BOOTS.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_PICKAXE.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_SWORD.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_SWORD.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(25))))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_SWORD.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(20))))
					.add(LootItem.lootTableItem(Items.IRON_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(30))))
					.add(LootItem.lootTableItem(Items.BOW).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(30))))
					.add(LootItem.lootTableItem(Items.DIAMOND_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(15))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.DESTRUCTION))))
					.add(LootItem.lootTableItem(TFItems.MAZE_MAP_FOCUS.get()))));

		register.accept(TFLootTables.DARKTOWER_CACHE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.CHARCOAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.EXPERIMENT_115.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75))
					.add(LootItem.lootTableItem(Items.RED_WOOL).setWeight(75))
					.add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(Items.REDSTONE_LAMP).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
					.add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));

		register.accept(TFLootTables.DARKTOWER_KEY,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.EXPERIMENT_115.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFItems.STEELEAF_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_HELMET.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_LEGGINGS.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_BOOTS.get()))
					.add(LootItem.lootTableItem(TFItems.STEELEAF_PICKAXE.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_SWORD.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get()))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.FEATHER_FALLING), ConstantValue.exactly(3))))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.KNOCKBACK), ConstantValue.exactly(2))))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.EFFICIENCY), ConstantValue.exactly(3)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.TOWER_KEY.get()))));

		// Keeping this here for legacy generated chests
		register.accept(TFLootTables.DARKTOWER_BOSS,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(LootItem.lootTableItem(TFItems.CARMINITE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					.add(LootItem.lootTableItem(TFItems.FIERY_TEARS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFBlocks.UR_GHAST_TROPHY.get()))));

		register.accept(TFLootTables.AURORA_CACHE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(NestedLootTable.lootTableReference(TFLootTables.USELESS_LOOT).setWeight(25))
					//common loot
					.add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
					.add(LootItem.lootTableItem(Items.ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(Items.PACKED_ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1.get()))
					.add(LootItem.lootTableItem(TFItems.IRONWOOD_INGOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(TFBlocks.AURORA_BLOCK.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.ICE_BOW.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.ENDER_BOW.get()).setWeight(75))
					.add(LootItem.lootTableItem(TFItems.ICE_SWORD.get()).setWeight(75))
					//ultrarare loot
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.SHARPNESS), ConstantValue.exactly(4))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.POWER), ConstantValue.exactly(4))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.PUNCH), ConstantValue.exactly(2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.UNBREAKING), ConstantValue.exactly(2))).setWeight(25))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.CHILL_AURA))).setWeight(25))
					.add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder(false).withEnchantment(lookup.getOrThrow(Enchantments.INFINITY), ConstantValue.exactly(1))).setWeight(25))));

		register.accept(TFLootTables.AURORA_ROOM,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(TFItems.MAZE_WAFER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
					.add(LootItem.lootTableItem(TFItems.ICE_BOMB.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(TFBlocks.FIREFLY.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
					.add(LootItem.lootTableItem(Items.ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.PACKED_ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_HELMET.get()))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_LEGGINGS.get()))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_BOOTS.get()))
					.add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_CHESTPLATE.get()))
					.add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_SWORD.get()))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_LIFE_1.get())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.ICE_BOW.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(30))))
					.add(LootItem.lootTableItem(TFItems.ENDER_BOW.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(5))))
					.add(LootItem.lootTableItem(TFItems.ICE_SWORD.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(25))))
					.add(LootItem.lootTableItem(Items.BOOK).apply(new EnchantRandomlyFunction.Builder().withEnchantment(lookup.getOrThrow(TFEnchantments.CHILL_AURA))))
					.add(LootItem.lootTableItem(TFItems.GLASS_SWORD.get()).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries(), ConstantValue.exactly(20))))));

		register.accept(TFLootTables.TROLL_GARDEN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.RED_MUSHROOM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
					.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.BONE_MEAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFBlocks.UBEROUS_SOIL.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					//rare loot
					.add(LootItem.lootTableItem(TFItems.MAGIC_BEANS.get()))));

		register.accept(TFLootTables.TROLL_VAULT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					//common loot
					.add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16))))
					.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16))))
					.add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2))
					//uncommon loot
					.add(LootItem.lootTableItem(TFBlocks.TROLLSTEINN.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
					.add(LootItem.lootTableItem(Items.OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))));

		register.accept(TFLootTables.TROLL_VAULT_WITH_LAMP,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(NestedLootTable.lootTableReference(TFLootTables.TROLL_VAULT)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.LAMPOFCINDERS.get()))));
	}
}
