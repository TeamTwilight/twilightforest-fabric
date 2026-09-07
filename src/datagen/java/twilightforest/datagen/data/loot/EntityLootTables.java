package twilightforest.datagen.data.loot;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.SlimePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.loot.LootingEnchantNumberProvider;
import twilightforest.loot.MultiplayerBasedAdditionLootFunction;
import twilightforest.loot.MultiplayerBasedNumberProvider;
import twilightforest.loot.TFLootTables;
import twilightforest.loot.conditions.IsMinionCondition;

public class EntityLootTables extends EntityLootSubProvider {

	protected EntityLootTables(HolderLookup.Provider provider) {
		super(FeatureFlags.REGISTRY.allFlags(), provider);
	}

	@Override
	public void generate() {
		add(TFEntities.ADHERENT, emptyLootTable());
		add(TFEntities.LICH_MINION, emptyLootTable());
		add(TFEntities.LOYAL_ZOMBIE, emptyLootTable());
		add(TFEntities.HARBINGER_CUBE, emptyLootTable());
		add(TFEntities.MOSQUITO_SWARM, emptyLootTable());
		add(TFEntities.PINCH_BEETLE, emptyLootTable());
		add(TFEntities.QUEST_RAM, emptyLootTable());
		add(TFEntities.SQUIRREL, emptyLootTable());
		add(TFEntities.DWARF_RABBIT, fromEntityLootTable(EntityType.RABBIT));
		add(TFEntities.HEDGE_SPIDER, fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.HOSTILE_WOLF, fromEntityLootTable(EntityType.WOLF));
		add(TFEntities.KING_SPIDER, fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.MIST_WOLF, fromEntityLootTable(EntityType.WOLF));
		add(TFEntities.REDCAP_SAPPER, fromEntityLootTable(TFEntities.REDCAP));
		add(TFEntities.SWARM_SPIDER, fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.CARMINITE_BROODLING, fromEntityLootTable(EntityType.SPIDER));
		add(TFEntities.CARMINITE_GHASTGUARD, fromEntityLootTable(EntityType.GHAST));
		add(TFEntities.BIGHORN_SHEEP, fromEntityLootTable(EntityType.SHEEP));
		add(TFEntities.RISING_ZOMBIE, fromEntityLootTable(EntityType.ZOMBIE));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_BLACK, sheepLootTableBuilderWithDrop(Blocks.BLACK_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_BLUE, sheepLootTableBuilderWithDrop(Blocks.BLUE_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_BROWN, sheepLootTableBuilderWithDrop(Blocks.BROWN_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_CYAN, sheepLootTableBuilderWithDrop(Blocks.CYAN_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_GRAY, sheepLootTableBuilderWithDrop(Blocks.GRAY_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_GREEN, sheepLootTableBuilderWithDrop(Blocks.GREEN_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_LIGHT_BLUE, sheepLootTableBuilderWithDrop(Blocks.LIGHT_BLUE_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_LIGHT_GRAY, sheepLootTableBuilderWithDrop(Blocks.LIGHT_GRAY_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_LIME, sheepLootTableBuilderWithDrop(Blocks.LIME_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_MAGENTA, sheepLootTableBuilderWithDrop(Blocks.MAGENTA_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_ORANGE, sheepLootTableBuilderWithDrop(Blocks.ORANGE_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_PINK, sheepLootTableBuilderWithDrop(Blocks.PINK_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_PURPLE, sheepLootTableBuilderWithDrop(Blocks.PURPLE_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_RED, sheepLootTableBuilderWithDrop(Blocks.RED_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_WHITE, sheepLootTableBuilderWithDrop(Blocks.WHITE_WOOL));
		add(TFEntities.BIGHORN_SHEEP, TFLootTables.BIGHORN_SHEEP_YELLOW, sheepLootTableBuilderWithDrop(Blocks.YELLOW_WOOL));

		add(TFEntities.FIRE_BEETLE,
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(
							LootItem.lootTableItem(Items.GUNPOWDER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						)
				));

		add(TFEntities.ARMORED_GIANT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.GIANT_SWORD))
					.when(LootItemKilledByPlayerCondition.killedByPlayer())));

		add(TFEntities.GIANT_MINER,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.GIANT_PICKAXE))
					.when(LootItemKilledByPlayerCondition.killedByPlayer())));

		add(TFEntities.BLOCKCHAIN_GOBLIN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.CARMINITE_GHASTLING,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(NestedLootTable.lootTableReference(EntityType.GHAST.getDefaultLootTable().orElseThrow()))
					.when(IsMinionCondition.builder(true))));

		/*registerLootTable(TFEntities.BOGGARD,
				LootTable.builder()
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(TFItems.MAZE_MAP_FOCUS)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.2F)))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_BOOTS)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.1666F)))
						.addLootPool(LootPool.builder()
								.rolls(ConstantRange.of(1))
								.addEntry(ItemLootEntry.builder(Items.IRON_PICKAXE)
										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
								.acceptCondition(RandomChance.builder(0.1111F))));*/

		add(TFEntities.BOAR,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.PORKCHOP)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
						.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(SetNameFunction.setName(Component.translatable("item.twilightforest.boarkchop").withStyle(Style.EMPTY.withItalic(false)), SetNameFunction.Target.ITEM_NAME)
							.when(LootItemRandomChanceCondition.randomChance(0.002F))
							.when(LootItemKilledByPlayerCondition.killedByPlayer())))));

		add(TFEntities.HELMET_CRAB,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.COD)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
						.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
					.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.5F, 0.1F))));

		add(TFEntities.UPPER_GOBLIN_KNIGHT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.LOWER_GOBLIN_KNIGHT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.ARMOR_SHARD)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.WRAITH,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.REDCAP,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.COAL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.YETI,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.WINTER_WOLF,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.ARCTIC_FUR)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.TINY_BIRD,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.FEATHER)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.PENGUIN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.FEATHER)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.ICE_CRYSTAL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.SNOWBALL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.UNSTABLE_ICE_CORE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.SNOWBALL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.STABLE_ICE_CORE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.SNOWBALL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.SNOW_GUARDIAN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.SNOWBALL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.RAVEN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.RAVEN_FEATHER)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.TOWERWOOD_BORER,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.BORER_ESSENCE)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.SKELETON_DRUID,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.BONE)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.TORCHBERRIES)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.DEER,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.LEATHER)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.RAW_VENISON)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
						.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.KOBOLD,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.WHEAT)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.GOLD_NUGGET)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
					.when(LootItemKilledByPlayerCondition.killedByPlayer())));

		add(TFEntities.MAZE_SLIME,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.MAZE_SLIME_BALL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.when(this.killedByFrog(this.registries.lookupOrThrow(Registries.ENTITY_TYPE)).invert()))
					.add(LootItem.lootTableItem(TFItems.MAZE_SLIME_BALL)
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
						.when(this.killedByFrog(this.registries.lookupOrThrow(Registries.ENTITY_TYPE))))
					.when(LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity().subPredicate(SlimePredicate.sized(MinMaxBounds.Ints.exactly(1)))
					)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.CHARM_OF_KEEPING_1))
					.when(LootItemKilledByPlayerCondition.killedByPlayer())
					.when((LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.015F, 0.005F)))));

		add(TFEntities.MINOTAUR,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.RAW_MEEF)
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.MAZE_MAP_FOCUS))
					.when((LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F)))));

		add(TFEntities.CARMINITE_GOLEM,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.IRON_INGOT)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFBlocks.TOWERWOOD))
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
					.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))));

		add(TFEntities.SLIME_BEETLE,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(Items.SLIME_BALL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.TROLL,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.MAGIC_BEANS))
					.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));

		add(TFEntities.DEATH_TOME,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.PAPER))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
					.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0, 1))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(2).setQuality(3))
					.add(LootItem.lootTableItem(Items.BOOK).setWeight(19))
					.add(NestedLootTable.lootTableReference(TFLootTables.DEATH_TOME_BOOKS).setWeight(1)))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
					.when(LootItemKilledByPlayerCondition.killedByPlayer())
					.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.005F))
					.add(LootItem.lootTableItem(TFItems.MAGIC_MAP_FOCUS))));

		add(TFEntities.DEATH_TOME, TFLootTables.DEATH_TOME_HURT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(EmptyLootItem.emptyItem())
					.add(LootItem.lootTableItem(Items.PAPER))));

		add(TFEntities.DEATH_TOME, TFLootTables.DEATH_TOME_BOOKS,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.BOOK).setWeight(32)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(1, 10))))
					.add(LootItem.lootTableItem(Items.BOOK).setWeight(8)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(11, 20))))
					.add(LootItem.lootTableItem(Items.BOOK).setWeight(4)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(21, 30))))
					.add(LootItem.lootTableItem(Items.BOOK).setWeight(1)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(31, 40))))));

		add(TFEntities.NAGA,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.NAGA_SCALE)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0F, 11.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(2.0F, 4.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.NAGA_TROPHY.asItem()))));

		add(TFEntities.LICH,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-2.0F, 1.0F), ConstantValue.exactly(1.0F)))
					.add(LootItem.lootTableItem(TFItems.TWILIGHT_SCEPTER))
					.add(LootItem.lootTableItem(TFItems.LIFEDRAIN_SCEPTER))
					.add(LootItem.lootTableItem(TFItems.ZOMBIE_SCEPTER))
					.add(LootItem.lootTableItem(TFItems.FORTIFICATION_SCEPTER)))
				.withPool(LootPool.lootPool()
					.setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(0.0F, 1.0F), UniformGenerator.between(2, 4)))
					.add(LootItem.lootTableItem(Items.GOLDEN_SWORD)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 40.0F))))
					.add(LootItem.lootTableItem(Items.GOLDEN_HELMET)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 40.0F))))
					.add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 40.0F))))
					.add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 40.0F))))
					.add(LootItem.lootTableItem(Items.GOLDEN_BOOTS)
						.apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, UniformGenerator.between(10.0F, 40.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(Items.ENDER_PEARL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(0.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(Items.BONE)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 9.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.LICH_TROPHY)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.EXANIMATE_ESSENCE)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(0.0F, 1.0F))))));

		add(TFEntities.MINOSHROOM,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(0.0F, 1.0F), UniformGenerator.between(2.0F, 5.0F)))
					.add(LootItem.lootTableItem(TFItems.MEEF_STROGANOFF)
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.MINOSHROOM_TROPHY.asItem())))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.DIAMOND_MINOTAUR_AXE)
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(-2.0F, 1.0F))))));

		add(TFEntities.HYDRA,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.HYDRA_CHOP)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 35.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(5.0F, 10.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.FIERY_BLOOD)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(7.0F, 10.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 2.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.HYDRA_TROPHY.asItem()))));

		add(TFEntities.KNIGHT_PHANTOM, LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(new BinomialDistributionGenerator(LootingEnchantNumberProvider.applyLootingLevelTo(this.registries, MultiplayerBasedNumberProvider.rollsForPlayers(ConstantValue.exactly(1.5F), ConstantValue.exactly(4.0F))), ConstantValue.exactly(0.17F)))
				.add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_SWORD).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20))))
				.add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_PICKAXE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20))))
				.add(LootItem.lootTableItem(TFItems.KNIGHTMETAL_AXE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20)))))
			.withPool(LootPool.lootPool()
				.setRolls(new BinomialDistributionGenerator(LootingEnchantNumberProvider.applyLootingLevelTo(this.registries, MultiplayerBasedNumberProvider.rollsForPlayers(ConstantValue.exactly(1.0F), ConstantValue.exactly(2.0F))), ConstantValue.exactly(0.17F)))
				.add(LootItem.lootTableItem(TFItems.PHANTOM_HELMET).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20))))
				.add(LootItem.lootTableItem(TFItems.PHANTOM_CHESTPLATE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(20)))))
			.withPool(LootPool.lootPool()
				.setRolls(new BinomialDistributionGenerator(LootingEnchantNumberProvider.applyLootingLevelTo(this.registries, MultiplayerBasedNumberProvider.rollsForPlayers(ConstantValue.exactly(0.5F), ConstantValue.exactly(1.0F))), ConstantValue.exactly(0.17F)))
				.add(LootItem.lootTableItem(TFItems.PHANTOM_HELMET).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30))))
				.add(LootItem.lootTableItem(TFItems.PHANTOM_CHESTPLATE).apply(EnchantWithLevelsFunction.enchantWithLevels(this.registries, ConstantValue.exactly(30))))));

		add(TFEntities.UR_GHAST,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4.0F))
					.add(LootItem.lootTableItem(TFItems.CARMINITE)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(2.0F))
					.add(LootItem.lootTableItem(TFItems.FIERY_TEARS)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.UR_GHAST_TROPHY.asItem()))));

		add(TFEntities.ALPHA_YETI,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.ALPHA_YETI_FUR)
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(1.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFItems.ICE_BOMB)
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
						.apply(MultiplayerBasedAdditionLootFunction.addForAllParticipatingPlayers(UniformGenerator.between(0.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.ALPHA_YETI_TROPHY.asItem()))));

		add(TFEntities.SNOW_QUEEN,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(MultiplayerBasedNumberProvider.rollsForPlayers(UniformGenerator.between(-2.0F, 1.0F), ConstantValue.exactly(1.0F)))
					.add(LootItem.lootTableItem(TFItems.TRIPLE_BOW))
					.add(LootItem.lootTableItem(TFItems.SEEKER_BOW)))
				.withPool(LootPool.lootPool()
					.setRolls(UniformGenerator.between(1.0F, 4.0F))
					.add(LootItem.lootTableItem(Blocks.PACKED_ICE.asItem())
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(7.0F)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(UniformGenerator.between(2.0F, 5.0F))
					.add(LootItem.lootTableItem(Items.SNOWBALL)
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(16.0F)))))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1.0F))
					.add(LootItem.lootTableItem(TFBlocks.SNOW_QUEEN_TROPHY.asItem()))));

		add(TFEntities.QUEST_RAM, TFLootTables.QUESTING_RAM_REWARDS,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(TFItems.CRUMBLE_HORN)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.BUNDLE).apply(SetContainerContents.setContents(ContainerComponentManipulators.BUNDLE_CONTENTS)
						.withEntry(LootItem.lootTableItem(TFBlocks.QUEST_RAM_TROPHY))
						.withEntry(NestedLootTable.lootTableReference(TFLootTables.QUESTING_RAM_REWARD_BLOCKS))
					))));

		add(TFEntities.QUEST_RAM, TFLootTables.QUESTING_RAM_REWARD_BLOCKS, LootTable.lootTable()
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.COAL_BLOCK)))
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.IRON_BLOCK)))
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.COPPER_BLOCK)))
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.LAPIS_BLOCK)))
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.GOLD_BLOCK)))
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.DIAMOND_BLOCK)))
			.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Blocks.EMERALD_BLOCK)))
		);
	}

	public LootTable.Builder emptyLootTable() {
		return LootTable.lootTable();
	}

	public LootTable.Builder fromEntityLootTable(EntityType<?> parent) {
		return LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(NestedLootTable.lootTableReference(parent.getDefaultLootTable().orElseThrow())));
	}

	private static LootTable.Builder sheepLootTableBuilderWithDrop(ItemLike wool) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(wool))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(NestedLootTable.lootTableReference(EntityType.SHEEP.getDefaultLootTable().orElseThrow())));
	}
}