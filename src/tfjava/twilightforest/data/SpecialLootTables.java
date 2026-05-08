package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.loot.TFLootTables;

import java.util.function.BiConsumer;

public record SpecialLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		consumer.accept(TFLootTables.CICADA_SQUISH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.GRAY_DYE))));
		consumer.accept(TFLootTables.FIREFLY_SQUISH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.GLOWSTONE_DUST))));
		consumer.accept(TFLootTables.MOONWORM_SQUISH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.LIME_DYE))));

		consumer.accept(TFLootTables.MOONWORM_FAILED_TO_PLACE_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFBlocks.MOONWORM.get().asItem()))));

		consumer.accept(TFLootTables.LIFEDRAIN_SCEPTER_KILL_BONUS, LootTable.lootTable().withPool(
			LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
		));

		consumer.accept(TFLootTables.KNIGHT_PHANTOM_DEFEATED, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem()))));

		consumer.accept(TFLootTables.OMINOUS_SPAWNER_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.EXANIMATE_ESSENCE.get()))));

		consumer.accept(TFLootTables.RASPBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.RASPBERRY.get()))));
		consumer.accept(TFLootTables.BLUEBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.BLUEBERRY.get()))));
		consumer.accept(TFLootTables.BLACKBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.BLACKBERRY.get()))));
		consumer.accept(TFLootTables.MALOBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.MALOBERRY.get()))));
		consumer.accept(TFLootTables.BLIGHTBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.BLIGHTBERRY.get()))));
		consumer.accept(TFLootTables.DUSKBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.DUSKBERRY.get()))));
		consumer.accept(TFLootTables.SKYBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.SKYBERRY.get()))));
		consumer.accept(TFLootTables.STINGBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.STINGBERRY.get()))));

		consumer.accept(TFLootTables.COPPER_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.COPPER_BERRY.get()))));
		consumer.accept(TFLootTables.IRON_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.IRON_BERRY.get()))));
		consumer.accept(TFLootTables.GOLD_OREBERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.GOLD_BERRY.get()))));
		consumer.accept(TFLootTables.ESSENCE_BERRY_BUSH_DROPS, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(TFItems.ESSENCE_BERRY.get()))));

	}
}

