package twilightforest.data;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import twilightforest.loot.TFTreasure;
import twilightforest.loot.conditions.ModExists;
import twilightforest.loot.functions.Enchant;
import twilightforest.loot.functions.ModItemSwap;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChestLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {


    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> register) {
        register.accept(TFTreasure.USELESS_LOOT,
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

        register.accept(TFTreasure.basement.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:water")))).setWeight(75))
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
                                .add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.BREAD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
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
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.GOLDEN_CARROT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.CAKE).setWeight(25))
                                .add(LootItem.lootTableItem(Items.OAK_BOAT).setWeight(25))
                                .add(LootItem.lootTableItem(TFBlocks.hollow_oak_sapling.asItem()).setWeight(25))));

        register.accept(TFTreasure.hedgemaze.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
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
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
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
                                .add(LootItem.lootTableItem(Items.DIAMOND_HOE).setWeight(25))));

        register.accept(TFTreasure.tree_cache.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
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
                                .add(LootItem.lootTableItem(TFBlocks.oak_sapling.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFBlocks.canopy_sapling.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFBlocks.mangrove_sapling.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFBlocks.darkwood_sapling.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(Items.PUMPKIN_PIE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(TFBlocks.hollow_oak_sapling.asItem()).setWeight(25))
                                .add(LootItem.lootTableItem(TFBlocks.time_sapling.asItem()).setWeight(25))
                                .add(LootItem.lootTableItem(TFBlocks.transformation_sapling.asItem()).setWeight(25))
                                .add(LootItem.lootTableItem(TFBlocks.mining_sapling.asItem()).setWeight(25))
                                .add(LootItem.lootTableItem(TFBlocks.sorting_sapling.asItem()).setWeight(25))));

        register.accept(TFTreasure.graveyard.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 32))))
                                .add(LootItem.lootTableItem(Items.PUMPKIN_PIE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.torchberries).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.moon_dial)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.transformation_powder).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(TFBlocks.uncrafting_table.asItem()))
                                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))));

        register.accept(TFTreasure.hill1.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
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
                                .add(LootItem.lootTableItem(TFItems.ore_magnet)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.liveroot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(25))
                                .add(LootItem.lootTableItem(TFItems.transformation_powder).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(25))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(25))));

        register.accept(TFTreasure.hill2.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
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
                                .add(LootItem.lootTableItem(TFItems.ore_magnet))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.naga_scale).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.transformation_powder).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(TFBlocks.uncrafting_table.asItem()).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(25))
                                .add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(25))
                                .add(LootItem.lootTableItem(TFItems.peacock_fan).setWeight(25))
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1).setWeight(25))));

        register.accept(TFTreasure.hill3.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.POTATO).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.COD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.torchberries).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.TORCH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(Items.PUMPKIN_PIE))
                                .add(LootItem.lootTableItem(TFItems.ore_magnet)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.transformation_powder).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ironwood_pickaxe).apply(Enchant.builder().apply(Enchantments.BLOCK_EFFICIENCY, 1).apply(Enchantments.BLOCK_FORTUNE, 1)).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.naga_scale).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(20))
                                .add(LootItem.lootTableItem(TFItems.moonworm_queen).setWeight(20))
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1).setWeight(20))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1).setWeight(20))
                                .add(LootItem.lootTableItem(TFBlocks.mangrove_sapling.asItem()).setWeight(25))));

        register.accept(TFTreasure.quest_grove.lootTable,
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

        register.accept(TFTreasure.tower_library.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:water")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.LADDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.CLAY_BALL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(5))))
                                .add(LootItem.lootTableItem(Items.FIRE_CHARGE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(Items.BOOK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(Items.MAP))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:poison")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:awkward")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:weakness"))))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(Items.STONE_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(10))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.WOODEN_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(15))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.BOW).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(5))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:weakness")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_regeneration")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_healing")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_swiftness")))).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(10))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.IRON_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.BOW).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.BOOKSHELF).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(25))));

        register.accept(TFTreasure.tower_room.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:water")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.SUGAR).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.SPIDER_EYE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GHAST_TEAR).setWeight(75))
                                .add(LootItem.lootTableItem(Items.MAGMA_CREAM).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.FERMENTED_SPIDER_EYE).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GLISTERING_MELON_SLICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.BLAZE_POWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(10))))
                                .add(LootItem.lootTableItem(Items.GOLDEN_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(7))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:swiftness")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:healing")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:fire_resistance")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:harming"))))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(10))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.transformation_powder).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1).setWeight(75))
                                .add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:healing")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.SPLASH_POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_harming")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_swiftness")))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_regeneration")))).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.GOLDEN_AXE).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(25))
                                .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(25))
                                .add(LootItem.lootTableItem(TFItems.moonworm_queen).setWeight(25))
                                .add(LootItem.lootTableItem(TFItems.peacock_fan).setWeight(25))
                                .add(LootItem.lootTableItem(Items.OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(25))));

        register.accept(TFTreasure.labyrinth_deadend.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.MILK_BUCKET))
                                .add(LootItem.lootTableItem(Items.PAPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
                                .add(LootItem.lootTableItem(Items.BLAZE_ROD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))));

        register.accept(TFTreasure.labyrinth_room.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(Items.MILK_BUCKET))
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_helmet))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_chestplate))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_leggings))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_boots))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_pickaxe))
                                .add(LootItem.lootTableItem(TFItems.ironwood_chestplate))
                                .add(LootItem.lootTableItem(TFItems.ironwood_sword)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.maze_map_focus))
                                .add(LootItem.lootTableItem(Items.TNT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_healing")))))));

        register.accept(TFTreasure.labyrinth_vault.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))))
                                .add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))))
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_regeneration")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_healing")))))
                                .add(LootItem.lootTableItem(Items.POTION).apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> nbt.putString("Potion", "minecraft:strong_swiftness"))))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.BOW).apply(Enchant.builder().apply(Enchantments.INFINITY_ARROWS, 1).apply(Enchantments.PUNCH_ARROWS, 2)))
                                .add(LootItem.lootTableItem(Items.BOW).apply(Enchant.builder().apply(Enchantments.POWER_ARROWS, 3).apply(Enchantments.FLAMING_ARROWS, 1)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_shovel).apply(Enchant.builder().apply(Enchantments.BLOCK_EFFICIENCY, 4).apply(Enchantments.UNBREAKING, 2)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_axe).apply(Enchant.builder().apply(Enchantments.BLOCK_EFFICIENCY, 5)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_chestplate).apply(Enchant.builder().apply(Enchantments.ALL_DAMAGE_PROTECTION, 3)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_boots).apply(Enchant.builder().apply(Enchantments.ALL_DAMAGE_PROTECTION, 2)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_leggings).apply(Enchant.builder().apply(Enchantments.FIRE_PROTECTION, 4)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_helmet).apply(Enchant.builder().apply(Enchantments.RESPIRATION, 3))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(Items.EMERALD_BLOCK))
                                .add(LootItem.lootTableItem(Items.ENDER_CHEST))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_pickaxe).apply(Enchant.builder().apply(Enchantments.BLOCK_EFFICIENCY, 4).apply(Enchantments.SILK_TOUCH, 1)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_sword).apply(Enchant.builder().apply(Enchantments.SHARPNESS, 4).apply(Enchantments.KNOCKBACK, 2)))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_sword).apply(Enchant.builder().apply(Enchantments.BANE_OF_ARTHROPODS, 5).apply(Enchantments.FIRE_ASPECT, 2)))
                                .add(LootItem.lootTableItem(TFItems.mazebreaker_pickaxe).apply(Enchant.builder().apply(Enchantments.BLOCK_EFFICIENCY, 4).apply(Enchantments.UNBREAKING, 3).apply(Enchantments.BLOCK_FORTUNE, 2)))));

        register.accept(TFTreasure.stronghold_cache.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))))
                                .add(LootItem.lootTableItem(Items.BLUE_WOOL))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.BUCKET))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1))
                                .add(LootItem.lootTableItem(TFItems.armor_shard).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.knightmetal_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.BOW).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.IRON_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ironwood_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(15))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(10))).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.BANE_OF_ARTHROPODS, 4)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.SHARPNESS, 4)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.SMITE, 4)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.UNBREAKING, 2)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.ALL_DAMAGE_PROTECTION, 3)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.PROJECTILE_PROTECTION, 3)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.FALL_PROTECTION, 3)).setWeight(25))));

        register.accept(TFTreasure.stronghold_room.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_helmet))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_chestplate))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_leggings))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_boots))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_pickaxe))
                                .add(LootItem.lootTableItem(TFItems.ironwood_chestplate))
                                .add(LootItem.lootTableItem(TFItems.ironwood_sword)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.ironwood_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(25))))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))))
                                .add(LootItem.lootTableItem(Items.IRON_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30))))
                                .add(LootItem.lootTableItem(Items.BOW).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30))))
                                .add(LootItem.lootTableItem(Items.DIAMOND_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(15))))
                                .add(LootItem.lootTableItem(TFItems.maze_map_focus))));

        register.accept(TFTreasure.stronghold_boss.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(TFItems.knightmetal_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))))
                                .add(LootItem.lootTableItem(TFItems.knightmetal_pickaxe).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))))
                                .add(LootItem.lootTableItem(TFItems.knightmetal_axe).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFItems.phantom_helmet).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))))
                                .add(LootItem.lootTableItem(TFItems.phantom_chestplate).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.phantom_helmet).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30))))
                                .add(LootItem.lootTableItem(TFItems.phantom_chestplate).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFBlocks.knight_phantom_trophy.asItem())))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFItems.carminite)
                                        .when(ModExists.builder("immersiveengineering"))
                                        .apply(ModItemSwap.builder().apply("immersiveengineering", Registry.ITEM.get(TwilightForestMod.prefix("shader")), TFItems.knightmetal_ingot))
                                        .apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
                                            nbt.putString("shader_name", "twilightforest:knight_phantom");
                                        })))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFItems.carminite)
                                        .when(ModExists.builder("immersiveengineering"))
                                        .apply(ModItemSwap.builder().apply("immersiveengineering", Registry.ITEM.get(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.knightmetal_ingot)))));

        register.accept(TFTreasure.darktower_cache.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.CHARCOAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.experiment_115).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.RED_WOOL).setWeight(75))
                                .add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(Items.REDSTONE_LAMP).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
                                .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));

        register.accept(TFTreasure.darktower_key.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.experiment_115).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFItems.steeleaf_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_helmet))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_chestplate))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_leggings))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_boots))
                                .add(LootItem.lootTableItem(TFItems.steeleaf_pickaxe))
                                .add(LootItem.lootTableItem(TFItems.ironwood_chestplate))
                                .add(LootItem.lootTableItem(TFItems.ironwood_sword)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.FALL_PROTECTION, 3)))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.KNOCKBACK, 2)))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.BLOCK_EFFICIENCY, 3))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFItems.tower_key))));

        register.accept(TFTreasure.darktower_boss.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootItem.lootTableItem(TFItems.carminite).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                .add(LootItem.lootTableItem(TFItems.fiery_tears).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFBlocks.ur_ghast_trophy.asItem())))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFItems.carminite)
                                        .when(ModExists.builder("immersiveengineering"))
                                        .apply(ModItemSwap.builder().apply("immersiveengineering", Registry.ITEM.get(TwilightForestMod.prefix("shader")), TFItems.carminite))
                                        .apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
                                            nbt.putString("shader_name", "twilightforest:ur-ghast");
                                        })))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TFItems.carminite)
                                        .when(ModExists.builder("immersiveengineering"))
                                        .apply(ModItemSwap.builder().apply("immersiveengineering", Registry.ITEM.get(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.carminite)))));

        register.accept(TFTreasure.aurora_cache.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                .add(LootTableReference.lootTableReference(TFTreasure.USELESS_LOOT).setWeight(25))
                                //common loot
                                .add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(Items.PACKED_ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 9))).setWeight(75)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFItems.charm_of_keeping_1))
                                .add(LootItem.lootTableItem(TFItems.ironwood_ingot).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(TFBlocks.aurora_block.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(TFItems.arctic_fur).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.arctic_fur).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ice_bow).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ender_bow).setWeight(75))
                                .add(LootItem.lootTableItem(TFItems.ice_sword).setWeight(75))
                                //ultrarare loot
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.SHARPNESS, 4)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.POWER_ARROWS, 4)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.PUNCH_ARROWS, 2)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.UNBREAKING, 2)).setWeight(25))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(Enchant.builder().apply(Enchantments.INFINITY_ARROWS, 1)).setWeight(25))));

        register.accept(TFTreasure.aurora_room.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(TFItems.maze_wafer).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))
                                .add(LootItem.lootTableItem(TFItems.ice_bomb).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(TFBlocks.firefly.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                                .add(LootItem.lootTableItem(Items.ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                                .add(LootItem.lootTableItem(Items.PACKED_ICE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFItems.arctic_fur).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(TFItems.arctic_helmet))
                                .add(LootItem.lootTableItem(TFItems.arctic_chestplate))
                                .add(LootItem.lootTableItem(TFItems.arctic_leggings))
                                .add(LootItem.lootTableItem(TFItems.arctic_boots))
                                .add(LootItem.lootTableItem(TFItems.knightmetal_chestplate))
                                .add(LootItem.lootTableItem(TFItems.knightmetal_sword))
                                .add(LootItem.lootTableItem(TFItems.charm_of_life_1)))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.ice_bow).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30))))
                                .add(LootItem.lootTableItem(TFItems.ender_bow).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(5))))
                                .add(LootItem.lootTableItem(TFItems.ice_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(25))))
                                .add(LootItem.lootTableItem(TFItems.glass_sword).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(20))))));

        register.accept(TFTreasure.troll_garden.lootTable,
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
                                .add(LootItem.lootTableItem(TFBlocks.uberous_soil.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.magic_beans))));

        register.accept(TFTreasure.troll_vault.lootTable,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(4))
                                //common loot
                                .add(LootItem.lootTableItem(Items.COAL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 32))))
                                .add(LootItem.lootTableItem(TFItems.torchberries).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16))))
                                .add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(2))
                                //uncommon loot
                                .add(LootItem.lootTableItem(TFBlocks.trollsteinn.asItem()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                                .add(LootItem.lootTableItem(Items.OBSIDIAN).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                //rare loot
                                .add(LootItem.lootTableItem(TFItems.lamp_of_cinders))));
    }
}
