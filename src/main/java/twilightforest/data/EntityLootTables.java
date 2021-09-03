//package twilightforest.data;
//
//import net.minecraft.Util;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.ItemLike;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.storage.loot.BuiltInLootTables;
//import net.minecraft.world.level.storage.loot.LootContext;
//import net.minecraft.world.level.storage.loot.LootPool;
//import net.minecraft.world.level.storage.loot.LootTable;
//import net.minecraft.world.level.storage.loot.entries.LootItem;
//import net.minecraft.world.level.storage.loot.entries.LootTableReference;
//import net.minecraft.world.level.storage.loot.functions.*;
//import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
//import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
//import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
//import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
//import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
//import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
//import net.minecraftforge.registries.ForgeRegistries;
//import twilightforest.TwilightForestMod;
//import twilightforest.block.TFBlocks;
//import twilightforest.entity.TFEntities;
//import twilightforest.item.TFItems;
//import twilightforest.loot.TFTreasure;
//import twilightforest.loot.conditions.IsMinion;
//import twilightforest.loot.conditions.ModExists;
//import twilightforest.loot.functions.ModItemSwap;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class EntityLootTables extends net.minecraft.data.loot.EntityLoot {
//
//	private final Set<EntityType<?>> knownEntities = new HashSet<>();
//
//	@Override
//	protected void add(EntityType<?> entity, LootTable.Builder builder) {
//		super.add(entity, builder);
//		knownEntities.add(entity);
//	}
//
//	@Override
//	protected void add(ResourceLocation id, LootTable.Builder table) {
//		super.add(id, table);
//	}
//
//	@Override
//	protected void addTables() {
//		add(TFEntities.adherent, emptyLootTable());
//		add(TFEntities.harbinger_cube, emptyLootTable());
//		add(TFEntities.mosquito_swarm, emptyLootTable());
//		add(TFEntities.pinch_beetle, emptyLootTable());
//		add(TFEntities.quest_ram, emptyLootTable());
//		add(TFEntities.roving_cube, emptyLootTable());
//		add(TFEntities.squirrel, emptyLootTable());
//		add(TFEntities.bunny, fromEntityLootTable(EntityType.RABBIT));
//		add(TFEntities.hedge_spider, fromEntityLootTable(EntityType.SPIDER));
//		add(TFEntities.fire_beetle, fromEntityLootTable(EntityType.CREEPER));
//		add(TFEntities.hostile_wolf, fromEntityLootTable(EntityType.WOLF));
//		add(TFEntities.king_spider, fromEntityLootTable(EntityType.SPIDER));
//		add(TFEntities.mist_wolf, fromEntityLootTable(EntityType.WOLF));
//		add(TFEntities.redcap_sapper, fromEntityLootTable(TFEntities.redcap));
//		add(TFEntities.slime_beetle, fromEntityLootTable(EntityType.SLIME));
//		add(TFEntities.swarm_spider, fromEntityLootTable(EntityType.SPIDER));
//		add(TFEntities.tower_broodling, fromEntityLootTable(EntityType.SPIDER));
//		add(TFEntities.tower_ghast, fromEntityLootTable(EntityType.GHAST));
//		add(TFEntities.bighorn_sheep, fromEntityLootTable(EntityType.SHEEP));
//		add(TFTreasure.BIGHORN_SHEEP_BLACK, sheepLootTableBuilderWithDrop(Blocks.BLACK_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_BLUE, sheepLootTableBuilderWithDrop(Blocks.BLUE_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_BROWN, sheepLootTableBuilderWithDrop(Blocks.BROWN_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_CYAN, sheepLootTableBuilderWithDrop(Blocks.CYAN_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_GRAY, sheepLootTableBuilderWithDrop(Blocks.GRAY_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_GREEN, sheepLootTableBuilderWithDrop(Blocks.GREEN_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_LIGHT_BLUE, sheepLootTableBuilderWithDrop(Blocks.LIGHT_BLUE_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_LIGHT_GRAY, sheepLootTableBuilderWithDrop(Blocks.LIGHT_GRAY_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_LIME, sheepLootTableBuilderWithDrop(Blocks.LIME_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_MAGENTA, sheepLootTableBuilderWithDrop(Blocks.MAGENTA_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_ORANGE, sheepLootTableBuilderWithDrop(Blocks.ORANGE_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_PINK, sheepLootTableBuilderWithDrop(Blocks.PINK_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_PURPLE, sheepLootTableBuilderWithDrop(Blocks.PURPLE_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_RED, sheepLootTableBuilderWithDrop(Blocks.RED_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_WHITE, sheepLootTableBuilderWithDrop(Blocks.WHITE_WOOL));
//		add(TFTreasure.BIGHORN_SHEEP_YELLOW, sheepLootTableBuilderWithDrop(Blocks.YELLOW_WOOL));
//
//		add(TFEntities.armored_giant,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.giant_sword.get()))
//								.when(LootItemKilledByPlayerCondition.killedByPlayer())));
//
//		add(TFEntities.giant_miner,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.giant_pickaxe.get()))
//								.when(LootItemKilledByPlayerCondition.killedByPlayer())));
//
//		add(TFEntities.blockchain_goblin,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.armor_shard.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.mini_ghast,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootTableReference.lootTableReference(EntityType.GHAST.getDefaultLootTable()))
//								.when(IsMinion.builder(true))));
//
//		/*registerLootTable(TFEntities.boggard,
//				LootTable.builder()
//						.addLootPool(LootPool.builder()
//								.rolls(ConstantRange.of(1))
//								.addEntry(ItemLootEntry.builder(TFItems.maze_map_focus.get())
//										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
//								.acceptCondition(RandomChance.builder(0.2F)))
//						.addLootPool(LootPool.builder()
//								.rolls(ConstantRange.of(1))
//								.addEntry(ItemLootEntry.builder(Items.IRON_BOOTS)
//										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
//								.acceptCondition(RandomChance.builder(0.1666F)))
//						.addLootPool(LootPool.builder()
//								.rolls(ConstantRange.of(1))
//								.addEntry(ItemLootEntry.builder(Items.IRON_PICKAXE)
//										.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(1.0F, 1.0F))))
//								.acceptCondition(RandomChance.builder(0.1111F))));*/
//
//		add(TFEntities.wild_boar,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.PORKCHOP)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
//										.apply(SmeltItemFunction.smelted()
//												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.helmet_crab,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.armor_shard.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.COD)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 1.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
//								.when(LootItemRandomChanceCondition.randomChance(0.5F))));
//
//		add(TFEntities.goblin_knight_upper,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.armor_shard.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.goblin_knight_lower,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.armor_shard.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.wraith,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.GLOWSTONE_DUST)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.redcap,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.COAL)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.yeti,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.arctic_fur.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.winter_wolf,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.arctic_fur.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.tiny_bird,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.FEATHER)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.penguin,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.FEATHER)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.ice_crystal,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.SNOWBALL)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.unstable_ice_core,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.SNOWBALL)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.stable_ice_core,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.SNOWBALL)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.snow_guardian,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.SNOWBALL)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.raven,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.raven_feather.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.tower_termite,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.borer_essence.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.skeleton_druid,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.BONE)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
//								.withPool(LootPool.lootPool()
//										.setRolls(ConstantValue.exactly(1))
//										.add(LootItem.lootTableItem(TFItems.torchberries.get())
//												.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//												.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.deer,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.LEATHER)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.raw_venison.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
//										.apply(SmeltItemFunction.smelted()
//												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
//
//		add(TFEntities.kobold,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.WHEAT)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.GOLD_NUGGET)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
//								.when(LootItemKilledByPlayerCondition.killedByPlayer())));
//
//		add(TFEntities.maze_slime,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.SLIME_BALL)
//										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.charm_of_keeping_1.get()))
//								.when(LootItemRandomChanceCondition.randomChance(0.025F))));
//
//		add(TFEntities.minotaur,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.raw_meef.get())
//										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
//										.apply(SmeltItemFunction.smelted()
//												.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.maze_map_focus.get()))
//								.when(LootItemRandomChanceCondition.randomChance(0.025F))));
//
//		add(TFEntities.tower_golem,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.IRON_INGOT)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.tower_wood.get()))
//									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
//									.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))));
//
//		add(TFEntities.troll,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.magic_beans.get()))
//								.when(LootItemRandomChanceCondition.randomChance(0.025F))));
//
//		add(TFEntities.death_tome,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.PAPER))
//								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)))
//								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 1))))
//						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.WRITABLE_BOOK).setWeight(2).setQuality(3))
//								.add(LootItem.lootTableItem(Items.BOOK).setWeight(19))
//								.add(LootTableReference.lootTableReference(TFTreasure.DEATH_TOME_BOOKS).setWeight(1)))
//						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
//								.when(LootItemKilledByPlayerCondition.killedByPlayer())
//								.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.005F))
//								.add(LootItem.lootTableItem(TFItems.magic_map_focus.get()))));
//
//		add(TFTreasure.DEATH_TOME_HURT,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootTableReference.lootTableReference(BuiltInLootTables.EMPTY))
//								.add(LootItem.lootTableItem(Items.PAPER))));
//
//		add(TFTreasure.DEATH_TOME_BOOKS,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.BOOK).setWeight(32)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(1, 10))))
//								.add(LootItem.lootTableItem(Items.BOOK).setWeight(8)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(11, 20))))
//								.add(LootItem.lootTableItem(Items.BOOK).setWeight(4)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(21, 30))))
//								.add(LootItem.lootTableItem(Items.BOOK).setWeight(1)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(31, 40))))));
//
//		add(TFEntities.naga,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.naga_scale.get())
//								.apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 11)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.naga_trophy.get().asItem())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.naga_scale.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.naga_scale.get()))
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											nbt.putString("shader_name", "twilightforest:naga");
//										})))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.naga_scale.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.naga_scale.get())))));
//
//		add(TFEntities.lich,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.twilight_scepter.get()))
//								.add(LootItem.lootTableItem(TFItems.lifedrain_scepter.get()))
//								.add(LootItem.lootTableItem(TFItems.zombie_scepter.get()))
//								.add(LootItem.lootTableItem(TFItems.shield_scepter.get())))
//						.withPool(LootPool.lootPool()
//								.setRolls(UniformGenerator.between(2, 4))
//								.add(LootItem.lootTableItem(Items.GOLDEN_SWORD)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
//								.add(LootItem.lootTableItem(Items.GOLDEN_HELMET)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
//								.add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
//								.add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40))))
//								.add(LootItem.lootTableItem(Items.GOLDEN_BOOTS)
//										.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(10, 40)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.ENDER_PEARL)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.BONE)
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 9)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.lich_trophy.get().asItem())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.GOLD_NUGGET)
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), Items.GOLD_NUGGET))
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											nbt.putString("shader_name", "twilightforest:lich");
//										})))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.GOLD_NUGGET)
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), Items.GOLD_NUGGET)))));
//
//		add(TFEntities.minoshroom,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.meef_stroganoff.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.minoshroom_trophy.get().asItem())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.meef_stroganoff.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.meef_stroganoff.get()))
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											nbt.putString("shader_name", "twilightforest:minoshroom");
//										})))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.meef_stroganoff.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.meef_stroganoff.get())))));
//
//		add(TFEntities.hydra,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.hydra_chop.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 35)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 2)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.fiery_blood.get())
//										.apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.hydra_trophy.get().asItem())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.fiery_blood.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.fiery_blood.get()))
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											nbt.putString("shader_name", "twilightforest:hydra");
//										})))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.fiery_blood.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.fiery_blood.get())))));
//
//		add(TFEntities.yeti_alpha,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.alpha_fur.get())
//										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.ice_bomb.get())
//										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.yeti_trophy.get().asItem())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.ice_bomb.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.ice_bomb.get()))
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											nbt.putString("shader_name", "twilightforest:alpha_yeti");
//										})))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.ice_bomb.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.ice_bomb.get())))));
//
//		add(TFEntities.snow_queen,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.triple_bow.get()))
//								.add(LootItem.lootTableItem(TFItems.seeker_bow.get())))
//						.withPool(LootPool.lootPool()
//								.setRolls(UniformGenerator.between(1, 4))
//								.add(LootItem.lootTableItem(Blocks.PACKED_ICE.asItem())
//										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(7)))
//										.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(UniformGenerator.between(2, 5))
//								.add(LootItem.lootTableItem(Items.SNOWBALL)
//										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(16)))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFBlocks.snow_queen_trophy.get().asItem())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.ice_bomb.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")), TFItems.ice_bomb.get()))
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											nbt.putString("shader_name", "twilightforest:snow_queen");
//										})))))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.ice_bomb.get())
//										.when(ModExists.builder("immersiveengineering"))
//										.apply(ModItemSwap.builder().apply("immersiveengineering", ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader_bag_twilight")), TFItems.ice_bomb.get())))));
//
//		add(TFTreasure.QUESTING_RAM_REWARDS,
//				LootTable.lootTable()
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(TFItems.crumble_horn.get())))
//						.withPool(LootPool.lootPool()
//								.setRolls(ConstantValue.exactly(1))
//								.add(LootItem.lootTableItem(Items.BUNDLE)
//										.apply(SetNbtFunction.setTag(Util.make(new CompoundTag(), (nbt) -> {
//											ListTag items = new ListTag();
//
//											// Do NOT overstuff the bag.
//											items.add(new ItemStack(TFBlocks.quest_ram_trophy.get()).serializeNBT());
//											items.add(new ItemStack(Blocks.COAL_BLOCK).serializeNBT());
//											items.add(new ItemStack(Blocks.IRON_BLOCK).serializeNBT());
//											items.add(new ItemStack(Blocks.COPPER_BLOCK).serializeNBT());
//											items.add(new ItemStack(Blocks.LAPIS_BLOCK).serializeNBT());
//											items.add(new ItemStack(Blocks.GOLD_BLOCK).serializeNBT());
//											items.add(new ItemStack(Blocks.DIAMOND_BLOCK).serializeNBT());
//											items.add(new ItemStack(Blocks.EMERALD_BLOCK).serializeNBT());
//
//											nbt.put("Items", items);
//										}))))));
//	}
//
//	public LootTable.Builder emptyLootTable() {
//		return LootTable.lootTable();
//	}
//
//	public LootTable.Builder fromEntityLootTable(EntityType<?> parent) {
//		return LootTable.lootTable()
//				.withPool(LootPool.lootPool()
//						.setRolls(ConstantValue.exactly(1))
//						.add(LootTableReference.lootTableReference(parent.getDefaultLootTable())));
//	}
//
//	private static LootTable.Builder sheepLootTableBuilderWithDrop(ItemLike wool) {
//		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(wool))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
//	}
//
//	@Override
//	public Set<EntityType<?>> getKnownEntities() {
//		return knownEntities;
//	}
//}
