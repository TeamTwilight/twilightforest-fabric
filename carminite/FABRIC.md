TODO
====

## Main Init
- ResourceConditions.register(UncraftingTableCondition.TYPE);
- Register client-side MapData cache clearing event by calling MapDatamanger.init()
- Register MasonJar BE storage

## Add these to Fabric convention tags
- Gold Ingot to ConventionalItemTags.GOLD_INGOTS
- Ores to ConventionalBlocktags.ORES and ConventionalItemTags.ORES
- Redstone Dust to ConventionalItemtags.REDSTONE_DUSTS
- Bosses to ConventionalEntityTypeTags.BOSSES

## ICondition for Uncrafting Table
- Replace the UncraftingTableCondition usages with ResourceCondition from Fabric

## IGlobalLootModifier & LootModifier
- Port to post-generation mixin

# TwilightForestRenderInfo
- Port implementation and usages to Fabric alternatives

# PartEntity
- Port system to Fabric

# Uberous Soil
- Add Uberous Soil to the proper Block tags in order to have it support crops

# Hostile mounts
- Create marker interface for hostile mounts and add a listener at the end of entity ticks

# ChainBlock
- Try to move away from IEntityWithComplexSpawn to a native Fabric API or direct mixins